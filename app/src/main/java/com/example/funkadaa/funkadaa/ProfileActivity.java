package com.example.funkadaa.funkadaa;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funkadaa.classes.SearchAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    Context c;
    GridView gridView;
    TextView name;
    String userid;
    SearchAdapter ad;

    ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            String username = (String)dataSnapshot.child("name").getValue();
            name.setText(username);

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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

//                startActivity(i);
//
//                F = new SearchItem();
//                Bundle b = new Bundle();
//                String postid = ad.getPostIDs().get(position);
//                b.putString("postid", postid);
//                F.setArguments(b);
//                ChangeFrag();
            }
        });
    }
}
