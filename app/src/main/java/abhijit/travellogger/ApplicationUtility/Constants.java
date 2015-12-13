package abhijit.travellogger.ApplicationUtility;

import android.Manifest;

/*
 * Created by abhijit on 12/9/15.
 */
public class Constants {

    //SharedPreferences keys
    public static String SP_IS_FIRST_LAUNCH = "True";
    public static String SP_TRIP_NAME = "TripName";
    public static String SP_NAVIGATION_ACTIVITY = "NavActivity";

    public static int SP_HOME = 0;
    public static int SP_TRIPS = 1;
    public static int SP_SETTINGS = 2;
    public static int SP_ABOUT = 3;

    public static String[] PERMISSION_LIST = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET};

}
