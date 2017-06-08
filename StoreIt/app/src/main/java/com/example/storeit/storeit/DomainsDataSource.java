package com.example.storeit.storeit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DomainsDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_DOMAIN,
            MySQLiteHelper.COLUMN_USERNAME,
            MySQLiteHelper.COLUMN_PASSWORD
    };
    private String[] allColumnsUser = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_USERNAME,
            MySQLiteHelper.COLUMN_PASSWORD
    };
    public DomainsDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Domain createDomain(String domain, String username, String password){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DOMAIN, domain);
        values.put(MySQLiteHelper.COLUMN_USERNAME, username);
        values.put(MySQLiteHelper.COLUMN_PASSWORD, password);

        long insertId = database.insert(MySQLiteHelper.TABLE_DOMAINS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DOMAINS, allColumns, MySQLiteHelper.COLUMN_ID+" = "+insertId, null, null, null, null);
        cursor.moveToFirst();

        Domain newDomain = cursorToDomain(cursor);
        cursor.close();

        return newDomain;
    }

    public int addUser(String userName, String passWord){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_USERNAME, userName);
        values.put(MySQLiteHelper.COLUMN_PASSWORD, passWord);

        long insertId = database.insert(MySQLiteHelper.TABLE_USERS, null, values);

        if(insertId >=0 ){
            return 1;
        }
        else{
            return -1;
        }
    }
    public List<Domain> getAllDomains(){
        List<Domain> domains = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DOMAINS, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Domain domain = cursorToDomain(cursor);
            domains.add(domain);
            cursor.moveToNext();
        }
        cursor.close();
        return domains;
    }

    public boolean isUserFirstTime(){
        Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, allColumnsUser, null, null, null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean isUserExist(String username, String password){

       String Query = "select * from " + MySQLiteHelper.TABLE_USERS + " where " + MySQLiteHelper.COLUMN_USERNAME + " = '" + username +
                "' and " + MySQLiteHelper.COLUMN_PASSWORD + " = '" + password + "'";
        //System.out.println("\n\n\n"+MySQLiteHelper.COLUMN_USERNAME+"\n\n\n");
        Cursor cursor = database.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private Domain cursorToDomain(Cursor cursor){
        Domain domain = new Domain();
        domain.setDomain(cursor.getString(1));
        domain.setUserName(cursor.getString(2));
        domain.setPassword(cursor.getString(3));

        return domain;
    }
}