package com.evideostb.kdroid.app.evfactory.item;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evideostb.kdroid.app.evfactory.R;

public class UnKnownTest extends BaseTest{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.unknown, container);
    }

    @Override
    public boolean isNeedTest() {
        return true;
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.unknown_title);
    }
}
