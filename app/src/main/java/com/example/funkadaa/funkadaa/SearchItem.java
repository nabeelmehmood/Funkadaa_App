package com.example.funkadaa.funkadaa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funkadaa.classes.*;
import com.example.funkadaa.classes.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;


public class SearchItem extends Fragment implements SensorEventListener {

    DatabaseReference mref;
    TextView desc;
    TextView name;
    TextView name2;
    ImageView dp;
    ImageView image;
    Context c;
    Bundle b;
    String curruser;
    private SensorManager mSensorManager;
    private Sensor Gyro;
    private static final int SENSOR_SENSITIVITY = 4;

    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            final com.example.funkadaa.classes.Post p = dataSnapshot.getValue(Post.class);
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
            ref.addValueEventListener(userListener);

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
        String id = getArguments().getString("postid");
        mref = FirebaseDatabase.getInstance().getReference().child("posts").child(id);
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

        mref.addValueEventListener(postListener);



    }

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
}
