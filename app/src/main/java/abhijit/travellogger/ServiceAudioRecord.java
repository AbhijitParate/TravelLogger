package abhijit.travellogger;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import abhijit.travellogger.ApplicationUtility.InitiateApplication;


public class ServiceAudioRecord extends Service {


    MediaRecorder recorder;
    File audioFile = null;
    NotificationCompat.Builder mBuilder;
    private static Long startTime;

    public static final String BROADCAST_ACTION = "abhijit.travellogger.AudioRecord";
    private final Handler handler = new Handler(Looper.getMainLooper());
    Intent intent;

    @Override
    public void onCreate(){
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show();
        try {
            this.startRecording();
        } catch (IOException e){
            e.printStackTrace();
        }
        showNotification();
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000);
        return START_STICKY;
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        @Override
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 1000);
        }
    };

    private void DisplayLoggingInfo(){
        intent.putExtra("time", new Date().toString());
        Long timeElapsed = (SystemClock.elapsedRealtime() - startTime);
        String counter =  String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(timeElapsed),
                TimeUnit.MILLISECONDS.toMinutes(timeElapsed),
                TimeUnit.MILLISECONDS.toSeconds(timeElapsed)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeElapsed))
        );
        intent.putExtra("counter", counter );
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        this.stopRecording();
        super.onDestroy();
        mBuilder.mNotification.flags  = Notification.FLAG_AUTO_CANCEL;
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancel(1);
    }

    private void showNotification() {
        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.mic).setAutoCancel(true)
                .setContentTitle("Recording started.")
                .setOngoing(true);
        mBuilder.mNotification.flags  = Notification.FLAG_NO_CLEAR;
        Intent resultIntent = new Intent(this, AudioRecord.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(AudioRecord.class);

        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    public void startRecording() throws IOException {
        String timeStamp = DateFormat.getDateTimeInstance().format(new Date());
        String audioName = "AUDIO_";// + timeStamp +"_";

        File appFolderAudio = InitiateApplication.getAppFolderAudio();

        try {
            audioFile = File.createTempFile(audioName, ".aac", appFolderAudio);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        recorder = new MediaRecorder();
        recorder.reset();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(audioFile.getAbsolutePath());

        recorder.prepare();
        recorder.start();
        startTime = SystemClock.elapsedRealtime();
    }

    public void stopRecording() {
        recorder.stop();
        recorder.reset();
        startTime = (long) 0;
        recorder.release();
        recorder = null;
        try {
            saveAudio();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void saveAudio() throws IOException {
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();

        values.put(MediaStore.Audio.Media.TITLE, "Travel Logger - audio" + audioFile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audioFile.getAbsolutePath());
        ContentResolver contentResolver = getContentResolver();

        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        Toast.makeText(this, "Audio saved successfully. \nLocation: " + audioFile.getAbsolutePath() , Toast.LENGTH_LONG).show();
    }
}