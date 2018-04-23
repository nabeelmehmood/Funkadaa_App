package com.example.funkadaa.classes;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
 * Created by nabee on 4/23/2018.
 */

public class ImageThumbnailDownloaderAsync extends AsyncTask<String, Void, Void> {

        WeakReference<Context> c;
        WeakReference<ImageView> imageView = null;
        String url;
        Bitmap bmp;
        boolean found;

        public ImageThumbnailDownloaderAsync(ImageView imv,Context c) {
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
                    Task<byte[]> task = ref.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            // Use the bytes to display the image
                            ByteArrayInputStream imageStream = new ByteArrayInputStream(bytes);

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeStream(imageStream, null, options);
                            // The new size we want to scale to
                            final int REQUIRED_SIZE = 20;

                            // Find the correct scale value. It should be the power of 2.
                            int scale = 1;
                            while (options.outWidth / scale / 2 >= REQUIRED_SIZE &&
                                    options.outHeight / scale / 2 >= REQUIRED_SIZE) {
                                scale *= 2;
                            }
                            options.inJustDecodeBounds = false;
                            imageStream.reset();

                            Bitmap theImage = BitmapFactory.decodeStream(imageStream, null, options);
                            setBitmap(theImage);
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


                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(fis, null, options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;
                    String imageType = options.outMimeType;

                    // The new size we want to scale to
                    final int REQUIRED_SIZE = 20;

                    // Find the correct scale value. It should be the power of 2.
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE &&
                            options.outHeight / scale / 2 >= REQUIRED_SIZE) {
                        scale *= 2;
                    }
                    fis = null;
                    try {
                        fis = new FileInputStream(mypath);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize = scale;
                    bmp = BitmapFactory.decodeStream(fis,null,o2);
                }
                return bmp;
        }

}
