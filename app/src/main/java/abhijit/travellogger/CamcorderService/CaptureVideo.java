package abhijit.travellogger.CamcorderService;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

import abhijit.travellogger.ApplicationUtility.InitiateApplication;

/*
 * Created by abhijit on 11/16/15.
 */
public class CaptureVideo {

    public static String TEMP_VIDEO = "camera_video.mp4";

    public static Intent prepareCamcorderIntent(){
        Intent videoCameraIntent = new Intent();
        videoCameraIntent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
        File videoFile = new File(InitiateApplication.getAppFolderTemp(), TEMP_VIDEO);
        videoCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
        videoCameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        return videoCameraIntent;
    }
}
