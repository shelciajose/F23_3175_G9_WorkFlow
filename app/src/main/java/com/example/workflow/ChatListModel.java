package com.example.workflow;

public class ChatListModel {
    private String userId;

    public ChatListModel(){}

    public ChatListModel(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
