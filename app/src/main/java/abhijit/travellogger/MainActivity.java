package abhijit.travellogger;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    //App Folder Setup
    public static final File appFolder = new File(Environment.getExternalStorageDirectory() + "/TravelLogger");
    public static final File appFolderCamera = new File(appFolder + "/TLCamera");
    public static final File appFolderVideo = new File(appFolder + "/TLVideo");
    public static final File appFolderAudio = new File(appFolder + "/TLAudio");
    public static final File appFolderTemp = new File(appFolder + "/Temp");

    private File[] mediaFiles;
    private  static List<File> resultList;

    //FAB Button Sizes
    private static final int FAB_BUTTON_SIZE = 500;
    private static final int FAB_SUB_BUTTON_SIZE = 230;
    private static final int FAB_SUB_BUTTON_DISTANCE = 400;

    private static FloatingActionMenu actionMenu;

//tags to select the appropriate activity
    private static final String TAG_FAB = "Add new Photo, Video, Audio or Note.";
    private static final String TAG_CAMERA = "Still Camera";
    private static final String TAG_CAMCORDER = "Video Camera";
    private static final String TAG_AUDIO_RECORDER = "Audio Recorder";
    private static final String TAG_NOTE = "Notes";
//ImageView
    private ImageView ivCapturedImage;
    private RecyclerView recyclerView;

//for Camera Intent
    private static final int ACTIVITY_START_CAMERA_APP = 1;
    private static final int ACTIVITY_START_GALLERY_PHOTO = 11 ;
    private String imageFileLocation ="";

//For Video Camera Intent
    private static final int ACTIVITY_START_VIDEO_CAMERA_APP = 2;
    private static final int ACTIVITY_START_GALLERY_VIDEO = 12 ;
    private String videoFileLocation ="";

//For Audio Recorder Intent
    private static final int ACTIVITY_START_AUDIO_REC_APP = 3;
    private static final int ACTIVITY_START_GALLERY_AUDIO = 13 ;
    private String audioFileLocation ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.checkDir();
        this.buildFab();

        resultList = new ArrayList<>();

        listFiles(getAppFolder());
        removeUnwanted();

        mediaFiles = getMediaFiles();
        mediaFiles = sortFiles(mediaFiles);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter viewAdapter = new ViewAdapter(mediaFiles, this);
        recyclerView.setAdapter(viewAdapter);

    }

    public File[] getMediaFiles() {
        return resultList.toArray(new File[resultList.size()]);
    }

    public void removeUnwanted(){
        Iterator<File> iterator = resultList.iterator();
        while (iterator.hasNext()) {
            File f = iterator.next();
            String mimeType = getMimeTypeFromFile(f);
            if (mimeType == null || !(mimeType.equals("image/jpeg") || mimeType.equals("video/mp4")) ) {
                iterator.remove();
            }
        }
    }

    public String getMimeTypeFromFile(File file){
        Uri uri = Uri.fromFile(file);
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        String mimeType =  MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        return mimeType;
    }

    public void listFiles(File directory) {
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile() && !file.isDirectory()) {
                resultList.add(file);
            } else if (file.isDirectory()) {
                listFiles(file);
            }
        }
    }



    private File getAppFolder(){
        return appFolder;
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

    public void checkDir(){
//        File folder = appFolder;
        boolean appDir = true;
        String result ="";
        //Check for base dir
        if (!appFolder.exists() && !appFolder.isDirectory()) {
            appDir = appFolder.mkdir();
            if(appDir == false){
                result = "APP";
            }
        }
        //Check for Camera Dir
        if (!appFolderCamera.exists() && !appFolderCamera.isDirectory()) {
            appDir = appFolderCamera.mkdir();
            if(appDir == false){
                result = "CAM";
            }
        }
        //Check for Video Dir
        if (!appFolderVideo.exists() && !appFolderVideo.isDirectory()) {
            appDir = appFolderVideo.mkdir();
            if(appDir == false){
                result = "VID";
            }
        }
        //Check for Audio Dir
        if (!appFolderAudio.exists() && !appFolderAudio.isDirectory()) {
            appDir = appFolderAudio.mkdir();
            if(appDir == false){
                result = "AUD";
            }
        }
        if (!appFolderTemp.exists() && !appFolderTemp.isDirectory()) {
            appDir = appFolderTemp.mkdir();
            if(appDir == false){
                result = "TMP";
            }
        }

        switch (result.toString()){
            case "APP":
                Toast.makeText(this, "Failed to create Application Directory.", Toast.LENGTH_SHORT).show();
                break;
            case "CAM":
                Toast.makeText(this, "Failed to create Camera Directory.", Toast.LENGTH_SHORT).show();
                break;
            case "VID":
                Toast.makeText(this, "Failed to create Video Directory.", Toast.LENGTH_SHORT).show();
                break;
            case "AUD":
                Toast.makeText(this, "Failed to create Audio Directory.", Toast.LENGTH_SHORT).show();
                break;
            case "TMP":
                Toast.makeText(this, "Failed to create Audio Directory.", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Directory structure available.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getTag().toString()) {
            case (TAG_FAB):
                Toast.makeText(this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                break;
            case (TAG_CAMERA):
//                Toast.makeText(this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                Intent imageGalleryIntent = new Intent();
                imageGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                imageGalleryIntent.setType("*/*");
                String[] imageMT = {"image/jpg", "image/jpeg"};
                imageGalleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, imageMT);
//                imageGalleryIntent.setType("image/jpg|image/jpeg|image/gif|image/png ");
                imageGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(imageGalleryIntent, "Select a Picture"), ACTIVITY_START_GALLERY_PHOTO);
                break;
          case (TAG_CAMCORDER):
//                Toast.makeText(this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                Intent videoGalleryIntent = new Intent();
                videoGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                videoGalleryIntent.setType("*/*");
                String[] videoMT = {"video/mp4"};
                videoGalleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, videoMT);
                videoGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(videoGalleryIntent, "Select a Video"), ACTIVITY_START_GALLERY_VIDEO);
                break;
            case (TAG_AUDIO_RECORDER):
//                Toast.makeText(this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                Intent audioGalleryIntent = new Intent();
                audioGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                audioGalleryIntent.setType("*/*");
                String[] audioMT = {"audio/aac"};
                audioGalleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, audioMT);
