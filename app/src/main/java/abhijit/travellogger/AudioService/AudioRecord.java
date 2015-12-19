package abhijit.travellogger.AudioService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import abhijit.travellogger.R;

public class AudioRecord extends AppCompatActivity implements View.OnClickListener {

    public static final String TEMP_AUDIO = "temp_audio.aac";
    private ImageButton startButton;
    private ImageButton stopButton;

    private boolean isBRRegistered = false;
    private static boolean isRecording = false;

    private static final String START = "Start";
    private static final String STOP = "Stop";

    private static TextView elapsedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_audio_record);
        elapsedTime = (TextView)findViewById(R.id.recordedTime);

        startButton = (ImageButton)findViewById(R.id.start);
        startButton.setTag(START);
        startButton.setOnClickListener(this);
        startButton.setVisibility(View.VISIBLE);

        stopButton = (ImageButton)findViewById(R.id.stop);
        stopButton.setTag(STOP);
        stopButton.setOnClickListener(this);
        stopButton.setVisibility(View.INVISIBLE);
//        stopButton.setVisibility(View.GONE);

        if(isRecording) {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if(isBRRegistered) {
            unregisterReceiver(broadcastReceiver);
            isBRRegistered = false;
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if(isBRRegistered) {
            unregisterReceiver(broadcastReceiver);
            isBRRegistered = false;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(!isBRRegistered && isRecording) {
            startButton.setVisibility(View.INVISIBLE);
            stopButton.setVisibility(View.VISIBLE);
            registerReceiver(broadcastReceiver, new IntentFilter(AudioRecorderService.BROADCAST_ACTION));
            isBRRegistered = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getTag().toString()) {
            case (START):
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = new String[]{android.Manifest.permission.RECORD_AUDIO};
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.RECORD_AUDIO)){
                            requestPermissions(permissions,1);
                        }
                        break;
                    }
                }
                isRecording = true;
                startButton.setEnabled(false);
                startButton.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                stopButton.setEnabled(true);
                startService(new Intent(this, AudioRecorderService.class));
                isRecording = true;
                if (!isBRRegistered) {
                    registerReceiver(broadcastReceiver, new IntentFilter(AudioRecorderService.BROADCAST_ACTION));
                    isBRRegistered = true;
                }
                break;
            case (STOP):
                startButton.setEnabled(true);
                startButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                stopButton.setEnabled(false);
                stopService(new Intent(this, AudioRecorderService.class));
                isRecording = false;
                if(isBRRegistered) {
                    unregisterReceiver(broadcastReceiver);
                    isBRRegistered = false;
                }
                elapsedTime.setTextColor(Color.RED);
                finishWithResultOk();
                break;
        }
    }

    private void finishWithResultOk() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    private void updateUI(Intent intent){
        String counter = intent.getStringExtra("counter");
        Log.d("Counter in AudioRecord", counter);
        elapsedTime.setTextColor(Color.GREEN);
        elapsedTime.setText(counter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_audio_record, menu);
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
}
