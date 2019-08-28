package com.evideostb.kdroid.app.evfactory.item;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evideostb.kdroid.app.evfactory.R;
import com.evideostb.kdroid.app.evfactory.TestList;

public class ChargerTest extends BaseTest {

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                    BatteryManager.BATTERY_STATUS_UNKNOWN);
            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                case BatteryManager.BATTERY_STATUS_FULL:
                    setStepText(R.string.charger_plugged);
                    setTimerTask(0, 1000);
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    setStepText(R.string.charger_plug_in);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.charger, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public boolean isNeedTest() {
        return true;
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.charger_title);
    }

    private void setStepText(int textId) {
        ((TextView) getView().findViewById(R.id.charger_text)).setText(textId);
    }
}
