package abhijit.travellogger.TripManager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import abhijit.travellogger.ApplicationUtility.Constants;
import abhijit.travellogger.ClickHandlers.ClickHandlerNavigationDrawer;
import abhijit.travellogger.R;
import abhijit.travellogger.ShardedPrefHandler;
import abhijit.travellogger.TravelLoggerHomeActivity;

public class TripManagerActivity
        extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener
{
    private FloatingActionButton fab;
    private ListView tripListView;
    private List<Trip> tripList;
    private TripDBManager dbManager;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_trip_manager);
        toolbar = (Toolbar) findViewById(R.id.toolbar_trip);
        setSupportActionBar(toolbar);
        buildNavDrawer();
        fab = createFAB();
        tripListView = (ListView) findViewById(R.id.list_view_trips);
        dbManager = new TripDBManager(this);
        populateListView();
    }

    @Override
    protected void onStart(){
        super.onStart();
        navigationView.getMenu().getItem(Constants.SP_HOME).setChecked(false);
        navigationView.getMenu().getItem(Constants.SP_TRIPS).setChecked(true);
        navigationView.getMenu().getItem(Constants.SP_SETTINGS).setChecked(false);
        navigationView.getMenu().getItem(Constants.SP_ABOUT).setChecked(false);
    }

    private void populateListView(){
        tripList = dbManager.getAllTrips();
        final TripListViewAdapter tripAdapter = new TripListViewAdapter(this.getBaseContext(), R.layout.listview_item_layout ,tripList);
        tripListView.setAdapter(tripAdapter);
        tripAdapter.setNotifyOnChange(true);
        tripAdapter.notifyDataSetChanged();
        TripListViewClickListeners clickListener = new TripListViewClickListeners(this,dbManager,tripAdapter);
        tripListView.setOnItemClickListener(clickListener);
        tripListView.setOnItemLongClickListener(clickListener);
        tripListView.setOnItemSelectedListener(clickListener);
    }

    private FloatingActionButton createFAB(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_trip);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LayoutInflater li = LayoutInflater.from(view.getContext());
                @SuppressLint("InflateParams") View promptsView = li.inflate(R.layout.dialog_layout_confirmation_add_trip, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                // set prompts.xml to alert dialog builder
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.add_trip_edit_text);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        TripDBManager dbManager = new TripDBManager(TripManagerActivity.this);
                                        Trip newTrip = new Trip();
                                        newTrip.setTitle(userInput.getText().toString());
                                        dbManager.addTrip(newTrip);
                                        populateListView();
                                        Toast.makeText(getApplicationContext(), "New trip added. " + userInput.getText().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                alertDialog.show();
            }
        });
        return fab;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.cancel();
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trip_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void buildNavDrawer(){
        drawer = (DrawerLayout) findViewById(R.id.nav_drawer_layout_trip);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this,
                        drawer,
                        toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close){
                    public void onDrawerClosed(View view) {
                        super.onDrawerClosed(view);
                        fab.setVisibility(View.VISIBLE);
                    }

                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                        fab.setVisibility(View.INVISIBLE);
                    }
                };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view_trip);
        navigationView.setNavigationItemSelectedListener(new ClickHandlerNavigationDrawer(TripManagerActivity.this, this, drawer));

    }

}
