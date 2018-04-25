package com.example.funkadaa.classes;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nabee on 4/25/2018.
 */

public class Post {
    String imageID;
    String description;
    String uploaderID;
    Date time;
    Map<String,Boolean> likedby;


    public Map<String, Boolean> getLikedby() {
        return likedby;
    }

    public void setLikedby(Map<String, Boolean> likedby) {
        this.likedby = likedby;
    }



    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }



    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Post(String imageID, String description, String uploaderID) {
        this.imageID = imageID;
        this.description = description;
        this.uploaderID = uploaderID;
        this.likedby = new HashMap<>();
        likedby.put(uploaderID,true);
    }

    public String getUploaderID() {

        return uploaderID;
    }

    public void setUploaderID(String uploaderID) {
        this.uploaderID = uploaderID;
    }

    public int getLikes() {
        return likedby.size();
    }


    public Post(String imageID, String description) {
        this.imageID = imageID;
        this.description = description;
    }

    public Post() {
    }
}
