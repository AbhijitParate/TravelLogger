package abhijit.travellogger.RecyclerView.Views;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;

import abhijit.travellogger.R;
import abhijit.travellogger.RecyclerView.RecyclerViewHolder;

/*
 * Created by abhijit on 11/20/15.
 */
public class AudioView {

    private RecyclerViewHolder itemHolder;
    private File itemFile;

    public AudioView(RecyclerViewHolder recyclerViewHolder, File file) {
        itemHolder = recyclerViewHolder;
        itemFile = file;
    }

    public RecyclerViewHolder setItemHolder(){

        itemHolder.getAudioTitle().setText("Title for for Audio");
        Uri audioFileUri;
        audioFileUri = Uri.fromFile(itemFile);
        final MediaPlayer player = MediaPlayer.create(itemHolder.getAudioTitle().getContext(), audioFileUri);

        final ImageButton playButton =  itemHolder.getAudioButton();
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.isPlaying()) {
                    player.pause();
                    playButton.setImageResource(R.drawable.play);
                } else {
                    player.start();
                    playButton.setImageResource(R.drawable.pause);
                }
            }
        });

        itemHolder.getAudioLayout().setVisibility(View.VISIBLE);
        return itemHolder;
    }
}
