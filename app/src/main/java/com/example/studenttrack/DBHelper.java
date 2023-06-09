package com.example.studenttrack;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.Sampler;

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

    //class table

    private static final String CLASS_TABLE_NAME = "CLASS_TABLE";
    public static final String C_ID = "_CID";
    public static final String CLASS_NAME_KEY = "CLASS_NAME";
    public static final String SUBJECT_NAME_KEY = "SUBJECT_NAME";

    private static final String CREATE_CLASS_TABLE =
            "CREATE TABLE " + CLASS_TABLE_NAME + "( " +
                    C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    CLASS_NAME_KEY + " TEXT NOT NULL, " +
                    SUBJECT_NAME_KEY + " TEXT NOT NULL, " +
                    "UNIQUE (" + CLASS_NAME_KEY + "," + SUBJECT_NAME_KEY + ")" +
                    ");";

    private static final String DROP_CLASS_TABLE = "DROP TABLE IF EXISTS " + CLASS_TABLE_NAME;
    private static final String SELECT_CLASS_TABLE = "SELECT * FROM " + CLASS_TABLE_NAME;


    //student table

    private static final String STUDENT_TABLE_NAME = "STUDENT_TABLE";
    public static final String S_ID = "S_ID";
    public static final String STUDENT_NAME_KEY = "STUDENT_NAME";
    public static final String STUDENT_ROLL_KEY = "ROLL";

    private static final String CREATE_STUDENT_TABLE =
            "CREATE TABLE "+ STUDENT_TABLE_NAME +
                    "("+
                    S_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
                    C_ID + " INTEGER NOT NULL, "+
                    STUDENT_NAME_KEY + " TEXT NOT NULL, "+
                    STUDENT_ROLL_KEY + " INEGER, "+
                    " FOREIGN KEY ( "+C_ID+") REFERENCES "+ CLASS_TABLE_NAME + "("+C_ID+")"+
                    ");";

    private static final String DROP_STUDENT_TABLE = "DROP TABLE IF EXISTS "+STUDENT_TABLE_NAME;
    private static final String SELECT_STUDENT_TABLE = "SELECT * FROM "+STUDENT_TABLE_NAME;


    //STATUS TABLE

    private static final String STATUS_TABLE_NAME = "STATUS_TABLE";
    public static final String STATUS_ID = "STATUS_ID";
    public static final String DATE_KEY = "STATUS_DATE";
    public static final String STATUS_KEY = "STATUS";

    private static final String CREATE_STATUS_TABLE =
        "CREATE TABLE "+ STATUS_TABLE_NAME +
                "("+
                STATUS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
                S_ID + " INTEGER NOT NULL, "+
                C_ID + " INTEGER NOT NULL, "+
                DATE_KEY + " DATE NOT NULL, "+
                STATUS_KEY + " TEXT NOT NULL, "+
                "UNIQUE ("+ S_ID + "," +DATE_KEY+"),"+
                " FOREIGN KEY ( "+C_ID+") REFERENCES "+ CLASS_TABLE_NAME + "("+C_ID+")"+
                ");";

    private static final String DROP_STATUS_TABLE = "DROP TABLE IF EXISTS "+STATUS_TABLE_NAME;
    private static final String SELECT_STATUS_TABLE = "SELECT * FROM "+STATUS_TABLE_NAME;
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
        db.execSQL(CREATE_CLASS_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_STATUS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
        try {
            db.execSQL(DROP_CLASS_TABLE);
            db.execSQL(DROP_STUDENT_TABLE);
            db.execSQL(DROP_STATUS_TABLE);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    long addClass (String className , String subjectName){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASS_NAME_KEY, className);
        values.put(SUBJECT_NAME_KEY, subjectName);

        return database.insert(CLASS_TABLE_NAME, null, values );
    }

    Cursor getClassTable(){
        SQLiteDatabase database = this.getReadableDatabase();

        return database.rawQuery(SELECT_CLASS_TABLE,null);
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

    int deleteClass(long cid){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.delete(CLASS_TABLE_NAME,C_ID+"=?",new String[]{String.valueOf(cid)});
    }

    long updateClass (long cid, String className, String subjectName){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASS_NAME_KEY, className);
        values.put(SUBJECT_NAME_KEY, subjectName);

        return database.update(CLASS_TABLE_NAME, values,C_ID+"=?",new String[]{String.valueOf(cid)});
    }

    long addStudent(long cid, int roll, String name){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_ID,cid);
        values.put(STUDENT_ROLL_KEY,roll);
        values.put(STUDENT_NAME_KEY,name);
        return database.insert(STUDENT_TABLE_NAME,null,values);
    }

    Cursor getStudentTable(long cid){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(STUDENT_TABLE_NAME,null,C_ID+"=?",new String[]{String.valueOf(cid)},null,null,STUDENT_ROLL_KEY);
    }

    int deleteStudent(long sid){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.delete(STUDENT_TABLE_NAME,S_ID+"=?",new String[]{String.valueOf(sid)});
    }

    long updateStudent(long sid, String name){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STUDENT_NAME_KEY,name);

        return database.update(STUDENT_TABLE_NAME,values,S_ID+"=?",new String[]{String.valueOf(sid)});
    }
    long addStatus(long sid,long cid ,String date,String status){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(S_ID,sid);
        values.put(C_ID,cid);
        values.put(DATE_KEY,date);
        values.put(STATUS_KEY,status);
        return database.insert(STATUS_TABLE_NAME,null,values);
    }

    long updateStatus(long sid,String date,String status){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS_KEY,status);
        String whereClause = DATE_KEY+"='"+date+"' AND "+S_ID+"="+sid;
        return database.update(STATUS_TABLE_NAME, values,whereClause,null);
    }


    String getStatus(long sid, String date){
        String status= null;
        SQLiteDatabase database = this.getReadableDatabase();
        String whereClause = DATE_KEY+"='"+date+"' AND "+S_ID+"="+sid;
        Cursor cursor = database.query(STATUS_TABLE_NAME,null,whereClause,null,null,null,null);
        if (cursor.moveToFirst())
            status = cursor.getString(cursor.getColumnIndexOrThrow(STATUS_KEY));
        return status;
    }
    Cursor getDistinctMonths(long cid){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(STATUS_TABLE_NAME,new String[]{DATE_KEY},C_ID+"="+cid,null,"substr("+DATE_KEY+",4,7)",null,null);
    }


}

