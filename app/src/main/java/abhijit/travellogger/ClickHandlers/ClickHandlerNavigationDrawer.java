package abhijit.travellogger.ClickHandlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import abhijit.travellogger.ApplicationUtility.Constants;
import abhijit.travellogger.R;
import abhijit.travellogger.ShardedPrefHandler;
import abhijit.travellogger.TravelLoggerHomeActivity;
import abhijit.travellogger.TripManager.TripManagerActivity;

/*
 * Created by abhijit on 12/9/15.
 */
public class ClickHandlerNavigationDrawer
        implements NavigationView.OnNavigationItemSelectedListener {

    //Calling activity
    private Activity activity;
    private Context activityContext;
    private DrawerLayout activityDrawer;

    public ClickHandlerNavigationDrawer(Activity activity, Context context, DrawerLayout drawer) {
        this.activity = activity;
        activityContext = context;
        activityDrawer = drawer;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_trips) {
            Toast.makeText(activityContext, "Trips", Toast.LENGTH_SHORT).show();
            item.setChecked(true);
            ShardedPrefHandler.setSharedPref(Constants.SP_NAVIGATION_ACTIVITY, "Trips");
            Intent tripManager= new Intent(activityContext,TripManagerActivity.class);
            tripManager.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            tripManager.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            tripManager.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(tripManager);
        } else if (id == R.id.nav_home) {
            Toast.makeText(activityContext, "Home", Toast.LENGTH_SHORT).show();
            item.setChecked(true);
            ShardedPrefHandler.setSharedPref(Constants.SP_NAVIGATION_ACTIVITY, "Trips");
            Intent tripManager= new Intent(activityContext,TravelLoggerHomeActivity.class);
            tripManager.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            tripManager.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(tripManager);
        } else if (id == R.id.nav_settings) {
            Toast.makeText(activityContext, "Settings", Toast.LENGTH_SHORT).show();
            ShardedPrefHandler.setSharedPref(Constants.SP_NAVIGATION_ACTIVITY, "Trips");
        } else if (id == R.id.nav_about) {
            Toast.makeText(activityContext, "About", Toast.LENGTH_SHORT).show();
            ShardedPrefHandler.setSharedPref(Constants.SP_NAVIGATION_ACTIVITY, "Trips");
        }
        activityDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void itemCheckHandler(){

    }
}
