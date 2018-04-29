package com.example.funkadaa.classes;

import java.util.Date;

/**
 * Created by nabee on 4/19/2018.
 */

public class FollowNotification extends MyNotification {

    public FollowNotification() {

    }


    public FollowNotification(User user, String notif, String imageUrl, Date time) {
        super(user, notif, imageUrl, time);
    }

    public FollowNotification(User user, boolean followed) {
        super(user);


    }
}
