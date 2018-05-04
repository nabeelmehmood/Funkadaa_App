package com.example.funkadaa.funkadaa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funkadaa.classes.*;
import com.example.funkadaa.classes.Post;
import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;


public class SearchItem extends Fragment implements SensorEventListener {
    TextToSpeech ts;
    DatabaseReference mref;
    TextView desc;
    TextView name;
    TextView name2;
    ImageView dp;
    ImageView image;
    Context c;
    Bundle b;
    String curruser;
    ImageButton twitterButton;
    ImageButton shareButton;
    ImageButton cartButton;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private SensorManager mSensorManager;
    private Sensor Gyro;
    private static final int SENSOR_SENSITIVITY = 4;
    int WRITE_STORAGE_PERMISSION_REQUEST_CODE = 1194;
    String postid;
    Post p;
    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            p = dataSnapshot.getValue(Post.class);
            desc = (TextView)getView().findViewById(R.id.searchitem_description);
            name = (TextView)getView().findViewById(R.id.searchitem_name);
            name2 = (TextView)getView().findViewById(R.id.searchitem_name1);
            dp =(ImageView) getView().findViewById(R.id.searchitem_dp);

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
            ref.addListenerForSingleValueEvent(userListener);

            image=(ImageView) getView().findViewById(R.id.searchitem_image);
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
            ts.speak(username,TextToSpeech.QUEUE_FLUSH,null);
            name.setText(username);
            name2.setText(username);
            new ImageThumbnailDownloaderAsync(dp,c).execute((String)dataSnapshot.child("dp").getValue());
            // ...
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("POSTS", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };


    public SearchItem() {
        // Required empty public constructor



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ts=new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });
        postid = getArguments().getString("postid");
        mref = FirebaseDatabase.getInstance().getReference().child("posts").child(postid);
        c = getContext();

        mSensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
        Gyro =mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        curruser = FirebaseAuth.getInstance().getUid();
        return inflater.inflate(R.layout.fragment_search_item, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, Gyro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shareButton = (ImageButton)getView().findViewById(R.id.imageButton1);
        shareButton.setOnClickListener(shareListener);
        shareDialog = new ShareDialog(this);
        twitterButton = (ImageButton)getView().findViewById(R.id.imageButton4);
        mref.addValueEventListener(postListener);
        twitterButton.setOnClickListener(tweetListener);
        cartButton = (ImageButton)getView().findViewById(R.id.imageButton3);
        cartButton.setOnClickListener(cartListener);

    }

    View.OnClickListener cartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(curruser);
            ref = ref.child("cart").child(postid);
            ref.child("imageID").setValue(p.getImageID());
            String username = (String) name.getText();
            ref.child("uploadername").setValue(username);
            ref.child("description").setValue(p.getDescription());
            Toast.makeText(c,"Added to card",Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(((BitmapDrawable)image.getDrawable()).getBitmap())
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values[2] > 0.5f) { // anticlockwise
            getView().findViewById(R.id.cid).setBackgroundColor(Color.BLACK);
        } else if(event.values[2] < -0.5f) { // clockwise
            getView().findViewById(R.id.cid).setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void followOnProximity() {
        if (curruser != null) {


        }
    }

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

}
