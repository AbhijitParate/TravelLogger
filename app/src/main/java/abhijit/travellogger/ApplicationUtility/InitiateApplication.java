package abhijit.travellogger.ApplicationUtility;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by abhijit on 10/28/15.
 */
public class InitiateApplication {

    //App Folder Setup
    public static final File appFolder = new File(Environment.getExternalStorageDirectory() + "/TravelLogger");
    public static final File appFolderCamera = new File(appFolder + "/TLCamera");
    public static final File appFolderVideo = new File(appFolder + "/TLVideo");
    public static final File appFolderAudio = new File(appFolder + "/TLAudio");
    public static final File appFolderNotes = new File(appFolder + "/TLNotes");

    public static File getAppFolderTemp() {
        return appFolderTemp;
    }

    public static final File appFolderTemp = new File(appFolder + "/Temp");

    public static boolean checkAppPermissions(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> missingPermissions = new ArrayList<String>();
            for( String permission : Constants.PERMISSION_LIST ) {
                if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED ){
                    missingPermissions.add(permission);
                }
            }
            if (!missingPermissions.isEmpty()) {
                for (String permission: missingPermissions) {
                    if (activity.shouldShowRequestPermissionRationale(permission)){
                        activity.requestPermissions(new String[]{permission}, 1);
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkDirectoryStructure(Context context){

        boolean appDir;
        String result ="";

//        //Check for base dir
        if (!appFolder.exists() && !appFolder.isDirectory()) {
            appDir = appFolder.mkdir();
            if(!appDir){
                result = "APP";
            }
        }
        //Check for Camera Dir
        if (!appFolderCamera.exists() && !appFolderCamera.isDirectory()) {
            appDir = appFolderCamera.mkdir();
            if(!appDir){
                result = "CAM";
            }
        }

        //Check for Video Dir
        if (!appFolderVideo.exists() && !appFolderVideo.isDirectory()) {
            appDir = appFolderVideo.mkdir();
            if(!appDir){
                result = "VID";
            }
        }

        //Check for Audio Dir
        if (!appFolderAudio.exists() && !appFolderAudio.isDirectory()) {
            appDir = appFolderAudio.mkdir();
            if(!appDir){
                result = "AUD";
            }
        }

        //Check for Notes Dir
        if (!appFolderNotes.exists() && !appFolderNotes.isDirectory()) {
            appDir = appFolderNotes.mkdir();
            if(!appDir){
                result = "TXT";
            }
        }

        //Check for Temp Dir
        if (!appFolderTemp.exists() && !appFolderTemp.isDirectory()) {
            appDir = appFolderTemp.mkdir();
            if(!appDir){
                result = "TMP";
            }
        }

        switch (result){
            case "APP":
                Toast.makeText(context, "Failed to create Application Directory.", Toast.LENGTH_SHORT).show();
                return false;
            case "CAM":
                Toast.makeText(context, "Failed to create Camera Directory.", Toast.LENGTH_SHORT).show();
                return false;
            case "VID":
                Toast.makeText(context, "Failed to create Video Directory.", Toast.LENGTH_SHORT).show();
                return false;
            case "AUD":
                Toast.makeText(context, "Failed to create Audio Directory.", Toast.LENGTH_SHORT).show();
                return false;
            case "TXT":
                Toast.makeText(context, "Failed to create Notes Directory.", Toast.LENGTH_SHORT).show();
                return false;
            case "TMP":
                Toast.makeText(context, "Failed to create Audio Directory.", Toast.LENGTH_SHORT).show();
                return false;
            default:
                Toast.makeText(context, "Directory structure available.", Toast.LENGTH_SHORT).show();

        }
        return true;
    }

    public static File getAppFolder(){
        return appFolder;
    }

    public static File getAppFolderCamera() {
        return appFolderCamera;
    }

    public static File getAppFolderVideo() {
        return appFolderVideo;
    }

    public static File getAppFolderAudio() {
        return appFolderAudio;
    }

    public static File getAppFolderNotes() { return appFolderNotes; }
}
