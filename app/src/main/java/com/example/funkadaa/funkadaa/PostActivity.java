package com.example.funkadaa.funkadaa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funkadaa.classes.*;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.ByteArrayOutputStream;

public class PostActivity extends AppCompatActivity {

    Context c;
    TextView name;
    String postid;
    TextView desc;
    TextView name2;
    ImageView dp;
    ImageView image;
    ImageButton shareButton;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    ImageButton twitterButton;
    int WRITE_STORAGE_PERMISSION_REQUEST_CODE = 1193;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        shareButton = (ImageButton)findViewById(R.id.imageButton1);
        shareButton.setOnClickListener(shareListener);
        twitterButton = (ImageButton)findViewById(R.id.imageButton4);
        shareDialog = new ShareDialog(this);
        c=this;
        postid = getIntent().getStringExtra("postid");
        name = (TextView)findViewById(R.id.post_name);
        name2 = (TextView)findViewById(R.id.post_name1);
        desc = (TextView)findViewById(R.id.post_description);
        image = (ImageView)findViewById(R.id.post_image);
        twitterButton.setOnClickListener(tweetListener);
        dp = (ImageView)findViewById(R.id.post_dp);
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("posts").child(postid);
        mref.addValueEventListener(postListener);
    }

    View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bitmap b = ((BitmapDrawable)image.getDrawable()).getBitmap();
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(b)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            if(shareDialog.canShow(SharePhotoContent.class)){
                shareDialog.show(content);
            }
            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
        }
    };

    View.OnClickListener tweetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkPermissionForWriteExtertalStorage()) {
                Bitmap b = ((BitmapDrawable) image.getDrawable()).getBitmap();
                Uri u = getImageUri(c, b);
                TweetComposer.Builder builder = new TweetComposer.Builder(c)
                        .text("Check out this post from FunKadaa. #FunKadaa")
                        .image(u);
                builder.show();
            }
            else{
                try {
                    requestPermissionForReadExtertalStorage();
                    Bitmap b = ((BitmapDrawable) image.getDrawable()).getBitmap();
                    Uri u = getImageUri(c, b);
                    TweetComposer.Builder builder = new TweetComposer.Builder(c)
                            .text("Check out this post from FunKadaa. #FunKadaa")
                            .image(u);
                    builder.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public boolean checkPermissionForWriteExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = c.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) c, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            final com.example.funkadaa.classes.Post p = dataSnapshot.getValue(com.example.funkadaa.classes.Post.class);
            name = (TextView)findViewById(R.id.post_name);
            name2 = (TextView)findViewById(R.id.post_name1);
            desc = (TextView)findViewById(R.id.post_description);
            image = (ImageView)findViewById(R.id.post_image);
            dp = (ImageView)findViewById(R.id.post_dp);

            if (p != null) {
                desc.setText(p.getDescription());
            }

            String id = p.getUploaderID();
            dp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(c,"Loading profile",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(c,ProfileActivity.class);
                    i.putExtra("userid", p.getUploaderID());
                    startActivity(i);
                }
            });
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(id);
            ref.addValueEventListener(userListener);


            if (p != null) {
                new ImageDownloaderAsync(image,c).execute(p.getImageID());
            }
            // ...
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("POSTS", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };

    ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            String username = (String)dataSnapshot.child("name").getValue();
            String url = (String)dataSnapshot.child("dp").getValue();
            name.setText(username);
            name2.setText(username);

            new ImageThumbnailDownloaderAsync(dp,c).execute(url);

            // ...
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("POSTS", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };


}
