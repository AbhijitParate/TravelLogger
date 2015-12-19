package abhijit.travellogger.MediaManager;

import java.io.File;

import abhijit.travellogger.ApplicationUtility.Helper;
import abhijit.travellogger.MediaManager.Views.AudioView;
import abhijit.travellogger.MediaManager.Views.ImageView;
import abhijit.travellogger.MediaManager.Views.NoteView;
import abhijit.travellogger.MediaManager.Views.VideoView;

/*
 * Created by abhijit on 11/20/15.
 */
public class MediaView {

    private MediaViewHolder itemHolder;
    private File itemFile;

    public MediaView(MediaViewHolder mediaViewHolder, File file) {
        itemHolder = mediaViewHolder;
        itemFile = file;
    }

    public void setItemHolder(){
        String mimeType = Helper.getMimeTypeFromFile(itemFile);

        switch (mimeType){
            case "image/jpeg":
                ImageView imageView = new ImageView(itemHolder, itemFile);
                imageView.setItemHolder();
                break;
            case "video/mp4":
                VideoView videoView =  new VideoView(itemHolder, itemFile);
                videoView.setItemHolder();
                break;
            case "audio/aac":
                AudioView audioView = new AudioView(itemHolder, itemFile);
                audioView.setItemHolder();
                break;
            case "text/plain":
                NoteView noteView = new NoteView(itemHolder, itemFile);
                noteView.setItemHolder();
                break;
        }
    }

}
