package com.example.cxmuserapp.helpersClasses;

public class MessageModel {
    private String msgId, senderId, message;
    private long timestamp; // New field for timestamp

    public MessageModel(String msgId, String senderId, String message, long timestamp) {
        this.msgId = msgId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MessageModel() {
    }

    public String getMsgId() {
        return msgId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
