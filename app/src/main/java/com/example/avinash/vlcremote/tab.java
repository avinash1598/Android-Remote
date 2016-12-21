package com.example.avinash.vlcremote;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by AVINASH on 7/13/2016.
 */
public class tab extends AppCompatActivity {
   public static boolean flag=false;
    int k=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablayout);



        //startService(new Intent(this, proxmitysensor.class));
        TabLayout tl = (TabLayout) findViewById(R.id.tab_layout);
        tl.setTabTextColors(0xFFFEFEFC,0xFFFEFEFC);
        tl.addTab(tl.newTab().setText("MOVIE"));
        tl.addTab(tl.newTab().setText("TOUCH_PAD"));
        RelativeLayout rl=(RelativeLayout)findViewById(R.id.rel);


       final ViewPager v = (ViewPager) findViewById(R.id.pager);
        adapter pa = new adapter(getSupportFragmentManager());
        v.setAdapter(pa);
       //
       // v.beginFakeDrag();



            //v.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl));

            tl.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()

                                        {
                                            @Override
                                            public void onTabSelected(TabLayout.Tab tab) {
                                                v.setCurrentItem(tab.getPosition());
                                            }

                                            @Override
                                            public void onTabUnselected(TabLayout.Tab tab) {

                                            }

                                            @Override
                                            public void onTabReselected(TabLayout.Tab tab) {

                                            }
                                        }

            );
        }

        @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        Button button = (Button) menu.findItem(R.id.type).getActionView();
        Button button2 = (Button) menu.findItem(R.id.disconnect1).getActionView();
            Button button1=(Button)menu.findItem(R.id.service).getActionView();
        //Button button3 = (Button) menu.findItem(R.id.connect).getActionView();

    return true;}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.type) {
            Intent i=new Intent(tab.this,Keyboard.class);
            startActivity(i);
            return true;
        }
        if(id==R.id.service){
            if(k==0) {
                startService(new Intent(this, Accelerometer.class));
                Toast.makeText(tab.this,"ACC. ENABLED",Toast.LENGTH_LONG).show();
                k=1;
            }
            else if(k==1){
                k=0;
                Toast.makeText(tab.this,"ACC. DISABLED",Toast.LENGTH_LONG).show();
                stopService(new Intent(this,Accelerometer.class));
            }
        }
        if(id==R.id.disconnect1){
            tab1 t=new tab1();
            if(flag)
            t.out.println("exit");

            Log.d("exitttttt","ex");
            //t.status.setText("Disconnected");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

