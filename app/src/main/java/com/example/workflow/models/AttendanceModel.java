package com.example.workflow.models;


public class AttendanceModel {

    private String attendanceDate;

    private String checkintime;

    private String checkinlocation;

    private String checkouttime;

    private String checkoutlocation;

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getCheckintime() {
        return checkintime;
    }

    public void setCheckintime(String checkintime) {
        this.checkintime = checkintime;
    }

    public String getCheckinlocation() {
        return checkinlocation;
    }

    public void setCheckinlocation(String checkinLocation) {
        checkinlocation = checkinLocation;
    }

    public String getCheckouttime() {
        return checkouttime;
    }

    public void setCheckouttime(String checkoutTime) {
        checkouttime = checkoutTime;
    }

    public String getCheckoutlocation() {
        return checkoutlocation;
    }

    public void setCheckoutlocation(String checkoutLocation) { this.checkoutlocation = checkoutLocation; }


}