//                audioGalleryIntent.setType("audio/3gp|audio/wav");
                audioGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(audioGalleryIntent, "Select an Audio"), ACTIVITY_START_GALLERY_AUDIO);
                break;
            case (TAG_NOTE):
                Toast.makeText(this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getTag().toString()) {
            case (TAG_CAMERA):
                takePhoto(ivCapturedImage);
                break;
            case (TAG_CAMCORDER):
                takeVideo(ivCapturedImage);
                break;
            case (TAG_AUDIO_RECORDER):
                startRecordActivity();
                // Snackbar.make(v, v.getTag() + " pressed.", Snackbar.LENGTH_LONG).show();
                break;
            case (TAG_NOTE):
                Snackbar.make(v, v.getTag() + " pressed.", Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    public void startRecordActivity() {
        Intent record = new Intent(MainActivity.this, AudioRecord.class);
        startActivity(record);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data ){

        switch (requestCode){
            case ACTIVITY_START_CAMERA_APP :
                switch (resultCode) {
                    case RESULT_OK :
                        Toast.makeText(this, "Image saved successfully.\nLocation: " + imageFileLocation, Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this, "Camera canceled.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Camera failed.", Toast.LENGTH_SHORT).show();
                }
                break;

            case ACTIVITY_START_VIDEO_CAMERA_APP :
                switch (resultCode) {
                    case RESULT_OK :
//                        saveVideo(data);
                        Toast.makeText(this, "Video saved successfully.\nLocation: " + videoFileLocation, Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this, "Recording canceled.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Recording failed.", Toast.LENGTH_SHORT).show();
                }
                break;

            case ACTIVITY_START_AUDIO_REC_APP :
                switch (resultCode) {
                    case RESULT_OK :
                        Toast.makeText(this, "Video saved successfully.\nLocation: " + audioFileLocation, Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this, "Recording canceled.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Recording failed.", Toast.LENGTH_SHORT).show();
                }
                break;

            case ACTIVITY_START_GALLERY_PHOTO :
                switch (resultCode) {
                    case RESULT_OK :
                        Toast.makeText(this, "Image imported successfully.\nLocation: " + data.getData().getPath(), Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this, "Image selection canceled.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Image selection failed.", Toast.LENGTH_SHORT).show();
                }
                break;

            case ACTIVITY_START_GALLERY_VIDEO :
                switch (resultCode) {
                    case RESULT_OK :
                        Toast.makeText(this, "Video imported successfully.\nLocation: " + data.getData().getPath(), Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this, "Video selection canceled.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Video selection failed.", Toast.LENGTH_SHORT).show();
                }
                break;

            case ACTIVITY_START_GALLERY_AUDIO :
                switch (resultCode) {
                    case RESULT_OK :
                        Toast.makeText(this, "Audio imported successfully.\nLocation: "+ data.getData().getPath(), Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this, "Audio selection canceled.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Audio selection failed.", Toast.LENGTH_SHORT).show();
                }
                break;


            default:
                break;
        }

        listFiles(getAppFolder());
        removeUnwanted();
        mediaFiles = null;
        mediaFiles = getMediaFiles();
        mediaFiles = sortFiles(mediaFiles);

        RecyclerView.Adapter newImageAdapter = new ViewAdapter(sortFiles(mediaFiles), this.getBaseContext());
        recyclerView.swapAdapter(newImageAdapter, false);

    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
//To close the action Button on resume
        if (actionMenu.isOpen()) {
            actionMenu.close(true);
        }
    }

    public void takeVideo(View view){
        Intent vidCameraIntent = new Intent();
        vidCameraIntent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);

        File videoFile = null;
        try {
            videoFile = saveVideo();
        } catch (IOException e) {
            e.printStackTrace();
        }

        vidCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
        vidCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,0);
        vidCameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        startActivityForResult(vidCameraIntent, ACTIVITY_START_VIDEO_CAMERA_APP);
    }

    public void takePhoto(View view){
        Intent callCameraApp = new Intent();
        callCameraApp.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile = null;
        try {
            imageFile = saveImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        callCameraApp.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        startActivityForResult(callCameraApp, ACTIVITY_START_CAMERA_APP);
    }

    public File saveVideo()
            throws IOException
    {
        String timeStamp = DateFormat.getDateTimeInstance().format(new Date());
        String videoName = "VIDEO_" + timeStamp;

        /*
        Uri videoUri = data.getData();
        String videoPath = getRealPathFromURI(videoUri);
        File videoFile = new File(videoPath);
        File tmpFile = new File(getAppFolderVideo() + "/" + videoName + ".mp4" );
        videoFile.renameTo(tmpFile);
        /*
        try {
            FileInputStream fis = new FileInputStream(videoFile);
            //this is where you set whatever path you want to save it as:

            //save the video to the File path
            FileOutputStream fos = new FileOutputStream(tmpFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fis.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } */
        File videoFile = File.createTempFile(videoName, ".mp4", getAppFolderVideo());
        videoFileLocation = videoFile.getAbsolutePath();
        return videoFile;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    File saveImage() throws IOException {
        String timeStamp = DateFormat.getDateTimeInstance().format(new Date());
        String imageName = "IMAGE_" + timeStamp;
        File image = File.createTempFile(imageName,".jpg", appFolderCamera);
        imageFileLocation = image.getAbsolutePath();
        return image;
    }

    private void buildFab(){
//Add FAB setup
        ImageView imageView = new ImageView(this); // Create an icon
        imageView.setImageResource(R.drawable.fab_button);
        imageView.setScaleX((float) 0.25);
        imageView.setScaleY((float) 0.25);

//Add it to layout
        FloatingActionButton actionButton;
        FloatingActionButton.LayoutParams fab_params = new FloatingActionButton.LayoutParams(FAB_BUTTON_SIZE, FAB_BUTTON_SIZE);
        actionButton = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.fab_subbutton_selector))
                .setLayoutParams(fab_params)
                .build();
        actionButton.setOnLongClickListener(this);

//Create Sub menu items icons
        ImageView camera = new ImageView(this);
        camera.setImageResource(R.drawable.camera);

        ImageView camcorder = new ImageView(this);
        camcorder.setImageResource(R.drawable.camcorder);

        ImageView audioRecorder = new ImageView(this);
        audioRecorder.setImageResource(R.drawable.mic);

        ImageView note = new ImageView(this);
        note.setImageResource(R.drawable.note);

//Link sub menu items to FAB
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.fab_subbutton_selector));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FAB_SUB_BUTTON_SIZE, FAB_SUB_BUTTON_SIZE);
        itemBuilder.setLayoutParams(params);

