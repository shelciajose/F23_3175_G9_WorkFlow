package com.example.workflow.chat;

public class ChatModel {
    private String message;
    private String receiver;
    private String sender;
    private String type;
    private String timeStamp;

    ChatModel(){}

    public ChatModel(String message, String receiver, String sender, String type, String timeStamp) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.type = type;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
