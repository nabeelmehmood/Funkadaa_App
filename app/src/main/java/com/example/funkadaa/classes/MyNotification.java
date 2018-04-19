package com.example.funkadaa.classes;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by nabee on 4/19/2018.
 */

public class MyNotification {
    User user;
    String notif;
    String imageUrl;
    Date time;

    public MyNotification(User user, String notif, String imageUrl, Date time) {

        this.user = user;
        this.notif = notif;
        this.imageUrl = imageUrl;
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public MyNotification() {

    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public MyNotification(User user) {

        this.user = user;
    }

    public String getNotif() {
        return notif;
    }

    public void setNotif(String notif) {
        this.notif = notif;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
