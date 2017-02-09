package com.ramo.campuslive.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ramo on 2016/6/30.
 */
public class User implements Parcelable{
    private String name;
    private byte[] head;
    private String desc;
    private int level;

    public User(String name,byte[] head, String desc){
        this.name=name;
        this.head=head;
        this.desc=desc;
    }
    public User(String name,byte[] head, String desc,int level){
        this.name=name;
        this.head=head;
        this.desc=desc;
        this.level=level;
    }

    protected User(Parcel in) {
        name = in.readString();
        head = in.createByteArray();
        desc = in.readString();
        level = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getHead() {
        return head;
    }

    public void setHead(byte[] head) {
        this.head = head;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByteArray(head);
        dest.writeString(desc);
        dest.writeInt(level);
    }
}
