package com.juss.mediaplay.entity;

import android.os.Parcel;
import android.os.Parcelable;


public class AnchorLive implements Parcelable {


	/*
	*  [{"userNickname":"请不要看昵称",  "userHead":"",  "userMoney":998.0,   "fans":0,  "liveId":1011,
	*  "liveCover":"/live/upload/livecover/2016/07/20/16/5a3528c5-a136-4756-bc85-dd4054a7bd5d.jpg",
	*  "liveTypeId":-1,   "liveType":"",    "permissions":"",   "liveMoney":0.0,   "distance":"",  "liveTitle":"",  "livePushAddress":""}*/
	private String userNickname;
	private String userHead;
	private Double userMoney = 0.0;
	private Integer fans = 0;
	private Integer liveId = -1;
	private String liveCover;
	private Integer liveTypeId = -1;
	private String liveType;
	private String permissions;
	private Double liveMoney = 0.0;
	private String distance;
	private String liveTitle;
	private String livePushAddress;
	private String school;


	public String getLiveTitle() {
		return liveTitle;
	}

	public void setLiveTitle(String liveTitle) {
		this.liveTitle = liveTitle;
	}

	public String getLivePushAddress() {
		return livePushAddress;
	}

	public void setLivePushAddress(String livePushAddress) {
		this.livePushAddress = livePushAddress;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

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

	public Double getUserMoney() {
		return userMoney;
	}

	public void setUserMoney(Double userMoney) {
		this.userMoney = userMoney;
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

	public Integer getLiveTypeId() {
		return liveTypeId;
	}

	public void setLiveTypeId(Integer liveTypeId) {
		this.liveTypeId = liveTypeId;
	}

	public String getLiveType() {
		return liveType;
	}

	public void setLiveType(String liveType) {
		this.liveType = liveType;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public Double getLiveMoney() {
		return liveMoney;
	}

	public void setLiveMoney(Double liveMoney) {
		this.liveMoney = liveMoney;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.userNickname);
		dest.writeString(this.userHead);
		dest.writeValue(this.userMoney);
		dest.writeValue(this.fans);
		dest.writeValue(this.liveId);
		dest.writeString(this.liveCover);
		dest.writeValue(this.liveTypeId);
		dest.writeString(this.liveType);
		dest.writeString(this.permissions);
		dest.writeValue(this.liveMoney);
		dest.writeString(this.distance);
		dest.writeString(this.liveTitle);
		dest.writeString(this.livePushAddress);
	}

	public AnchorLive() {
	}

	protected AnchorLive(Parcel in) {
		this.userNickname = in.readString();
		this.userHead = in.readString();
		this.userMoney = (Double) in.readValue(Double.class.getClassLoader());
		this.fans = (Integer) in.readValue(Integer.class.getClassLoader());
		this.liveId = (Integer) in.readValue(Integer.class.getClassLoader());
		this.liveCover = in.readString();
		this.liveTypeId = (Integer) in.readValue(Integer.class.getClassLoader());
		this.liveType = in.readString();
		this.permissions = in.readString();
		this.liveMoney = (Double) in.readValue(Double.class.getClassLoader());
		this.distance = in.readString();
		this.liveTitle = in.readString();
		this.livePushAddress = in.readString();
	}

	public static final Parcelable.Creator<AnchorLive> CREATOR = new Parcelable.Creator<AnchorLive>() {
		@Override
		public AnchorLive createFromParcel(Parcel source) {
			return new AnchorLive(source);
		}

		@Override
		public AnchorLive[] newArray(int size) {
			return new AnchorLive[size];
		}
	};

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}
}
