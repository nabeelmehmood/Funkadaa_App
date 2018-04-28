package com.example.funkadaa.classes;

import java.util.HashMap;

/**
 * Created by nabee on 4/28/2018.
 */

public class PostsList {
    HashMap<String,Post> h;

    public HashMap<String, Post> getH() {
        return h;
    }

    public void setH(HashMap<String, Post> h) {
        this.h = h;
    }

    public PostsList() {

    }

    public PostsList(HashMap<String, Post> h) {

        this.h = h;
    }
}
