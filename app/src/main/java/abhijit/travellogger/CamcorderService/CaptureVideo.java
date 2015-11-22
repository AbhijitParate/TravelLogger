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

    public static Intent takeVideo(){
        Intent videoCameraIntent = new Intent();
        videoCameraIntent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);

        File videoFile = null;
        try {
            videoFile = saveVideo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
        videoCameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        return videoCameraIntent;
    }

    private static File saveVideo() throws IOException {
        String videoName = "/VIDEO_" ;// + InitiateApplication.getTimeStamp();
        return File.createTempFile(videoName, ".mp4", InitiateApplication.getAppFolderVideo());
    }
}
