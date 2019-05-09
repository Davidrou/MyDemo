package com.example.luxiaolin.handlerdemo.network.bean;

import android.util.Log;

public class Translation2 {
    public int status;

    public Translation.content content;
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