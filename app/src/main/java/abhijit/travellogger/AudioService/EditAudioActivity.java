package abhijit.travellogger.AudioService;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

import abhijit.travellogger.ApplicationUtility.Constants;
import abhijit.travellogger.ApplicationUtility.Helper;
import abhijit.travellogger.ApplicationUtility.InitiateApplication;
import abhijit.travellogger.CamcorderService.CaptureVideo;
import abhijit.travellogger.GPSService.AddressService;
import abhijit.travellogger.GPSService.GPSService;
import abhijit.travellogger.GalleryService.GalleryImage;
import abhijit.travellogger.MediaManager.Media;
import abhijit.travellogger.MediaManager.MediaDBManager;
import abhijit.travellogger.R;
import abhijit.travellogger.SharedPreferencesHandler;

public class EditAudioActivity extends AppCompatActivity {

    private static final int ACTIVITY_START_AUDIO_APP = 0;
    File audioFile;
    AlertDialog.Builder alertBuilder;
    AlertDialog alert;
    GPSService gpsService;
    Location location;
    File audioSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_audio);
        gpsService = new GPSService(EditAudioActivity.this);

        //Dispatch camera intent
        Intent recorderIntent = new Intent(this, AudioRecord.class);
        startActivityForResult(recorderIntent, ACTIVITY_START_AUDIO_APP);
    }

    private void finishWithResultOk()
    {
        Intent intent = new Intent();
        intent.putExtra("media", audioFile);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void finishWithResultCanceled() {
        if (audioFile != null) {
            audioFile.delete();
        }
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private File getAudioFile(){
        String destinationFileName = "AUDIO_" + Helper.getTimeStamp() + ".aac" ;
        File destination = new File(InitiateApplication.getAppFolderAudio() , destinationFileName);
        GalleryImage.CopyFiles(audioSource, destination);
        return destination;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data ){
        String sourceVideoFilePath = InitiateApplication.getAppFolderTemp() + "/" + AudioRecord.TEMP_AUDIO;
        if(requestCode == ACTIVITY_START_AUDIO_APP) {
            audioSource = new File(sourceVideoFilePath);
            switch (resultCode) {
                case RESULT_OK: // Set image for cropView
                    buildAlertDialog();
                    Toast.makeText(this, "Audio captured successfully.", Toast.LENGTH_SHORT).show();
                    break;
                default: //delete temp image and return to home
                    finishWithResultCanceled();
                    Toast.makeText(this, "Audio capture canceled.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void buildAlertDialog(){
        LayoutInflater inflater = LayoutInflater.from(EditAudioActivity.this);
        View dialogLayout = inflater.inflate(R.layout.edit_media_desc_dialog, null);

        final EditText editTextTitle = (EditText) dialogLayout.findViewById(R.id.edittext_title);
        final EditText editTextStreet = (EditText) dialogLayout.findViewById(R.id.edittext_street);
        final EditText editTextCity = (EditText) dialogLayout.findViewById(R.id.edittext_city);
        final EditText editTextZip = (EditText) dialogLayout.findViewById(R.id.editText_zip);
        final ImageButton getAddress = (ImageButton) dialogLayout.findViewById(R.id.get_gps_button);
        getAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gpsService.canGetLocation()) {
                    if (gpsService.isLocationAvailable()) {
                        location = gpsService.getLocation();
                        Address address = AddressService.getLocationName(EditAudioActivity.this, location);
                        editTextTitle.setText(address.getFeatureName());
                        editTextStreet.setText(address.getAddressLine(1));
                        editTextCity.setText(address.getLocality());
                        editTextZip.setText(address.getPostalCode());
                    }
                }
            }
        });

        alertBuilder = new AlertDialog.Builder(EditAudioActivity.this);
        alertBuilder.setTitle("DETAILS");
        alertBuilder.setView(dialogLayout);
        alertBuilder.setCancelable(false);
        alertBuilder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finishWithResultCanceled();
                    }
                });
        alertBuilder.setPositiveButton("SAVE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        audioFile = getAudioFile();
                        audioSource.delete();

                        MediaDBManager mediaManager = new MediaDBManager(EditAudioActivity.this);
                        Media newMedia = new Media();

                        newMedia.setTitle(String.valueOf(editTextTitle.getText()));
                        String locationAddress = editTextTitle.getText() + "," + editTextStreet.getText()
                                + "," + editTextCity.getText();
                        newMedia.setLocation(locationAddress);
                        if(location != null) {
                            location = gpsService.getLocation();
                            newMedia.setLongitude(String.valueOf(location.getLongitude()));
                            newMedia.setLatitude(String.valueOf(location.getLatitude()));
                        }
                        newMedia.setFileName(FilenameUtils.getBaseName(audioFile.getName()));
                        newMedia.setTripName(SharedPreferencesHandler.getSharedPref(Constants.SP_TRIP_NAME));

                        mediaManager.addMedia(newMedia);
                        finishWithResultOk();
                        Toast.makeText(getApplicationContext(), "New media added.", Toast.LENGTH_SHORT).show();
                    }
                });
        alert = alertBuilder.create();
        alert.show();
    }

}
