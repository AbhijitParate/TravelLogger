package abhijit.travellogger.ApplicationUtility;

import android.util.Log;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import abhijit.travellogger.MediaManager.Media;
import abhijit.travellogger.MediaManager.MediaDBManager;
import abhijit.travellogger.SharedPreferencesHandler;

/*
 * Created by abhijit on 11/14/15.
 */
public class FileGenerator {

    //List of files in Dir Structure
    private List<File> resultList;

    public File[] getMediaFiles(File directory) {
        resultList = generateFiles(directory);
        return sortFiles(resultList.toArray(new File[resultList.size()]));
    }

    public List<File> generateFiles(File directory){
        resultList = new ArrayList<>();
        listFiles(directory);
        removeUnwanted();
        return removeNonTripMedia();
//        return resultList;
    }

    private List<File> removeNonTripMedia(){
        String tripName = SharedPreferencesHandler.getSharedPref(Constants.SP_TRIP_NAME);
        MediaDBManager mediaManager = new MediaDBManager(TravelLogger.getAppContext());
        List<Media> medias = mediaManager.getAllMediaForTrip(tripName);
//        Iterator<File> mediaFileIterator = resultList.iterator();
        List<File> newResultList = new ArrayList<>();
        for (Media media : medias) {
            Iterator<File> mediaFileIterator = resultList.iterator();
            while (mediaFileIterator.hasNext()) {
                File file = mediaFileIterator.next();
                if (media.getFileName().equals(FilenameUtils.getBaseName(file.getName()))) {
//                    Log.d("File removed", file.getName());
//                    mediaFileIterator.remove();
                    newResultList.add(file);
                }
            }
        }
        return newResultList;
    }

    private File[] sortFiles(File[] fileImagesDir){
        Arrays.sort(fileImagesDir, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return Long.valueOf(rhs.lastModified()).compareTo(lhs.lastModified());
            }
        });
        return fileImagesDir;
    }

    private void listFiles(File directory) {
        if(directory != null) {
            File[] fList = directory.listFiles();
            for (File file : fList) {
                if (file.isFile() && !file.isDirectory()) {
                    this.resultList.add(file);
                } else if (file.isDirectory()) {
                    listFiles(file);
                }
            }
        }
    }

    private void removeUnwanted(){
        Iterator<File> iterator = resultList.iterator();
        while (iterator.hasNext()) {
            File f = iterator.next();
            String mimeType = Helper.getMimeTypeFromFile(f);
            if ( mimeType == null ||
                    !(mimeType.equals("image/jpeg") ||
                            mimeType.equals("video/mp4")  ||
                            mimeType.equals("audio/aac")  ||
                            mimeType.equals("text/plain"))
                    )
            {
                iterator.remove();
            }
        }
    }
}
