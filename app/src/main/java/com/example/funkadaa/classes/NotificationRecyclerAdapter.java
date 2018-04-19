package com.example.funkadaa.classes;

import android.app.Notification;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by nabee on 4/19/2018.
 */

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<MyNotificationViewHolder> {
    private List<MyNotification> items;
    private int itemLayout;
    private Context c;
    private MyNotificationViewHolder holder;

    public NotificationRecyclerAdapter(List<MyNotification> items, int itemLayout, Context c) {
        this.items = items;
        this.itemLayout = itemLayout;
        this.c = c;
    }

    @Override
    public MyNotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        holder = new MyNotificationViewHolder(v);
        if (viewType == 1) {
            holder = new FollowNotificationHolder(v);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(MyNotificationViewHolder holder, int position) {
        if(items != null && holder != null)
        {
            switch (holder.getItemViewType()) {
                case 1:
                    ((FollowNotificationHolder)holder).setValues(items.get(position), c);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        int type = 0;
        if (items.get(position) instanceof FollowNotification){
            type = 1;
        }
        return type;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
