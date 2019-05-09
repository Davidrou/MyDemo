package com.example.luxiaolin.handlerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by luxiaolin on 18/5/6.
 */

public class SecondMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
            }
        };
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("LZW","TEST***********");
//
//            }
//        },1000*10);
//    }

        Message message = handler.obtainMessage();
        handler.sendMessageDelayed(message, 1000*10);
    }
}
