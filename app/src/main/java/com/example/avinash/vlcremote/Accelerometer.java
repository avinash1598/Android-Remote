package com.example.avinash.vlcremote;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by AVINASH on 7/22/2016.
 */
public class Accelerometer extends Service implements SensorEventListener {
    SensorManager sm;
    int i=0;
    @Override
    public void onCreate(){
        Log.d("started","yoooo");

    }
@Override
    public void onDestroy(){
    sm.unregisterListener( this);
   }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x=event.values[0];
        float y=event.values[1];
        float z=event.values[2];
        if(i%10==0){
            Log.d("valuesAcc",""+x+" "+y+" "+z);
        }
        i++;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flag,int start){
        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor s=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this,s,SensorManager.SENSOR_DELAY_NORMAL);
       return Service.START_STICKY;
    }
}
