package abhijit.travellogger.ApplicationUtility;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Created by abhijit on 10/28/15.
 */
public class Helper {

    public static String getMimeTypeFromFile(File file){
        Uri uri = Uri.fromFile(file);
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        String mimeType =  MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        return mimeType;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getTimeStamp(){
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        return s.format(new Date());
    }

    public static boolean checkPermission(String permission, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (TravelLogger.getAppContext().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                if (activity.shouldShowRequestPermissionRationale(permission)) {
                    activity.requestPermissions(new String[]{permission}, 0);
                }
                return false;
            }
            return true;
        }
        return false;
    }
}
