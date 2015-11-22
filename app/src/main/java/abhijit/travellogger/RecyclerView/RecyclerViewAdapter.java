package abhijit.travellogger.RecyclerView;

/*
 * Created by abhijit on 10/27/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import abhijit.travellogger.ApplicationUtility.Helper;
import abhijit.travellogger.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    //1. Mime types for view
    private static final int MIME_TYPE_INVALID  = 0;
    private static final int MIME_TYPE_IMAGE    = 1;
    private static final int MIME_TYPE_VIDEO    = 2;
    private static final int MIME_TYPE_AUDIO    = 3;
    private static final int MIME_TYPE_NOTE     = 4;

    private File[] mediaFiles;
    private final Context context;

    //Receive data from mainActivity
    public RecyclerViewAdapter(File[] folderFiles, Context mainContext){
        mediaFiles = folderFiles;
        context = mainContext;
    }

    //3 return mimetypes from the list of files
    @Override
    public int getItemViewType(int position) {
        File file = mediaFiles[position];
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
        return mediaFiles.length;
    }

    //4 assign view to viewholder according to mimetype
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerViewHolder recyclerViewHolder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layouts, parent, false);
        recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    //5. bind view to viewHolder
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        File mediaFile = mediaFiles[position];
        ItemView itemView = new ItemView(holder, mediaFile);
        itemView.setItemHolder();
    }

    public void deleteItem(int position) {
        List<File> mediaList = Arrays.asList(mediaFiles);
        mediaList.remove(position);
        mediaFiles = new File[mediaList.size()];
        mediaList.toArray(mediaFiles);
        notifyItemRemoved(position);
    }
}
