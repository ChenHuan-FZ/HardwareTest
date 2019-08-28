package com.evideostb.kdroid.app.evfactory;

import android.content.Context;

import com.evideostb.kdroid.app.evfactory.item.BaseTest;
import com.evideostb.kdroid.app.evfactory.item.ChargerTest;
import com.evideostb.kdroid.app.evfactory.item.KeyTest;
import com.evideostb.kdroid.app.evfactory.item.LCDTest;
import com.evideostb.kdroid.app.evfactory.item.TouchTest;
import com.evideostb.kdroid.app.evfactory.item.UnKnownTest;
import com.evideostb.kdroid.app.evfactory.item.VersionTest;
import com.evideostb.kdroid.app.evfactory.item.WifiTest;

public class TestList {

    private static BaseTest ALL_TEST[] = {
        new VersionTest(),
        new LCDTest(),
        new TouchTest(),
        new KeyTest(),
        new WifiTest(),
        new ChargerTest(),
    };

    private static BaseTest unKnownTest = new UnKnownTest();
    private static BaseTest mTestItems[];

    static void updateItems (Context context) {
        unKnownTest.setContext(context);

        int size = 0;
        for (BaseTest t: ALL_TEST) {
            t.setContext(context);
            size ++;
        }

        int i = 0;
        mTestItems = new BaseTest[size];
        for (BaseTest t: ALL_TEST) {
            if (t.isNeedTest()) {
                mTestItems[i] = t;
                i++;
            }
        }
    }

    static int getCount () {
        return ALL_TEST.length;
    }

    static BaseTest get (int positon) {
        if (positon>=0 && positon<getCount())
            return mTestItems[positon];
         return unKnownTest;
    }
}
