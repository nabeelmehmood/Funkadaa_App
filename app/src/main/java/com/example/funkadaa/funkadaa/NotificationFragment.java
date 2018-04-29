package com.example.funkadaa.funkadaa;


import android.app.DownloadManager;
import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.funkadaa.classes.ImageThumbnailDownloaderAsync;
import com.example.funkadaa.classes.MyNotification;
import com.example.funkadaa.classes.MyNotificationViewHolder;
import com.example.funkadaa.classes.NotificationRecyclerAdapter;
import com.example.funkadaa.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class NotificationFragment extends Fragment {
    RecyclerView rv;
    String userid;
    RecyclerView.Adapter<MyNotificationViewHolder> ad;
    DatabaseReference mref;
    ArrayList<MyNotification> list;
    Context c;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        c=getContext();
        userid = FirebaseAuth.getInstance().getUid();
        mref = FirebaseDatabase.getInstance().getReference().child("notifications").child(userid);
        //dummydata
        list = new ArrayList<MyNotification>();
        ad = new NotificationRecyclerAdapter(list,R.layout.follow_notification,c);


        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        return view;
    }

    ValueEventListener notificationListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            list = new ArrayList<>();
            for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                FollowNotification f = messageSnapshot.getValue(FollowNotification.class);
                f.setNotif(f.getUser().getName() + " " + f.getNotif());
                list.add(f);
            }

            ad = new NotificationRecyclerAdapter(list,R.layout.follow_notification,c);
            rv.setAdapter(ad);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("POSTS", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rv.setAdapter(null);
        rv = null;
        list=null;
        ad=null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = (RecyclerView) getView().findViewById(R.id.notification_recycler);
        rv.setAdapter(ad);
        mref.addListenerForSingleValueEvent(notificationListener);
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
