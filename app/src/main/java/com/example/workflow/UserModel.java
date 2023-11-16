package com.example.workflow;

public class UserModel {
    private String userName;
    private String userId;
    private String emailAddress;
//    private String image;

    public UserModel(){}

    public UserModel(String userName, String userId, String emailAddress) {
        this.userName = userName;
        this.userId = userId;
        this.emailAddress = emailAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
