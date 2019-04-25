package com.example.luxiaolin.handlerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by luxiaolin on 18/5/6.
 */

public class SecondMainActivity extends AppCompatActivity {
    Handler mHandler = new MyHandler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },10*1000);
    }

    static class MyHandler extends Handler{
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    }
}
