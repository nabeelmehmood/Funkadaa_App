package com.example.funkadaa.classes;

import java.util.Date;

/**
 * Created by nabee on 4/19/2018.
 */

public class FollowNotification extends MyNotification {
    boolean followed;

    public FollowNotification() {

    }

    public FollowNotification(boolean followed) {

        this.followed = followed;
    }

    public FollowNotification(User user, String notif, String image, Date time, boolean followed) {
        super(user, notif, image, time);
        this.followed = followed;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public FollowNotification(User user, boolean followed) {
        super(user);
        this.followed = followed;

    }
}
