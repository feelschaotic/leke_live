package com.juss.mediaplay.po;

public class Live {
    private Integer liveId;

	private Integer typeId;

	private Integer permissionsId;

	private Integer themeId;

	private Integer liveState;

	private Double longitude;

	private Double latitude;

	private String liveCover;

	private Float permissionsMoney;

	private String permissionsPassword;

	private String liveVerification;

	private String livePushAddress;

	private String liveTitle;

	public Integer getLiveId() {
		return liveId;
	}

	public void setLiveId(Integer liveId) {
		this.liveId = liveId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getPermissionsId() {
		return permissionsId;
	}

	public void setPermissionsId(Integer permissionsId) {
		this.permissionsId = permissionsId;
	}

	public Integer getThemeId() {
		return themeId;
	}

	public void setThemeId(Integer themeId) {
		this.themeId = themeId;
	}

	public Integer getLiveState() {
		return liveState;
	}

	public void setLiveState(Integer liveState) {
		this.liveState = liveState;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getLiveCover() {
		return liveCover;
	}

	public void setLiveCover(String liveCover) {
		this.liveCover = liveCover;
	}

	public Float getPermissionsMoney() {
		return permissionsMoney;
	}

	public void setPermissionsMoney(Float permissionsMoney) {
		this.permissionsMoney = permissionsMoney;
	}

	public String getPermissionsPassword() {
		return permissionsPassword;
	}

	public void setPermissionsPassword(String permissionsPassword) {
		this.permissionsPassword = permissionsPassword;
	}

	public String getLiveVerification() {
		return liveVerification;
	}

	public void setLiveVerification(String liveVerification) {
		this.liveVerification = liveVerification;
	}

	public String getLivePushAddress() {
		return livePushAddress;
	}

	public void setLivePushAddress(String livePushAddress) {
		this.livePushAddress = livePushAddress;
	}

	public String getLiveTitle() {
		return liveTitle;
	}

	public void setLiveTitle(String liveTitle) {
		this.liveTitle = liveTitle;
	}
}