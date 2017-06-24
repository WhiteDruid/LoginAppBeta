package com.example.bblac.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Bblac on 6/23/2017.
 */

public class DatabaseAdapter {
    // table name
    private static final String LOGIN_TABLE = "user";
    // table unique id
     static final String COL_ID = "id";
    // user and password
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";

    private Context context;
    private SQLiteDatabase dbHelper;
    private DatabaseHelper dh;

    public DatabaseAdapter(Context context) {
        this.context = context;
    }

    //Creates the database helper and gets the database
    public DatabaseAdapter open() throws SQLException {
        dh = new DatabaseHelper(context);
        dbHelper = dh.getWritableDatabase();
        return this;
    }

    public void close(){
        dh.close();
    }

    // create username and password
    public long createUser(String username, String password){
        ContentValues cv = createUserTableContentValues(username , password);
        return dbHelper.insert(LOGIN_TABLE , null , cv);
    }

    public boolean updateUserTable(long rowId, String username, String password) {
        ContentValues cv = createUserTableContentValues(username , password);
        return dbHelper.update(LOGIN_TABLE , cv , COL_ID + "=" + rowId , null) > 0;
    }

    public Cursor fetchAllUser() {
        return dbHelper.query(LOGIN_TABLE ,new String[] {COL_ID , COL_USERNAME , COL_PASSWORD }
       , null , null , null , null , null);
    }

    public Cursor fetchUser(String username , String password) {
        Cursor cursor = dbHelper.query(LOGIN_TABLE , new String[] {COL_ID , COL_USERNAME , COL_PASSWORD} ,
                COL_USERNAME + "='" + username + "' AND " +
                COL_PASSWORD + "='" + password + "'", null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchUserById(long rowId) throws SQLException {
        Cursor myCursor = dbHelper.query(LOGIN_TABLE,
                new String[] { COL_ID, COL_USERNAME, COL_PASSWORD },
                COL_ID + "=" + rowId, null, null, null, null);
        if (myCursor != null) {
            myCursor.moveToFirst();
        }
        return myCursor;
    }

    public ContentValues createUserTableContentValues(String username , String password){
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME , username);
        cv.put(COL_PASSWORD , password);
        return cv;
    }

}
