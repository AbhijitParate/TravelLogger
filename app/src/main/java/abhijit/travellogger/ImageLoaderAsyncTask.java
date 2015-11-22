package abhijit.travellogger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;

import abhijit.travellogger.RecyclerView.RecyclerViewAdapter;

/**
 * Created by abhijit on 11/12/15.
 */
public class ImageLoaderAsyncTask extends AsyncTask<File, Void, Bitmap> {

    WeakReference<ImageView> imageViewWeakReference;

    final static int IMAGE_WIDTH = 1072;
    final static int IMAGE_HEIGHT = 603;

    private File imageFile;

    public ImageLoaderAsyncTask(ImageView imageView){
        imageViewWeakReference =  new WeakReference<ImageView>(imageView);
    }

    public File getImageFile(){
        return imageFile;
    }

    @Override
    protected Bitmap doInBackground(File... params) {
        imageFile = params[0];
//        return decodeBitmapFromFile(params[0]);
        Bitmap bitmap = decodeBitmapFromFile(imageFile);
        //Uncomment this to use async task
        MainActivity.setBitmapToMemoryCache(imageFile.getName(), bitmap);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap){
        if(isCancelled()) {
            bitmap = null;
        }
        if(bitmap != null && imageViewWeakReference != null) {
            ImageView imageView = imageViewWeakReference.get();
            //Uncomment this to use async task
//            ImageLoaderAsyncTask bitmapWorkerTask = RecyclerViewAdapter.getImageLoaderTask(imageView);
//            if(this == bitmapWorkerTask && imageView != null){
//                imageView.setImageBitmap(bitmap);
//            }
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options bmOptions) {
        final int photoWidth = bmOptions.outWidth;
        final int photoHeight = bmOptions.outHeight;
        int scaleFactor = 1;

        if(photoWidth > IMAGE_WIDTH || photoHeight > IMAGE_HEIGHT) {
            final int halfPhotoWidth = photoWidth/2;
            final int halfPhotoHeight = photoHeight/2;
            while(halfPhotoWidth/scaleFactor > IMAGE_WIDTH
                    || halfPhotoHeight/scaleFactor > IMAGE_HEIGHT) {
                scaleFactor *= 2;
            }
        }
        return scaleFactor;
    }

    private Bitmap decodeBitmapFromFile(File imageFile) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
        bmOptions.inSampleSize = calculateInSampleSize(bmOptions);
        bmOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
    }
}
