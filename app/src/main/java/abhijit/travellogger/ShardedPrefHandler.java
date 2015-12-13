package abhijit.travellogger;

import android.content.Context;
import android.content.SharedPreferences;

import abhijit.travellogger.ApplicationUtility.TravelLogger;

/*
 * Created by abhijit on 12/9/15.
 */
public class ShardedPrefHandler {

    public static String TLPreferences = "TravelLoggerData";

    public static String getSharedPref(String prefKey) {
        String SPValue = "n/a";
        SharedPreferences TLData = TravelLogger.getAppContext().getSharedPreferences(TLPreferences, Context.MODE_PRIVATE);
        SPValue = TLData.getString(prefKey, null);
        return SPValue;
    }

    public static void setSharedPref(String sharedPrefKey, String sharedPrefValue){
        SharedPreferences TLData = TravelLogger.getAppContext().getSharedPreferences(TLPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = TLData.edit();
        editor.putString(sharedPrefKey, sharedPrefValue);
        editor.apply();
    }

    public void clearSharedPreference() {
        SharedPreferences TLData = TravelLogger.getAppContext().getSharedPreferences(TLPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = TLData.edit();
        editor.clear();
        editor.apply();
    }
}
