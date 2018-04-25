package com.example.funkadaa.funkadaa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.funkadaa.classes.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {
    Bitmap bitmap;
    String userid;
    Context c;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        c = this;
        userid = getIntent().getStringExtra("user");
        ImageView img = (ImageView)findViewById(R.id.uploadmage);
        String uri = getIntent().getStringExtra("uri");
        img.setImageURI(Uri.parse(uri));
        bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
        Button upbutton = (Button)findViewById(R.id.upload);
        upbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                byte[] byteArray = stream.toByteArray();
                bitmap.recycle();
                StorageReference storage = FirebaseStorage.getInstance().getReference();
                id = UUID.randomUUID().toString();
                storage = storage.child("images").child(id+".jpg");
                Toast.makeText(c,"Uploading",Toast.LENGTH_LONG).show();
                StorageMetadata metadata = new StorageMetadata.Builder()
                        .setContentType("image/jpg")
                        .build();

                UploadTask uploadTask = storage.putBytes(byteArray,metadata);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(c,"Failed",Toast.LENGTH_LONG).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Toast.makeText(c,"Upload successful",Toast.LENGTH_LONG).show();
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Post p = new Post();
                        p.setImageID(getid()+".jpg");
                        p.setDescription("heres a picture");
                        p.setLikes(0);
                        p.setUploaderID(userid);
                        Date d = new Date();

                        p.setTime(d);
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference ref = db.getReference().child("posts").child(id);
                        ref.setValue(p);
                        ref = db.getReference().child("users").child(userid).child("userposts").child(id);
                        ref.setValue(p);
                        Intent i = new Intent(c,MainActivity.class);

                    }
                });
            }
        });

    }

    public String getid() {
        return id;
    }
}
