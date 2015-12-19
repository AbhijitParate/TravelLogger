package abhijit.travellogger.MediaManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;


/*
 * Created by abhijit on 12/14/15.
 */
public class MediaDBManager {

    private Context appContext;
    private MediaDBHelper mediaDBHelper;
    private SQLiteDatabase sqLiteDatabase;
    private MediaDAO mediaDAO;

    public MediaDBManager(Context context) {
        this.appContext = context;
        mediaDBHelper = new MediaDBHelper(this.appContext);
        sqLiteDatabase = mediaDBHelper.getWritableDatabase();
        mediaDAO = new MediaDAO(sqLiteDatabase);
    }

    public long addMedia(Media media) {
        return this.mediaDAO.addMedia(media);
    }

    public boolean updateMedia(Media media) {
        return this.mediaDAO.updateMedia(media);
    }

    public boolean deleteMedia(Media media) {
        return this.mediaDAO.deleteMedia(media);
    }

    public Media getMedia(String fileName) {
        return this.mediaDAO.getMedia(fileName);
    }

    public List<Media> getAllMediaForTrip(String tripName){
        return this.mediaDAO.getAllMediaForTrip(tripName);
    }

    public List<Media> getAllTrips() {
        return this.mediaDAO.getAll();
    }
}
