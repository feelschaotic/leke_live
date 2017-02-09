package com.ramo.campuslive.bean;

import java.util.Date;

/**
 * Created by ramo on 2016/7/7.
 */
public class Planting {
    private User u;
    private Date destroy_time;
    private String desc;
    private Date release_time;
    private String firstView;

    public Planting(User u, Date destroy_time, Date release_time, String desc, String firstView) {
        this.u = u;
        this.destroy_time = destroy_time;
        this.release_time = release_time;
        this.desc = desc;
        this.firstView = firstView;

    }

    public User getU() {
        return u;
    }

    public void setU(User u) {
        this.u = u;
    }

    public Date getDestroy_time() {
        return destroy_time;
    }

    public void setDestroy_time(Date destroy_time) {
        this.destroy_time = destroy_time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getRelease_time() {
        return release_time;
    }

    public void setRelease_time(Date release_time) {
        this.release_time = release_time;
    }

    public String getFirstView() {
        return firstView;
    }

    public void setFirstView(String firstView) {
        this.firstView = firstView;
    }
}
