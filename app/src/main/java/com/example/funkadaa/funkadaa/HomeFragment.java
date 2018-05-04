package com.example.funkadaa.funkadaa;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.funkadaa.classes.FollowNotification;
import com.example.funkadaa.classes.HomeAdapter;
import com.example.funkadaa.classes.HomeViewHolder;
import com.example.funkadaa.classes.MyNotification;
import com.example.funkadaa.classes.MyNotificationViewHolder;
import com.example.funkadaa.classes.Post;
import com.example.funkadaa.classes.SingleHomeFeedItem;
import com.example.funkadaa.classes.User;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


public class HomeFragment extends Fragment {


    RecyclerView rv;
    HomeAdapter ad;
    ArrayList<SingleHomeFeedItem> list;
    Context c;
    DatabaseReference ref;
    ArrayList<String> userids;
    private AdView mAdView;


    ValueEventListener followerPostsListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<SingleHomeFeedItem> s = new ArrayList<>();

            if (dataSnapshot.exists()){
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    for (int i = 0; i < userids.size(); i++){
                        if (messageSnapshot.getKey().equals(userids.get(i))){
                            for (DataSnapshot posts: messageSnapshot.child("userposts").getChildren()){
                                Post p = posts.getValue(Post.class);
                                s.add(new SingleHomeFeedItem(messageSnapshot.getValue(User.class),p.getImageID(),messageSnapshot.child("dp").getValue().toString(),p.getDescription(),p.getTime()));
                            }
                        }
                    }
                }

                Collections.shuffle(s);

                list = s;
                ad = new HomeAdapter(list,c);
                rv.setAdapter(ad);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener followerListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            userids = new ArrayList<>();
            list = new ArrayList<>();
            if (dataSnapshot.exists()) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    userids.add(messageSnapshot.getKey());
                }
            }
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("users");
            databaseReference.addListenerForSingleValueEvent(followerPostsListener);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        User u=new User();
        FirebaseUser f = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("users").child(f.getUid()).child("following");
        ref.addListenerForSingleValueEvent(followerListener);
        // String imageUrlItem, String imageUrlDp, String description, Date time
        c=getContext();

        list = new ArrayList<>();
        ad = new HomeAdapter(list,c);




        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rv.setAdapter(null);
        rv=null;
        ad=null;
        list=null;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdView = getView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        rv = (RecyclerView) getView().findViewById(R.id.recyclerview);
        rv.setAdapter(ad);
        rv.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(c);
        rv.setLayoutManager(mLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                rv.getContext(),
                mLayoutManager.getOrientation()
        );
        rv.addItemDecoration(mDividerItemDecoration);
    }

}
