package abhijit.travellogger.MediaManager.Views;

import android.view.View;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import abhijit.travellogger.ApplicationUtility.TravelLogger;
import abhijit.travellogger.MediaManager.Media;
import abhijit.travellogger.MediaManager.MediaDBManager;
import abhijit.travellogger.MediaManager.MediaViewHolder;

/*
 * Created by abhijit on 11/20/15.
 */
public class NoteView {

    private MediaViewHolder itemHolder;
    private File itemFile;

    public NoteView(MediaViewHolder mediaViewHolder, File file) {
        itemHolder = mediaViewHolder;
        itemFile = file;
    }

    public MediaViewHolder setItemHolder(){

        MediaDBManager mediaManager = new MediaDBManager(TravelLogger.getAppContext());
        Media media = mediaManager.getMedia(FilenameUtils.getBaseName(itemFile.getName()));

        if(media != null){
            itemHolder.getNoteTitle().setText(media.getTripName());
            itemHolder.getNoteDate().setText(media.getCreateDate());
            itemHolder.getNoteLocation().setText(media.getLocation());
        } else {
            itemHolder.getNoteTitle().setText("Not available");
            itemHolder.getNoteDate().setText("Not available");
            itemHolder.getNoteLocation().setText("Not available");
        }

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
