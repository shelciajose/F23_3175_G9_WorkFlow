package com.example.workflow.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.workflow.database.offlineModels.AttendanceList;
import com.example.workflow.models.AttendanceModel;
import com.example.workflow.models.UserDetails;
import com.example.workflow.utils.CommonFunc;
import com.example.workflow.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.Objects;


public class OfflineDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "workflowlite.db";
    private static final int DATABASE_VERSION = 4;
    private Context context;
    // tabel name
    private static final String OFFLINE_LOC_TABEL = "OFFLINE_LOC_TABEL";
    // COL
    private static final String OFFLINE_LOC_ID = "OFFLINE_LOC_ID";
    private static final String OFFLINE_LOC_USER_ID = "OFFLINE_LOC_USER_ID";
    private static final String OFFLINE_LOC_LONGITUDE = "OFFLINE_LOC_LONGITUDE";
    private static final String OFFLINE_LOC_LATITUDE = "OFFLINE_LOC_LATITUDE";
    private static final String OFFLINE_LOC_USER_ACTIVITY = "OFFLINE_LOC_USER_ACTIVITY";
    private static final String OFFLINE_LOC_DECODED_ADDRESS = "OFFLINE_LOC_DECODED_ADDRESS";
    private static final String OFFLINE_LOC_BATTERY_STRENGTH = "OFFLINE_LOC_BATTERY_STRENGTH";
    private static final String OFFLINE_LOC_NETWORK_STRENGTH = "OFFLINE_LOC_NETWORK_STRENGTH";
    private static final String OFFLINE_LOC_TIME_STAMP = "OFFLINE_LOC_TIME_STAMP";
    private static final String OFFLINE_LOC_BATTERY_TEMP = "OFFLINE_LOC_BATTERY_TEMP";
    private static final String OFFLINE_LOC_NETWORK_TYPE = "OFFLINE_LOC_NETWORK_TYPE";
    private static final String OFFLINE_LOC_IS_OFFLINE = "OFFLINE_LOC_IS_OFFLINE";

    private static final String LAST_CHECK_OUT_TABLE = "LAST_CHECK_OUT";

    public OfflineDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_OFFLINE_LOC_DB = "CREATE TABLE " + OFFLINE_LOC_TABEL + "(" +
                OFFLINE_LOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OFFLINE_LOC_USER_ID + "  TEXT," +
                OFFLINE_LOC_LONGITUDE + " TEXT," +
                OFFLINE_LOC_LATITUDE + " TEXT," +
                OFFLINE_LOC_USER_ACTIVITY + " TEXT," +
                OFFLINE_LOC_BATTERY_STRENGTH + " TEXT," +
                OFFLINE_LOC_NETWORK_STRENGTH + " TEXT," +
                OFFLINE_LOC_DECODED_ADDRESS + " TEXT," +
                OFFLINE_LOC_TIME_STAMP + " DEFAULT CURRENT_TIMESTAMP," +
                OFFLINE_LOC_BATTERY_TEMP + " TEXT," +
                OFFLINE_LOC_IS_OFFLINE + " TEXT," +
                OFFLINE_LOC_NETWORK_TYPE + " TEXT" + ");";
        db.execSQL(CREATE_OFFLINE_LOC_DB);

        // last check out tabel
        String CREATE_LAST_CHECK_OUT = "CREATE TABLE " + LAST_CHECK_OUT_TABLE + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type TEXT" + ");";
        db.execSQL(CREATE_LAST_CHECK_OUT);

        db.execSQL(DBTableColumnsNames.CREATE_OFFLINE_ATTENDENCE_CHECKIN_TABLE);
        db.execSQL(DBTableColumnsNames.CREATE_OFFLINE_ATTENDENCE_CHECKOUT_TABLE);
        db.execSQL(DBTableColumnsNames.CREATE_OFFLINE_USER_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKIN_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKOUT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBTableColumnsNames.OFFLINE_USER_TABLE_NAME);
        onCreate(db);
    }

    public void deleteAllAtTheTimeOfLogout() {
        deleteAllCheckOut();
        deleteAllCheckIn();
        deleteAllLastCheckOuts();
        deleteAllUSerDetails();
    }

    public void addLastCheckOutDetails(String notification) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", notification);
        database.insert(LAST_CHECK_OUT_TABLE, null, values);
    }

    public void deleteAllLastCheckOuts() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(LAST_CHECK_OUT_TABLE, null, null);
        database.close();
    }

    /////////////////// attendence add checkin //////////////////////////
    public int addCheckInDetails(String checkInTime, String latitude, String longitude, String location,
                                 String referenceId, String date) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBTableColumnsNames.OFFLINE_CHECK_IN_TIME, checkInTime);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_IN_LATITUDE, latitude);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_IN_LONGITUDE, longitude);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_IN_LOCATION, location);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_IN_REFERENCE_ID, referenceId);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_IN_DATE, date);
        return (int) database.insert(DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKIN_TABLE_NAME, null, values);
    }

    /////////////////// attendence add checkout //////////////////////////
    public int addCheckOutDetails(String referenceId, String checkOutTime, String checkOutLatitude,
                                  String checkOutLongitude, String location, String date, String checkinID) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBTableColumnsNames.OFFLINE_CHECK_OUT_TIME, checkOutTime);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_OUT_LATITUDE, checkOutLatitude);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_OUT_LONGITUDE, checkOutLongitude);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_OUT_LOCATION, location);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_OUT_REFERENCE_ID, referenceId);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_OUT_CHECKIN_REFID, checkinID);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_OUT_DATE, date);
        return (int) database.insert(DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKOUT_TABLE_NAME, null, values);
    }

    public int addUserDetailsToDB(String userID, String userName, String department,
                                  String phone, String email, String address, String fname, String lname) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBTableColumnsNames.OFFLINE_USERDETAILS_ID, userID);
        values.put(DBTableColumnsNames.OFFLINE_USERDETAILS_USERNAME, userName);
        values.put(DBTableColumnsNames.OFFLINE_USERDETAILS_FIRSTNAME, fname);
        values.put(DBTableColumnsNames.OFFLINE_USERDETAILS_LASTNAME, lname);
        values.put(DBTableColumnsNames.OFFLINE_USERDETAILS_DEPARTMENT, department);
        values.put(DBTableColumnsNames.OFFLINE_USERDETAILS_PHONE, phone);
        values.put(DBTableColumnsNames.OFFLINE_USERDETAILS_EMAIL, email);
        values.put(DBTableColumnsNames.OFFLINE_USERDETAILS_ADDRESS, address);
        return (int) database.insert(DBTableColumnsNames.OFFLINE_USER_TABLE_NAME, null, values);
    }


    public ArrayList<UserDetails> getUser() {
        ArrayList<UserDetails> userArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + DBTableColumnsNames.OFFLINE_USER_TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                UserDetails userList = new UserDetails();

                userList.setUserID(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_USERDETAILS_ID)));
                userList.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_USERDETAILS_USERNAME)));
                userList.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_USERDETAILS_EMAIL)));
                userList.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_USERDETAILS_PHONE)));
                userList.setfName(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_USERDETAILS_FIRSTNAME)));
                userList.setlName(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_USERDETAILS_LASTNAME)));
                userList.setDepartment(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_USERDETAILS_DEPARTMENT)));
                userList.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_USERDETAILS_ADDRESS)));
                userArrayList.add(userList);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return userArrayList;
    }

    public void updateUserDetailsToDatabase(String userID, UserDetails userModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBTableColumnsNames.OFFLINE_USERDETAILS_FIRSTNAME, userModel.getfName() + "");
        values.put(DBTableColumnsNames.OFFLINE_USERDETAILS_LASTNAME, userModel.getlName() + "");
        values.put(DBTableColumnsNames.OFFLINE_USERDETAILS_ADDRESS, userModel.getAddress() + "");
        values.put(DBTableColumnsNames.OFFLINE_USERDETAILS_DEPARTMENT, userModel.getDepartment() + "");
        values.put(DBTableColumnsNames.OFFLINE_USERDETAILS_EMAIL, userModel.getEmail() + "");
        values.put(DBTableColumnsNames.OFFLINE_USERDETAILS_PHONE, userModel.getPhone() + "");
        database.update(DBTableColumnsNames.OFFLINE_USER_TABLE_NAME, values, DBTableColumnsNames.OFFLINE_USERDETAILS_ID + "=?", new String[]{userID});
    }

    ////////////////////////// all user checkin details /////////////////
    public ArrayList<AttendanceList> getUserCheckIn(String date) {
        ArrayList<AttendanceList> attendenceListArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKIN_TABLE_NAME + " WHERE " +  DBTableColumnsNames.OFFLINE_CHECK_IN_DATE + " = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{date});
        if (cursor.moveToFirst()) {
            do {
                AttendanceList attendenceList = new AttendanceList();

                attendenceList.setCheckInLatitude(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_IN_LATITUDE)));
                attendenceList.setCheckInLongitude(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_IN_LONGITUDE)));
                attendenceList.setCheckInTime(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_IN_TIME)));
                attendenceList.setRefNo(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_IN_REFERENCE_ID)));
                attendenceList.setCheckInRef(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_IN_REFERENCE_ID)));
                attendenceList.setCheckInLocation(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_IN_LOCATION)));
                attendenceListArrayList.add(attendenceList);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return attendenceListArrayList;
    }

    ////////////////////////// all user checkout details /////////////////
    public ArrayList<AttendanceList> getUserCheckOut(String date) {
        ArrayList<AttendanceList> attendenceListArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKOUT_TABLE_NAME+ " WHERE " +  DBTableColumnsNames.OFFLINE_CHECK_OUT_DATE + " = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{date});
        if (cursor.moveToFirst()) {
            do {

                AttendanceList attendenceList = new AttendanceList();

                attendenceList.setCheckOutLatitude(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_OUT_LATITUDE)));
                attendenceList.setCheckOutLongitude(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_OUT_LONGITUDE)));
                attendenceList.setCheckOutTime(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_OUT_TIME)));
                attendenceList.setRefNo(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_OUT_REFERENCE_ID)));
                attendenceList.setCheckInRef(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_OUT_CHECKIN_REFID)));
                attendenceList.setCheckOutLocation(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_OUT_LOCATION)));
                attendenceListArrayList.add(attendenceList);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return attendenceListArrayList;
    }

    public ArrayList<AttendanceModel> getAttendanceDetails(String date){
        ArrayList<AttendanceList> checkinDetails = getUserCheckIn(date);
        ArrayList<AttendanceList> checkoutDetails = getUserCheckOut(date);

        ArrayList<AttendanceModel> attendanceModelArrayList = new ArrayList<>();

        for(int i = 0; i< checkinDetails.size(); i++){
            if(checkoutDetails.size()>0) {
                for (int j = 0; j < checkoutDetails.size(); j++) {
                    AttendanceModel attendanceModel = new AttendanceModel();
                    if (Objects.equals(checkinDetails.get(i).getCheckInRef(), checkoutDetails.get(j).getCheckInRef())) {
                        attendanceModel.setAttendanceDate(date);
                        attendanceModel.setCheckintime(checkinDetails.get(i).getCheckInTime());
                        attendanceModel.setCheckinlocation(checkinDetails.get(i).getCheckInLocation());
                        attendanceModel.setCheckouttime(checkoutDetails.get(j).getCheckOutTime());
                        attendanceModel.setCheckoutlocation(checkoutDetails.get(j).getCheckOutLocation());
                    } else {
                        attendanceModel.setAttendanceDate(date);
                        attendanceModel.setCheckintime(checkinDetails.get(i).getCheckInTime());
                        attendanceModel.setCheckinlocation(checkinDetails.get(i).getCheckInLocation());
                        attendanceModel.setCheckouttime("NA");
                        attendanceModel.setCheckoutlocation("NA");
                    }
                    attendanceModelArrayList.add(attendanceModel);
                }
            }
            else {
                AttendanceModel attendanceModel = new AttendanceModel();
                attendanceModel.setAttendanceDate(date);
                attendanceModel.setCheckintime(checkinDetails.get(i).getCheckInTime());
                attendanceModel.setCheckinlocation(checkinDetails.get(i).getCheckInLocation());
                attendanceModel.setCheckouttime("NA");
                attendanceModel.setCheckoutlocation("NA");
                attendanceModelArrayList.add(attendanceModel);
            }
        }

        return attendanceModelArrayList;
    }

    //////////////////////////// get count from check in
    public int getCheckInCountFromDb() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKIN_TABLE_NAME;
        Cursor res = db.rawQuery(query, null);
        return res.getCount();
    }




    //////////////////////////// get count from check out
    public int getCheckOutCountFromDb() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKOUT_TABLE_NAME;
        Cursor res = db.rawQuery(query, null);
        return res.getCount();
    }


    //////////////////// delete all from checkin table
    public void deleteAllCheckIn() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKIN_TABLE_NAME, null, null);
        database.close();
    }

    //////////////////// delete all from checkout table
    public void deleteAllCheckOut() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKOUT_TABLE_NAME, null, null);
        database.close();
    }

    public void deleteAllUSerDetails() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(DBTableColumnsNames.OFFLINE_USER_TABLE_NAME, null, null);
        database.close();
    }

}
