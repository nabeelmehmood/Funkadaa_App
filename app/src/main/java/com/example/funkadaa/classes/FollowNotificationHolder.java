package com.example.funkadaa.classes;

import android.app.Notification;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funkadaa.funkadaa.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by nabee on 4/19/2018.
 */

public class FollowNotificationHolder extends MyNotificationViewHolder {

    Button followButton;

    public FollowNotificationHolder(View view) {
        super(view);
        image = (ImageView) view.findViewById(R.id.follownotification_image);
        notif = (TextView) view.findViewById(R.id.follownotification_string);
        date = (TextView) view.findViewById(R.id.follownotification_date);
        followButton = (Button) view.findViewById(R.id.follownotification_button);
    }

    public void setValues(MyNotification notification, Context c){
        new ImageDownloaderAsync(image,c).execute(notification.getImageUrl());
        notif.setText(notification.getNotif());
        DateFormat df3 = new SimpleDateFormat("dd-MMM-yyyy");
        date.setText(df3.format(notification.getTime()));
        followButton.setVisibility(View.VISIBLE);

    }

}

