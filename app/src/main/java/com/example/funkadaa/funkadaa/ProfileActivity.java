package com.example.funkadaa.funkadaa;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funkadaa.classes.FollowNotification;
import com.example.funkadaa.classes.ImageThumbnailDownloaderAsync;
import com.example.funkadaa.classes.SearchAdapter;
import com.example.funkadaa.classes.SelectUpload;
import com.example.funkadaa.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileActivity extends AppCompatActivity  implements SensorEventListener{
    private static final String TAG = "PostFragmentvia profile";
    Context c;
    GridView gridView;
    TextView name;
    String userid;
    SearchAdapter ad;
    ImageView dp;
    Button follow;
    String curruser;
    String dpurl;
    boolean isfollow = false;

    private SensorManager mSensorManager;
    private Sensor mProximity;
    private static final int SENSOR_SENSITIVITY = 4;

    ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            String username = (String)dataSnapshot.child("name").getValue();
            name.setText(username);

            dpurl = (String)dataSnapshot.child("dp").getValue();
            new ImageThumbnailDownloaderAsync(dp,c).execute(dpurl);

            ArrayList<String> s = new ArrayList<>();
            ArrayList<String> p = new ArrayList<>();
            DataSnapshot postSnapshot = dataSnapshot.child("userposts");
            for (DataSnapshot messageSnapshot: postSnapshot.getChildren()) {
                String imageID = (String) messageSnapshot.child("imageID").getValue();
                String postID = (String) messageSnapshot.getKey();
                s.add(imageID);
                p.add(postID);
            }
            ad.setImgIDs(s);
            ad.setPostIDs(p);
            ad.notifyDataSetChanged();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("POSTS", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };

    ValueEventListener followListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            if (dataSnapshot.child(userid).exists()) {
              follow.setEnabled(false);
              isfollow = true;
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("POSTS", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        c=this;

        mSensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        gridView = (GridView)findViewById(R.id.gridview);
        userid = getIntent().getStringExtra("userid");
        ad = new SearchAdapter(c);
        gridView.setAdapter(ad);
        name = (TextView)findViewById(R.id.profilename);
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("users").child(userid);
        mref.addValueEventListener(userListener);
        dp = (ImageView)findViewById(R.id.profileimage);
        curruser = FirebaseAuth.getInstance().getUid();
        follow = (Button)findViewById(R.id.followbutton);
        DatabaseReference following = FirebaseDatabase.getInstance().getReference().child("users").child(curruser).child("following");
        following.addListenerForSingleValueEvent(followListener);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curruser != null){
                    Toast.makeText(c,"Following user",Toast.LENGTH_SHORT).show();
                    DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("users").child(curruser);
                    mref.child("following").child(userid).setValue("true");
                    Toast.makeText(c,"User followed",Toast.LENGTH_SHORT).show();
                    isfollow=true;
                    DatabaseReference curruserref = FirebaseDatabase.getInstance().getReference().child("users").child(curruser);
                    curruserref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String dp = (String) dataSnapshot.child("dp").getValue();
                                String username = (String) dataSnapshot.child("name").getValue();
                                DatabaseReference notref = FirebaseDatabase.getInstance().getReference().child("notifications").child(userid);
                                String key = notref.push().getKey();
                                User u = new User();
                                u.setName(username);

                                String notif = "followed you";
                                Date t = new Date();
                                FollowNotification f = new FollowNotification(u, notif, dp, t);
                                notref.child(key).setValue(f);
                                follow.setEnabled(false);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Toast.makeText(c,"Unable to retrieve data", Toast.LENGTH_SHORT).show();
                }

            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String postid = ad.getPostIDs().get(position);
                Intent i = new Intent(c,PostActivity.class);
                i.putExtra("postid",postid);
                startActivity(i);
                
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                //near
                if (isfollow){
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(curruser).child("following");
                    ref.child(userid).removeValue();
                    follow.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "User Unfollowed", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getApplicationContext(), "User is not being followed", Toast.LENGTH_SHORT).show();

                }


            } else {
                //far
                Toast.makeText(getApplicationContext(), "far", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void followOnProximity() {
        if (curruser != null) {


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}
