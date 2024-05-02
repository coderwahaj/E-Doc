package com.example.myapplication;
public class Message {
    private String text;
    private long timestamp;
    private String senderName;
    private String senderEmail;
    private String senderId;

    public Message() {
        // Default constructor required for Firebase
    }

    public Message(String text, long timestamp, String senderName, String senderEmail, String senderId) {
        this.text = text;
        this.timestamp = timestamp;
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSenderId() {
        return senderId;
    }
    @Override
    public String toString() {
        return text + "\n" + senderName; // Display text and sender name in the ListView
    }
}