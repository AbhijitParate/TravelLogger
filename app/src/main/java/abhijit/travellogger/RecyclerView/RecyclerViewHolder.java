package abhijit.travellogger.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import abhijit.travellogger.R;

/*
 * Created by abhijit on 11/20/15.
 */
//2. Views for mimetypes
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private View imageLayout;
    private View videoLayout;
    private View audioLayout;
    private View noteLayout;
    private View dismissLayout;

    private TextView imageTitle;
    private ImageView imageView;

    private TextView videoTitle;
    private VideoView videoView;

    private TextView audioTitle;
    private ImageButton audioButton;
    private SeekBar audioSeekbar;

    private TextView noteTitle;
    private TextView noteBody;

    private TextView undoText;
    private TextView deleteText;

    public RecyclerViewHolder(View view){
        super(view);
        imageLayout = view.findViewById(R.id.image);
        videoLayout = view.findViewById(R.id.video);
        audioLayout = view.findViewById(R.id.audio);
        noteLayout = view.findViewById(R.id.note);
        dismissLayout = view.findViewById(R.id.dismiss_layout);

        imageTitle = (TextView) view.findViewById(R.id.image_title);
        imageView = (ImageView) view.findViewById(R.id.image_view);

        videoTitle = (TextView) view.findViewById(R.id.video_title);
        videoView = (VideoView) view.findViewById(R.id.video_View);

        audioTitle = (TextView) view.findViewById(R.id.audio_title);
        audioButton = (ImageButton) view.findViewById(R.id.audio_button);
        audioSeekbar = (SeekBar) view.findViewById(R.id.audio_seekbar);

        noteTitle = (TextView) view.findViewById(R.id.note_title);
        noteBody = (TextView) view.findViewById(R.id.note_text);

        undoText = (TextView) view.findViewById(R.id.txt_undo);
        deleteText = (TextView) view.findViewById(R.id.txt_delete);

    }

    public View getImageLayout(){ return imageLayout; }

    public View getVideoLayout(){ return videoLayout; }

    public View getAudioLayout(){ return audioLayout; }

    public View getNoteLayout(){ return noteLayout; }

    public TextView getImageTitle() {
        return imageTitle;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getNoteBody() {
        return noteBody;
    }

    public TextView getNoteTitle() {
        return noteTitle;
    }

    public SeekBar getAudioSeekbar() {
        return audioSeekbar;
    }

    public ImageButton getAudioButton() {
        return audioButton;
    }

    public TextView getAudioTitle() {
        return audioTitle;
    }

    public VideoView getVideoView() {
        return videoView;
    }

    public TextView getVideoTitle() {
        return videoTitle;
    }

}