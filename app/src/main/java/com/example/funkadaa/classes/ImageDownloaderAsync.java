package com.example.funkadaa.classes;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by nabee on 4/19/2018.
 */

public class ImageDownloaderAsync extends AsyncTask<String, Void, Void> {

    WeakReference<Context> c;
    WeakReference<ImageView> imageView;
    String url;
    Bitmap bmp;
    boolean found;

    public ImageDownloaderAsync(ImageView imv,Context c) {
        this.imageView = new WeakReference<ImageView>(imv);
        this.c = new WeakReference<Context>(c);
        found = false;
    }
    @Override
    protected Void doInBackground(String... urlString) {

        download_Image(urlString[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if(found){
            ImageView img = imageView.get();
            img.setImageBitmap(bmp);
        }

    }

    public String getUrl() {
        return url;
    }

    private void download_Image(String url) {
        this.url = url;

        bmp =null;

        if (ifExists(url)){
            bmp = getImage(url);
            found = true;
        }
        else {
            try {
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference ref = storage.getReference().child("images").child(url);
                ref.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Use the bytes to display the image
                        ByteArrayInputStream imageStream = new ByteArrayInputStream(bytes);

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeStream(imageStream, null, options);
                        int imageHeight = options.outHeight;
                        int imageWidth = options.outWidth;
                        String imageType = options.outMimeType;
                        imageStream.reset();
                        // The new size we want to scale to
                        final int REQUIRED_SIZE = 200;

                        // Find the correct scale value. It should be the power of 2.
                        int scale = 1;
                        while (options.outWidth / scale / 2 >= REQUIRED_SIZE &&
                                options.outHeight / scale / 2 >= REQUIRED_SIZE) {
                            scale *= 2;
                        }
                        options.inJustDecodeBounds = false;
                        BitmapFactory.Options o2 = new BitmapFactory.Options();
                        o2.inSampleSize = scale;
                        Bitmap theImage = BitmapFactory.decodeStream(imageStream,null,o2);
                        setBitmap(theImage);
                        Context context = c.get();
                        saveToInternalStorage(theImage,context,getUrl());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

            } catch (Exception e) {
                Log.d("Exception", e.getMessage());
            }
        }



    }
    public void setBitmap(Bitmap b){
        ImageView img = imageView.get();
        img.setImageBitmap(b);
    }

    private String saveToInternalStorage(Bitmap bitmapImage, Context c, String imageid){

        ContextWrapper cw = new ContextWrapper(c);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,imageid);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 70, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public Boolean ifExists(String url) {
        Context context = c.get();
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        File mypath = new File(directory, url);

        if (mypath.exists())
            return true;
        return false;
    }

    public Bitmap getImage(String url) {
        Context context = c.get();
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        if(ifExists(url)) {
            File mypath = new File(directory, url);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(mypath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            bmp = BitmapFactory.decodeStream(fis, null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 130;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            fis = null;
            try {
                fis = new FileInputStream(mypath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            bmp = BitmapFactory.decodeStream(fis, null, o2);
        }
        return bmp;
    }
}
