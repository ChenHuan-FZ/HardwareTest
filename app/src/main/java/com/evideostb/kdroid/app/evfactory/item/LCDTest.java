package com.evideostb.kdroid.app.evfactory.item;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evideostb.kdroid.app.evfactory.R;

public class LCDTest extends BaseTest {

    private int mColorIndex = 0;

    private static final int[] COLORS ={
            Color.RED,
            Color.GREEN,
            Color.WHITE,
            Color.BLACK,
            Color.BLUE
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lcd, container, false);
    }

    @Override
    public boolean onSingleTapConfirmed() {
        View text = getView().findViewById(R.id.lcd_text);
        View color = getView().findViewById(R.id.lcd_color);

        int index = (mColorIndex++)%(COLORS.length+1);

        if (index == 0) {
            setButtonVisibility(View.GONE);
            text.setVisibility(View.GONE);
            color.setVisibility(View.VISIBLE);
            color.setBackgroundColor(COLORS[index]);
        } else if (index == COLORS.length){
            setButtonVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);
            color.setVisibility(View.GONE);
        } else {
            color.setBackgroundColor(COLORS[index]);
        }

        return true;
    }

    @Override
    public boolean isNeedTest() {
        return true;
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.lcd_title);
    }
}
