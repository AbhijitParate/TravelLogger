package abhijit.travellogger;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

/*
 * Created by abhijit on 11/16/15.
 */
public class CaptureImage {

    public static Intent takePhoto(){
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile = null;
        try {
            imageFile = saveImage();
//            imageFile = File.createTempFile( InitiateApplication.getTimeStamp(), "", InitiateApplication.getAppFolderTemp());
        } catch (IOException e) {
            e.printStackTrace();
        }
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        return cameraIntent;
    }

    public static File saveImage() throws IOException {
        String imageName = "IMAGE_" + InitiateApplication.getTimeStamp();
        return File.createTempFile(imageName, ".jpg", InitiateApplication.getAppFolderCamera());
    }

}
