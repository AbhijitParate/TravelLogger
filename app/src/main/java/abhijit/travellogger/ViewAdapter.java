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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by abhijit on 10/25/15.
 */
public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    //1. Mime types for view
    private static final int MIME_TYPE_INVALID  = 0;
    private static final int MIME_TYPE_IMAGE    = 1;
    private static final int MIME_TYPE_VIDEO    = 2;
    private static final int MIME_TYPE_AUDIO    = 3;
    private static final int MIME_TYPE_NOTE     = 4;

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

//    public static class AsyncDrawable extends BitmapDrawable {
//        final WeakReference<ImageLoaderAsyncTask> taskReference;
//
//        public AsyncDrawable(Resources resources,
//                             Bitmap bitmap,
//                             ImageLoaderAsyncTask bitmapWorkerTask) {
//            super(resources, bitmap);
//            taskReference = new WeakReference(bitmapWorkerTask);
//        }
//
//        public ImageLoaderAsyncTask getImageLoaderTask() {
//            return taskReference.get();
//        }
//    }

//    public static ImageLoaderAsyncTask getImageLoaderTask(ImageView imageView) {
//        Drawable drawable = imageView.getDrawable();
//        if(drawable instanceof AsyncDrawable) {
//            AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
//            return asyncDrawable.getImageLoaderTask();
//        }
//        return null;
//    }

//    public static boolean checkImageLoaderTask(File imageFile, ImageView imageView) {
//        ImageLoaderAsyncTask imageLoaderTask = getImageLoaderTask(imageView);
//        if(imageLoaderTask != null) {
//            final File workerFile = imageLoaderTask.getImageFile();
//            if(workerFile != null) {
//                if(workerFile != imageFile) {
//                    imageLoaderTask.cancel(true);
//                } else {
//                    // bitmap worker task file is the same as the imageview is expecting
//                    // so do nothing
//                    return false;
//                }
//            }
//        }
//        return true;
//    }

    //2. Views for mimetypes
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View imageLayout;
        private View videoLayout;
        private View audioLayout;
        private View noteLayout;

//        private ImageView imageLayout;
//        private VideoView videoLayout;
//        private Button audioLayout;
//        private TextView  noteLayout;

        private TextView imageText;
        private ImageView imageView;

        private TextView videoText;
        private VideoView videoView;

        private TextView audioText;
        private Button audioButton;
        private SeekBar audioSeekbar;


        private TextView noteText;

        public ViewHolder(View view){
            super(view);
//            imageLayout = (ImageView) view.findViewById(R.id.image);
//            videoLayout = (VideoView) view.findViewById(R.id.videoLayout);
//            audioLayout = (Button) view.findViewById(R.id.audioPlay);
//            noteLayout = (TextView) view.findViewById(R.id.textView);
            imageLayout = view.findViewById(R.id.image);
            videoLayout = view.findViewById(R.id.video);
            audioLayout = view.findViewById(R.id.audio);
            noteLayout = view.findViewById(R.id.note);

            imageText = (TextView) view.findViewById(R.id.image_view_text);
            imageView = (ImageView) view.findViewById(R.id.image_view);

            videoText = (TextView) view.findViewById(R.id.video_text);
            videoView = (VideoView) view.findViewById(R.id.video_View);

            audioText = (TextView) view.findViewById(R.id.audio_text);
            audioButton = (Button) view.findViewById(R.id.audio_button);
            audioSeekbar = (SeekBar) view.findViewById(R.id.audio_seekbar);

            noteText = (TextView) view.findViewById(R.id.note_text);

        }

        public View getImageLayout(){ return imageLayout; }

        public View getVideoLayout(){ return videoLayout; }

        public View getAudioLayout(){ return audioLayout; }

        public View getNoteLayout(){ return noteLayout; }

        public TextView getImageText() {
            return imageText;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getNoteText() {
            return noteText;
        }

        public SeekBar getAudioSeekbar() {
            return audioSeekbar;
        }

        public Button getAudioButton() {
            return audioButton;
        }

        public TextView getAudioText() {
            return audioText;
        }

        public VideoView getVideoView() {
            return videoView;
        }

        public TextView getVideoText() {
            return videoText;
        }
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
            case "text/plain":
                bindNote(holder, mediaFile);
                break;
        }
    }

    public void bindAudio(final ViewHolder holder, final File file){

        TextView audioText = holder.getAudioText();
        audioText.setText("Text for Audio");

        Uri audioFileUri;
        audioFileUri = Uri.fromFile(file);
        player = MediaPlayer.create(context.getApplicationContext(), audioFileUri);

        final Button playButton =  holder.getAudioButton();
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.isPlaying()) {
                    player.pause();
                    playButton.setText("Play");
                } else {
                    player.start();
                    playButton.setText("Pause");
                }
            }
        });

        SeekBar seekBar = holder.getAudioSeekbar();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        holder.getAudioLayout().setVisibility(View.VISIBLE);
    }

    public void bindImage(ViewHolder holder, final File file){

//        holder.getImageLayout().setVisibility(View.VISIBLE);
        ImageView imageView = holder.getImageView();
        TextView  textView  = holder.getImageText();

//        imageView.setImageBitmap(null);
//        Picasso.with(imageView.getContext()).cancelRequest(imageView);
        //Place image in the imageLayout
        Picasso.with(imageView.getContext())
                .load(file)
                .resize(768, 432)
                .centerCrop()
                .into(imageView);

        //Set ImageView clicks
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(file.toURI().toString()), "image/*");
                context.startActivity(intent);
            }
        });

        //Set text for the ImageView
        textView.setText("This is text for ImageView");
