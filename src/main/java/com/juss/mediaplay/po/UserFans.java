package com.juss.mediaplay.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2016/8/4.
 */
public class UserFans implements Parcelable {
    private int userId;
    private String userNickName;
    private String userHead;
    private String userSignature;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.userNickName);
        dest.writeString(this.userHead);
        dest.writeString(this.userSignature);
    }

    public UserFans() {
    }

    protected UserFans(Parcel in) {
        this.userId = in.readInt();
        this.userNickName = in.readString();
        this.userHead = in.readString();
        this.userSignature = in.readString();
    }

    public static final Creator<UserFans> CREATOR = new Creator<UserFans>() {
        @Override
        public UserFans createFromParcel(Parcel source) {
            return new UserFans(source);
        }

        @Override
        public UserFans[] newArray(int size) {
            return new UserFans[size];
        }
    };
}
