package abhijit.travellogger.CameraService;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.isseiaoki.simplecropview.CropImageView;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import abhijit.travellogger.ApplicationUtility.Constants;
import abhijit.travellogger.ApplicationUtility.Helper;
import abhijit.travellogger.ApplicationUtility.InitiateApplication;
import abhijit.travellogger.GPSService.AddressService;
import abhijit.travellogger.GPSService.GPSService;
import abhijit.travellogger.MediaManager.Media;
import abhijit.travellogger.MediaManager.MediaDBManager;
import abhijit.travellogger.R;
import abhijit.travellogger.SharedPreferencesHandler;

public class EditImageActivity extends AppCompatActivity {

    private static final int ACTIVITY_START_CAMERA_APP = 0;
    CropImageView cropImageView;
    Button cropButton;
    File imageFile;
    AlertDialog.Builder alertBuilder;
    AlertDialog alert;
    GPSService gpsService;
    Location location;
    File imageSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        gpsService = new GPSService(EditImageActivity.this);

        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        cropButton = (Button) findViewById(R.id.cropButton);
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageFile == null) {
                    buildAlertDialog();
                }
            }
        });

        //Dispatch camera intent
        Intent cameraIntent = CaptureImage.prapareCameraIntent();
        startActivityForResult(cameraIntent, ACTIVITY_START_CAMERA_APP);
    }

    private void buildAlertDialog(){
        LayoutInflater inflater = LayoutInflater.from(EditImageActivity.this);
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
                        Address address = AddressService.getLocationName(EditImageActivity.this, location);
                        editTextTitle.setText(address.getFeatureName());
                        editTextStreet.setText(address.getAddressLine(1));
                        editTextCity.setText(address.getLocality());
                        editTextZip.setText(address.getPostalCode());
                    }
                }
            }
        });

        alertBuilder = new AlertDialog.Builder(EditImageActivity.this);
        alertBuilder.setTitle("DETAILS");
        alertBuilder.setView(dialogLayout);
        alertBuilder.setCancelable(false);
        alertBuilder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertBuilder.setPositiveButton("SAVE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        imageFile = getCroppedImage();
                        MediaDBManager mediaManager = new MediaDBManager(EditImageActivity.this);
                        Media newMedia = new Media();

                        newMedia.setTitle(String.valueOf(editTextTitle.getText()));
                        String locationAddress = editTextTitle.getText() + "," + editTextStreet.getText()
                                + "," + editTextCity.getText() + "-" + editTextZip.getText();
                        newMedia.setLocation(locationAddress);
                        if(location != null) {
                            newMedia.setLongitude(String.valueOf(location.getLongitude()));
                            newMedia.setLatitude(String.valueOf(location.getLatitude()));
                        }
                        newMedia.setFileName(FilenameUtils.getBaseName(imageFile.getName()));
                        newMedia.setTripName(SharedPreferencesHandler.getSharedPref(Constants.SP_TRIP_NAME));

                        mediaManager.addMedia(newMedia);
                        imageSource.delete();
                        finishWithResultOk();
                        Toast.makeText(getApplicationContext(), "New media added." , Toast.LENGTH_SHORT).show();
                    }
                });
        alert = alertBuilder.create();
        alert.show();
    }

    private void finishWithResultOk() {
        Intent intent = new Intent();
        intent.putExtra("media", imageFile);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void finishWithResultCanceled()
    {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private File getCroppedImage(){
        FileOutputStream out = null;
        Bitmap imageBitmap = cropImageView.getCroppedBitmap();
        String destinationImageFileName = "IMAGE_" + Helper.getTimeStamp() + ".jpg" ;
        File croppedImage = new File(InitiateApplication.getAppFolderCamera() , destinationImageFileName);
        try {
            out = new FileOutputStream(croppedImage);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return croppedImage;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data ){
        if(requestCode == ACTIVITY_START_CAMERA_APP) {
            switch (resultCode) {
                case RESULT_OK: // Set image for cropView
                    String sourceImageFilePath = InitiateApplication.getAppFolderTemp() + "/" + CaptureImage.TEMP_IMAGE;
                    imageSource = new File(sourceImageFilePath);
                    Bitmap bitmap = BitmapFactory.decodeFile(imageSource.getAbsolutePath());
                    cropImageView.setImageBitmap(bitmap);
                    Toast.makeText(this, "Image captured successfully.", Toast.LENGTH_SHORT).show();
                    break;

                default: //delete temp image and return to home
                    String imageFilePath = InitiateApplication.getAppFolderTemp() + "/" + CaptureImage.TEMP_IMAGE;
                    imageSource = new File(imageFilePath);
                    imageSource.delete();
                    finishWithResultCanceled();
                    Toast.makeText(this, "Image capture canceled.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