//        ImageLoaderAsyncTask imageLoader = new ImageLoaderAsyncTask(holder.getImageLayout());
//        imageLoader.execute(file);
//        Bitmap bitmap = MainActivity.getBitmapFromMemoryCache(file.getName());
//        if(bitmap != null) {
//            holder.getImageLayout().setImageBitmap(bitmap);
//        }
//        else if(checkImageLoaderTask(file, holder.getImageLayout())) {
//            ImageLoaderAsyncTask imageLoaderTask = new ImageLoaderAsyncTask(holder.getImageLayout());
//            AsyncDrawable asyncDrawable = new AsyncDrawable(
//                    holder.getImageLayout().getResources(),
//                    placeholderBitmap,
//                    imageLoaderTask);
//            holder.getImageLayout().setImageDrawable(asyncDrawable);
//            imageLoaderTask.execute(file);
//        }

        holder.getImageLayout().setVisibility(View.VISIBLE);
    }

    public void bindVideo(ViewHolder holder, File file){

        final VideoView videoView = (VideoView) holder.getVideoLayout().findViewById(R.id.video_View);
        final TextView  textView  = (TextView) holder.getVideoLayout().findViewById(R.id.video_text);

        videoView.setVideoPath(file.getAbsolutePath());
        videoView.requestFocus();
        videoView.seekTo(5000);
//        final MediaController mediaController = new MediaController(holder.getVideoLayout().getContext());
//        mediaController.setAnchorView(holder.getVideoLayout());
//        mediaController.setMediaPlayer(holder.getVideoLayout());
//        mediaController.hide();
//        videoLayout.setMediaController(mediaController);
//        videoLayout.setClickable(true);
        /*
        videoLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (videoLayout.isPlaying()) {
                    videoLayout.pause();
                    mediaController.show(10);
                }
                else {
                    videoLayout.start();
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
                        textView.setText("Paused.");
                    }
                    else {
                        videoView.start();
//                        mediaController.hide();
                        textView.setText("Playing");
                    }
                }
                return false;
            }
        });

        holder.getVideoLayout().setVisibility(View.VISIBLE);
    }

    public void bindNote(ViewHolder holder, File file){

        TextView textView = (TextView) holder.getNoteLayout().findViewById(R.id.note_text);

        try {
            FileInputStream fIn = new FileInputStream(file);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow;
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }
            textView.setText(aBuffer);
            myReader.close();
        } catch (Exception e) {
            Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        holder.getNoteLayout().setVisibility(View.VISIBLE);
    }

    public String getMimeTypeFromFile(File file){
        Uri uri = Uri.fromFile(file);
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        String mimeType =  MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        return mimeType;
    }

    //4 assign view to viewholder according to mimetype
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //this will contain all the views in gone mode
        LinearLayout containerLayout;
        //this will contain the individual views in gone mode
        View itemView;
        //this will contain the comib=ned view from above two
        View finalView;

        ViewHolder viewHolder;

//        LayoutInflater layoutInflater = LayoutInflater.from(context);

        finalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layouts, parent, false);
//        containerLayout = (LinearLayout) parent.findViewById(R.id.general_view);

//        switch (viewType) {
//            case 1:
////                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layouts, parent, false);
//                itemView = parent.findViewById(R.id.image);
//                containerLayout.addView(itemView);
//                break;
//
//            case 2:
////                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos, parent, false);
////                viewHolder = new ViewHolder(view);
////                return viewHolder;
//                itemView = parent.findViewById(R.id.video);
//                containerLayout.addView(itemView);
//                break;
//
//            case 3:
////                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audios, parent, false);
////                viewHolder = new ViewHolder(view);
////                return viewHolder;
//                itemView = parent.findViewById(R.id.audio);
//                containerLayout.addView(itemView);
//                break;
//
//            case 4:
////                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes, parent, false);
////                viewHolder = new ViewHolder(view);
////                return viewHolder;
//                itemView = parent.findViewById(R.id.note);
//                containerLayout.addView(itemView);
//                break;
//
//            default:
//                return null;
//        }


//        finalView.setVisibility(View.VISIBLE);
        viewHolder = new ViewHolder(finalView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mediaFiles.length;
    }

    //3 return mimetypes from the list of files
    @Override
    public int getItemViewType(int position) {
        File file = mediaFiles[position];
        String mimeType = getMimeTypeFromFile(file);
        if        (mimeType.equals("image/jpeg")) {
            return MIME_TYPE_IMAGE;
        } else if (mimeType.equals("video/mp4")){
            return MIME_TYPE_VIDEO;
        } else if (mimeType.equals("audio/aac")){
            return MIME_TYPE_AUDIO;
        } else if (mimeType.equals("text/plain")){
            return MIME_TYPE_NOTE;
        }
        return MIME_TYPE_INVALID;
    }
}
