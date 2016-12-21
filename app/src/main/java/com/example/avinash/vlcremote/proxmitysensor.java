package com.example.avinash.vlcremote;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;

/**
 * Created by AVINASH on 7/20/2016.
 */
public class proxmitysensor extends Service implements SensorEventListener {
    Sensor proxy;
    SensorManager sm;
    tab1 t;
    int temp=0,k=0;
    @Override
    public void onCreate(){
        //Toast.makeText(getApplicationContext(), "serviceStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
       // sm.unregisterListener( this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        Log.d("aaayaaa","sensor me");
        temp++;
        if(temp%2==0){
            t.out.println("play");
            /*if (k == 0) {
                InputStream i = (InputStream)getResources().openRawResource(+R.mipmap.ic_launcher_pause);
                t.play.setBackground(Drawable.createFromStream(i, null));
                k = 1;

            } else if (k == 1) {
                InputStream i = (InputStream)getResources().openRawResource(+R.mipmap.ic_launcher_play);
                t.play.setBackground(Drawable.createFromStream(i,null));
                k = 0;
            }*/
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Log.d("aaayaaa","sensor me");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //Toast.makeText(this,"CommandStarted",Toast.LENGTH_SHORT).show();
        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        proxy=sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sm.registerListener(this, proxy , SensorManager.SENSOR_DELAY_NORMAL);
        t=new tab1();
        return Service.START_STICKY;
    }
}
