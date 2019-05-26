package com.example.cdfinger.aidl_demo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AIDLService extends Service {
    private String TAG = this.getClass().getSimpleName();
    private myBinder binder = new myBinder();
    //包含Book对象的list
    private List<Book> mBooks = new ArrayList<>();

    public AIDLService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "onBind");
        return mBookManager;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    class myBinder extends Binder{

    }

    //由AIDL文件生成的BookManager
    private final BookManager.Stub mBookManager = new BookManager.Stub() {

        /**
         * Demonstrates some basic types that you can use as parameters
         * and return values in AIDL.
         *
         * @param anInt
         * @param aLong
         * @param aBoolean
         * @param aFloat
         * @param aDouble
         * @param aString
         */
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) {

        }

        public List<Book> getBooks() {
            synchronized (this) {
                Log.d(TAG, "invoking getBooks() method");
                if (mBooks != null) {
                    return mBooks;
                }
                return new ArrayList<>();
            }
        }
        public void addBook(Book book) {
            synchronized (this) {
                int i;
                Log.d(TAG, "invoking addBook() method");
                if (book == null) {
                    Log.e(TAG, "Book is null,so don't need to add");
                }
                for (i=0; i<mBooks.size();i++) {
                    //Log.d(TAG, "i="+i+" name="+mBooks.get(i).getName()+" price="+mBooks.get(i).getPrice());
                    if ((book.getName().matches(mBooks.get(i).getName())) && (book.getPrice().matches(mBooks.get(i).getPrice()))) {
                        break;
                    }
                }
                if(i == mBooks.size())
                    mBooks.add(book);
            }
        }

        public void removeBook(Book book) {
            synchronized (this) {
                Log.d(TAG, "invoking removeBook() method");
                if (book == null) {
                    Log.e(TAG, "Book is null, so don't need to remove");
                }
                Log.d(TAG, "removeBook");
                Log.d(TAG, "book.name="+book.getName()+" book.price="+book.getPrice());

                for (int i=0; i<mBooks.size();i++) {
                    Log.d(TAG, "i="+i+" name="+mBooks.get(i).getName()+" price="+mBooks.get(i).getPrice());
                    if ((book.getName().matches(mBooks.get(i).getName())) && (book.getPrice().matches(mBooks.get(i).getPrice()))) {
                        Log.d(TAG, "mBooks.remove(book)");
                        mBooks.remove(mBooks.get(i));
                    }
                }

            }
        }

    };
}
