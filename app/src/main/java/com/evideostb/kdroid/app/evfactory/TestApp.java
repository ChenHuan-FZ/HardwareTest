package com.evideostb.kdroid.app.evfactory;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by ChenHuan on 2019/8/3/0015.
 */
public class TestApp extends Application{
    private static Context mContext;
    private static Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        handler = new Handler(getMainLooper());
        TestList.updateItems(getApplicationContext());
    }

    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return handler;
    }
}
