package abhijit.travellogger.RecyclerView;

import java.io.File;

import abhijit.travellogger.ApplicationUtility.Helper;
import abhijit.travellogger.RecyclerView.Views.AudioView;
import abhijit.travellogger.RecyclerView.Views.ImageView;
import abhijit.travellogger.RecyclerView.Views.NoteView;
import abhijit.travellogger.RecyclerView.Views.VideoView;

/*
 * Created by abhijit on 11/20/15.
 */
public class ItemView {

    private RecyclerViewHolder itemHolder;
    private File itemFile;

    public ItemView(RecyclerViewHolder recyclerViewHolder, File file) {
        itemHolder = recyclerViewHolder;
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
