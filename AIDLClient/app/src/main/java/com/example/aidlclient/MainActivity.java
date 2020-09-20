package com.example.aidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = MainActivity.class.getName();
    private EditText mETNum1;
    private EditText mETNum2;
    private EditText mETRes;

    private Button mBtnAdd;
    ICommunication iCommunication;
    private ServiceConnection conn = new ServiceConnection() {
        //绑定上服务
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //拿到了远程的服务对象
            iCommunication = ICommunication.Stub.asInterface(service);
            Log.d(TAG, "onServiceConnected");
        }

        //断开服务
        @Override
        public void onServiceDisconnected(ComponentName name) {
            //回收服务
            iCommunication = null;
            Log.d(TAG, "onServiceDisconnected");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();

        binderService(); //软件启动，绑定服务
        Log.d(TAG, "binderService");
    }

    private void initview() {
        mETNum1 = findViewById(R.id.et_num1);
        mETNum2 = findViewById(R.id.et_num2);
        mETRes = findViewById(R.id.et_res);

        mBtnAdd = findViewById(R.id.button1);
        mBtnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int num1 = Integer.parseInt(mETNum1.getText().toString());
        int num2 = Integer.parseInt(mETNum2.getText().toString());
        try {
            int res = iCommunication.add(num1, num2);
            mETRes.setText("" + num1 + "+" +num2+" = "+res);
        } catch (RemoteException e) {
            e.printStackTrace();
            mETRes.setText("计算出错");
        }

    }

    private void binderService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.aidlservice", "com.example.aidlservice.aidlService"));
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
