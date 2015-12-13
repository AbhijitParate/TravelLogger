package abhijit.travellogger.ApplicationUtility;

import android.app.Application;
import android.content.Context;

/*
 * Created by abhijit on 12/3/15.
 */
public class TravelLogger extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        TravelLogger.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return TravelLogger.context;
    }
}
