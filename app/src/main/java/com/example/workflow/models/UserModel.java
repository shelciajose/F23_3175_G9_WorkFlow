package com.example.workflow.models;

public class UserModel {
    private String firstName;
    private String lastName;
    private String Address;
    private String Dept;
    private String emailAddress;
    private String phonenumber;
    private String userName;

    private String userId;

    public UserModel(){}

    public UserModel(String firstName, String lastName, String address, String dept, String emailAddress, String phonenumber, String userName, String userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        Address = address;
        Dept = dept;
        this.emailAddress = emailAddress;
        this.phonenumber = phonenumber;
        this.userName = userName;
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDept() {
        return Dept;
    }

    public void setDept(String dept) {
        Dept = dept;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
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

    //    private String userName;
//    private String userId;
//    private String emailAddress;
////    private String image;
//
//    public UserModel(){}
//
//    public UserModel(String userName, String userId, String emailAddress) {
//        this.userName = userName;
//        this.userId = userId;
//        this.emailAddress = emailAddress;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getEmailAddress() {
//        return emailAddress;
//    }
//
//    public void setEmailAddress(String emailAddress) {
//        this.emailAddress = emailAddress;
//    }
}
