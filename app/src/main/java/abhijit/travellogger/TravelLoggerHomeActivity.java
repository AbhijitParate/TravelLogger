package abhijit.travellogger;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import abhijit.travellogger.ApplicationUtility.Constants;
import abhijit.travellogger.ApplicationUtility.FABBuilder;
import abhijit.travellogger.ApplicationUtility.FileGenerator;
import abhijit.travellogger.ApplicationUtility.InitiateApplication;
import abhijit.travellogger.CamcorderService.CaptureVideo;
import abhijit.travellogger.ClickHandlers.ClickHandlerNavigationDrawer;
import abhijit.travellogger.GalleryService.GalleryImage;
import abhijit.travellogger.MediaManager.MediaViewAdapter;
import abhijit.travellogger.MediaManager.SwipeHandlerForRecyclerView;

public class TravelLoggerHomeActivity extends AppCompatActivity {
    //RecyclerView
    private RecyclerView recyclerView;
    private MediaViewAdapter viewAdapter;

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
    private File[] mediaFiles;
    //Floating action button
    private FABBuilder FAB;
    private Toolbar toolbar;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    TextView noMediaText;
    boolean isFABOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_travel_logger_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        FAB = new FABBuilder(TravelLoggerHomeActivity.this, this);
        FAB.build();
        buildNavDrawer();
        buildRecyclerView();
    }

    @Override
    protected void onStart(){
        super.onStart();
        navigationView.getMenu().getItem(Constants.SP_HOME).setChecked(true);
        navigationView.getMenu().getItem(Constants.SP_TRIPS).setChecked(false);
    }

    public void buildNavDrawer(){
        drawer = (DrawerLayout) findViewById(R.id.nav_drawer_layout_home);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this,
                        drawer,
                        toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close){
                    public void onDrawerClosed(View view) {
                        super.onDrawerClosed(view);
                        FAB.Close();
                        FAB.getFAB().setVisibility(View.VISIBLE);

                    }

                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                        FAB.Close();
                        FAB.getFAB().setVisibility(View.INVISIBLE);
                    }
                };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view_home);
        navigationView.setNavigationItemSelectedListener(new ClickHandlerNavigationDrawer(TravelLoggerHomeActivity.this, this, drawer));
        navigationView.getMenu().getItem(Constants.SP_HOME).setChecked(true);
    }

    FileGenerator fileGenerator;

    private void buildRecyclerView(){
        InitiateApplication.checkDirectoryStructure(this);
        fileGenerator = new FileGenerator();
        mediaFiles = fileGenerator.getMediaFiles(InitiateApplication.getAppFolder());
        noMediaText = (TextView) findViewById(R.id.textview_no_media);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        viewAdapter = new MediaViewAdapter(mediaFiles);
        recyclerView.setAdapter(viewAdapter);
        // To add swipe feature
        SwipeHandlerForRecyclerView.create(this, recyclerView).attachToRecyclerView(recyclerView);

        if(mediaFiles.length != 0){
            noMediaText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            noMediaText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        FAB.Close();
    }

    @Override
    public void onResume(){
        super.onResume();
        mediaFiles = fileGenerator.getMediaFiles(InitiateApplication.getAppFolder());
        if(mediaFiles.length != 0) {
            noMediaText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            viewAdapter.updateList(mediaFiles);
        } else {
            noMediaText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(isFABOpen) {
            FAB.Close();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.travel_logger_home, menu);
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

        switch (requestCode){
            case ACTIVITY_START_CAMERA_APP :
                switch (resultCode) {
                    case RESULT_OK :
                        File imageFile = (File) data.getExtras().get("media");
                        if (imageFile != null) {
                            viewAdapter.addItem(0,imageFile);
                            Toast.makeText(this, "Image saved successfully." + imageFile.getAbsolutePath() , Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        Toast.makeText(this, "Camera canceled.", Toast.LENGTH_SHORT).show();
                }
                break;

            case ACTIVITY_START_VIDEO_CAMERA_APP :
                switch (resultCode) {
                    case RESULT_OK :
                        File videoFile = (File) data.getExtras().get("media");
                        if (videoFile != null) {
                            viewAdapter.addItem(0,videoFile);
                            Toast.makeText(this, "Video saved successfully." + videoFile.getAbsolutePath() , Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case RESULT_CANCELED:
                        String videoFilePath = InitiateApplication.getAppFolderTemp() + "/" + CaptureVideo.TEMP_VIDEO;
                        File video = new File(videoFilePath);
                        video.delete();
                        Toast.makeText(this, "Recording canceled.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Recording failed.", Toast.LENGTH_SHORT).show();
                }
                break;

            case ACTIVITY_START_AUDIO_REC_APP :
                switch (resultCode) {
                    case RESULT_OK :
                        Toast.makeText(this, "Video saved successfully.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Recording canceled.", Toast.LENGTH_SHORT).show();
                }
                break;

            case ACTIVITY_START_GALLERY_PHOTO :
                switch (resultCode) {
                    case RESULT_OK :
                        File imageFile = GalleryImage.saveGalleryMedia(data, "IMAGE");
                        viewAdapter.addItem(0, imageFile);
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
                        File videoFile = GalleryImage.saveGalleryMedia(data, "VIDEO");
                        viewAdapter.addItem(0, videoFile);
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
                        File audioFile = GalleryImage.saveGalleryMedia(data, "AUDIO");
                        viewAdapter.addItem(0,audioFile );
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
