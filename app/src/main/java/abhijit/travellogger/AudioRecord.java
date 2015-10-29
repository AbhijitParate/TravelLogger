package abhijit.travellogger;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;

public class AudioRecord extends AppCompatActivity implements View.OnClickListener {

    private View startButton;
    private View stopButton;

    private static final String START = "Start";
    private static final String STOP = "Stop";

    private static boolean isRecording = false;

    private static Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        startButton = findViewById(R.id.start);
        startButton.setTag(START);
        startButton.setOnClickListener(this);
        stopButton = findViewById(R.id.stop);
        stopButton.setTag(STOP);
        stopButton.setOnClickListener(this);

        if(isRecording) {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        }

        chronometer = (Chronometer) findViewById(R.id.chronometer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getTag().toString()) {
            case (START):
                isRecording = true;
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
//                showNotification();
                startService(new Intent(this, ServiceAudioRecord.class));
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.setTextColor(Color.RED);
                chronometer.start();
                break;
            case (STOP):
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                stopService(new Intent(this, ServiceAudioRecord.class));
                chronometer.stop();
                chronometer.setTextColor(Color.GREEN);
                isRecording = false;
                break;
        }
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
