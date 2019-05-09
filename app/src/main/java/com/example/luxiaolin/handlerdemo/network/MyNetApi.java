package com.example.luxiaolin.handlerdemo.network;

import android.util.Log;

import com.example.luxiaolin.handlerdemo.network.bean.Translation;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by luozhanwei on 19-4-28.
 */

public class MyNetApi {
    private static final String TAG = "MyNetApi";
    private static volatile MyNetInterface sApi;

    public static MyNetInterface getInterface(){
        if(sApi == null) {
            synchronized (MyNetApi.class) {
                if(sApi ==null) {
                    Retrofit retrofit = new Retrofit.Builder().
                            baseUrl("http://fy.iciba.com/").
                            addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                            addConverterFactory(GsonConverterFactory.create()).build();
                    sApi = retrofit.create(MyNetInterface.class);
                }
            }
        }
        return sApi;
    }

}
