package abhijit.travellogger;

import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Created by abhijit on 10/28/15.
 */
public class Helper {

    public static String getMimeTypeFromFile(File file){
        Uri uri = Uri.fromFile(file);
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        String mimeType =  MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        return mimeType;
    }
}
