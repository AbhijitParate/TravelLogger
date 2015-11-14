package abhijit.travellogger;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by abhijit on 10/28/15.
 */
public class InitiateApplication {

    //Package
    private static String packageName = "abhijit.travellogger";

    //Permissions
//    private static String WRITE = "android.permission.WRITE_EXTERNAL_STORAGE";
//    private static String READ = "android.permission.READ_EXTERNAL_STORAGE";
//    private static String INTERNET = "android.permission.INTERNET";

    //App Folder Setup
    public static final File appFolder = new File(Environment.getExternalStorageDirectory() + "/TravelLogger");
    public static final File appFolderCamera = new File(appFolder + "/TLCamera");
    public static final File appFolderVideo = new File(appFolder + "/TLVideo");
    public static final File appFolderAudio = new File(appFolder + "/TLAudio");
    public static final File appFolderNotes = new File(appFolder + "/TLNotes");
    public static final File appFolderTemp = new File(appFolder + "/Temp");

    public static boolean checkPermissions(Context context, String permission){
        PackageManager packageManager = context.getPackageManager();
        int hasPermission = packageManager.checkPermission(permission,packageName);
        if(hasPermission != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }

    public static boolean checkDirectoryStructure(Context context){

        boolean appDir = true;
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

    public static String getTimeStamp(){
        return DateFormat.getDateTimeInstance().format(new Date());
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
