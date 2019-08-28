package com.evideostb.kdroid.app.evfactory.item;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.evideostb.kdroid.app.evfactory.R;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class BaseTest extends Fragment implements Runnable {

    private ScheduledThreadPoolExecutor mTimerTask;
    protected int mIndex = -1;
    protected Context mContext;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            onHandleMessage(msg);
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cancelTimerTask();
    }

    @Override
    public void run() {
        Message message = new Message();
        message.arg1 = mIndex;
        mHandler.sendMessage(message);
    }

    public void setTimerTask(final int index, int delay) {
        cancelTimerTask();

        this.mIndex = index;
        mTimerTask = new ScheduledThreadPoolExecutor(10);
        mTimerTask.schedule(this, delay, TimeUnit.MILLISECONDS);
    }

    public void setTimerTask(final int index, int delay, int period) {
        cancelTimerTask();

        this.mIndex = index;
        mTimerTask = new ScheduledThreadPoolExecutor(10);
        mTimerTask.scheduleAtFixedRate(this, delay, period, TimeUnit.MILLISECONDS);
    }

    public void cancelTimerTask() {
        if (mTimerTask != null) {
            mTimerTask.remove(this);
            mTimerTask.shutdownNow();
            mTimerTask = null;
        }
    }

    public void onNewIntent() {}

    public void setContext(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public abstract boolean isNeedTest ();

    public abstract String getTestName();

    public boolean onSingleTapUp(){
        return true;
    }

    public boolean onSingleTapConfirmed() {
        return true;
    }

    protected void onHandleMessage(Message msg) {
    }

    protected void clickPassButton() {
        ((Button)(getActivity().findViewById(R.id.btn_pass))).performClick();
    }

    protected void clickFailButton() {
        ((Button)(getActivity().findViewById(R.id.btn_fail))).performClick();
    }

    protected void setButtonVisibility(int visibility) {
        getActivity().findViewById(R.id.buttons).setVisibility(visibility);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    protected void setButtonEnabled(boolean enabled) {
        getActivity().findViewById(R.id.buttons).setEnabled(enabled);
    }
}
