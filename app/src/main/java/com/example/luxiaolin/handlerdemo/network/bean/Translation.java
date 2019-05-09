package com.example.luxiaolin.handlerdemo.network.bean;

import android.util.Log;

public class Translation {

    public int status;

    public content content;
    public static class content {
        private String from;
        private String to;
        private String vendor;
        public String out;
        private int errNo;
    }

    //定义 输出返回数据 的方法
    public void show() {
        Log.d("RxJava", content.out );
    }
}