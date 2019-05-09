package com.example.luxiaolin.handlerdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.luxiaolin.IMyAidlInterface;

/**
 * Created by luozhanwei on 19-5-5.
 */

public class MyService extends Service{
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("LZW","onBind");
        return new IMyAidlInterface.Stub() {
            @Override
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

            }
        };
    }


}
