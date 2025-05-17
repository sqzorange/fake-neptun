package com.example.fakeneptun.model;

public class Message {
    private String id;
    private String senderId;
    private String senderName;
    private String text;
    private long timestamp;

    public Message() { }

    public Message(String senderId, String senderName, String text, long timestamp) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getSenderName() { return senderName; }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public long getTimestamp() { return timestamp; }
}