package abhijit.travellogger.GalleryService;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

import abhijit.travellogger.ApplicationUtility.Constants;
import abhijit.travellogger.GPSService.AddressService;
import abhijit.travellogger.GPSService.GPSService;
import abhijit.travellogger.MediaManager.Media;
import abhijit.travellogger.MediaManager.MediaDBManager;
import abhijit.travellogger.R;
import abhijit.travellogger.SharedPreferencesHandler;

public class EditGallery extends AppCompatActivity {

    private static final int ACTIVITY_START_GALLERY_APP = 0;

    File galleryFile;
    AlertDialog.Builder alertBuilder;
    AlertDialog alert;
    GPSService gpsService;
    Location location;
    File fileSource;
    String fileType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gallery);

        gpsService = new GPSService(EditGallery.this);

        Intent receivedIntent = getIntent();
        if(receivedIntent.getExtras() != null) {
            String action = receivedIntent.getAction();
            if(action.equals("gallery")) {
                if(receivedIntent.hasExtra("type")) {
                    String type = (String) receivedIntent.getExtras().get("type");
                    if(type != null) {
                        switch (type) {
                            case "image":
                                fileType = "image";
                                Intent imageGalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                imageGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                imageGalleryIntent.setType("*/*");
                                String[] imageMT = {"image/jpg", "image/jpeg"};
                                imageGalleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, imageMT);
                                imageGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(imageGalleryIntent, "Select a Picture"), ACTIVITY_START_GALLERY_APP);
                                break;

                            case "video":
                                fileType = "video";
                                Intent videoGalleryIntent = new Intent();
                                videoGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                videoGalleryIntent.setType("*/*");
                                String[] videoMT = {"video/mp4"};
                                videoGalleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, videoMT);
                                videoGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(videoGalleryIntent, "Select a Video"), ACTIVITY_START_GALLERY_APP);
                                break;

                            case "audio":
                                fileType = "audio";
                                Intent audioGalleryIntent = new Intent();
                                audioGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                audioGalleryIntent.setType("*/*");
                                String[] audioMT = {"audio/aac"};
                                audioGalleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, audioMT);
                                audioGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(audioGalleryIntent, "Select an Audio"), ACTIVITY_START_GALLERY_APP);
                                break;
                        }
                    }
                }
            }
        }
    }

    private void buildAlertDialog(){
        LayoutInflater inflater = LayoutInflater.from(EditGallery.this);
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
                        Address address = AddressService.getLocationName(EditGallery.this, location);
                        editTextTitle.setText(address.getFeatureName());
                        editTextStreet.setText(address.getAddressLine(1));
                        editTextCity.setText(address.getLocality());
                        editTextZip.setText(address.getPostalCode());
                    }
                }
            }
        });

        alertBuilder = new AlertDialog.Builder(EditGallery.this);
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
//                        galleryFile = getMediaFile();
//                        galleryFile.delete();
//                        Log.d("File name", galleryFile.getAbsolutePath());
                        MediaDBManager mediaManager = new MediaDBManager(EditGallery.this);
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
                        newMedia.setFileName(FilenameUtils.getBaseName(galleryFile.getName()));
                        newMedia.setTripName(SharedPreferencesHandler.getSharedPref(Constants.SP_TRIP_NAME));

                        mediaManager.addMedia(newMedia);
                        finishWithResultOk();
                        Toast.makeText(getApplicationContext(), "New media added.", Toast.LENGTH_SHORT).show();
                    }
                });
        alert = alertBuilder.create();
        alert.show();
    }

    private void finishWithResultOk() {
        Intent intent = new Intent();
        intent.putExtra("media", galleryFile);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void finishWithResultCanceled() {
//        galleryFile.delete();
//        fileSource.delete();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data ){
        if(requestCode == ACTIVITY_START_GALLERY_APP) {
            switch (resultCode) {
                case RESULT_OK:
                    switch (fileType) {
                        case "image":
                                galleryFile = GalleryImage.saveGalleryMedia(data, "IMAGE");
                                Log.d("Switch", "Audio");
                                break;

                        case "video":
                                galleryFile = GalleryImage.saveGalleryMedia(data, "VIDEO");
                                Log.d("Switch", "Audio");
                                break;
                        case "audio":
                                galleryFile = GalleryImage.saveGalleryMedia(data, "AUDIO");
                                Log.d("Switch", "Audio");
                                break;
                        }
                        Log.d("File name", galleryFile.getAbsolutePath());

                    buildAlertDialog();
//                    Toast.makeText(this, "File imported successfully.", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    finishWithResultCanceled();
//                    Toast.makeText(this, "File import canceled.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
