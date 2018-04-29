package com.example.funkadaa.funkadaa;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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

public class ProfileActivity extends AppCompatActivity {

    Context c;
    GridView gridView;
    TextView name;
    String userid;
    SearchAdapter ad;
    ImageView dp;
    Button follow;
    String curruser;
    String dpurl;

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
                    follow.setEnabled(false);

                    DatabaseReference curruserref = FirebaseDatabase.getInstance().getReference().child("users").child(curruser).child("dp");
                    curruserref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String dp =(String) dataSnapshot.getValue();
                            DatabaseReference notref = FirebaseDatabase.getInstance().getReference().child("notifications").child(userid);
                            String key = notref.push().getKey();
                            User u = new User();
                            u.setName(name.getText().toString());

                            String notif = "followed you";
                            Date t = new Date();
                            FollowNotification f = new FollowNotification(u,notif,dp,t);
                            notref.child(key).setValue(f);

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
}
