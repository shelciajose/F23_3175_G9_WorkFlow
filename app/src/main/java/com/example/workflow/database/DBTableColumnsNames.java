package com.example.workflow.database;

public class DBTableColumnsNames {

    ///////////////////////////////////// Table names  ////////////////////////////////////////////
////////////////////// Table Create ---- Columns names


    // tabel name
    static final String OFFLINE_ATTENDENCE_CHECKIN_TABLE_NAME = "Offline_CheckIn";
    static final String OFFLINE_ATTENDENCE_CHECKOUT_TABLE_NAME = "Offline_CheckOut";
    static final String OFFLINE_USER_TABLE_NAME = "Users";
    // COL
    static final String OFFLINE_CHECK_IN_TIME = "CheckInTime";
    static final String OFFLINE_CHECK_IN_LATITUDE = "CheckIn_Latitude";
    static final String OFFLINE_CHECK_IN_LONGITUDE = "CheckIn_Longitude";
    static final String OFFLINE_CHECK_IN_LOCATION = "CheckIn_Location";
    static final String OFFLINE_CHECK_IN_IS_MOCK = "CheckIn_IsMock";
    static final String OFFLINE_CHECK_IN_BATTERY_PERCENTAGE = "CheckIn_BatteryPerc";
    static final String OFFLINE_CHECK_IN_SIGNAL_STRENGTH = "CheckIn_SignalStrength";
    static final String OFFLINE_CHECK_IN_NETWORK_TYPE = "CheckIn_NetworkType";
    static final String OFFLINE_CHECK_IN_REFERENCE_ID = "CheckIn_RefeId";
    static final String OFFLINE_CHECK_IN_DATE = "CheckIn_Date";

    static final String OFFLINE_CHECK_OUT_TIME = "CheckOutTime";
    static final String OFFLINE_CHECK_OUT_LATITUDE = "CheckOut_Latitude";
    static final String OFFLINE_CHECK_OUT_LONGITUDE = "CheckOut_Longitude";
    static final String OFFLINE_CHECK_OUT_LOCATION = "CheckOut_Location";
    static final String OFFLINE_CHECK_OUT_REFERENCE_ID = "CheckOut_RefeId";
    static final String OFFLINE_CHECK_OUT_CHECKIN_REFID = "CheckOut_CheckInRefeId";
    static final String OFFLINE_CHECK_OUT_DATE = "CheckOut_Date";
    static final String OFFLINE_USERDETAILS_ID = "UserID";
    static final String OFFLINE_USERDETAILS_USERNAME = "UserName";
    static final String OFFLINE_USERDETAILS_FIRSTNAME = "FirstName";
    static final String OFFLINE_USERDETAILS_LASTNAME = "LastName";
    static final String OFFLINE_USERDETAILS_DEPARTMENT = "Department";
    static final String OFFLINE_USERDETAILS_EMAIL = "Email";
    static final String OFFLINE_USERDETAILS_PHONE = "Phone";
    static final String OFFLINE_USERDETAILS_ADDRESS = "Address";



    static final String CREATE_OFFLINE_ATTENDENCE_CHECKIN_TABLE = "CREATE TABLE " + OFFLINE_ATTENDENCE_CHECKIN_TABLE_NAME + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            OFFLINE_CHECK_IN_TIME + " TEXT," +
            OFFLINE_CHECK_IN_LATITUDE + " TEXT," +
            OFFLINE_CHECK_IN_LONGITUDE + " TEXT," +
            OFFLINE_CHECK_IN_LOCATION + " TEXT," +
            OFFLINE_CHECK_IN_DATE + " TEXT," +
            OFFLINE_CHECK_IN_REFERENCE_ID + " TEXT);";

    static final String CREATE_OFFLINE_ATTENDENCE_CHECKOUT_TABLE = "CREATE TABLE " + OFFLINE_ATTENDENCE_CHECKOUT_TABLE_NAME + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            OFFLINE_CHECK_OUT_TIME + " TEXT," +
            OFFLINE_CHECK_OUT_LATITUDE + " TEXT," +
            OFFLINE_CHECK_OUT_LONGITUDE + " TEXT," +
            OFFLINE_CHECK_OUT_LOCATION + " TEXT," +
            OFFLINE_CHECK_OUT_DATE + " TEXT," +
            OFFLINE_CHECK_OUT_CHECKIN_REFID + " TEXT," +
            OFFLINE_CHECK_OUT_REFERENCE_ID + " TEXT);";


    static final String CREATE_OFFLINE_USER_TABLE = "CREATE TABLE " + OFFLINE_USER_TABLE_NAME + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            OFFLINE_USERDETAILS_ID + " TEXT," +
            OFFLINE_USERDETAILS_USERNAME + " TEXT," +
            OFFLINE_USERDETAILS_FIRSTNAME + " TEXT," +
            OFFLINE_USERDETAILS_LASTNAME + " TEXT," +
            OFFLINE_USERDETAILS_EMAIL + " TEXT," +
            OFFLINE_USERDETAILS_PHONE + " TEXT," +
            OFFLINE_USERDETAILS_ADDRESS + " TEXT," +
            OFFLINE_USERDETAILS_DEPARTMENT + " TEXT);";



}

