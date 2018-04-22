package com.example.funkadaa.classes;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by nabee on 4/22/2018.
 */

public class ImageURLHandler {
    Context c;
    ImageView img;
    Bitmap bmp;

    public ImageURLHandler(Context c, ImageView img) {
        this.c = c;
        this.img = img;
    }

    public ImageURLHandler() {

    }

    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public void loadImage(String url){
        if(ifExists(url)){
            bmp = getImage(url);
        }
        else{
            bmp = downloadImage(url);
            saveToInternalStorage(bmp,c,url);
        }
    }

    public Boolean ifExists(String url){
        ContextWrapper cw = new ContextWrapper(c);
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        File mypath=new File(directory,url);

        if(mypath.exists())
            return true;
        return false;
    }


    public Bitmap downloadImage(String url){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images").child(url);
        Log.v("STRING",imageRef.toString());
        imageRef=storageRef.child("dp");
        Log.v("STRING",imageRef.toString());
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Log.v("STRING",uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e("STRING", exception.toString());
            }
        });
        new ImageDownloaderAsync(img,c).execute("https://firebasestorage.googleapis.com/v0/b/funkadaa-5454.appspot.com/o/dp.jpg?alt=media&token=57495531-9cec-403b-8c8b-b1a8abb8af83");
        return ((BitmapDrawable)img.getDrawable()).getBitmap();
    }

    public Bitmap getImage(String url){
        ContextWrapper cw = new ContextWrapper(c);
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        File mypath=new File(directory,url);
        FileInputStream fis = null;


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bmp = BitmapFactory.decodeStream(new FileInputStream(mypath), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bmp;


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
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
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
}
