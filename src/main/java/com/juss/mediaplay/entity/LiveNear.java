package com.juss.mediaplay.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2016/7/27.
 */
public class LiveNear implements Parcelable {
    /*[{"userNickname":"","userHead":"","userMoney":120.0,"fans":0,"liveId":1008,"liveCover":"/live/upload/livecover/2016/07/15/18/cff497d4-480d-4600-b31b-83a0b96d5a5d.jpg","liveTypeId":"","liveType":"","permissions":"","liveMoney":0.0,"distance":""},{"userNickname":"","userHead":"","userMoney":0.0,"fans":0,"liveId":1010,"liveCover":"/live/upload/livecover/2016/07/19/20/0d60b609-2908-438c-bab9-f2a7c8032d57.jpg","liveTypeId":"","liveType":"","permissions":"","liveMoney":0.0,"distance":""},{"userNickname":"请不要看昵称","userHead":"","userMoney":998.0,"fans":0,"liveId":1011,"liveCover":"/live/upload/livecover/2016/07/20/16/5a3528c5-a136-4756-bc85-dd4054a7bd5d.jpg","liveTypeId":"","liveType":"","permissions":"","liveMoney":0.0,"distance":""},{"userNickname":"mdzz","userHead":"/live/upload/userhead/2016/07/11/15/4fc80a71-d5c1-4de7-9cf4-568f705346b7.jpg","userMoney":100.0,"fans":1,"liveId":1005,"liveCover":"/live/upload/livecover/2016/07/11/16/6580fdd0-1d92-4000-8f5a-22538edfaf50.jpg","liveTypeId":1001,"liveType":"体育","permissions":"1002","liveMoney":100.0,"distance":"1211510"}]*/
    private String userNickname;
    private String userHead;
    private double userMoney;
    private int fans;
    private int liveId;
    private String liveCover;
    private int liveTypeId;
    private String liveType;
    private int permissions;
    private double liveMoney;
    private int distance;


    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }


    public int getFans() {
        return fans;
    }

    public double getUserMoney() {
        return userMoney;
    }

    public double getLiveMoney() {
        return liveMoney;
    }

    public void setLiveMoney(double liveMoney) {
        this.liveMoney = liveMoney;
    }

    public void setUserMoney(double userMoney) {
        this.userMoney = userMoney;
    }

    public void setFans(int fans) {

        this.fans = fans;
    }

    public int getLiveId() {
        return liveId;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
    }

    public String getLiveCover() {
        return liveCover;
    }

    public void setLiveCover(String liveCover) {
        this.liveCover = liveCover;
    }

    public int getLiveTypeId() {
        return liveTypeId;
    }

    public void setLiveTypeId(int liveTypeId) {
        this.liveTypeId = liveTypeId;
    }

    public String getLiveType() {
        return liveType;
    }

    public void setLiveType(String liveType) {
        this.liveType = liveType;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }




    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userNickname);
        dest.writeString(this.userHead);
        dest.writeDouble(this.userMoney);
        dest.writeInt(this.fans);
        dest.writeInt(this.liveId);
        dest.writeString(this.liveCover);
        dest.writeInt(this.liveTypeId);
        dest.writeString(this.liveType);
        dest.writeInt(this.permissions);
        dest.writeDouble(this.liveMoney);
        dest.writeInt(this.distance);
    }

    public LiveNear() {
    }

    protected LiveNear(Parcel in) {
        this.userNickname = in.readString();
        this.userHead = in.readString();
        this.userMoney = in.readDouble();
        this.fans = in.readInt();
        this.liveId = in.readInt();
        this.liveCover = in.readString();
        this.liveTypeId = in.readInt();
        this.liveType = in.readString();
        this.permissions = in.readInt();
        this.liveMoney = in.readDouble();
        this.distance = in.readInt();
    }

    public static final Parcelable.Creator<LiveNear> CREATOR = new Parcelable.Creator<LiveNear>() {
        @Override
        public LiveNear createFromParcel(Parcel source) {
            return new LiveNear(source);
        }

        @Override
        public LiveNear[] newArray(int size) {
            return new LiveNear[size];
        }
    };
}
