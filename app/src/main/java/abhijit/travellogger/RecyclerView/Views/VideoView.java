package abhijit.travellogger.RecyclerView.Views;

import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;

import abhijit.travellogger.RecyclerView.RecyclerViewHolder;

/*
 * Created by abhijit on 11/20/15.
 */
public class VideoView {

    private RecyclerViewHolder itemHolder;
    private File itemFile;

    public VideoView(RecyclerViewHolder recyclerViewHolder, File file) {
        itemHolder = recyclerViewHolder;
        itemFile = file;
    }

    public void setItemHolder(){

        itemHolder.getVideoTitle().setText("Title for Video.");
        itemHolder.getVideoView().setTag("VideoView");
        itemHolder.getVideoView().setVideoPath(itemFile.getAbsolutePath());
        itemHolder.getVideoView().requestFocus();
        itemHolder.getVideoView().seekTo(5000);

        itemHolder.getVideoView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getActionMasked()) {
                    if (itemHolder.getVideoView().isPlaying()) {
                        itemHolder.getVideoView().pause();
                        itemHolder.getVideoTitle().setText("Paused.");
                    } else {
                        itemHolder.getVideoView().start();
                        itemHolder.getVideoTitle().setText("Playing");
                    }
                }
                return false;
            }
        });

        itemHolder.getVideoLayout().setVisibility(View.VISIBLE);
    }
}
