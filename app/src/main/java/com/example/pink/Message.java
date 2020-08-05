package com.example.pink;

public class Message {
    private  String Message;
    private  String time;
    private  String date;
    private  String from;

    public Message() {
    }

    public Message(String message, String time, String date, String from) {
        Message = message;
        this.time = time;
        this.date = date;
        this.from = from;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
