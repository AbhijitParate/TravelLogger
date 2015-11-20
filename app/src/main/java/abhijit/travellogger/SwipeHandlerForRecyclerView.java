package abhijit.travellogger;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by abhijit on 11/19/15.
 */
public class SwipeHandlerForRecyclerView {

    public static ItemTouchHelper create(final Context context, final RecyclerView recyclerView){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int itemPosition = viewHolder.getAdapterPosition();

                if(direction == ItemTouchHelper.LEFT){
                    Toast.makeText(context.getApplicationContext(), "Left swipe", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context.getApplicationContext(), "Right swipe", Toast.LENGTH_SHORT).show();
                }
                FileGenerator fileGenerator = new FileGenerator();
                File mediaFiles[] = fileGenerator.getMediaFiles(InitiateApplication.getAppFolder());
                File fileToDelete = mediaFiles[itemPosition];
                if (fileToDelete.delete()){
//                    List<File> mediaList = Arrays.asList(mediaFiles);
//                    mediaList.remove(itemPosition);
//                    mediaFiles = new File[mediaList.size()];
//                    mediaList.toArray(mediaFiles);
                    Toast.makeText(context.getApplicationContext(), "File deleted successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context.getApplicationContext(), "Failed to delete file.", Toast.LENGTH_SHORT).show();
                }
//                recyclerView.removeViewAt(itemPosition);
//                recyclerView.getAdapter().notifyItemRemoved(itemPosition);
//                recyclerView.getAdapter().notifyItemRangeChanged(itemPosition, mediaFiles.length - 1);
//                recyclerView.getAdapter().notifyDataSetChanged();

                mediaFiles = fileGenerator.getMediaFiles(InitiateApplication.getAppFolder());
                RecyclerView.Adapter newAdapter = new ViewAdapter(mediaFiles, context.getApplicationContext());
                recyclerView.swapAdapter(newAdapter, false);
            }
        };

        return new ItemTouchHelper(simpleCallback);
    }

}
