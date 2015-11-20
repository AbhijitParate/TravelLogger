package abhijit.travellogger;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    //RecyclerView
    private RecyclerView recyclerView;

    //for Camera Intent
    private static final int ACTIVITY_START_CAMERA_APP = 1;
    private static final int ACTIVITY_START_GALLERY_PHOTO = 11 ;

    //For Video Camera Intent
    private static final int ACTIVITY_START_VIDEO_CAMERA_APP = 2;
    private static final int ACTIVITY_START_GALLERY_VIDEO = 12 ;

    //For Audio Recorder Intent
    private static final int ACTIVITY_START_AUDIO_REC_APP = 3;
    private static final int ACTIVITY_START_GALLERY_AUDIO = 13 ;

    //Cache
    private static LruCache<String, Bitmap> imageCache ;

    //Files for recyclerView
    private FileGenerator fileGenerator;
    private File[] mediaFiles;

    //Floating action button
    private FABBuilder FAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitiateApplication.checkDirectoryStructure(this);
        fileGenerator = new FileGenerator();
        mediaFiles = fileGenerator.getMediaFiles(InitiateApplication.getAppFolder());

        FAB = new FABBuilder(MainActivity.this, this.getApplicationContext());
        FAB.build();

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter viewAdapter = new ViewAdapter(mediaFiles, this);
        recyclerView.setAdapter(viewAdapter);

        // To add swipe feature
        SwipeHandlerForRecyclerView.create(this, recyclerView).attachToRecyclerView(recyclerView);

        final int maxMemorySize = (int) Runtime.getRuntime().maxMemory() / 1024;
        final int cacheSize = maxMemorySize / 10;

        imageCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
//To close the action Button on resume
        FAB.Close();
    }

    @Override
    public void onResume(){
        super.onResume();
        mediaFiles = fileGenerator.getMediaFiles(InitiateApplication.getAppFolder());
        RecyclerView.Adapter newAdapter = new ViewAdapter(mediaFiles, this.getBaseContext());
        recyclerView.swapAdapter(newAdapter, false);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data ){
//        String fileLocation = data.getExtras().getString("file_path", null);
//        File temp = new File(fileLocation);

        switch (requestCode){
            case ACTIVITY_START_CAMERA_APP :
                switch (resultCode) {
                    case RESULT_OK :
//                        try {
//                            Uri imageUri = data.getData();
//                            File temp = new File(Helper.getRealPathFromURI(this,imageUri));
//                            File saveFile = CaptureImage.saveImage();
//                            temp.renameTo(saveFile);
                        Toast.makeText(this, "Image saved successfully." , Toast.LENGTH_SHORT).show();
//                        } catch (IOException e){
//                            e.printStackTrace();
//                        }
                        break;
                    case RESULT_CANCELED:
//                        temp.delete();
                        Toast.makeText(this, "Camera canceled.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
//                        temp.delete();
                        Toast.makeText(this, "Camera failed.", Toast.LENGTH_SHORT).show();
                }
                break;

            case ACTIVITY_START_VIDEO_CAMERA_APP :
                switch (resultCode) {
                    case RESULT_OK :
                        Uri videoUri = data.getData();
//                        Toast.makeText(this, "Video saved successfully." , Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "Video saved successfully. \nLocation : " + videoUri.toString() , Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
//                        temp.delete();
                        Toast.makeText(this, "Recording canceled.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
//                        temp.delete();
                        Toast.makeText(this, "Recording failed.", Toast.LENGTH_SHORT).show();
                }
                break;

            case ACTIVITY_START_AUDIO_REC_APP :
                switch (resultCode) {
                    case RESULT_OK :
                        Toast.makeText(this, "Video saved successfully.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this, "Image imported successfully.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this, "Video imported successfully.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this, "Audio imported successfully.", Toast.LENGTH_SHORT).show();
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

    public static Bitmap getBitmapFromMemoryCache(String key) {
        return imageCache.get(key);
    }

    public static void setBitmapToMemoryCache(String key, Bitmap bitmap) {
        if(getBitmapFromMemoryCache(key) == null) {
            imageCache.put(key, bitmap);
        }
    }

}
