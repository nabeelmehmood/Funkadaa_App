package com.example.funkadaa.classes;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by nabee on 4/25/2018.
 */

public class Post implements Serializable{
    String imageID;
    String description;
    String uploaderID;
    Date time;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    int likes;

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
        this.likes = 0;
    }

    public String getUploaderID() {

        return uploaderID;
    }

    public void setUploaderID(String uploaderID) {
        this.uploaderID = uploaderID;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Post(String imageID, String description) {
        this.imageID = imageID;
        this.description = description;
    }

    public Post() {
    }
}
