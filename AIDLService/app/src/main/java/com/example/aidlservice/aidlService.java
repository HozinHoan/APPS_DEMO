package com.example.aidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

public class aidlService extends Service {
    private final String TAG = aidlService.class.getName();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind enter");
        return iBinder;
    }

   private IBinder iBinder = new ICommunication.Stub() {
       @Override
       public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

       }

       @Override
       public int add(int num1, int num2) throws RemoteException {
           Log.d(TAG, "服务端接受到参数 num1:"+num1+"num2:"+num2);
           return (num1+num2);
       }
   };
}
