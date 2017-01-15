package com.constructefile.democonstract.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Optimus Prime on 12/18/2016.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login table name
    private static final String TABLE_LABELS = "labels";

    // Login Table Columns names
    private static final String KEY_ID = "uid";
    private static final String KEY_FNAME = "first_name";
    private static final String KEY_LNAME = "last_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "id";
    private static final String KEY_LOGIN_AT_DATE = "last_login_date";
    private static final String KEY_LOGIN_AT_TIME = "last_login_time";
    private static final String KEY_SHIFT_ID = "shift_id";

    // Labels Table Columns names
    private static final String KEY_LABEL_ID = "label_id";
    private static final String KEY_LABEL_VEHICLE_TYPE = "label_vehicle_type";
    private static final String KEY_LABEL_VEHICLE_ID = "label_vehicle_id";
    private static final String KEY_LABEL_SERIAL = "label_serial";


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FNAME + " TEXT," + KEY_LNAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + KEY_LOGIN_AT_DATE + " TEXT," + KEY_LOGIN_AT_TIME + " TEXT,"
                + KEY_SHIFT_ID + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);


        // Category table create query
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_LABELS + "("
                + KEY_LABEL_ID + " INTEGER PRIMARY KEY,"
                + KEY_LABEL_VEHICLE_TYPE + " TEXT," + KEY_LABEL_VEHICLE_ID + " TEXT,"
                + KEY_LABEL_SERIAL + " TEXT)";
        db.execSQL(CREATE_CATEGORIES_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Inserting new lable into lables table
     */
    public void insertLabel(String type, String vid, String label) {
        //deleteLabels();


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valuesVehicle = new ContentValues();
        valuesVehicle.put(KEY_LABEL_VEHICLE_TYPE, type);
        valuesVehicle.put(KEY_LABEL_VEHICLE_ID, vid);
        valuesVehicle.put(KEY_LABEL_SERIAL, label);

        // Inserting Row
        long uid = db.insert(TABLE_LABELS, null, valuesVehicle);
        String strLong = Long.toString(uid);

        if (strLong != null) {
            Log.i("inserted", "successfully " + type + vid + label);
        } else {
            Log.i("inserted", "error");
        }

        db.close(); // Closing database connection
    }

    /**
     * Getting all labels
     * returns list of labels
     */
    public List<String> getAllLabels(String id) {

        //Log.i("labels", "getAllLabels method called with " + id);
        List<String> labels = new ArrayList<String>();

        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_LABELS;


        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery(selectQuery, null);

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LABELS + " where " + KEY_LABEL_VEHICLE_TYPE + " = '" + id + "'", null);
        /*Cursor cursor = db.query(TABLE_LABELS,
                new String[]{KEY_LABEL_VEHICLE_TYPE, KEY_LABEL_VEHICLE_ID, KEY_LABEL_SERIAL}, KEY_LABEL_VEHICLE_TYPE + "=?",
                new String[]{id}, null, null, null, null);*/

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(3));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        Log.i("labels", "load spinner data " + labels.toString());

        // returning lables
        return labels;
    }

    /**
     * Getting all labels
     * returns list of labels
     */
    public List<String> getAllLabelIds(String id) {
        List<String> ids = new ArrayList<String>();

        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_LABELS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LABELS + " where " + KEY_LABEL_VEHICLE_TYPE + " = '" + id + "'", null);
       /* Cursor cursor = db.query(TABLE_LABELS,
                new String[]{KEY_LABEL_VEHICLE_TYPE, KEY_LABEL_VEHICLE_ID, KEY_LABEL_SERIAL}, KEY_LABEL_VEHICLE_TYPE + "=?",
                new String[]{id}, null, null, null, null);*/

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return ids;
    }


    /**
     * Storing user details in database
     */
    public void addUser(String f_name, String l_name, String email, String id, String login_at_date, String login_at_time, String shift_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FNAME, f_name); // First Name
        values.put(KEY_LNAME, l_name); // Last Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, id); // User ID from Web
        values.put(KEY_LOGIN_AT_DATE, login_at_date); // Created At
        values.put(KEY_LOGIN_AT_TIME, login_at_time); // Created At
        values.put(KEY_SHIFT_ID, shift_id); // shift_id

        // Inserting Row
        long uid = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.i(TAG, "New user inserted into sqlite: " + uid);
    }

    /**
     * Getting user data from database
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("f_name", cursor.getString(1));
            user.put("l_name", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("id", cursor.getString(4));
            user.put("login_at_date", cursor.getString(5));
            user.put("login_at_time", cursor.getString(6));
            user.put("shift_id", cursor.getString(7));
        }
        cursor.close();
        db.close();
        // return user
        Log.i("user", "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.i(TAG, "Deleted all user info from sqlite");
    }


    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteLabels() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.execSQL("delete from " + TABLE_LABELS);
        db.close();

        Log.i(TAG, "Deleted all labels info from sqlite");
    }

}
