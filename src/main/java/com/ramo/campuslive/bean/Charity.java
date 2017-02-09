package com.ramo.campuslive.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ramo on 2016/9/8.
 */
public class Charity implements Parcelable {
    private String userNickname;
    private String userHead;
    private Integer fans = 0;
    private Integer shareNum = 0;
    private Integer liveId = -1;
    private String liveCover;
    private String communityName;
    private String livePushAddress;
    private String school;
    private String explain;


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

    public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }

    public Integer getLiveId() {
        return liveId;
    }

    public void setLiveId(Integer liveId) {
        this.liveId = liveId;
    }

    public String getLiveCover() {
        return liveCover;
    }

    public void setLiveCover(String liveCover) {
        this.liveCover = liveCover;
    }


    public String getLivePushAddress() {
        return livePushAddress;
    }

    public void setLivePushAddress(String livePushAddress) {
        this.livePushAddress = livePushAddress;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Integer getShareNum() {
        return shareNum;
    }

    public void setShareNum(Integer shareNum) {
        this.shareNum = shareNum;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userNickname);
        dest.writeString(this.userHead);
        dest.writeValue(this.fans);
        dest.writeValue(this.shareNum);
        dest.writeValue(this.liveId);
        dest.writeString(this.liveCover);
        dest.writeString(this.communityName);
        dest.writeString(this.livePushAddress);
        dest.writeString(this.school);
        dest.writeString(this.explain);
    }

    public Charity() {
    }

    protected Charity(Parcel in) {
        this.userNickname = in.readString();
        this.userHead = in.readString();
        this.fans = (Integer) in.readValue(Integer.class.getClassLoader());
        this.shareNum = (Integer) in.readValue(Integer.class.getClassLoader());
        this.liveId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.liveCover = in.readString();
        this.communityName = in.readString();
        this.livePushAddress = in.readString();
        this.school = in.readString();
        this.explain = in.readString();
    }

    public static final Creator<Charity> CREATOR = new Creator<Charity>() {
        @Override
        public Charity createFromParcel(Parcel source) {
            return new Charity(source);
        }

        @Override
        public Charity[] newArray(int size) {
            return new Charity[size];
        }
    };
}
