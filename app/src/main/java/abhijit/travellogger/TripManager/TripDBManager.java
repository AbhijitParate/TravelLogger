package abhijit.travellogger.TripManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/*
 * Created by abhijit on 12/7/15.
 */
public class TripDBManager {

    private Context appContext;
    private TripsDBHelper tripsDBHelper;
    private SQLiteDatabase sqLiteDatabase;
    private TripDAO tripDAO;

    public TripDBManager(Context context) {
        this.appContext = context;
        tripsDBHelper = new TripsDBHelper(this.appContext);
        sqLiteDatabase = tripsDBHelper.getWritableDatabase();
        tripDAO = new TripDAO(sqLiteDatabase);
    }

    public long addTrip(Trip trip) {
        return this.tripDAO.addTrip(trip);
    }

    public boolean updateTrip(Trip trip) {
        return this.tripDAO.updateTrip(trip);
    }

    public boolean deleteTrip(Trip trip) {
        return this.tripDAO.deleteTrip(trip);
    }

    public Trip get(long id) {
        return this.tripDAO.getTrip(id);
    }

    public List<Trip> getAllTrips() {
        return this.tripDAO.getAll();
    }
}
