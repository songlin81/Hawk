package com.chubbymobile.wwh.hawk.utils;

import android.app.Activity;
import android.graphics.Point;

public class ScreenManager {

    public static int getScreenWidth(Activity act) {
        Point size = new Point();
        act.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }
}
