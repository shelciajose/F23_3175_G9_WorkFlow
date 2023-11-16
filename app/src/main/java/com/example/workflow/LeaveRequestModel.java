package com.example.workflow;

public class LeaveRequestModel {
    private String userId;
    private String firstName;
    private String lastName;
    private String Dept;

    LeaveRequestModel(){}

    public LeaveRequestModel(String userId, String firstName, String lastName, String dept) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        Dept = dept;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public String getDept() {
        return Dept;
    }

    public void setDept(String dept) {
        Dept = dept;
    }
}
