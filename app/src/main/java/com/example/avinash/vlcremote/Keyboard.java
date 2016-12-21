package com.example.avinash.vlcremote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by AVINASH on 7/21/2016.
 */
public class Keyboard extends AppCompatActivity {
   public static boolean flag=false,enter=false;
    int prevl=0,pr1=0,curl=0,cr1=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyboard);

        final EditText editText=(EditText)findViewById(R.id.editText3);
        editText.setHintTextColor(getResources().getColor(R.color.white));
        editText.setHint("Start Typing Here.....");


       editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tab1 t=new tab1();
                Character c=s.charAt(s.length()-1);
                cr1=s.length();

                if(cr1>pr1){pr1=cr1;
                    if(  c.toString().equals("\n") ){
                    if(flag)t.out.println("nl");
                    enter=true;
                }}

            }

            @Override
            public void afterTextChanged(Editable s) {
                tab1 t=new tab1();

                String ch=s.toString();
                Character character=null;

                curl=ch.length();
                if(curl==0){curl=prevl=0;}
                if(flag){
                if(curl>0) {
                    character = ch.charAt(ch.length() - 1);

                    if (curl > prevl&&enter==false) {
                        //if(check==true)
                        t.out.println(" " + character.toString());
                        prevl = curl;
                    } else if (curl == prevl&&enter==false) {
                        t.out.println("bs");
                    } else if (curl < prevl&&enter==false) {
                        t.out.println("bs");
                    }
                    Log.d("typed", character.toString());

                }
                else t.out.println("bs");

            }
            enter=false;}
        });
    }
}
