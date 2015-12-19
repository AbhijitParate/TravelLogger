package abhijit.travellogger.TripManager;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import abhijit.travellogger.ApplicationUtility.Constants;
import abhijit.travellogger.SharedPreferencesHandler;

/*
 * Created by abhijit on 12/7/15.
 */
public class TripListViewClickListeners implements
        AdapterView.OnItemSelectedListener,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private Context appContext;
    private TripDBManager dbManager;
    private TripListViewAdapter tripAdapter;

    public TripListViewClickListeners(Context context, TripDBManager tripDBManager, TripListViewAdapter tripListViewAdapter){
        appContext = context;
        dbManager = tripDBManager;
        tripAdapter = tripListViewAdapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferencesHandler.setSharedPref(Constants.SP_TRIP_NAME, tripAdapter.getItem(position).getTitle());
        Toast.makeText(view.getContext()
                , "Trip changed to " + tripAdapter.getItem(position).getTitle() + "."
                , Toast.LENGTH_SHORT).show();
        tripAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final AlertDialog.Builder deleteConfirmationDialog = new AlertDialog.Builder(appContext);
        deleteConfirmationDialog.setTitle("Delete trip.");
        deleteConfirmationDialog.setMessage("Are you sure you want to delete?");
        final int viewPosition = position;

        deleteConfirmationDialog.setPositiveButton("OK", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tripName = SharedPreferencesHandler.getSharedPref(Constants.SP_TRIP_NAME);
                if(tripAdapter.getItem(viewPosition).getTitle().equals(tripName)){
                    SharedPreferencesHandler.setSharedPref(Constants.SP_TRIP_NAME, null);
                }
                dbManager.deleteTrip(tripAdapter.getItem(viewPosition));
                Toast.makeText(appContext, "Trip '" + tripAdapter.getItem(viewPosition).getTitle() + "' deleted.", Toast.LENGTH_LONG).show();
                tripAdapter.remove(tripAdapter.getItem(viewPosition));
                tripAdapter.setNotifyOnChange(true);
                tripAdapter.notifyDataSetChanged();
            }
        });

        deleteConfirmationDialog.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        deleteConfirmationDialog.show();
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
