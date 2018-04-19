package com.example.funkadaa.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nabee on 4/19/2018.
 */

public class ImageDownloaderAsync extends AsyncTask<String, Void, Bitmap> {


    ImageView imageView = null;
    Context context;

    public ImageDownloaderAsync(ImageView imv,Context c) {
        this.imageView = imv;
        context = c;
    }
    @Override
    protected Bitmap doInBackground(String... urlString) {

        return download_Image(urlString[0]);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }

    private Bitmap download_Image(String url) {

        Bitmap bmp =null;
        try{
            URL ulrn = new URL(url);
            HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
            InputStream is = con.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
        }catch(Exception e){
            Log.d("Exception",e.getMessage());
        }
        return bmp;
    }

}
