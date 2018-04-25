package com.example.funkadaa.funkadaa;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class SearchItem extends Fragment {

    public SearchItem() {
        // Required empty public constructor



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment




        return inflater.inflate(R.layout.fragment_search_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv1=(TextView)getView().findViewById(R.id.textView);
                tv1.setText("Hi");

        TextView tv2=(TextView)getView().findViewById(R.id.textView2);
        tv2.setText("Hi");

        TextView tv3=(TextView)getView().findViewById(R.id.textView3);
        tv3.setText("Hi");

        ImageView imageView=(ImageView) getView().findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.zain19);


        ImageView imageView2=(ImageView) getView().findViewById(R.id.imageView2);
        imageView.setImageResource(R.drawable.starrynight);

    }
}
