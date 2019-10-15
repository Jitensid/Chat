package com.finalp.chatapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {

    private String author;
    private String content;
    private String date;
    private String time;

    public Message(){

    }

    public Message(String author, String content) {
        this.author = author;
        this.content = content;
        this.date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        this.time = new SimpleDateFormat("HH:mm a",Locale.getDefault()).format(new Date());
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
