package com.example.funkadaa.classes;

/**
 * Created by nabee on 4/28/2018.
 */

public class GridViewItem {
    String imgurl;
    String postid;

    public String getImgurl() {
        return imgurl;
    }

    public void setImurl(String imurl) {
        this.imgurl = imgurl;
    }

    public GridViewItem(String imurl, String postid) {

        this.imgurl = imurl;
        this.postid = postid;
    }
}
