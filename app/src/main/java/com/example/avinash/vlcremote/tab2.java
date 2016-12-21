package com.example.avinash.vlcremote;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by AVINASH on 7/14/2016.
 */
public class tab2 extends Fragment {
    TextView textView;
    float curx, cury, delx = 0, dely = 0;
    tab1 t;
    public GestureDetectorCompat gdc;
    RelativeLayout r;
    private ScaleGestureDetector SGD;
    public  static  boolean flag=true;

    @Override
    public View onCreateView(LayoutInflater l, ViewGroup v, Bundle b) {
        View v2 = l.inflate(R.layout.tab2layout, v, false);
        super.onCreate(b);
        t = new tab1();
        SGD = new ScaleGestureDetector(getActivity(),new ScaleListener());
        gdc = new GestureDetectorCompat(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {


                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        Log.d("double tap", "yooooooooo");
                        if(flag)
                        t.out.println("ok");
                        return true;
                    }


                    @Override
                    public boolean onDown(MotionEvent e) {
                        Log.d("down", "down bro");
                        int count =e.getPointerCount();
                        Log.d("countttttttttt",""+count);
                        return true;
                    }


                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        Log.d("single tap1", "tap1 bro");
                        if(flag)
                        t.out.println("rightclick");
                        return true;
                    }

                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        //Log.d("scroll", "scroll bro");
                        if(flag)
                        t.out.println((-distanceX*1.5)+","+(-distanceY*1.5));
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                      t.out.println("leftclick");
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        return false;
                    }
                });


        v2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SGD.onTouchEvent(event);
                return gdc.onTouchEvent(event);
            }
        });
        return v2;
    }
    private class ScaleListener extends ScaleGestureDetector.

            SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
              Log.d("Scale me","AAYAAAAAAAAA");
            return true;
        }
    }


    @Override
    public void onActivityCreated(Bundle b) {
        super.onCreate(b);
        r=(RelativeLayout)getActivity().findViewById(R.id.idr);
        t = new tab1();
    }
}
