package com.example.funkadaa.classes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funkadaa.funkadaa.R;

public class SearchItemClicked extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_feed);
        TextView tv1=(TextView)findViewById(R.id.searchitem_name);
        tv1.setText("Hi");

        TextView tv2=(TextView)findViewById(R.id.searchitem_name);
        tv2.setText("Hi");

        TextView tv3=(TextView)findViewById(R.id.searchitem_description);
        tv3.setText("Hi");

        ImageView imageView=(ImageView) findViewById(R.id.searchitem_image);
        imageView.setImageResource(R.drawable.zain1);


        ImageView imageView2=(ImageView) findViewById(R.id.searchitem_dp);
        imageView.setImageResource(R.drawable.zain19);

    }
}
