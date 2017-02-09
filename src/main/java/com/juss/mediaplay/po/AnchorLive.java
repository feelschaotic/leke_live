package com.juss.mediaplay.po;

public class AnchorLive {
	
	private String userNickname;
	
	private String userHead;
	
	private Double userMoney = 0.0;
	
	private Integer fans = 0;
	
	private Integer liveId;
	
	private String liveCover;
	
	private Integer liveTypeId;
	
	private String liveType;

	private String permissions;
	
	private Double liveMoney = 0.0;
	
	private String distance;
	
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

}
