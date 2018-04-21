package com.example.funkadaa.classes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.funkadaa.funkadaa.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private List<SingleHomeFeedItem> items;
    private Context c;
    private HomeViewHolder holder;

    public HomeAdapter(List<SingleHomeFeedItem> items, Context c) {
        this.items = items;
        this.c = c;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_feed, parent, false);
        holder = new HomeViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {

        holder.setValues(items.get(position),c);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
