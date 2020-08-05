package com.example.pink;

public class postdetails {
    private String username;
    private String dp;
    private String postimage;
    private String time;
    private String caption;
    private String date;
    private String uid;
    private String postid;

    public postdetails() {
    }

    public postdetails(String username, String dp, String postimage, String time, String caption, String date, String uid, String postid) {
        this.username = username;
        this.dp = dp;
        this.postimage = postimage;
        this.time = time;
        this.caption = caption;
        this.date = date;
        this.uid = uid;
        this.postid = postid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