//Build sub menu items
        SubActionButton buttonCamera = itemBuilder.setContentView(camera).build();
        SubActionButton buttonCamcorder = itemBuilder.setContentView(camcorder).build();
        SubActionButton buttonAudioRecorder = itemBuilder.setContentView(audioRecorder).build();
        SubActionButton buttonNote = itemBuilder.setContentView(note).build();

//Set tags to each sub button
        actionButton.setTag(TAG_FAB);
        buttonCamera.setTag(TAG_CAMERA);
        buttonCamcorder.setTag(TAG_CAMCORDER);
        buttonAudioRecorder.setTag(TAG_AUDIO_RECORDER);
        buttonNote.setTag(TAG_NOTE);

//Set OnClickListener to all sub buttons
        buttonCamera.setOnClickListener(this);
        buttonCamcorder.setOnClickListener(this);
        buttonAudioRecorder.setOnClickListener(this);
        buttonNote.setOnClickListener(this);

//Set OnLongClickListener
        buttonCamera.setOnLongClickListener(this);
        buttonCamcorder.setOnLongClickListener(this);
        buttonAudioRecorder.setOnLongClickListener(this);
        buttonNote.setOnLongClickListener(this);


//Add the sub menu items to fab
        actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonCamera)
                .addSubActionView(buttonCamcorder)
                .addSubActionView(buttonAudioRecorder)
                .addSubActionView(buttonNote)
                .attachTo(actionButton)
                .setRadius(FAB_SUB_BUTTON_DISTANCE)
                .build();
    }
/*
    @Override
    public void onResume(){
        super.onResume();

        listFiles(getAppFolder());
        removeUnwanted();
        mediaFiles = null;
        mediaFiles = getMediaFiles();
        mediaFiles = sortFiles(mediaFiles);

        RecyclerView.Adapter newImageAdapter = new ViewAdapter(sortFiles(mediaFiles), this.getBaseContext());
        recyclerView.swapAdapter(newImageAdapter, false);
    }
*/
    public static File getAppFolderCamera() {
        return appFolderCamera;
    }

    public static File getAppFolderVideo() {
        return appFolderVideo;
    }

    public static File getAppFolderAudio() {
        return appFolderAudio;
    }


}
