package com.example.funkadaa.classes;

import java.util.List;

/**
 * Created by nabee on 4/25/2018.
 */

class Cart {
    List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Cart(List<Post> posts) {

        this.posts = posts;
    }
}
