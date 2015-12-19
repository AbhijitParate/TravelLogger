package abhijit.travellogger.MediaManager;

/*
 * Created by abhijit on 10/27/15.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import abhijit.travellogger.ApplicationUtility.Constants;
import abhijit.travellogger.ApplicationUtility.Helper;
import abhijit.travellogger.ApplicationUtility.TravelLogger;
import abhijit.travellogger.R;
import abhijit.travellogger.SharedPreferencesHandler;

public class MediaViewAdapter extends RecyclerView.Adapter<MediaViewHolder> {

    //1. Mime types for view
    private static final int MIME_TYPE_INVALID  = 0;
    private static final int MIME_TYPE_IMAGE    = 1;
    private static final int MIME_TYPE_VIDEO    = 2;
    private static final int MIME_TYPE_AUDIO    = 3;
    private static final int MIME_TYPE_NOTE     = 4;

    public List<File> getMediaList() {
        return mediaList;
    }

    List<File> mediaList ;

    //Receive data from mainActivity
    public MediaViewAdapter(File[] folderFiles){
        mediaList = new LinkedList<>(Arrays.asList(folderFiles));
    }

    //3 return mimetypes from the list of files
    @Override
    public int getItemViewType(int position) {
        File file = mediaList.get(position);
        String mimeType = Helper.getMimeTypeFromFile(file);
        switch (mimeType) {
            case "image/jpeg":
                return MIME_TYPE_IMAGE;
            case "video/mp4":
                return MIME_TYPE_VIDEO;
            case "audio/aac":
                return MIME_TYPE_AUDIO;
            case "text/plain":
                return MIME_TYPE_NOTE;
            default:
                return MIME_TYPE_INVALID;
        }
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    //4 assign view to view holder according to mime type
    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        MediaViewHolder mediaViewHolder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_layouts, parent, false);
        mediaViewHolder = new MediaViewHolder(view);
        return mediaViewHolder;
    }

    //5. bind view to viewHolder
    @Override
    public void onBindViewHolder(MediaViewHolder holder, int position) {
        File mediaFile = mediaList.get(position);
        MediaView mediaView = new MediaView(holder, mediaFile);
        mediaView.setItemHolder();
    }

    public void updateList(File[] mediaFiles){
        mediaList = new LinkedList<>(Arrays.asList(mediaFiles));
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        mediaList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(int position, File mediaFile ){
        mediaList.add(position, mediaFile);
        notifyItemInserted(position);
    }
}
