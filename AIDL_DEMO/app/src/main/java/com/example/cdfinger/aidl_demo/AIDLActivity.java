package com.example.cdfinger.aidl_demo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class AIDLActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = this.getClass().getSimpleName();
    //由AIDL文件生成的Java类
    private BookManager mBookManager = null;
    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean mBound = false;
    //包含Book对象的list
    private List<Book> mBooks;

    EditText EditText_BookName;
    EditText EditText_BookPrice;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            mBookManager = BookManager.Stub.asInterface(service);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            mBound = false;
        }
    };

    private void attemptToBindService() {
        Log.d(TAG,"attemptToBindService");
        Intent intent = new Intent(AIDLActivity.this, AIDLService.class);
        //startService(intent);
        bindService(intent,mServiceConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aidl_client);
        Button button_add = findViewById(R.id.button_add);
        Button button_search = findViewById(R.id.button_search);
        Button button_remove = findViewById(R.id.button_remove);
        button_add.setOnClickListener(this);
        button_search.setOnClickListener(this);
        button_remove.setOnClickListener(this);

        EditText_BookName = findViewById(R.id.editText_bookname);
        EditText_BookPrice = findViewById(R.id.editText_bookprice);

        Log.d(TAG,"onCreate");
        if (!mBound) {
            attemptToBindService();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        Log.d(TAG,"onClick");
        String  BookName = EditText_BookName.getText().toString();
        String  BookPrice = EditText_BookPrice.getText().toString();
        switch (view.getId()){
            case R.id.button_add:
                Log.d(TAG,"button_add click");
                Log.d(TAG,"BookName="+BookName+" BookPrice="+BookPrice);
                if (mBound) {
                    try {
                        mBookManager.addBook(new Book(BookName,BookPrice));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.button_search:
                try {
                    mBooks = mBookManager.getBooks();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                for (int i=0; i<mBooks.size();i++)
                    Log.d(TAG, "i="+i+" name="+mBooks.get(i).getName()+" price="+mBooks.get(i).getPrice());
                break;

            case R.id.button_remove:
                Log.d(TAG,"button_remove click");
                Log.d(TAG,"BookName="+BookName+" BookPrice="+BookPrice);
                if (mBound) {
                    try {
                        mBookManager.removeBook(new Book(BookName,BookPrice));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
