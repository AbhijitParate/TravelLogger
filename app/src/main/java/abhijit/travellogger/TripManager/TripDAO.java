package abhijit.travellogger.TripManager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by abhijit on 12/7/15.
 */
public class TripDAO {
    private SQLiteDatabase db;

    public TripDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public Long addTrip(Trip trip) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TripsDBHelper.COL_TITLE, trip.getTitle());
        return db.insert(TripsDBHelper.TABLE_NAME, null, contentValues);
    }

    public boolean updateTrip(Trip trip) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TripsDBHelper.COL_ID, trip.getTripId());
        contentValues.put(TripsDBHelper.COL_TITLE, trip.getTitle());
        contentValues.put(TripsDBHelper.COL_UPDATE_DATE, trip.getUpdateDate());
        return db.update(TripsDBHelper.TABLE_NAME, contentValues, TripsDBHelper.COL_ID + "=?", new String[]{trip.getTripId() + ""}) > 0;
    }

    public boolean deleteTrip(Trip trip) {
        return db.delete(TripsDBHelper.TABLE_NAME, TripsDBHelper.COL_ID + "=?", new String[]{trip.getTripId() + ""}) > 0;
    }

    public Trip getTrip(long id) {
        Trip trip = null;
        Cursor c = db.query(true, TripsDBHelper.TABLE_NAME, TripsDBHelper.ALL_COLUMNS, TripsDBHelper.COL_ID + "=?", new String[]{id + ""}, null, null, null, null, null);

        if (c != null && c.moveToFirst()) {
            trip = buildFromCursor(c);
            if (!c.isClosed()) {
                c.close();
            }
        }
        return trip;
    }

    public List<Trip> getAll() {
        List<Trip> tripList = new ArrayList<Trip>();

        String dbQuery = "SELECT * FROM " + TripsDBHelper.TABLE_NAME + " ORDER BY " + TripsDBHelper.COL_UPDATE_DATE + " DESC";
        Cursor c = db.rawQuery(dbQuery, null);

        if (c != null && c.moveToFirst()) {
            do {
                Trip trip = buildFromCursor(c);
                if (trip != null) {
                    tripList.add(trip);
                }
            } while (c.moveToNext());
        }
        return tripList;
    }


    private Trip buildFromCursor(Cursor c) {
        Trip trip = null;
        if (c != null) {
            trip = new Trip();
            trip.setTripId(c.getInt(0));
            trip.setTitle(c.getString(1));
            trip.setCreateDate(c.getString(2));
            trip.setUpdateDate(c.getString(3));
        }
        return trip;
    }
}
