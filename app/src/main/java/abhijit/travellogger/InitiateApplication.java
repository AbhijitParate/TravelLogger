package abhijit.travellogger;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

/**
 * Created by abhijit on 10/28/15.
 */
public class InitiateApplication {

    //PackageManager
    private static PackageManager packageManager ;

    //Package
    private static String packageName = "abhijit.travellogger";

    //Permissions
    private static String WRITE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static String READ = "android.permission.READ_EXTERNAL_STORAGE";
    private static String INTERNET = "android.permission.INTERNET";

    //App Folder Setup
    public static final File appFolder = new File(Environment.getExternalStorageDirectory() + "/TravelLogger");
    public static final File appFolderCamera = new File(appFolder + "/TLCamera");
    public static final File appFolderVideo = new File(appFolder + "/TLVideo");
    public static final File appFolderAudio = new File(appFolder + "/TLAudio");
    public static final File appFolderNotes = new File(appFolder + "/TLNotes");
    public static final File appFolderTemp = new File(appFolder + "/Temp");

    public InitiateApplication(Context context){
        packageManager = context.getPackageManager();
        int hasPermission = packageManager.checkPermission(WRITE,packageName);
        if(hasPermission != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "Write permission not available.",Toast.LENGTH_SHORT).show();
        }
    }

    public static void checkPermissions(Context context){
        packageManager = context.getPackageManager();
        int hasPermission = packageManager.checkPermission(WRITE,packageName);
        if(hasPermission != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "Write permission not available.",Toast.LENGTH_SHORT).show();
        }

        hasPermission = packageManager.checkPermission(READ,packageName);
        if(hasPermission != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "Read permission not available.",Toast.LENGTH_SHORT).show();
        }
    }

//    public void checkDirectoryStructure(){
//
//        boolean appDir = true;
//        String result ="";
//
//        //Check for base dir
//        if (!appFolder.exists() && !appFolder.isDirectory()) {
//            appDir = appFolder.mkdir();
//            if(appDir == false){
//                result = "APP";
//            }
//        }
//        //Check for Camera Dir
//        if (!appFolderCamera.exists() && !appFolderCamera.isDirectory()) {
//            appDir = appFolderCamera.mkdir();
//            if(appDir == false){
//                result = "CAM";
//            }
//        }
//        //Check for Video Dir
//        if (!appFolderVideo.exists() && !appFolderVideo.isDirectory()) {
//            appDir = appFolderVideo.mkdir();
//            if(appDir == false){
//                result = "VID";
//            }
//        }
//        //Check for Audio Dir
//        if (!appFolderAudio.exists() && !appFolderAudio.isDirectory()) {
//            appDir = appFolderAudio.mkdir();
//            if(appDir == false){
//                result = "AUD";
//            }
//        }
//
//        //Check for Notes Dir
//        if (!appFolderNotes.exists() && !appFolderNotes.isDirectory()) {
//            appDir = appFolderNotes.mkdir();
//            if(appDir == false){
//                result = "TXT";
//            }
//        }
//
//        if (!appFolderTemp.exists() && !appFolderTemp.isDirectory()) {
//            appDir = appFolderTemp.mkdir();
//            if(appDir == false){
//                result = "TMP";
//            }
//        }
//
//        switch (result.toString()){
//            case "APP":
//                Toast.makeText(this, "Failed to create Application Directory.", Toast.LENGTH_SHORT).show();
//                break;
//            case "CAM":
//                Toast.makeText(this, "Failed to create Camera Directory.", Toast.LENGTH_SHORT).show();
//                break;
//            case "VID":
//                Toast.makeText(this, "Failed to create Video Directory.", Toast.LENGTH_SHORT).show();
//                break;
//            case "AUD":
//                Toast.makeText(this, "Failed to create Audio Directory.", Toast.LENGTH_SHORT).show();
//                break;
//            case "TXT":
//                Toast.makeText(this, "Failed to create Notes Directory.", Toast.LENGTH_SHORT).show();
//                break;
//            case "TMP":
//                Toast.makeText(this, "Failed to create Audio Directory.", Toast.LENGTH_SHORT).show();
//                break;
//            default:
//                Toast.makeText(this, "Directory structure available.", Toast.LENGTH_SHORT).show();
//        }
//
//    }
}
