package abhijit.travellogger.MediaManager;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;

import abhijit.travellogger.ApplicationUtility.TravelLogger;

/*
 * Created by abhijit on 11/19/15.
 */
public class SwipeHandlerForRecyclerView {

    public static ItemTouchHelper create(final Context context, final RecyclerView recyclerView){
        final ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

                final int itemPosition = viewHolder.getAdapterPosition();
                final MediaViewAdapter adapter = (MediaViewAdapter) recyclerView.getAdapter();

                if(direction == ItemTouchHelper.LEFT){
                    Toast.makeText(context.getApplicationContext(), "Left swipe", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context.getApplicationContext(), "Right swipe", Toast.LENGTH_SHORT).show();
                }

                List<File> mediaList = adapter.getMediaList();
                final File fileToDelete = mediaList.get(itemPosition);

                Snackbar snackbarDelete = Snackbar
                        .make(recyclerView, "Item deleted", Snackbar.LENGTH_LONG)
                        .setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT){
                                    String fileName = FilenameUtils.getBaseName(fileToDelete.getName());
                                    MediaDBManager mediaDBManager = new MediaDBManager(TravelLogger.getAppContext());
                                    Media mediaToDelete = new Media();
                                    mediaToDelete.setFileName(fileName);
                                    mediaDBManager.deleteMedia(mediaToDelete);
                                    fileToDelete.delete();

                                    Toast.makeText(context.getApplicationContext(), "File deleted successfully.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                adapter.addItem(itemPosition, fileToDelete);
                                Snackbar snackbarUndo = Snackbar.make(recyclerView, "Item restored!", Snackbar.LENGTH_SHORT);
                                snackbarUndo.show();
                            }
                        });
                snackbarDelete.show();

                adapter.deleteItem(itemPosition);
             }
        };

        return new ItemTouchHelper(simpleCallback);
    }

}
