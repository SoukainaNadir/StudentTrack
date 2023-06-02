package com.example.studenttrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "absent.db";
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_USER_TYPE = "user_type";

    public static final String COLUMN_COURSE = "course";
    public static final String COLUMN_APOGEE = "apogee";
    public static final String COLUMN_FIELD = "field";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_USER_TYPE + " TEXT, " +
                COLUMN_COURSE + " TEXT, " +
                COLUMN_APOGEE + " TEXT, " +
                COLUMN_FIELD + " TEXT)";
        db.execSQL(createTableQuery);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public Boolean insertData(String username, String password, String userType, String course, String apogee, String field, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_USER_TYPE, userType);
        values.put(COLUMN_COURSE, course);
        values.put(COLUMN_APOGEE, apogee);
        values.put(COLUMN_FIELD, field);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }


    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USERNAME},
                COLUMN_USERNAME + " = ?", new String[]{username},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USERNAME},
                COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{username, password},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public String getUserType(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_TYPE},
                COLUMN_USERNAME + " = ?", new String[]{username},
                null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_USER_TYPE);
            if (columnIndex != -1) {
                String userType = cursor.getString(columnIndex);
                cursor.close();
                return userType;
            }
        }
        cursor.close();
        return null;
    }


}

