package abhijit.travellogger.ClickHandlers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import abhijit.travellogger.ApplicationUtility.Constants;
import abhijit.travellogger.AudioService.AudioRecord;
import abhijit.travellogger.AudioService.EditAudioActivity;
import abhijit.travellogger.CamcorderService.EditVideoActivity;
import abhijit.travellogger.CameraService.CaptureImage;
import abhijit.travellogger.CamcorderService.CaptureVideo;
import abhijit.travellogger.CameraService.EditImageActivity;
import abhijit.travellogger.GalleryService.EditGallery;
import abhijit.travellogger.NotesService.EditNoteActivity;
import abhijit.travellogger.NotesService.NotesActivity;
import abhijit.travellogger.SharedPreferencesHandler;
import abhijit.travellogger.TripManager.TripManagerActivity;

/*
 * Created by abhijit on 11/16/15.
 */
public class ClickHandlerTravelLoggerHome extends ClickHandlerCustom {

        //tags to select the appropriate activity
        private static final String TAG_FAB = "Add new Photo, Video, Audio or Note.";
        private static final String TAG_CAMERA = "Image Camera";
        private static final String TAG_CAMCORDER = "Video Camera";
        private static final String TAG_AUDIO_RECORDER = "Audio Recorder";
        private static final String TAG_NOTE = "Notes";

        //for Camera Intent
//      private static final int ACTIVITY_START_CAMERA_APP = 1;
        private static final int ACTIVITY_START_EDIT_IMAGE = 1;
        private static final int ACTIVITY_START_GALLERY_PHOTO = 11;

        //For Video Camera Intent
//      private static final int ACTIVITY_START_VIDEO_CAMERA_APP = 2;
        private static final int ACTIVITY_START_EDIT_VIDEO = 2;
        private static final int ACTIVITY_START_GALLERY_VIDEO = 12;

        //For Audio Recorder Intent
        private static final int ACTIVITY_START_AUDIO_REC_APP = 3;
        private static final int ACTIVITY_START_GALLERY_AUDIO = 13;

        //Calling activity
        Activity mainActivity;
        Context mainContext;

        public ClickHandlerTravelLoggerHome(Activity activity, Context context) {
            mainActivity = activity;
            mainContext = context;
        }


        @Override
        public void onClick(View v) {

            String tripName = SharedPreferencesHandler.getSharedPref(Constants.SP_TRIP_NAME);
            if(tripName == null) {
                Intent tripManager= new Intent(mainContext,TripManagerActivity.class);
                tripManager.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                tripManager.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainActivity.startActivity(tripManager);
                return;
            }

            Intent tempIntent;
            switch (v.getTag().toString()) {
//                case (TAG_FAB):
//                    String tripName = SharedPreferencesHandler.getSharedPref(Constants.SP_TRIP_NAME);
//                    if(tripName != null) {
//                        Intent tripManager= new Intent(mainActivity,TripManagerActivity.class);
//                        tripManager.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        tripManager.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        mainActivity.startActivity(tripManager);
//                    }
//                    break;
                case (TAG_CAMERA):
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (mainActivity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                            ||
                            mainActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            mainActivity.requestPermissions(
                                    new String[]{
                                            android.Manifest.permission.CAMERA
                                            ,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1);
                            break;
                        }
                    }
                    tempIntent = new Intent(mainActivity, EditImageActivity.class);
                    mainActivity.startActivityForResult(tempIntent, ACTIVITY_START_EDIT_IMAGE);
                    break;

                case (TAG_CAMCORDER):
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (mainActivity.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            ||
                            mainActivity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                            ||
                            mainActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                            mainActivity.requestPermissions(
                                    new String[]{
                                            android.Manifest.permission.CAMERA
                                            ,Manifest.permission.RECORD_AUDIO
                                            ,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1);
                            break;
                        }
                    }
                    tempIntent = new Intent(mainActivity, EditVideoActivity.class);
                    mainActivity.startActivityForResult(tempIntent, ACTIVITY_START_EDIT_VIDEO);
                    break;

                case (TAG_AUDIO_RECORDER):
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (mainActivity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED
                            ||
                            mainActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ) {
                            mainActivity.requestPermissions(
                                    new String[]{
                                            Manifest.permission.RECORD_AUDIO
                                            ,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1);
                            break;
                        }
                    }
                    tempIntent = new Intent(mainContext, EditAudioActivity.class);
                    mainActivity.startActivity(tempIntent);
                    break;

                case (TAG_NOTE):
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (mainActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ) {
                            mainActivity.requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE} , 1);
                            break;
                        }
                    }
                    tempIntent = new Intent(mainContext, EditNoteActivity.class);
                    mainActivity.startActivity(tempIntent);
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            Intent tempIntent;

            switch (v.getTag().toString()) {
                case (TAG_FAB):
                    Toast.makeText(mainContext, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                    break;

                case (TAG_CAMERA):
                    tempIntent = new Intent(mainContext, EditGallery.class);
                    tempIntent.setAction("gallery");
                    tempIntent.putExtra("type", "image");
                    mainActivity.startActivity(tempIntent);

//                    Intent imageGalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    imageGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                    imageGalleryIntent.setAction("gallery");
//                    imageGalleryIntent.putExtra("type", "image");
//                    imageGalleryIntent.setType("*/*");
//                    String[] imageMT = {"image/jpg", "image/jpeg"};
//                    imageGalleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, imageMT);
//                    imageGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                    mainActivity.startActivityForResult(Intent.createChooser(imageGalleryIntent, "Select a Picture"), ACTIVITY_START_GALLERY_PHOTO);
                    break;

                case (TAG_CAMCORDER):
                    tempIntent = new Intent(mainContext, EditGallery.class);
                    tempIntent.setAction("gallery");
                    tempIntent.putExtra("type", "video");
                    mainActivity.startActivity(tempIntent);

//                    Intent videoGalleryIntent = new Intent();
//                    videoGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                    videoGalleryIntent.setAction("gallery");
//                    videoGalleryIntent.putExtra("type", "video");
//                    videoGalleryIntent.setType("*/*");
//                    String[] videoMT = {"video/mp4"};
//                    videoGalleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, videoMT);
//                    videoGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                    mainActivity.startActivityForResult(Intent.createChooser(videoGalleryIntent, "Select a Video"), ACTIVITY_START_GALLERY_VIDEO);
                    break;

                case (TAG_AUDIO_RECORDER):
                    tempIntent = new Intent(mainContext, EditGallery.class);
                    tempIntent.setAction("gallery");
                    tempIntent.putExtra("type", "audio");
                    mainActivity.startActivity(tempIntent);

//                    Intent audioGalleryIntent = new Intent();
//                    audioGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                    audioGalleryIntent.setAction("gallery");
//                    audioGalleryIntent.putExtra("type", "audio");
//                    audioGalleryIntent.setType("*/*");
//                    String[] audioMT = {"audio/aac"};
//                    audioGalleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, audioMT);
//                    audioGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                    mainActivity.startActivityForResult(Intent.createChooser(audioGalleryIntent, "Select an Audio"), ACTIVITY_START_GALLERY_AUDIO);
                    break;

                case (TAG_NOTE):
                    Toast.makeText(mainContext, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    }
