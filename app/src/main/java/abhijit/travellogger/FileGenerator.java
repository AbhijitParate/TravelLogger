package abhijit.travellogger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
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
        return resultList;
    }

    private File[] sortFiles(File[] fileImagesDir){
        File[] files = fileImagesDir;
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return Long.valueOf(rhs.lastModified()).compareTo(lhs.lastModified());
            }
        });
        return files;
    }

    private void listFiles(File directory) {
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile() && !file.isDirectory()) {
                this.resultList.add(file);
            } else if (file.isDirectory()) {
                listFiles(file);
            }
        }
    }

    public void removeUnwanted(){
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
