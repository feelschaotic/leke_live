package com.juss.mediaplay.po;

public class UserWithBLOBs extends User {
    private byte[] userProfessionalImg;

	private byte[] userIdCardImg;

	public byte[] getUserProfessionalImg() {
		return userProfessionalImg;
	}

	public void setUserProfessionalImg(byte[] userProfessionalImg) {
		this.userProfessionalImg = userProfessionalImg;
	}

	public byte[] getUserIdCardImg() {
		return userIdCardImg;
	}

	public void setUserIdCardImg(byte[] userIdCardImg) {
		this.userIdCardImg = userIdCardImg;
	}

}