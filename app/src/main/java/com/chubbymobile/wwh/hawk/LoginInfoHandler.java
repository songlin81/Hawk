package com.chubbymobile.wwh.hawk;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LoginInfoHandler {

    private static LoginInfoHandler mHandler;
    private Context context;
    private static final String TBL_LOGIN_INFO = "LoginInfo";

    private LoginInfoHandler(Context context) {
        this.context = context;
    }

    /**
     * singleton
     *
     * @return
     */
    public static LoginInfoHandler create(Context context) {
        if (mHandler == null) {
            mHandler = new LoginInfoHandler(context);
        }
        return mHandler;
    }

    public List<DBLoginInfoObj> getLoginInfoObj() {
        List<DBLoginInfoObj> infoObjs = new ArrayList<DBLoginInfoObj>();
        DBHelper dbHelper = new DBHelper(context);
        Cursor c = null;
        try {
            c = dbHelper.quertAll(TBL_LOGIN_INFO);
            for (c.moveToLast(); !c.isBeforeFirst(); c.moveToPrevious()) {
                DBLoginInfoObj infoObj = new DBLoginInfoObj();
                infoObj.setID(c.getInt(c.getColumnIndex("_id")));
                infoObj.setUserName(c.getString(c.getColumnIndex("UserName")));
                infoObj.setPassword(c.getString(c.getColumnIndex("PassWord")));
                infoObjs.add(infoObj);

            }
        } catch (Exception e) {
            Log.i("db exception：", e.toString());
        } finally {
            c.close();
        }
        return infoObjs;
    }
}