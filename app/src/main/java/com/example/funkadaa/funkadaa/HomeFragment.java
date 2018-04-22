package com.example.funkadaa.funkadaa;


import android.content.Context;
import android.os.Bundle;
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
import com.example.funkadaa.classes.HomeAdapter;
import com.example.funkadaa.classes.HomeViewHolder;
import com.example.funkadaa.classes.MyNotification;
import com.example.funkadaa.classes.MyNotificationViewHolder;
import com.example.funkadaa.classes.SingleHomeFeedItem;
import com.example.funkadaa.classes.User;

import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {


    RecyclerView rv;
    HomeAdapter ad;
    ArrayList<SingleHomeFeedItem> list;
    Context c;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        User u=new User();
        u.setName("Blah Blah ");

        // String imageUrlItem, String imageUrlDp, String description, Date time

        SingleHomeFeedItem s = new SingleHomeFeedItem(u,"https://firebasestorage.googleapis.com/v0/b/funkadaa-5454.appspot.com/o/4CFED752-63BF-4C2F-81D3-751E6866EF7C.jpg?alt=media&token=3bf70999-5d0d-4a0d-9dc3-f42dd4481db3", "https://firebasestorage.googleapis.com/v0/b/funkadaa-5454.appspot.com/o/4CFED752-63BF-4C2F-81D3-751E6866EF7C.jpg?alt=media&token=3bf70999-5d0d-4a0d-9dc3-f42dd4481db3", "ABCDEFGHIJKLMNOPQ",new Date());

        list = new ArrayList<>();
        list.add(s);
        list.add(s);
        list.add(s);
        list.add(s);
        list.add(s);
        list.add(s);
        list.add(s);
        list.add(s);
        list.add(s);
        list.add(s);
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
