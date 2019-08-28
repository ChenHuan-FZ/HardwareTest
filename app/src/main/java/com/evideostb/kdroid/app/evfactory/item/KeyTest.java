package com.evideostb.kdroid.app.evfactory.item;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evideostb.kdroid.app.evfactory.R;

public class KeyTest extends BaseTest {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.key, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(View.VISIBLE);
        registerReceiver();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterReceiver();
    }

    public void onTouchKeyEventListener(int keyCode) {
        TextView tv = (TextView) getActivity().findViewById(R.id.key_text);
        tv.setText(KeyEvent.keyCodeToString(keyCode));
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.key_title);
    }

    protected void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.evideostb.kdroid.evlauncher.action.TOUCHKEY_EVENT");
        getContext().registerReceiver(mTkReciver, filter);
    }

    protected void unregisterReceiver() {
        getContext().unregisterReceiver(mTkReciver);
    }

    @Override
    public boolean isNeedTest() {
        return true;
    }

    private BroadcastReceiver mTkReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int keyCode = intent.getIntExtra("keycode", -1);
            Log.i("KeyTest", String.valueOf(keyCode));
            onTouchKeyEventListener(keyCode);
        }
    };


}
