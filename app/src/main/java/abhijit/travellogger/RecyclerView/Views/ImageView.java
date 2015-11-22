package abhijit.travellogger.RecyclerView.Views;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.io.File;

import abhijit.travellogger.RecyclerView.RecyclerViewHolder;

/*
 * Created by abhijit on 11/20/15.
 */
public class ImageView {

    private RecyclerViewHolder itemHolder;
    private File itemFile;

    public ImageView(RecyclerViewHolder recyclerViewHolder, File file) {
        itemHolder = recyclerViewHolder;
        itemFile = file;
    }

    public void setItemHolder(){
        itemHolder.getImageView().setTag("ImageView");;
        itemHolder.getImageTitle().setText("Title for Image.");

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
