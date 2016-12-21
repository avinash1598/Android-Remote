package com.example.avinash.vlcremote;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by AVINASH on 7/14/2016.
 */
public class adapter extends FragmentStatePagerAdapter {

    public adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:tab1 t1=new tab1();
                return t1;
            case 1:tab2 t2=new tab2();
                return t2;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
