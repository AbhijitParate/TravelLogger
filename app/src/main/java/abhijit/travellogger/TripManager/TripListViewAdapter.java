package abhijit.travellogger.TripManager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import abhijit.travellogger.ApplicationUtility.Constants;
import abhijit.travellogger.R;
import abhijit.travellogger.SharedPreferencesHandler;

/*
 * Created by abhijit on 12/7/15.
 */
public class TripListViewAdapter extends ArrayAdapter<Trip> {

    private Context appContext;
    private int tripResource;
    private List<Trip> tripList;

    public TripListViewAdapter(Context context, int resource, List<Trip> list) {
        super(context, resource, list);
        this.appContext = context;
        this.tripResource = resource;
        this.tripList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(tripResource, parent, false);
        }

        Trip trip = tripList.get(position);
        TextView tripTitle = (TextView) convertView.findViewById(R.id.title);
        TextView tripCreateDate = (TextView) convertView.findViewById(R.id.date_created);

        String tripName = SharedPreferencesHandler.getSharedPref(Constants.SP_TRIP_NAME);
        if(tripName != null) {
            Log.d(String.valueOf(position) + " tripName", tripName);
            Log.d(String.valueOf(position) + " tripTitle", trip.getTitle());
        }

        if(trip != null) {
            tripTitle.setText(trip.getTitle());
            if( tripName!= null && tripName.equals(trip.getTitle())){
                tripTitle.setBackgroundColor(Color.parseColor("#FF80CBC4"));
            } else  {
                tripTitle.setBackgroundColor(Color.WHITE);
            }
            tripCreateDate.setText(String.valueOf(trip.getCreateDate()));
        }
        return convertView;
    }
}
