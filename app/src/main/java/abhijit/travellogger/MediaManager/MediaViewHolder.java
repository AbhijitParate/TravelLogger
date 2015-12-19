package abhijit.travellogger.MediaManager;

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
public class MediaViewHolder extends RecyclerView.ViewHolder {

    private View imageLayout;
    private View videoLayout;
    private View audioLayout;
    private View noteLayout;
    private View dismissLayout;

    private TextView imageTitle;
    private TextView imageDate;
    private TextView imageLocation;
    private ImageView imageView;

    private TextView videoTitle;
    private TextView videoDate;
    private TextView videoLocation;
    private VideoView videoView;

    private TextView audioTitle;
    private TextView audioDate;
    private TextView audioLocation;
    private ImageButton audioButton;
    private TextView audioStartTime;
    private TextView audioCurrentTime;
    private TextView audioEndTime;
    private SeekBar audioSeekBar;

    private TextView noteTitle;
    private TextView noteDate;
    private TextView noteLocation;
    private TextView noteBody;

    private TextView undoText;
    private TextView deleteText;

    public MediaViewHolder(View view){
        super(view);
        imageLayout = view.findViewById(R.id.image);
        videoLayout = view.findViewById(R.id.video);
        audioLayout = view.findViewById(R.id.audio);
        noteLayout = view.findViewById(R.id.note);
        dismissLayout = view.findViewById(R.id.dismiss_layout);

        imageTitle = (TextView) view.findViewById(R.id.image_title);
        imageDate = (TextView) view.findViewById(R.id.image_date);
        imageLocation = (TextView) view.findViewById(R.id.image_location);
        imageView = (ImageView) view.findViewById(R.id.image_view);

        videoTitle = (TextView) view.findViewById(R.id.video_title);
        videoDate = (TextView) view.findViewById(R.id.video_date);
        videoLocation = (TextView) view.findViewById(R.id.video_location);
        videoView = (VideoView) view.findViewById(R.id.video_View);

        audioTitle = (TextView) view.findViewById(R.id.audio_title);
        audioDate = (TextView) view.findViewById(R.id.audio_date);
        audioLocation = (TextView) view.findViewById(R.id.audio_location);
        audioButton = (ImageButton) view.findViewById(R.id.audio_button);
        audioStartTime = (TextView) view.findViewById(R.id.audio_start_time);
        audioCurrentTime = (TextView) view.findViewById(R.id.audio_current_time);
        audioEndTime = (TextView) view.findViewById(R.id.audio_end_time);
        audioSeekBar = (SeekBar) view.findViewById(R.id.audio_seekbar);

        noteTitle = (TextView) view.findViewById(R.id.note_title);
        noteDate = (TextView) view.findViewById(R.id.note_date);
        noteLocation = (TextView) view.findViewById(R.id.note_location);
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

    public SeekBar getAudioSeekBar() {
        return audioSeekBar;
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

    public TextView getImageDate() {
        return imageDate;
    }

    public void setImageDate(TextView imageDate) {
        this.imageDate = imageDate;
    }

    public TextView getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(TextView imageLocation) {
        this.imageLocation = imageLocation;
    }

    public TextView getVideoLocation() {
        return videoLocation;
    }

    public void setVideoLocation(TextView videoLocation) {
        this.videoLocation = videoLocation;
    }

    public TextView getVideoDate() {
        return videoDate;
    }

    public void setVideoDate(TextView videoDate) {
        this.videoDate = videoDate;
    }

    public TextView getAudioDate() {
        return audioDate;
    }

    public void setAudioDate(TextView audioDate) {
        this.audioDate = audioDate;
    }

    public TextView getAudioLocation() {
        return audioLocation;
    }

    public void setAudioLocation(TextView audioLocation) {
        this.audioLocation = audioLocation;
    }

    public TextView getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(TextView noteDate) {
        this.noteDate = noteDate;
    }

    public TextView getNoteLocation() {
        return noteLocation;
    }

    public void setNoteLocation(TextView noteLocation) {
        this.noteLocation = noteLocation;
    }

    public TextView getAudioStartTime() {
        return audioStartTime;
    }

    public void setAudioStartTime(TextView audioStartTime) {
        this.audioStartTime = audioStartTime;
    }

    public TextView getAudioCurrentTime() {
        return audioCurrentTime;
    }

    public void setAudioCurrentTime(TextView audioCurrentTime) {
        this.audioCurrentTime = audioCurrentTime;
    }

    public TextView getAudioEndTime() {
        return audioEndTime;
    }

    public void setAudioEndTime(TextView audioEndTime) {
        this.audioEndTime = audioEndTime;
    }

}