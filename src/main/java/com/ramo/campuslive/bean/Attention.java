package com.ramo.campuslive.bean;

/**
 * Created by ramo on 2016/6/20.
 */
public class Attention extends LiveBase{
    private String liveName;
    private int type;

    public Attention(User u, String liveName, byte[] cover, int likeNum, int type) {
        this.setUser(u);
        this.liveName=liveName;
        this.setCover(cover);
        this.setLikeNum(likeNum);
        this.type=type;
    }

    public String getLiveName() {
        return liveName;
    }

    public void setLiveName(String liveName) {
        this.liveName = liveName;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
