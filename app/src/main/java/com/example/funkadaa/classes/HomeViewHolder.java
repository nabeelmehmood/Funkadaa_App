package com.example.funkadaa.classes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funkadaa.funkadaa.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeViewHolder  extends RecyclerView.ViewHolder{
    ImageButton Like;
    ImageButton Comment;
    ImageButton Save;
    ImageButton AddToCart;
    ImageView Image;
    ImageView Dp;
    TextView Name1;
    TextView Name2;
    TextView Description;
    String d;
    public HomeViewHolder(View view) {
        super(view);
    Like=(ImageButton) view.findViewById(R.id.imageButton1);
    Comment=(ImageButton)view.findViewById(R.id.imageButton2);
    Save=(ImageButton)view.findViewById(R.id.imageButton3);
    AddToCart=(ImageButton)view.findViewById(R.id.imageButton4);
    Image=(ImageView)view.findViewById(R.id.imageView);
    Dp=(ImageView)view.findViewById(R.id.imageView2);
    Name1=(TextView)view.findViewById(R.id.textView);
    Name2=(TextView)view.findViewById(R.id.textView2);
    Description=(TextView)view.findViewById(R.id.textView3);

    }
    public void setValues(SingleHomeFeedItem S, Context c){
        new ImageDownloaderAsync(Dp,c).execute(S.getImageUrlDp());
        new ImageDownloaderAsync(Image,c).execute(S.getImageUrlItem());

        DateFormat df3 = new SimpleDateFormat("dd-MMM-yyyy");
        d=(df3.format(S.getTime()));
       Name1.setText(S.getU().getName());

        if(S.getDescription()!=null)
        {
            Name2.setVisibility(View.VISIBLE);
            Name2.setText(S.getU().getName());
            Description.setVisibility(View.VISIBLE);
            Description.setText(S.getDescription());
        }

    }
}
