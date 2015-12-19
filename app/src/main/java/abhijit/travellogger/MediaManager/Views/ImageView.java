package abhijit.travellogger.MediaManager.Views;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

import abhijit.travellogger.ApplicationUtility.TravelLogger;
import abhijit.travellogger.MediaManager.Media;
import abhijit.travellogger.MediaManager.MediaDBManager;
import abhijit.travellogger.MediaManager.MediaViewHolder;

/*
 * Created by abhijit on 11/20/15.
 */
public class ImageView {

    private MediaViewHolder itemHolder;
    private File itemFile;

    public ImageView(MediaViewHolder mediaViewHolder, File file) {
        itemHolder = mediaViewHolder;
        itemFile = file;
    }

    public void setItemHolder(){
        itemHolder.getImageView().setTag("ImageView");
        MediaDBManager mediaManager = new MediaDBManager(TravelLogger.getAppContext());
        Media media = mediaManager.getMedia(FilenameUtils.getBaseName(itemFile.getName()));

        if(media != null){
            itemHolder.getImageTitle().setText(media.getTripName());
            itemHolder.getImageDate().setText(media.getCreateDate());
            itemHolder.getImageLocation().setText(media.getLocation());
        } else {
            itemHolder.getImageTitle().setText("Not available");
            itemHolder.getImageDate().setText("Not available");
            itemHolder.getImageLocation().setText("Not available");
        }


        Picasso.with(itemHolder.getImageView().getContext())
                .load(itemFile)
                .resize(1072,603)
                .centerCrop()
                .into(itemHolder.getImageView());

        itemHolder.getImageView().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse(itemFile.toURI().toString()), "image/*");
                view.getContext().getApplicationContext().startActivity(intent);
            }
        });

        itemHolder.getImageLayout().setVisibility(View.VISIBLE);
    }
}
