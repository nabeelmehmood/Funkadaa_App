package com.example.funkadaa.classes;

import java.util.Date;

public class SingleHomeFeedItem {
User U;
String imageUrlItem;
String imageUrlDp;
String description;
Date time;

    public SingleHomeFeedItem(User u, String imageUrlItem, String imageUrlDp, String description, Date time) {
        U = u;
        this.imageUrlItem = imageUrlItem;
        this.imageUrlDp = imageUrlDp;
        this.description = description;
        this.time = time;
    }

    public SingleHomeFeedItem(String imageUrlItem, String description) {
        this.imageUrlItem = imageUrlItem;
        this.description = description;
    }

    public SingleHomeFeedItem() {
    }

    public User getU() {
        return U;
    }

    public void setU(User u) {
        U = u;
    }

    public String getImageUrlItem() {
        return imageUrlItem;
    }

    public void setImageUrlItem(String imageUrlItem) {
        this.imageUrlItem = imageUrlItem;
    }

    public String getImageUrlDp() {
        return imageUrlDp;
    }

    public void setImageUrlDp(String imageUrlDp) {
        this.imageUrlDp = imageUrlDp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
