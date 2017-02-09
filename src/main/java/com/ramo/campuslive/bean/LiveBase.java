package com.ramo.campuslive.bean;

/**
 * Created by ramo on 2016/7/12.
 */
public class LiveBase {
    private byte[] cover;
    private User user;
    private int likeNum;

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }
}
