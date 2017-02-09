package com.ramo.campuslive.bean;

/**
 * Created by ramo on 2016/7/1.
 */
public class Community extends LiveBase{
    private String desc;

    public Community(byte[] cover,User u,String desc,int likeNum){
        super.setCover(cover);
        super.setUser(u);
        super.setLikeNum(likeNum);
        this.desc=desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
