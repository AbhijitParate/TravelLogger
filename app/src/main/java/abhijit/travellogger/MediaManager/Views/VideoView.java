package abhijit.travellogger.MediaManager.Views;

import android.view.MotionEvent;
import android.view.View;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

import abhijit.travellogger.ApplicationUtility.TravelLogger;
import abhijit.travellogger.MediaManager.Media;
import abhijit.travellogger.MediaManager.MediaDBManager;
import abhijit.travellogger.MediaManager.MediaViewHolder;

/*
 * Created by abhijit on 11/20/15.
 */
public class VideoView {

    private MediaViewHolder itemHolder;
    private File itemFile;

    public VideoView(MediaViewHolder mediaViewHolder, File file) {
        itemHolder = mediaViewHolder;
        itemFile = file;
    }

    public void setItemHolder(){

        itemHolder.getVideoTitle().setText("Title for Video.");

        MediaDBManager mediaManager = new MediaDBManager(TravelLogger.getAppContext());
        Media media = mediaManager.getMedia(FilenameUtils.getBaseName(itemFile.getName()));

        if(media != null){
            itemHolder.getVideoTitle().setText(media.getTitle());
            itemHolder.getVideoDate().setText(media.getCreateDate());
            itemHolder.getVideoLocation().setText(media.getLocation());
        } else {
            itemHolder.getVideoTitle().setText("Not available");
            itemHolder.getVideoDate().setText("Not available");
            itemHolder.getVideoLocation().setText("Not available");
        }

        itemHolder.getVideoView().setVideoPath(itemFile.getAbsolutePath());
        itemHolder.getVideoView().requestFocus();
        itemHolder.getVideoView().seekTo(5000);

        itemHolder.getVideoView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getActionMasked()) {
                    if (itemHolder.getVideoView().isPlaying()) {
                        itemHolder.getVideoView().pause();
                    } else {
                        itemHolder.getVideoView().start();
                    }
                }
                return false;
            }
        });
        itemHolder.getVideoLayout().setVisibility(View.VISIBLE);
    }
}
