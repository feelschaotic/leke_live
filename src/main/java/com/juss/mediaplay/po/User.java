package com.juss.mediaplay.po;

public class User {
	/*
    * {"state":0,"data":{
    * "userId":10003,
    * "schoolId":1019,
    * "userName":"admin",
    * "liveId":1010,
    * "userRealName":"bbbb",
    * "userIdCard":"hhhhh",
    * "userSex":1,
    * "userPhone":"13566669999",
    * "userHead":"",
    * "userNickName":"请不要看昵称"}}
    * */
    private Integer userId;

	private Integer schoolId;

	private String userName;

	private Integer liveId;

	private Integer levelId;

	private String userRealName;

	private String userIdCard;

	private String userProfessionalId;

	private Integer userSex;

	private String userPhone;

	private String userHead;

	private String userPassword;

	private Integer userState;

	private String userNickName;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getLiveId() {
		return liveId;
	}

	public void setLiveId(Integer liveId) {
		this.liveId = liveId;
	}

	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public String getUserIdCard() {
		return userIdCard;
	}

	public void setUserIdCard(String userIdCard) {
		this.userIdCard = userIdCard;
	}

	public String getUserProfessionalId() {
		return userProfessionalId;
	}

	public void setUserProfessionalId(String userProfessionalId) {
		this.userProfessionalId = userProfessionalId;
	}

	public Integer getUserSex() {
		return userSex;
	}

	public void setUserSex(Integer userSex) {
		this.userSex = userSex;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserHead() {
		return userHead;
	}

	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Integer getUserState() {
		return userState;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

}