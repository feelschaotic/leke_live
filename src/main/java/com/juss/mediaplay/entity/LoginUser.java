package com.juss.mediaplay.entity;

/**
 * Created by lenovo on 2016/7/19.
 */
public class LoginUser {
    //登录注册的实体
    private int userId;
    private String userName;
    private String userRealName;//真实姓名
    private String userIdCard;//身份证号
    private String userPhone;//手机号码
    private String userState;//用户状态

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public String getUserIdCard() {
        return userIdCard;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public void setUserIdCard(String userIdCard) {
        this.userIdCard = userIdCard;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }
}
