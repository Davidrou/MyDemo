package com.example.luxiaolin.handlerdemo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by luozhanwei on 19-5-5.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
       // LeakCanary.install(this);
    }
}
