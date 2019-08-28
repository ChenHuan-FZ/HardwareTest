package com.evideostb.kdroid.app.evfactory;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.evideostb.kdroid.app.evfactory.constant.Constant;

public class TestItemActivity extends Activity {

    private GestureDetector mDetector;
    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        setContentView(R.layout.test_item);

        mDetector = new GestureDetector(this, new onGestureListener());

        mIndex = getIntent().getIntExtra("ITEM_INDEX", 0);
        setTitle(TestList.get(mIndex).getTestName());
        addFragment(TestList.get(mIndex));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return TestList.get(mIndex).onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        TestList.get(mIndex).onNewIntent();
    }

    private void addFragment(final Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.item_fragment, fragment);
        transaction.commit();
    }

    private void replaceFragment(final Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.item_fragment, fragment);
        transaction.commit();
    }

    private void setTestResult(int result) {
        SharedPreferences sp = getSharedPreferences(Constant.TEST_ITEM_RESULT, Context.MODE_PRIVATE);
        sp.edit().putInt(String.valueOf(mIndex), result).apply();
    }

    private boolean isSingleMode() {
        SharedPreferences sp = getSharedPreferences(Constant.TEST_SINGLE_MODE, MODE_PRIVATE);
        return sp.getBoolean(Constant.TEST_SINGLE_MODE, true);
    }

    private void onNextTest() {
        mIndex = mIndex + 1;

        if (mIndex >= TestList.getCount() || isSingleMode()) {
            finish();
        } else {
            setTitle(TestList.get(mIndex).getTestName());
            replaceFragment(TestList.get(mIndex));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_pass) {
            setTestResult(1);
            onNextTest();
        } else {
            setTestResult(0);
            onNextTest();
        }
    }

    class onGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return TestList.get(mIndex).onSingleTapUp();
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return TestList.get(mIndex).onSingleTapConfirmed();
        }
    }
}
