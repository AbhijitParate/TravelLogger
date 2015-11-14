package abhijit.travellogger;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

/**
 * Created by abhijit on 11/14/15.
 */
/*
public class FABBuilder implements View.OnClickListener, View.OnLongClickListener{

    //FAB Button Sizes
    private static final int FAB_BUTTON_SIZE = 400;
    private static final int FAB_SUB_BUTTON_SIZE = 230;
    private static final int FAB_SUB_BUTTON_DISTANCE = 400;

    //tags to select the appropriate activity
    private static final String TAG_FAB = "Add new Photo, Video, Audio or Note.";
    private static final String TAG_CAMERA = "Still Camera";
    private static final String TAG_CAMCORDER = "Video Camera";
    private static final String TAG_AUDIO_RECORDER = "Audio Recorder";
    private static final String TAG_NOTE = "Notes";

    //Main FAB Button
    private static FloatingActionMenu actionMenu;

    //Main Activity Context
    private Context context;

    public FABBuilder(Context mainContext){
        context = mainContext;
    }

    public void build(){
        //Add FAB setup
        ImageView imageView = new ImageView(context); // Create an icon
        imageView.setImageResource(R.drawable.fab_button);
        imageView.setScaleX((float) 0.25);
        imageView.setScaleY((float) 0.25);

//Add it to layout
        FloatingActionButton actionButton;
        FloatingActionButton.LayoutParams fab_params = new FloatingActionButton.LayoutParams(FAB_BUTTON_SIZE, FAB_BUTTON_SIZE);
        actionButton = new FloatingActionButton.Builder(context)
                .setContentView(imageView)
                .setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.fab_subbutton_selector))
                .setLayoutParams(fab_params)
                .build();
        actionButton.setOnLongClickListener(context);

//Create Sub menu items icons
        ImageView camera = new ImageView(context);
        camera.setImageResource(R.drawable.camera);

        ImageView camcorder = new ImageView(context);
        camcorder.setImageResource(R.drawable.camcorder);

        ImageView audioRecorder = new ImageView(context);
        audioRecorder.setImageResource(R.drawable.mic);

        ImageView note = new ImageView(context);
        note.setImageResource(R.drawable.note);

//Link sub menu items to FAB
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(context);
        itemBuilder.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.fab_subbutton_selector));
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
        buttonCamera.setOnClickListener(context);
        buttonCamcorder.setOnClickListener(context);
        buttonAudioRecorder.setOnClickListener(context);
        buttonNote.setOnClickListener(context);

//Set OnLongClickListener
        buttonCamera.setOnLongClickListener(this);
        buttonCamcorder.setOnLongClickListener(context);
        buttonAudioRecorder.setOnLongClickListener(context);
        buttonNote.setOnLongClickListener(context);


//Add the sub menu items to fab
        actionMenu = new FloatingActionMenu.Builder(context)
                .addSubActionView(buttonCamera)
                .addSubActionView(buttonCamcorder)
                .addSubActionView(buttonAudioRecorder)
                .addSubActionView(buttonNote)
                .attachTo(actionButton)
                .setRadius(FAB_SUB_BUTTON_DISTANCE)
                .build();
    }


}
*/
