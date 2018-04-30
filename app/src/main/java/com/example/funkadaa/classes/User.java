package com.example.funkadaa.classes;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by nabee on 4/19/2018.
 */

public class User implements Serializable{
    String id;

    String name;

    String email;

    String phone;

    String dp;

    double latitude;

    double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    Map<String,Post> userposts;

    public Map<String, Post> getUserposts() {
        return userposts;
    }

    public void setUserposts(Map<String, Post> userposts) {
        this.userposts = userposts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String id, String name, String email, String phone) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User(String name, String email, String phone) {

        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String name) {

        this.name = name;
    }
}
