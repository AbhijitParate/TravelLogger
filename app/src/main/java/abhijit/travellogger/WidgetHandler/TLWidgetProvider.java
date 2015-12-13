package abhijit.travellogger.WidgetHandler;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import abhijit.travellogger.R;
import abhijit.travellogger.TravelLoggerHomeActivity;

/*
 * Created by abhijit on 12/8/15.
 */
public class TLWidgetProvider extends AppWidgetProvider {

    public static String ACTION_CAMERA = "Camera";
    public static String ACTION_CAMCORDER = "Camcorder";
    public static String ACTION_RECORDER = "Audio Recorder";
    public static String ACTION_NOTES = "Notes";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (   intent.getAction().equals(ACTION_CAMERA)
            || intent.getAction().equals(ACTION_CAMCORDER)
            || intent.getAction().equals(ACTION_RECORDER)
            || intent.getAction().equals(ACTION_NOTES)
//            || intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                ) {
            Toast.makeText(context, intent.getAction()+" button clicked." , Toast.LENGTH_SHORT).show();
        }
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {

            Intent cameraIntent = new Intent(context, TLWidgetProvider.class);
            cameraIntent.setAction(ACTION_CAMERA);
            cameraIntent.putExtra("action", ACTION_CAMERA);
            PendingIntent cameraPendingIntent = PendingIntent.getBroadcast(context, 0, cameraIntent, 0);
            views.setOnClickPendingIntent(R.id.cameraButton, cameraPendingIntent);

            Intent videoIntent = new Intent(context, TLWidgetProvider.class);
            videoIntent.setAction(ACTION_CAMCORDER);
            cameraIntent.putExtra("action", ACTION_CAMCORDER);
            PendingIntent videoPendingIntent = PendingIntent.getBroadcast(context, 0, videoIntent, 0);
            views.setOnClickPendingIntent(R.id.camcorderButton, videoPendingIntent);

            Intent audioIntent = new Intent(context, TLWidgetProvider.class);
            audioIntent.setAction(ACTION_RECORDER);
            cameraIntent.putExtra("action", ACTION_RECORDER);
            PendingIntent audioPendingIntent = PendingIntent.getBroadcast(context, 0, audioIntent, 0);
            views.setOnClickPendingIntent(R.id.audioButton,audioPendingIntent);

            Intent noteIntent = new Intent(context, TLWidgetProvider.class);
            noteIntent.setAction(ACTION_NOTES);
            cameraIntent.putExtra("action",ACTION_NOTES);
            PendingIntent notePendingIntent = PendingIntent.getBroadcast(context, 0, noteIntent, 0);
            views.setOnClickPendingIntent(R.id.noteButton, notePendingIntent);

            // Tell the AppWidgetManager to perform an update on the current appwidget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

}
