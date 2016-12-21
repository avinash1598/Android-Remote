package com.example.avinash.vlcremote;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by AVINASH on 7/21/2016.
 */
public class viewpager2 extends ViewPager {

    private boolean enabled;
    public viewpager2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled=true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
