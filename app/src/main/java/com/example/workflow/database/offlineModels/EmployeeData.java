package com.example.workflow.database.offlineModels;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmployeeData {

    public String empname;
    public String email;
    public String empid;
    public String phone;
    public String address;
    public String dept;

    public EmployeeData() {
    }

    public EmployeeData(String empname, String email, String empid, String phone, String address, String dept) {
        this.empname = empname;
        this.email = email;
        this.empid = empid;
        this.phone = phone;
        this.address = address;
        this.dept = dept;
    }
}
