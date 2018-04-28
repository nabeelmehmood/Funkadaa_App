package com.example.funkadaa.funkadaa;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funkadaa.classes.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostActivity extends AppCompatActivity {

    Context c;
    TextView name;
    String postid;
    TextView desc;
    TextView name2;
    ImageView dp;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        c=this;
        postid = getIntent().getStringExtra("postid");
        name = (TextView)findViewById(R.id.post_name);
        name2 = (TextView)findViewById(R.id.post_name1);
        desc = (TextView)findViewById(R.id.post_description);
        image = (ImageView)findViewById(R.id.post_image);
        dp = (ImageView)findViewById(R.id.post_dp);
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("posts").child(postid);
        mref.addValueEventListener(postListener);
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
