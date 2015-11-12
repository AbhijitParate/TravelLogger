package abhijit.travellogger;

/**
 * Created by abhijit on 10/27/15.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.PortUnreachableException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import nl.changer.audiowife.AudioWife;

/**
 * Created by abhijit on 10/25/15.
 */
public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    //1. Mime types for view
    private static final int MIME_TYPE_INVALID  = 0;
    private static final int MIME_TYPE_IMAGE    = 1;
    private static final int MIME_TYPE_VIDEO    = 2;
    private static final int MIME_TYPE_AUDIO    = 3;

    private File[] mediaFiles;
    private File[] imageFiles;
    private final Context context;

    private MediaPlayer player;

    //For AsyncTask
    private Bitmap placeholderBitmap;

    public ViewAdapter(File[] folderFiles, Context mainContext){
        mediaFiles = folderFiles;
        context = mainContext;
        imageFiles = getImageFiles();
    }

    private File[] getImageFiles(){
        List<File> mediaList = new ArrayList<File>(Arrays.asList(mediaFiles));
        Iterator<File> iterator = mediaList.iterator();
        while (iterator.hasNext()) {
            File f = iterator.next();
            String mimeType = getMimeTypeFromFile(f);
            if (mimeType == null || !(mimeType.equals("image/jpeg"))) {
                iterator.remove();
            }
        }
        return mediaList.toArray(new File[mediaList.size()]);
    }

    public static class AsyncDrawable extends BitmapDrawable {
        final WeakReference<ImageLoaderAsyncTask> taskReference;

        public AsyncDrawable(Resources resources,
                             Bitmap bitmap,
                             ImageLoaderAsyncTask bitmapWorkerTask) {
            super(resources, bitmap);
            taskReference = new WeakReference(bitmapWorkerTask);
        }

        public ImageLoaderAsyncTask getImageLoaderTask() {
            return taskReference.get();
        }
    }

    public static ImageLoaderAsyncTask getImageLoaderTask(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if(drawable instanceof AsyncDrawable) {
            AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
            return asyncDrawable.getImageLoaderTask();
        }
        return null;
    }

    public static boolean checkImageLoaderTask(File imageFile, ImageView imageView) {
        ImageLoaderAsyncTask imageLoaderTask = getImageLoaderTask(imageView);
        if(imageLoaderTask != null) {
            final File workerFile = imageLoaderTask.getImageFile();
            if(workerFile != null) {
                if(workerFile != imageFile) {
                    imageLoaderTask.cancel(true);
                } else {
                    // bitmap worker task file is the same as the imageview is expecting
                    // so do nothing
                    return false;
                }
            }
        }
        return true;
    }

    //2. Views for mimetypes
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private VideoView vv;
        private Button av;
//        private TextView  tv;

        public ViewHolder(View view){
            super(view);

            iv = (ImageView) view.findViewById(R.id.imageView);

            vv = (VideoView) view.findViewById(R.id.videoView);

            av = (Button) view.findViewById(R.id.audioPlay);
        }

        public ImageView getImageView(){ return iv; }

        public VideoView getVideoView(){
            return vv;
        }

        public Button getAudioView(){ return av; }
    }

    //3. bind viewholder to view
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File mediaFile = mediaFiles[position] ;
        String mimeType = getMimeTypeFromFile(mediaFile);
        switch (mimeType){
            case "image/jpeg":
                bindImage(holder, mediaFile);
                break;
            case "video/mp4":
                bindVideo(holder, mediaFile);
                break;
            case "audio/aac":
                bindAudio(holder, mediaFile);
                break;
        }
    }

    public void bindAudio(final ViewHolder holder, final File file){

        Uri audioFileUri;
        audioFileUri = Uri.fromFile(file);
        player = MediaPlayer.create(context.getApplicationContext(), audioFileUri);
//        player.start();
//        player.pause();

        holder.getAudioView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player.isPlaying()){
                    player.pause();
                    holder.getAudioView().setText("Play");
                } else {
                    player.start();
                    holder.getAudioView().setText("Pause");
                }
            }
        });
    }

    public void bindImage(ViewHolder holder, final File file){

        holder.getImageView().setClickable(true);

        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(file.toURI().toString()), "image/*");
                context.startActivity(intent);
            }
        });

//        Picasso.with(holder.getImageView().getContext())
//                .load(file)
//                .resize(768, 432)
//                .centerCrop()
//                .into(holder.getImageView());
//

        ImageLoaderAsyncTask imageLoader = new ImageLoaderAsyncTask(holder.getImageView());
        imageLoader.execute(file);
        Bitmap bitmap = MainActivity.getBitmapFromMemoryCache(file.getName());
        if(bitmap != null) {
            holder.getImageView().setImageBitmap(bitmap);
        }
        else if(checkImageLoaderTask(file, holder.getImageView())) {
            ImageLoaderAsyncTask imageLoaderTask = new ImageLoaderAsyncTask(holder.getImageView());
            AsyncDrawable asyncDrawable = new AsyncDrawable(
                    holder.getImageView().getResources(),
                    placeholderBitmap,
                    imageLoaderTask);
            holder.getImageView().setImageDrawable(asyncDrawable);
            imageLoaderTask.execute(file);
        }

    }

    public void bindVideo(ViewHolder holder, File file){
        final VideoView videoView = holder.getVideoView();
        videoView.setVideoPath(file.getAbsolutePath());
        videoView.requestFocus();
        videoView.seekTo(5000);
//        final MediaController mediaController = new MediaController(holder.getVideoView().getContext());
//        mediaController.setAnchorView(holder.getVideoView());
//        mediaController.setMediaPlayer(holder.getVideoView());
//        mediaController.hide();
//        videoView.setMediaController(mediaController);
//        videoView.setClickable(true);
        /*
        videoView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    mediaController.show(10);
                }
                else {
                    videoView.start();
                    mediaController.hide();
                }
            }
        });
        */
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getActionMasked()) {
                    if (videoView.isPlaying()) {
                        videoView.pause();
//                        mediaController.show(20);
                    }
                    else {
                        videoView.start();
//                        mediaController.hide();
                    }
                }
                return false;
            }
        });
    }

    public String getMimeTypeFromFile(File file){
        Uri uri = Uri.fromFile(file);
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        String mimeType =  MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        return mimeType;
    }

    // 4 assign view to viewholder according to mimetype
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images, parent, false);
                viewHolder = new ViewHolder(view);
                return viewHolder;

            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos, parent, false);
                viewHolder = new ViewHolder(view);
                return viewHolder;

            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audios, parent, false);
                viewHolder = new ViewHolder(view);
                return viewHolder;

            default:
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return mediaFiles.length;
    }

    //5 return mimetypes from the list of files
    @Override
    public int getItemViewType(int position) {
        File file = mediaFiles[position];
        String mimeType = getMimeTypeFromFile(file);
        if (mimeType.equals("image/jpeg")) {
            return MIME_TYPE_IMAGE;
        } else if (mimeType.equals("video/mp4")){
            return MIME_TYPE_VIDEO;
        } else if (mimeType.equals("audio/aac")){
            return MIME_TYPE_AUDIO;
        }
        return MIME_TYPE_INVALID;
    }
}
