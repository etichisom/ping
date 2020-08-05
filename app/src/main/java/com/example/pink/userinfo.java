package com.example.pink;

public class userinfo {
    private  String name;
    private  String username;
    private  String country;
    private  String imageuri;

    public userinfo() {
    }
    public userinfo(String name, String username, String country, String imageuri) {
        this.name = name;
        this.username = username;
        this.country = country;
        this.imageuri = imageuri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }
}
