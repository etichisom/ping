package com.example.pink;

public class stateofuser {
    private  String date;
    private  String time;
    private  String state;

    public stateofuser() {
    }

    public stateofuser(String date, String time, String state) {
        this.date = date;
        this.time = time;
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
