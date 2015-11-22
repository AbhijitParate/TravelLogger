package abhijit.travellogger.RecyclerView.Views;

import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import abhijit.travellogger.RecyclerView.RecyclerViewHolder;

/*
 * Created by abhijit on 11/20/15.
 */
public class NoteView {

    private RecyclerViewHolder itemHolder;
    private File itemFile;

    public NoteView(RecyclerViewHolder recyclerViewHolder, File file) {
        itemHolder = recyclerViewHolder;
        itemFile = file;
    }

    public RecyclerViewHolder setItemHolder(){
        itemHolder.getNoteTitle().setText("Title for Note.");
        itemHolder.getNoteBody().setTag("Notes");

        try {
            FileInputStream fIn = new FileInputStream(itemFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow;
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
            }
            itemHolder.getNoteBody().setText(aBuffer);
            myReader.close();
        } catch (Exception e) {
            Toast.makeText(itemHolder.getNoteBody().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        itemHolder.getNoteLayout().setVisibility(View.VISIBLE);
        return itemHolder;
    }
}
