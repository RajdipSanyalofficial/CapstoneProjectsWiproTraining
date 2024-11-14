package com.example.fitnesstrackerapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fitness_tracker600.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_ACTIVITY = "activities";
    private static final String TABLE_USER_PROFILE = "user_profile";

    // Activity columns
    private static final String COLUMN_ACTIVITY_ID = "id";
    private static final String COLUMN_ACTIVITY_TYPE = "activity_type";
    private static final String COLUMN_STEPS = "steps";
    private static final String COLUMN_DISTANCE = "distance";
    private static final String COLUMN_CALORIES = "calories";
    private static final String COLUMN_DATE = "date";

    // User Profile columns
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_HEIGHT = "height";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACTIVITY_TABLE = "CREATE TABLE " + TABLE_ACTIVITY + " (" +
                COLUMN_ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ACTIVITY_TYPE + " TEXT, " +
                COLUMN_STEPS + " INTEGER, " +
                COLUMN_DISTANCE + " REAL, " +
                COLUMN_CALORIES + " INTEGER, " +
                COLUMN_DATE + " TEXT)";
        db.execSQL(CREATE_ACTIVITY_TABLE);

        String CREATE_USER_PROFILE_TABLE = "CREATE TABLE " + TABLE_USER_PROFILE + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_AGE + " INTEGER, " +
                COLUMN_WEIGHT + " REAL, " +
                COLUMN_HEIGHT + " REAL)";
        db.execSQL(CREATE_USER_PROFILE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PROFILE);
        onCreate(db);
    }



    // Insert user profile
    public long insertUserProfile(UserProfile userProfile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, userProfile.name);
        values.put(COLUMN_AGE, userProfile.age);
        values.put(COLUMN_WEIGHT, userProfile.weight);
        values.put(COLUMN_HEIGHT, userProfile.height);

        long result=db.insert(TABLE_USER_PROFILE, null, values);
        db.close();
        return result;
    }

    // Retrieve user profile
    @SuppressLint("Range")
    public UserProfile getUserProfile() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER_PROFILE, null, null, null, null, null, null);
        UserProfile userProfile = null;
        if (cursor != null && cursor.moveToFirst()) {
            userProfile = new UserProfile();
            userProfile.id = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
            userProfile.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            userProfile.age = cursor.getInt(cursor.getColumnIndex(COLUMN_AGE));
            userProfile.weight = cursor.getFloat(cursor.getColumnIndex(COLUMN_WEIGHT));
            userProfile.height = cursor.getFloat(cursor.getColumnIndex(COLUMN_HEIGHT));
            cursor.close();
            return userProfile;
        }

        if(cursor != null)
        {
            cursor.close();

        }
        return null;
    }

    public boolean deleteUserProfile() {
        SQLiteDatabase db = this.getReadableDatabase();
        int rowsAffected= db.delete("user_profile",null,null);
        db.close();
        return rowsAffected >0;
    }
}