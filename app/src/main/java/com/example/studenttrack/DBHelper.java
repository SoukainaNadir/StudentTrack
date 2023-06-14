package com.example.studenttrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

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

    public static final String CLASS_TABLE_NAME = "CLASS_TABLE";
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
            "CREATE TABLE " + STUDENT_TABLE_NAME + "(" +
                    S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    C_ID + " INTEGER NOT NULL, " +
                    STUDENT_NAME_KEY + " TEXT NOT NULL, " +
                    STUDENT_ROLL_KEY + " INTEGER, " +
                    COLUMN_APOGEE + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + C_ID + ") REFERENCES " + CLASS_TABLE_NAME + "(" + C_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_APOGEE + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_APOGEE + ")" +
                    ");";

    private static final String DROP_STUDENT_TABLE = "DROP TABLE IF EXISTS "+STUDENT_TABLE_NAME;
    private static final String SELECT_STUDENT_TABLE = "SELECT * FROM "+STUDENT_TABLE_NAME;


    //STATUS TABLE

    public static final String STATUS_TABLE_NAME = "STATUS_TABLE";
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
                    "FOREIGN KEY ( "+C_ID+") REFERENCES "+ CLASS_TABLE_NAME + "("+C_ID+")"+
                    ");";

    private static final String DROP_STATUS_TABLE = "DROP TABLE IF EXISTS "+STATUS_TABLE_NAME;
    private static final String SELECT_STATUS_TABLE = "SELECT * FROM "+STATUS_TABLE_NAME;

    //Justification TABLE

    public static final String JUSTIFICATION_TABLE_NAME  ="JUSTIFICATION_TABLE";

    public static final String JUSTIFICATION_ID = "JUSTIFICATION_ID";
    public static final String JUSTIFICATION_TEXT = "JUSTIFICATION_TEXT";
    public static final String PDF_FILE_PATH = "PDF_FILE_PATH";



    private static final String CREATE_JUSTIFICATION_TABLE =
            "CREATE TABLE " + JUSTIFICATION_TABLE_NAME + "(" +
                    JUSTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    S_ID + " INTEGER NOT NULL, " +
                    JUSTIFICATION_TEXT + " TEXT, " +
                    PDF_FILE_PATH + " TEXT, " +
                    "FOREIGN KEY (" + S_ID + ") REFERENCES " + STUDENT_TABLE_NAME + "(" + S_ID + ")" +
                    ");";

    private static final String DROP_JUSTIFICATION_TABLE = "DROP TABLE IF EXISTS "+JUSTIFICATION_TABLE_NAME;
    private static final String SELECT_JUSTIFICATION_TABLE = "SELECT * FROM "+JUSTIFICATION_TABLE_NAME;

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
        db.execSQL(CREATE_JUSTIFICATION_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
        try {
            db.execSQL(DROP_CLASS_TABLE);
            db.execSQL(DROP_STUDENT_TABLE);
            db.execSQL(DROP_STATUS_TABLE);
            db.execSQL(DROP_JUSTIFICATION_TABLE);

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
    Cursor getStatusTable(){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(SELECT_STATUS_TABLE,null);
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

    long addStudent(long cid, int roll, String name, int apogee){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_ID,cid);
        values.put(STUDENT_ROLL_KEY,roll);
        values.put(STUDENT_NAME_KEY,name);
        values.put(COLUMN_APOGEE,apogee);
        return database.insert(STUDENT_TABLE_NAME,null,values);
    }

    Cursor getStudentTable(long cid){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(STUDENT_TABLE_NAME,null,C_ID+"=?",new String[]{String.valueOf(cid)},null,null,STUDENT_ROLL_KEY);
    }

    int deleteStudent(long sid){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.delete(STUDENT_TABLE_NAME, S_ID +"=?",new String[]{String.valueOf(sid)});
    }

    long updateStudent(long sid, String name, int apogee){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STUDENT_NAME_KEY,name);
        values.put(COLUMN_APOGEE,apogee);

        return database.update(STUDENT_TABLE_NAME,values, S_ID +"=?",new String[]{String.valueOf(sid)});
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
        String whereClause = DATE_KEY+"='"+date+"' AND "+ S_ID +"="+sid;
        return database.update(STATUS_TABLE_NAME, values,whereClause,null);
    }


    String getStatus(long sid, String date){
        String status= null;
        SQLiteDatabase database = this.getReadableDatabase();
        String whereClause = DATE_KEY+"='"+date+"' AND "+ S_ID +"="+sid;
        Cursor cursor = database.query(STATUS_TABLE_NAME,null,whereClause,null,null,null,null);
        if (cursor.moveToFirst())
            status = cursor.getString(cursor.getColumnIndexOrThrow(STATUS_KEY));
        return status;
    }
    Cursor getDistinctMonths(long cid){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(STATUS_TABLE_NAME,new String[]{DATE_KEY},C_ID+"="+cid,null,"substr("+DATE_KEY+",4,7)",null,null);
    }



    public int getStudentApogee(long studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int apogee = -1;

        Cursor cursor = db.query(STUDENT_TABLE_NAME, new String[]{COLUMN_APOGEE}, S_ID + "=?",
                new String[]{String.valueOf(studentId)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            apogee = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_APOGEE));
            cursor.close();
        }
        return apogee;
    }


    public Cursor countStudentAbsences() {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] columns = {S_ID, "COUNT(" + STATUS_KEY + ") AS AbsenceCount"};
        String selection = STATUS_KEY + " = ?";
        String[] selectionArgs = {"A"};
        String groupBy = S_ID;
        Cursor cursor = database.query(STATUS_TABLE_NAME, columns, selection, selectionArgs, groupBy, null, null);
        return cursor;
    }

    public String getApogee(String username) {
        String apogee = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_APOGEE + " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        if (cursor.moveToFirst()) {
            apogee = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APOGEE));
        }
        cursor.close();
        return apogee;
    }

    public String getApogeeForUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String apogee = "";

        String[] projection = {COLUMN_APOGEE};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(TABLE_USERS, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            apogee = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APOGEE));
        }

        cursor.close();
        return apogee;
    }

    public ArrayList<AbsenceDetails> getAbsencesByUserApogee(String userApogee) {
        ArrayList<AbsenceDetails> absences = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + STATUS_TABLE_NAME + "." + STATUS_KEY + ", " + STATUS_TABLE_NAME + "." + DATE_KEY + ", " + CLASS_TABLE_NAME + "." + SUBJECT_NAME_KEY +
                " FROM " + STATUS_TABLE_NAME +
                " INNER JOIN " + STUDENT_TABLE_NAME +
                " ON " + STATUS_TABLE_NAME + "." + S_ID + " = " + STUDENT_TABLE_NAME + "." + S_ID +
                " INNER JOIN " + CLASS_TABLE_NAME +
                " ON " + STUDENT_TABLE_NAME + "." + C_ID + " = " + CLASS_TABLE_NAME + "." + C_ID +
                " WHERE " + STUDENT_TABLE_NAME + "." + COLUMN_APOGEE + " = '" + userApogee + "'" +
                " AND " + STATUS_TABLE_NAME + "." + STATUS_KEY + " = 'A'";
        
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String absenceStatus = cursor.getString(cursor.getColumnIndexOrThrow(STATUS_KEY));
                String subjectName = cursor.getString(cursor.getColumnIndexOrThrow(SUBJECT_NAME_KEY));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE_KEY));

                AbsenceDetails absenceDetails = new AbsenceDetails(0, date, absenceStatus, subjectName);
                absences.add(absenceDetails);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return absences;
    }















}

