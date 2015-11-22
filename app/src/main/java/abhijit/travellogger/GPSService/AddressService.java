package abhijit.travellogger.GPSService;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/*
 * Created by abhijit on 11/21/15.
 */
public class AddressService {

    public static String getLocationName(Context context, Location location){

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        String streetAddress = "";

        try {
            if(location!= null) {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

//            String streetAddress = address + "\n" + city + ", " + state + "\n" + postalCode + ", " + country + "\nName: " + knownName);
                streetAddress = address + ", " + city + ", " + state + " " + postalCode;
            } else {
                streetAddress = "Not available.";
            }

        }
        catch (IOException e){
            e.printStackTrace();
            Toast.makeText(context, "Can't get location name.", Toast.LENGTH_SHORT).show();
        }

        return streetAddress;
    }
}
