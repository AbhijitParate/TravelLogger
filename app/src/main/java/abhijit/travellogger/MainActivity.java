package abhijit.travellogger;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{


    private File[] mediaFiles;

    //FAB Button Sizes
    private static final int FAB_BUTTON_SIZE = 400;
    private static final int FAB_SUB_BUTTON_SIZE = 230;
    private static final int FAB_SUB_BUTTON_DISTANCE = 400;

    private static FloatingActionMenu actionMenu;

    //tags to select the appropriate activity
    private static final String TAG_FAB = "Add new Photo, Video, Audio or Note.";
    private static final String TAG_CAMERA = "Still Camera";
    private static final String TAG_CAMCORDER = "Video Camera";
    private static final String TAG_AUDIO_RECORDER = "Audio Recorder";
    private static final String TAG_NOTE = "Notes";

    //RecyclerView
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

    //Cache
    private static LruCache<String, Bitmap> imageCache ;

    FileGenerator fileGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitiateApplication.checkDirectoryStructure(this);
        fileGenerator = new FileGenerator();
        mediaFiles = fileGenerator.getMediaFiles(InitiateApplication.getAppFolder());

        this.buildFab();

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter viewAdapter = new ViewAdapter(mediaFiles, this);
        recyclerView.setAdapter(viewAdapter);

        final int maxMemorySize = (int) Runtime.getRuntime().maxMemory() / 1024;
        final int cacheSize = maxMemorySize / 10;

        imageCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };

    }

    public static Bitmap getBitmapFromMemoryCache(String key) {
        return imageCache.get(key);
    }

    public static void setBitmapToMemoryCache(String key, Bitmap bitmap) {
        if(getBitmapFromMemoryCache(key) == null) {
            imageCache.put(key, bitmap);
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
                takePhoto();
                break;
            case (TAG_CAMCORDER):
                takeVideo();
                break;
            case (TAG_AUDIO_RECORDER):
                startRecordActivity();
                // Snackbar.make(v, v.getTag() + " pressed.", Snackbar.LENGTH_LONG).show();
                break;
            case (TAG_NOTE):
                takeNotes();
//                Snackbar.make(v, v.getTag() + " pressed.", Snackbar.LENGTH_LONG).show();
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
                        String audioFileLocation = "";
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

        mediaFiles = fileGenerator.getMediaFiles(InitiateApplication.getAppFolder());
        RecyclerView.Adapter newAdapter = new ViewAdapter(mediaFiles, this.getBaseContext());
        recyclerView.swapAdapter(newAdapter, false);
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
//To close the action Button on resume
        if (actionMenu.isOpen()) {
            actionMenu.close(true);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        mediaFiles = fileGenerator.getMediaFiles(InitiateApplication.getAppFolder());
        RecyclerView.Adapter newAdapter = new ViewAdapter(mediaFiles, this.getBaseContext());
        recyclerView.swapAdapter(newAdapter, false);
    }

    private void takeNotes(){
        //on click action
        Intent intent = new Intent(getApplicationContext(), NotesActivity.class);
        startActivity(intent);
    }

    private void takeVideo(){
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

    private void takePhoto(){
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

    private File saveVideo() throws IOException {
        String videoName = "VIDEO_" + InitiateApplication.getTimeStamp();
        File videoFile = File.createTempFile(videoName, ".mp4", InitiateApplication.getAppFolderVideo());
        videoFileLocation = videoFile.getAbsolutePath();
        return videoFile;
    }

    private File saveImage() throws IOException {
        String imageName = "IMAGE_" + InitiateApplication.getTimeStamp();
        File image = File.createTempFile(imageName, ".jpg", InitiateApplication.getAppFolderCamera());
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



}
