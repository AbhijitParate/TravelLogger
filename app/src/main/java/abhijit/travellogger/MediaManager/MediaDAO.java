package abhijit.travellogger.MediaManager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by abhijit on 12/14/15.
 */
public class MediaDAO {

    private SQLiteDatabase db;

    public MediaDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public Long addMedia(Media media) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaDBHelper.COL_TITLE, media.getTitle());
        contentValues.put(MediaDBHelper.COL_LONGITUDE, media.getLongitude());
        contentValues.put(MediaDBHelper.COL_LATITUDE, media.getLatitude());
        contentValues.put(MediaDBHelper.COL_LOCATION, media.getLocation());
        contentValues.put(MediaDBHelper.COL_FILENAME, media.getFileName());
        contentValues.put(MediaDBHelper.COL_TRIPNAME, media.getTripName());
        long res = db.insert(MediaDBHelper.TABLE_NAME, null, contentValues);
        db.close();
        return res;
    }

    public boolean updateMedia(Media media) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaDBHelper.COL_ID, media.getMediaId());
        contentValues.put(MediaDBHelper.COL_TITLE, media.getTitle());
        contentValues.put(MediaDBHelper.COL_LONGITUDE, media.getLongitude());
        contentValues.put(MediaDBHelper.COL_LATITUDE, media.getLatitude());
        contentValues.put(MediaDBHelper.COL_LOCATION, media.getLocation());
        contentValues.put(MediaDBHelper.COL_FILENAME, media.getFileName());
        contentValues.put(MediaDBHelper.COL_TRIPNAME, media.getTripName());
        contentValues.put(MediaDBHelper.COL_UPDATE_DATE, media.getUpdateDate());
        return db.update(MediaDBHelper.TABLE_NAME, contentValues, MediaDBHelper.COL_ID + "=?", new String[]{media.getMediaId() + ""}) > 0;
    }

    public boolean deleteMedia(Media media) {
        return db.delete(MediaDBHelper.TABLE_NAME, MediaDBHelper.COL_FILENAME + "=?", new String[]{media.getFileName() + ""}) > 0;
    }

    public Media getMedia(String fileName) {
        Media media = null;
        Cursor c = db.query(true, MediaDBHelper.TABLE_NAME, MediaDBHelper.ALL_COLUMNS, MediaDBHelper.COL_FILENAME + "=?", new String[]{fileName + ""}, null, null, null, null, null);

        if (c != null && c.moveToFirst()) {
            media = buildFromCursor(c);
            if (!c.isClosed()) {
                c.close();
            }
        }
        return media;
    }

    public List<Media> getAll() {
        List<Media> mediaList = new ArrayList<Media>();

        String dbQuery = "SELECT * FROM " + MediaDBHelper.TABLE_NAME + " ORDER BY " + MediaDBHelper.COL_UPDATE_DATE + " DESC";
        Cursor c = db.rawQuery(dbQuery, null);

        if (c != null && c.moveToFirst()) {
            do {
                Media media = buildFromCursor(c);
                if (media != null) {
                    mediaList.add(media);
                }
            } while (c.moveToNext());
        }
        return mediaList;
    }


    private Media buildFromCursor(Cursor c) {
        Media media = null;
        if (c != null) {
            media = new Media();
            media.setMediaId(c.getInt(0));
            media.setTitle(c.getString(1));
            media.setLatitude(c.getString(2));
            media.setLongitude(c.getString(3));
            media.setLocation(c.getString(4));
            media.setFileName(c.getString(5));
            media.setTripName(c.getString(6));
            media.setCreateDate(c.getString(7));
            media.setUpdateDate(c.getString(8));
        }
        return media;
    }

    public List<Media> getAllMediaForTrip(String tripName) {
        List<Media> mediaList = new ArrayList<Media>();

        String dbQuery = "SELECT * FROM " + MediaDBHelper.TABLE_NAME +
                " WHERE " + MediaDBHelper.COL_TRIPNAME + " = " + "'" + tripName + "'" +
                " ORDER BY " + MediaDBHelper.COL_UPDATE_DATE + " DESC";
        Cursor c = db.rawQuery(dbQuery, null);

        if (c != null && c.moveToFirst()) {
            do {
                Media media = buildFromCursor(c);
                if (media != null) {
                    mediaList.add(media);
                }
            } while (c.moveToNext());
        }
        return mediaList;
    }
}
