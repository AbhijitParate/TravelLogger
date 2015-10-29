package abhijit.travellogger;

/**
 * Created by abhijit on 10/27/15.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by abhijit on 10/25/15.
 */
public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    private static final int MIME_TYPE_INVALID  = 0;
    private static final int MIME_TYPE_IMAGE    = 1;
    private static final int MIME_TYPE_VIDEO    = 2;

    private File[] mediaFiles;
    private final Context context;
    public ViewAdapter(File[] folderFiles, Context mainContext){
        mediaFiles = folderFiles;
        context = mainContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private VideoView vv;

        public ViewHolder(View view){
            super(view);

            iv = (ImageView) view.findViewById(R.id.imageView);

            vv = (VideoView) view.findViewById(R.id.videoView);
        }

        public ImageView getImageView(){
            return iv;
        }

        public VideoView getVideoView(){
            return vv;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        TO DO : Find proper viewHolder for the file at the position
        File mediaFile = mediaFiles[position] ;
        String mimeType = getMimeTypeFromFile(mediaFile);
        switch (mimeType){
            case "image/jpeg":
                bindImage(holder, mediaFile);
                break;
            case "video/mp4":
                bindVideo(holder, mediaFile);
                break;
        }
    }

    public void bindImage(ViewHolder holder, final File file){
//        Bitmap imageBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 400, 300, true);
//        holder.getImageView().setImageBitmap(imageBitmap);

        holder.getImageView().setClickable(true);
        /*
        holder.getImageView().setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getActionMasked()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(file.toURI().toString()), "image/*");
                    context.startActivity(intent);
                }
                return false;
            }
        });
        */
        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(file.toURI().toString()), "image/*");
                context.startActivity(intent);
            }
        });
        Picasso.with(holder.getImageView().getContext())
                .load(file)
                .resize(768, 432)
                .centerCrop()
                .into(holder.getImageView());
    }

    public void bindVideo(ViewHolder holder, File file){
        final VideoView videoView = holder.getVideoView();
        videoView.setVideoPath(file.getAbsolutePath());
        videoView.requestFocus();
        videoView.seekTo(5000);
        final MediaController mediaController = new MediaController(holder.getVideoView().getContext());
        mediaController.setAnchorView(holder.getVideoView());
        mediaController.setMediaPlayer(holder.getVideoView());
//        mediaController.setClickable(false);
        mediaController.hide();
        videoView.setMediaController(mediaController);
        videoView.setClickable(true);
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
                        mediaController.show(20);
                    }
                    else {
                        videoView.start();
                        mediaController.hide();
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

            default:
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return mediaFiles.length;
    }

    @Override
    public int getItemViewType(int position) {
//        TO DO : return proper view type
        File file = mediaFiles[position];
        String mimeType = getMimeTypeFromFile(file);
        if (mimeType.equals("image/jpeg")) {
            return MIME_TYPE_IMAGE;
        } else {
            if (mimeType.equals("video/mp4")) {
                return MIME_TYPE_VIDEO;
            }
        }
        return MIME_TYPE_INVALID;
    }
}
