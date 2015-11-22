package abhijit.travellogger.RecyclerView;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.List;

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
                final RecyclerViewAdapter adapter = (RecyclerViewAdapter) recyclerView.getAdapter();

                if(direction == ItemTouchHelper.LEFT){
                    Toast.makeText(context.getApplicationContext(), "Left swipe", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context.getApplicationContext(), "Right swipe", Toast.LENGTH_SHORT).show();
                }

                List<File> mediaList = adapter.getMediaList();
                final File fileToDelete = mediaList.get(itemPosition);

                Snackbar snackbarDelete = Snackbar
                        .make(recyclerView , "Item deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                adapter.addItem(itemPosition, fileToDelete);
                                Snackbar snackbarUndo = Snackbar.make(recyclerView, "Item restored!", Snackbar.LENGTH_SHORT);
                                snackbarUndo.show();
                            }
                        });

                snackbarDelete.show();



//                if (fileToDelete.delete()){
                    adapter.deleteItem(itemPosition);
//                    Toast.makeText(context.getApplicationContext(), "File deleted successfully.", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context.getApplicationContext(), "Failed to delete file.", Toast.LENGTH_SHORT).show();
//                }

             }

        };

        return new ItemTouchHelper(simpleCallback);
    }

}
