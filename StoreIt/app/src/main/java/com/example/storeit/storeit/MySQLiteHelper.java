package com.example.storeit.storeit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by srikanth on 5/4/17.
 */

public class MySQLiteHelper extends SQLiteOpenHelper{

    public static final String TABLE_USERS = "users";
    public static final String TABLE_DOMAINS = "domains";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DOMAIN = "domain";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    private static final String DATABASE_NAME ="domains.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_DOMAINS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "+COLUMN_DOMAIN
            + " text not null, "+COLUMN_USERNAME
            + " text not null, "+COLUMN_PASSWORD
            + " text not null);";

    private static final String USER_TABLE_CREATE = "create table "
            + TABLE_USERS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "+COLUMN_USERNAME
            + " text not null, "+COLUMN_PASSWORD
            + " text not null);";

    public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        db.execSQL(USER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOMAINS);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_CREATE);
        onCreate(db);
    }
}
