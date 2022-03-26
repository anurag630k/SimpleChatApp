package com.example.simplechatapp;

public class message {
    private String message,senderid;

    public message() {
    }


    public message(String message, String senderid) {
        this.message = message;
        this.senderid = senderid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }
}
