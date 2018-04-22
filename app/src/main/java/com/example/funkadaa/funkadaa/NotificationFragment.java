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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.funkadaa.classes.FollowNotification;
import com.example.funkadaa.classes.MyNotification;
import com.example.funkadaa.classes.MyNotificationViewHolder;
import com.example.funkadaa.classes.NotificationRecyclerAdapter;
import com.example.funkadaa.classes.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;


public class NotificationFragment extends Fragment {
    RecyclerView rv;
    RecyclerView.Adapter<MyNotificationViewHolder> ad;
    ArrayList<MyNotification> list;
    Context c;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //dummydata
        User u = new User("Nabeel");
        MyNotification notification = new FollowNotification(u,"followed you", "https://firebasestorage.googleapis.com/v0/b/funkadaa-5454.appspot.com/o/4CFED752-63BF-4C2F-81D3-751E6866EF7C.jpg?alt=media&token=3bf70999-5d0d-4a0d-9dc3-f42dd4481db3", new Date(), false);
        list = new ArrayList<>();
        list.add(notification);
        list.add(notification);
        list.add(notification);
        list.add(notification);
        list.add(notification);
        list.add(notification);
        list.add(notification);
        list.add(notification);
        list.add(notification);

        ad = new NotificationRecyclerAdapter(list,R.layout.follow_notification,c);


        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        return view;
    }

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
