package com.example.myapplication.objects;

public class ChatConversation {
    private String sender,date,text,name,title;

    public ChatConversation(String sender, String date, String text, String title) {
        this.sender = sender;
        this.date = date;
        this.text = text;
        this.title = title;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ChatConversation() {
    }
}
