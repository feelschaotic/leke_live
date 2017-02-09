package com.ramo.campuslive.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ramo on 2016/6/22.
 */
public class Gift implements Parcelable{
    private byte[] img;
    private float price;
    private String name;
    public  Gift(byte[] img,float price,String name){
        this.img=img;
        this.price=price;
        this.name=name;
    }

    protected Gift(Parcel in) {
        img = in.createByteArray();
        price = in.readFloat();
        name = in.readString();
    }

    public static final Creator<Gift> CREATOR = new Creator<Gift>() {
        @Override
        public Gift createFromParcel(Parcel in) {
            return new Gift(in);
        }

        @Override
        public Gift[] newArray(int size) {
            return new Gift[size];
        }
    };

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(img);
        dest.writeFloat(price);
        dest.writeString(name);
    }
}
