package com.example.workflow.models;

import java.util.Date;

public class WorkScheduleModel {
    private String userName;
    private Date startDate;
    private Date endDate;

    public WorkScheduleModel(){}

    public WorkScheduleModel(String userName, Date startDate, Date endDate) {
        this.userName = userName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
