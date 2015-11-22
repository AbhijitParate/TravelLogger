package abhijit.travellogger.ClickHandlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import abhijit.travellogger.AudioService.AudioRecord;
import abhijit.travellogger.CameraService.CaptureImage;
import abhijit.travellogger.CamcorderService.CaptureVideo;
import abhijit.travellogger.NotesService.NotesActivity;

/*
 * Created by abhijit on 11/16/15.
 */
public class ClickHandlerForMain extends CustomClickHandler {

        //tags to select the appropriate activity
        private static final String TAG_FAB = "Add new Photo, Video, Audio or Note.";
        private static final String TAG_CAMERA = "Image Camera";
        private static final String TAG_CAMCORDER = "Video Camera";
        private static final String TAG_AUDIO_RECORDER = "Audio Recorder";
        private static final String TAG_NOTE = "Notes";

        //for Camera Intent
        private static final int ACTIVITY_START_CAMERA_APP = 1;
        private static final int ACTIVITY_START_GALLERY_PHOTO = 11;

        //For Video Camera Intent
        private static final int ACTIVITY_START_VIDEO_CAMERA_APP = 2;
        private static final int ACTIVITY_START_GALLERY_VIDEO = 12;

        //For Audio Recorder Intent
        private static final int ACTIVITY_START_AUDIO_REC_APP = 3;
        private static final int ACTIVITY_START_GALLERY_AUDIO = 13;

        //Calling activity
        Activity mainActivity;
        Context mainContext;

        public ClickHandlerForMain(Activity activity, Context context) {
            mainActivity = activity;
            mainContext = context;
        }


        @Override
        public void onClick(View v) {

            Intent tempIntent;
            switch (v.getTag().toString()) {
                case (TAG_CAMERA):
                    tempIntent = CaptureImage.takePhoto();
                    mainActivity.startActivityForResult(tempIntent, ACTIVITY_START_CAMERA_APP);
                    break;

                case (TAG_CAMCORDER):
                    tempIntent = CaptureVideo.takeVideo();
//                mainActivity.setImageFileLocation(tempIntent.getStringExtra("file_path"));
                    mainActivity.startActivityForResult(tempIntent, ACTIVITY_START_VIDEO_CAMERA_APP);
                    break;

                case (TAG_AUDIO_RECORDER):
                    tempIntent = new Intent(mainContext, AudioRecord.class);
                    mainActivity.startActivity(tempIntent);
                    break;

                case (TAG_NOTE):
                    tempIntent = new Intent(mainContext, NotesActivity.class);
                    mainActivity.startActivity(tempIntent);
                    break;

            }
        }

        @Override
        public boolean onLongClick(View v) {

            switch (v.getTag().toString()) {
                case (TAG_FAB):
                    Toast.makeText(mainContext, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                    break;

                case (TAG_CAMERA):
                    Intent imageGalleryIntent = new Intent();
                    imageGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    imageGalleryIntent.setType("*/*");
                    String[] imageMT = {"image/jpg", "image/jpeg"};
                    imageGalleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, imageMT);
//                imageGalleryIntent.setType("image/jpg|image/jpeg|image/gif|image/png ");
                    imageGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    mainActivity.startActivityForResult(Intent.createChooser(imageGalleryIntent, "Select a Picture"), ACTIVITY_START_GALLERY_PHOTO);
                    break;

                case (TAG_CAMCORDER):
//                Toast.makeText(this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                    Intent videoGalleryIntent = new Intent();
                    videoGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    videoGalleryIntent.setType("*/*");
                    String[] videoMT = {"video/mp4"};
                    videoGalleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, videoMT);
                    videoGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    mainActivity.startActivityForResult(Intent.createChooser(videoGalleryIntent, "Select a Video"), ACTIVITY_START_GALLERY_VIDEO);
                    break;

                case (TAG_AUDIO_RECORDER):
                    Intent audioGalleryIntent = new Intent();
                    audioGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    audioGalleryIntent.setType("*/*");
                    String[] audioMT = {"audio/aac"};
                    audioGalleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, audioMT);
                    audioGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    mainActivity.startActivityForResult(Intent.createChooser(audioGalleryIntent, "Select an Audio"), ACTIVITY_START_GALLERY_AUDIO);
                    break;

                case (TAG_NOTE):
                    Toast.makeText(mainContext, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    }
