package com.example.avinash.vlcremote;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by AVINASH on 7/14/2016.
 */
public class tab1 extends Fragment {
    ImageButton play, move, back;
    Button connect,disconnect;
    TextView status;
    int k = 0;
    public static boolean isConnected = false;
    private Socket socket;
    static public PrintWriter out;
    private static final int SERVER_PORT = 8080;
    public InetAddress serverAddr;
    int tapcount=0;
    boolean fing1=false,fing2=false;
    float curx1,curx2,cury1,cury2,ncurx1,ncurx2,ncury1,ncury2;
    int i=0,j=0;
    ConnectPhone connectPhone ;
    tab t=new tab();
    tab2 t2=new tab2();
    Keyboard keyboard=new Keyboard();
    String ip;
    EditText editText;
    @Override
    public View onCreateView(LayoutInflater l, ViewGroup v, Bundle b) {
        View v2 = l.inflate(R.layout.tab1layout, v, false);

        super.onCreate(b);

        return v2;
    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onCreate(b);
        play = (ImageButton) getActivity().findViewById(R.id.imageButton3);
        back = (ImageButton) getActivity().findViewById(R.id.imageButton4);
        move = (ImageButton) getActivity().findViewById(R.id.imageButton2);
        connect=(Button)getActivity().findViewById(R.id.button2);
        status=(TextView)getActivity().findViewById(R.id.textView3);
        editText=(EditText)getActivity().findViewById(R.id.ipaddr);
        RelativeLayout rl=(RelativeLayout)getActivity().findViewById(R.id.rellyt);

        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        curx1=event.getX();
                        cury1=event.getY();
                        tapcount++;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        curx2=event.getX();
                        cury2=event.getY();
                        tapcount++;
                        Log.d("counterfinger",""+tapcount);
                        break;
                    case MotionEvent.ACTION_UP:
                        fing1 = true;
                        if (fing1 == true && fing2 == true) {
                            Log.d("yobroooo","doublefinger tap");
                            fing2=fing1=false;
                            if(isConnected)out.println("leftclick");
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        fing2 = true;
                        if (fing1 == true && fing2 == true) {
                            Log.d("yobroooo","doublefinger tap");
                            fing2=fing1=false;
                            if(isConnected)out.println("leftclick");
                        }break;
                    case MotionEvent.ACTION_MOVE:

                        ncurx1=curx1-event.getX();
                        ncurx2=curx2-event.getX();
                        ncury1=cury1-event.getY();
                        ncury2=cury2-event.getY();
                        Log.d("values", "" + ncurx1 + "  " + ncury1+"  "+ncury1+"  "+ncury2);
                        if(ncury1<0)
                        {i++;
                            if(i%7==0&&isConnected)out.println("up");}
                        else if(ncury1>0){
                            j++;
                            if(j%7==0&&isConnected)
                            out.println("down");}
                        break;
                }
                return true;
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i2=R.mipmap.ic_launcher_pause;
                if (isConnected && out != null) {
                    out.println("play");
                }
                if (k == 0) {
                    InputStream i = (InputStream)getResources().openRawResource(+i2);
                    play.setBackground(Drawable.createFromStream(i, null));
                    k = 1;

                } else if (k == 1) {
                    InputStream i = (InputStream)getResources().openRawResource(+R.mipmap.ic_launcher_play);
                    play.setBackground(Drawable.createFromStream(i,null));
                    k = 0;
                }
                play.setFocusable(true);
            }
        });


        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        back.setImageResource(R.mipmap.ic_launcherback);
                        break;
                    case MotionEvent.ACTION_UP:
                        back.setImageResource(R.mipmap.ic_launcher_back2);
                        if (isConnected && out != null) {
                            out.println("rewind");
                        }
                        break;
                }
                return true;
            }
        });


       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (isConnected && out != null) {
                   out.println("rewind");
               }
           }
       });


        move.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        move.setImageResource(R.mipmap.ic_launcher3);
                        break;
                    case MotionEvent.ACTION_UP:
                        move.setImageResource(R.mipmap.ic_launcher_move);
                        if (isConnected && out != null) {
                            out.println("fastfrwd");
                        }
                        break;
                }
                return true;
            }
        });


        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected && out != null) {
                    out.println("fastfrwd");
                }
            }
        });

       connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("connecting....");
                ip=editText.getText().toString();
                connectPhone = new ConnectPhone();
                 connectPhone.execute();
            }
        });
    }


    public class ConnectPhone extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = true;
            try {
               // serverAddr = InetAddress.getByName("DESKTOP-5BEQKQL");
                //serverAddr="10.1.2.52";
               // Log.d("10.1.72.102",serverAddr.toString());
                ServerSocket s;
                //s.getInetAddress().toString();

                socket = new Socket(ip, SERVER_PORT);
            } catch (IOException e) {
                Log.e("server", e.toString());
                result = false;
            }
            return result;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            isConnected=result;keyboard.flag=result; t.flag=result;
            t2.flag = result;
            //dev.setText(serverAddr.toString());
            Toast.makeText(getContext(), isConnected ? "Connected to server!":"Error while connecting", Toast.LENGTH_LONG).show();
            if(isConnected)status.setText("CONNECTED");
            else status.setText("ERROR WHILE CONNECTING");
            try {
                if (isConnected) {
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                            .getOutputStream())), true);
                }
            } catch (IOException e) {
                Log.e("Server",e.toString());
               // Toast.makeText(getContext(), "Error while connecting", Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }


}
