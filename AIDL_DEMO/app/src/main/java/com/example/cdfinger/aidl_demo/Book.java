package com.example.cdfinger.aidl_demo;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String name;
    private String price;

    protected Book(Parcel in) {
        name = in.readString();
        price = in.readString();
    }
    public Book() {
    }

    public Book(String bookName, String price) {
        this.name = bookName;
        this.price = price;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(price);
    }

    /**
     * 参数是一个Parcel,用它来存储与传输数据
     * @param
     */
    public void readFromParcel(Parcel parcel) {
        //注意，此处的读值顺序应当是和writeToParcel()方法中一致的
        name = parcel.readString();
        price = parcel.readString();
    }

}
