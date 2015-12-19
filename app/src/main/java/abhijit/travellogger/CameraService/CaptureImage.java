package abhijit.travellogger.CameraService;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

import abhijit.travellogger.ApplicationUtility.Helper;
import abhijit.travellogger.ApplicationUtility.InitiateApplication;

/*
 * Created by abhijit on 11/16/15.
 */
public class CaptureImage {

    public static String TEMP_IMAGE = "camera_image.jpg";

    public static Intent prapareCameraIntent(){
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile = new File(InitiateApplication.getAppFolderTemp(), TEMP_IMAGE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        return cameraIntent;
    }
}
