package com.example.avinash.vlcremote;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        android.view.GestureDetector.OnDoubleTapListener {

    public EditText dev, ip;
    public Button back, play, pause, move, connect;
    public String ipad, dnam;
    private boolean isConnected = false;
    private Socket socket;
    private PrintWriter out;
    private static final int SERVER_PORT = 8080;
    public InetAddress serverAddr;
    ServerSocket server;
    public TextView tv;
     String data;

    private BluetoothAdapter myBluetooth = null;
    public Set<BluetoothDevice> pairedDevices;
    public ListView devlv;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false, ConnectSuccess = true;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public TextView t;

    String addr;

    private ConnectedThread mConnectedThread;
    public Handler bluetoothIn;
    final int handlerState = 0;
    private StringBuilder recDataString = new StringBuilder();

    public GestureDetectorCompat gdc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dev = (EditText) findViewById(R.id.editText);
        back = (Button) findViewById(R.id.back);
        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);
        move = (Button) findViewById(R.id.move);
        connect = (Button) findViewById(R.id.connect);
        tv=(TextView)findViewById(R.id.textView);
        //********************************************************************************************************************

        devlv = (ListView) findViewById(R.id.listView);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        gdc=new GestureDetectorCompat(this,this);
        gdc.setOnDoubleTapListener(this);


        if (!myBluetooth.isEnabled()) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }

        //*****************************************************************************************************************
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
               if(msg.what==handlerState) {
                   Toast.makeText(getApplicationContext(),"READing",Toast.LENGTH_SHORT).show();
                        String data = (String) msg.obj;
                       dev.setText(data);
                 //  recDataString.append(data);
                   //int endOfLineIndex = recDataString.indexOf(" ");                    // determine the end-of-line
                   //if (endOfLineIndex > 0) {                                           // make sure there data before ~
                     //  String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                       //tv.setText("Data Received = " + dataInPrint);

                   switch(data){
                       case "f" : if (isConnected && out != null) {
                           out.println("fastfrwd");break;}
                       case "r" : if (isConnected && out != null) {
                               out.println("rewind");break;}
                       case "p": if (isConnected && out != null) {
                           out.println("play");break;}
                       default: if (isConnected && out != null) {
                           out.println("invalid");}

                   }
               }
            }
        }
        ;

        //********************************************************************************************************************
    }


    public void connect(View v1) {
        //ipad = ip.getText().toString();
        ConnectPhoneTask connectPhoneTask = new ConnectPhoneTask();
        connectPhoneTask.execute(ipad);

    }

    public void back(View v2) {
        if (isConnected && out != null) {
            out.println("rewind");
        }
    }

    //*************************************************************************************************
    public void btconnect(View view) {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.paireddevices, list);
        devlv.setAdapter(adapter);
    }

    //****************************************************************************************************************
    public void play(View v3) {
        if (isConnected && out != null) {
            out.println("play");
        }
    }

    public void pause(View v4) {
        if (isConnected && out != null) {
            try {
                out.println("exit"); //tell server to exit
                socket.close(); //close socket
            } catch (IOException e) {
                Log.e("remotedroid", "Error in closing socket", e);
            }
        }
    }

    public void move(View v5) {
        if (isConnected && out != null) {
            out.println("fastfrwd");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isConnected && out != null) {
            try {
                out.println("exit"); //tell server to exit
                socket.close(); //close socket
            } catch (IOException e) {
                Log.e("remotedroid", "Error in closing socket", e);
            }
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }
//use/////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onDoubleTap(MotionEvent e) {
//        out.println("leftclick");
        Log.d("bheja","leftclick");
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gdc.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
  //      out.println("leftclick");
        Log.d("bheja2","leftclick");
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
//use////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("distance",""+distanceX+"andddddddddddd"+""+distanceY);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public class ConnectPhoneTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = true;
            try {

                serverAddr = InetAddress.getByName("DESKTOP-5BEQKQL");

                socket = new Socket(serverAddr, SERVER_PORT);//Open socket on server IP and port
            } catch (IOException e) {
                Log.e("remotedroid", "Error while connecting", e);
                result = false;
            }
            return result;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            isConnected = result;
            //dev.setText(serverAddr.toString());
            Toast.makeText(getApplicationContext(), isConnected ? "Connected to server!" : "Error while connecting", Toast.LENGTH_LONG).show();
            try {
                if (isConnected) {
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                            .getOutputStream())), true); //create output stream to send data to server
                }
            } catch (IOException e) {
                Log.e("remotedroid", "Error while creating OutWriter", e);
                Toast.makeText(getApplicationContext(), "Error while connecting", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class BtConnect extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                if (btSocket == null || !isBtConnected) {
                    //myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(addr);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    myBluetooth.cancelDiscovery();
                    //t.setText("START");
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                // msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                // finish();
                Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                //int in= btSocket.getConnectionType();
                ///dev.setText(""+in);
            } else {
                //msg("Connected.");
                isBtConnected = true;
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                mConnectedThread = new ConnectedThread(btSocket);
                mConnectedThread.start();
            }
            //progress.dismiss();
        }
    }

    //**************************************************** msg **************************************************************
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        boolean stopWorker = false;
        int readBufferPosition = 0;
        byte[] buffer = new byte[1024];
        int bytes;

        public void run() {
           // byte[] buffer = new byte[256];
            final byte delimiter = 10; //This is the ASCII code for a newline character

            // Keep looping to listen for received messages
            while (!Thread.currentThread().isInterrupted() ) {

                try {int bytesAvailable = mmInStream.available();
                    bytes = mmInStream.read(buffer);//read bytes from input buffer

                    byte[] packetBytes = new byte[bytesAvailable];
                    mmInStream.read(packetBytes);
                    String readMessage = new String(buffer, 0, bytes);

                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                    Log.d("pFOR", readMessage);
                    } catch (IOException e) {
                    break;
                }
            }
        }
    }
}

