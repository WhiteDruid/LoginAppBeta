package com.example.bblac.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Bblac on 6/23/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // The database name and version
    private static final String DB_NAME = "login";
    private static final int DB_VERSION = 1;
    // The database user table
    private static final String DB_TABLE = "create table user (id integer primary key autoincrement, "+ "username text not null, password text not null);";

    //@param context
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading databse from version" + oldVersion + "to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS user");
        onCreate(database);
    }
}