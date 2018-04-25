package com.example.funkadaa.funkadaa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.funkadaa.classes.SelectUpload;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

public class Post extends Fragment implements SelectUpload.OnPhotoSelectedListener {

    private Bitmap mSelectedBitmap;
    private Uri mSelectedUri;

    private static final String TAG = "PostFragment";
    ImageView imageView;
    EditText editText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         imageView=(ImageView)getView().findViewById(R.id.imageView4);
         editText=(EditText)getView().findViewById(R.id.editText);
        String temp= String.valueOf(getArguments().getBundle("image"));
        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(new File(temp)));
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void getImagePath(Uri imagePath) {
        Log.d(TAG, "getImagePath: setting the image to imageview");
        ImageLoader.getInstance().displayImage(imagePath.toString(), imageView);
        //assign to global variable
        mSelectedBitmap = null;
        mSelectedUri = imagePath;
    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        Log.d(TAG, "getImageBitmap: setting the image to imageview");
        imageView.setImageBitmap(bitmap);
        //assign to a global variable
        mSelectedUri = null;
        mSelectedBitmap = bitmap;
    }


}
