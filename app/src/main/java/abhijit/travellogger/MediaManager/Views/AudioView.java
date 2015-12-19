package abhijit.travellogger.MediaManager.Views;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

import abhijit.travellogger.ApplicationUtility.TravelLogger;
import abhijit.travellogger.MediaManager.Media;
import abhijit.travellogger.MediaManager.MediaDBManager;
import abhijit.travellogger.R;
import abhijit.travellogger.MediaManager.MediaViewHolder;

/*
 * Created by abhijit on 11/20/15.
 */
public class AudioView {

    private MediaViewHolder itemHolder;
    private File itemFile;
    private Handler seekHandler = new Handler();
    private SeekBar seekBar;
    private MediaPlayer player;

    public AudioView(MediaViewHolder mediaViewHolder, File file) {
        itemHolder = mediaViewHolder;
        itemFile = file;
    }

    public MediaViewHolder setItemHolder(){
        Uri audioFileUri;
        audioFileUri = Uri.fromFile(itemFile);
        player = MediaPlayer.create(TravelLogger.getAppContext(), audioFileUri);

        MediaDBManager mediaManager = new MediaDBManager(TravelLogger.getAppContext());
        Media media = mediaManager.getMedia(FilenameUtils.getBaseName(itemFile.getName()));

        if(media != null){
            itemHolder.getAudioTitle().setText(media.getTripName());
            itemHolder.getAudioDate().setText(media.getCreateDate());
            itemHolder.getAudioLocation().setText(media.getLocation());
        } else {
            itemHolder.getAudioTitle().setText("Not available");
            itemHolder.getAudioDate().setText("Not available");
            itemHolder.getAudioLocation().setText("Not available");
        }
        seekBar = itemHolder.getAudioSeekBar();
        final ImageButton playButton =  itemHolder.getAudioButton();
        if(player != null) {
            itemHolder.getAudioStartTime().setText("00:00");
            itemHolder.getAudioCurrentTime().setText("00:00");
            itemHolder.getAudioCurrentTime().setVisibility(View.GONE);
            long millis =  player.getDuration();
            long minutes = (millis / 1000)  / 60;
            long seconds = (millis / 1000) % 60;
            itemHolder.getAudioEndTime().setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
            seekBar.setMax(player.getDuration());

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playButton.setImageResource(R.drawable.play);
                }
            });
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (player.isPlaying()) {
                        player.pause();
                        playButton.setImageResource(R.drawable.play);
                    } else {
                        player.start();
                        playButton.setImageResource(R.drawable.pause);
                        seekUpdateOn();
                    }
                }
            });
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    player.seekTo(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    long millis = seekBar.getProgress();
                    long minutes = (millis / 1000)  / 60;
                    long seconds = (millis / 1000) % 60;
                    itemHolder.getAudioCurrentTime().setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        } else {
            playButton.setImageResource(R.drawable.audio_stop_button);
        }

//        player = MediaPlayer.create(itemHolder.getAudioTitle().getContext(), audioFileUri);
        itemHolder.getAudioLayout().setVisibility(View.VISIBLE);
        return itemHolder;
    }

    public void seekUpdateOn() {
        seekHandler.postDelayed(run, 1000);
        seekBar.setProgress(player.getCurrentPosition());
//        long millis = seekBar.getProgress();
//        long minutes = (millis / 1000)  / 60;
//        long seconds = (millis / 1000) % 60;
//        itemHolder.getAudioCurrentTime().setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
    }

    Runnable run = new Runnable() {
        @Override public void run() {
            seekUpdateOn();
        }
    };

}
