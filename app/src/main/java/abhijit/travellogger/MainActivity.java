package abhijit.travellogger;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


//FAB Button Sizes
    private static final int FAB_BUTTON_SIZE = 400;
    private static final int FAB_SUB_BUTTON_SIZE = 230;
    private static final int FAB_SUB_BUTTON_DISTANCE = 400;

//tags to select the appropriate activity
    private static final String TAG_CAMERA = "Still Camera";
    private static final String TAG_CAMCORDER = "Video Camera";
    private static final String TAG_AUDIO_RECORDER = "Audio Recorder";
    private static final String TAG_NOTE = "Notes";
//ImageView
    private ImageView ivCapturedImage;

//for camera intent
    private static final int ACTIVITY_START_CAMERA_APP = 1;
    private String imageFileLocation ="";

//For Video Camera
    private static final int ACTIVITY_START_VIDEO_CAMERA_APP = 2;
    private String videoFileLocation ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivCapturedImage = (ImageView) findViewById(R.id.ivCapturedImage);

        this.buildFab();
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
    public void onClick(View v) {

        Toast.makeText(this, v.getTag()+" pressed.", Toast.LENGTH_SHORT ).show();

        switch (v.getTag().toString()) {
            case (TAG_CAMERA):
                Toast.makeText(this, v.getTag() + " pressed.", Toast.LENGTH_SHORT).show();
                takePhoto(ivCapturedImage);
                break;
            case (TAG_CAMCORDER):
                Toast.makeText(this, v.getTag() + " pressed.", Toast.LENGTH_SHORT).show();
                takeVideo(ivCapturedImage);
                break;
            case (TAG_AUDIO_RECORDER):
                Toast.makeText(this, v.getTag() + " pressed.", Toast.LENGTH_SHORT).show();
//                SnackBar.show(getActivity(), R.string.hello_world);
                break;
            case (TAG_NOTE):
                Toast.makeText(this, v.getTag() + " pressed.", Toast.LENGTH_SHORT).show();
                Snackbar.make(v , v.getTag() + " pressed.", Snackbar.LENGTH_LONG).show();
                break;
        }
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
                        Toast.makeText(this, "Video saved successfully.\nLocation: " + videoFileLocation, Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this, "Recording canceled.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Recording failed.", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
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

    File saveVideo() throws IOException {
        String timeStamp = DateFormat.getDateTimeInstance().format(new Date());
        String videoName = "VIDEO_" + timeStamp +"_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File video = File.createTempFile(videoName, ".mp4", storageDirectory);
        videoFileLocation = video.getAbsolutePath();
        return video;
    }

    File saveImage() throws IOException {
        String timeStamp = DateFormat.getDateTimeInstance().format(new Date());
        String imageName = "IMAGE_" + timeStamp +"_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageName,".jpg", storageDirectory);
        imageFileLocation = image.getAbsolutePath();
        return image;
    }

    private void buildFab(){
//Add FAB setup
        ImageView imageView = new ImageView(this); // Create an icon
        imageView.setImageResource(R.drawable.fab_button);
//        imageView.setLayoutParams(new LinearLayout.LayoutParams(20,20));

//Add it to layout
        FloatingActionButton actionButton;
        FloatingActionButton.LayoutParams fab_params = new FloatingActionButton.LayoutParams(FAB_BUTTON_SIZE, FAB_BUTTON_SIZE);
        actionButton = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.fab_subbutton_selector))
                .setLayoutParams(fab_params)
                .build();

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
        buttonCamera.setTag(TAG_CAMERA);
        buttonCamcorder.setTag(TAG_CAMCORDER);
        buttonAudioRecorder.setTag(TAG_AUDIO_RECORDER);
        buttonNote.setTag(TAG_NOTE);

//Set onClickListener to all sub buttons
        buttonCamera.setOnClickListener(this);
        buttonCamcorder.setOnClickListener(this);
        buttonAudioRecorder.setOnClickListener(this);
        buttonNote.setOnClickListener(this);

//Add the sub menu items to fab
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonCamera)
                .addSubActionView(buttonCamcorder)
                .addSubActionView(buttonAudioRecorder)
                .addSubActionView(buttonNote)
                .attachTo(actionButton)
                .setRadius(FAB_SUB_BUTTON_DISTANCE)
                .build();
    }
}
