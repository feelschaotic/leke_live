package com.juss.mediaplay.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2016/8/4.
 */
public class MyCommunity1 implements Parcelable {
    private int socityId;
    private String socityName;
    private String socityDesc;
    private String socityLogo;
    private int memberNum;

    public MyCommunity1(int socityId, String socityName, String socityDesc, String socityLogo, int memberNum) {
        this.socityId = socityId;
        this.socityName = socityName;
        this.socityDesc = socityDesc;
        this.socityLogo = socityLogo;
        this.memberNum = memberNum;
    }

    public int getSocityId() {
        return socityId;
    }

    public void setSocityId(int socityId) {
        this.socityId = socityId;
    }

    public String getSocityName() {
        return socityName;
    }

    public void setSocityName(String socityName) {
        this.socityName = socityName;
    }

    public String getSocityDesc() {
        return socityDesc;
    }

    public void setSocityDesc(String socityDesc) {
        this.socityDesc = socityDesc;
    }

    public String getSocityLogo() {
        return socityLogo;
    }

    public void setSocityLogo(String socityLogo) {
        this.socityLogo = socityLogo;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.socityId);
        dest.writeString(this.socityName);
        dest.writeString(this.socityDesc);
        dest.writeString(this.socityLogo);
        dest.writeInt(this.memberNum);
    }

    public MyCommunity1() {
    }

    protected MyCommunity1(Parcel in) {
        this.socityId = in.readInt();
        this.socityName = in.readString();
        this.socityDesc = in.readString();
        this.socityLogo = in.readString();
        this.memberNum = in.readInt();
    }

    public static final Creator<MyCommunity> CREATOR = new Creator<MyCommunity>() {
        @Override
        public MyCommunity createFromParcel(Parcel source) {
            return new MyCommunity(source);
        }

        @Override
        public MyCommunity[] newArray(int size) {
            return new MyCommunity[size];
        }
    };



}
