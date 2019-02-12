package com.chubbymobile.wwh.hawk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "HawkApp.db";

    private static final String CREATE_TBL_LOGININFO = "create table LoginInfo(_id integer primary key autoincrement,UserName text,PassWord text)";

    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        this.db = arg0;
        db.execSQL(CREATE_TBL_LOGININFO);
    }

    /**
     * 向指定数据库中插入一条数据
     *
     * @param values    ContentValues 键值对， 相当于map
     * @param tableName
     */
    public void insert(ContentValues values, String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(tableName, null, values);
        db.close();
    }

    /**
     * 返回表中所有数据
     *
     * @param tableName
     * @return
     */
    public Cursor quertAll(String tableName) {
        Cursor c = null;
        SQLiteDatabase db = getWritableDatabase();
        c = db.query(tableName, null, null, null, null, null, null);
        return c;
    }

    /**
     * 根据ID删除一条数据
     *
     * @param id
     * @param tableName
     */
    public void del(int id, String tableName) {

        if (db == null)
            db = getWritableDatabase();
        db.delete(tableName, "_id=?", new String[]{String.valueOf(id)});

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}