package com.example.funkadaa.classes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.funkadaa.funkadaa.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by nabee on 5/4/2018.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.myViewHolder> {

    private List<SingleHomeFeedItem> items;
    private Context c;

    public CartAdapter(List<SingleHomeFeedItem> items, Context c) {
        this.items = items;
        this.c = c;
    }
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());

        View view=inflater.inflate(R.layout.mycart,parent,false);
        return new myViewHolder(view);

    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        holder.setValues(items.get(position),c);
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt1;
        TextView txt2;
        ImageView pic;


        public myViewHolder(View itemView) {
            super(itemView);
            txt1=(TextView)itemView.findViewById(R.id.title);
            txt2=(TextView)itemView.findViewById(R.id.price);
            pic=(ImageView) itemView.findViewById(R.id.imageView);

        }
        public void setValues(SingleHomeFeedItem S, Context c){

            new ImageThumbnailDownloaderAsync(pic,c).execute(S.getImageUrlDp());
            txt1.setText(S.getU().getName());
            txt2.setText(S.getDescription());


        }
    }


}