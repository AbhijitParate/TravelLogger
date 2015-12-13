package abhijit.travellogger.GalleryService;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Objects;

import abhijit.travellogger.ApplicationUtility.Helper;
import abhijit.travellogger.ApplicationUtility.InitiateApplication;
import abhijit.travellogger.ApplicationUtility.TravelLogger;

/*
 * Created by abhijit on 12/3/15.
 */
public class GalleryImage {

    public static File getMediaFile(Intent intent){
        String imagePath = getPath(TravelLogger.getAppContext(), intent.getData());
        assert imagePath != null;
        return new File(imagePath);
    }

    public static File saveGalleryMedia(Intent intent, String type){

        String imagePath = getPath(TravelLogger.getAppContext(), intent.getData());

        assert imagePath != null;
        File sourceFile = new File(imagePath);
        File destinationFile = null;

        if(Objects.equals(type, "IMAGE")) {
            destinationFile = new File(InitiateApplication.getAppFolderCamera(), "IMAGE_" + Helper.getTimeStamp() + ".jpg");
        } else if (Objects.equals(type, "VIDEO")) {
            destinationFile = new File(InitiateApplication.getAppFolderVideo(), "VIDEO_" + Helper.getTimeStamp() + ".mp4");
        } else if (Objects.equals(type, "AUDIO")) {
            destinationFile = new File(InitiateApplication.getAppFolderAudio(), "AUDIO_" + Helper.getTimeStamp() + ".aac");
        }

        if(destinationFile!=null) {
            Boolean result = CopyFiles(sourceFile, destinationFile);
            if (result) {
                return destinationFile;
            }
        }
        return null;
    }

    public static boolean CopyFiles(File sourceFile, File destinationFile){
        try{
            if(InitiateApplication.getAppFolderCamera().canWrite()){
                if (sourceFile.exists()) {
                    FileChannel src = new FileInputStream(sourceFile).getChannel();
                    FileChannel dst = new FileOutputStream(destinationFile).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
                Log.d("saveGalleryMedia", "Success" );
                return true;
            }
        } catch (Exception e){
            Log.d("File save exception", e.getMessage());
        }
        return false;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
