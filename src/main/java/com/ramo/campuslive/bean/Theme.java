package com.ramo.campuslive.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ramo on 2016/7/8.
 */
public class Theme {
    private int theme_id;
    private String theme_name;
    private Date theme_time;
    private List<LiveRoom> liveList = new ArrayList<>();

    public Theme(int theme_id, String theme_name, Date theme_time) {
        this.theme_id = theme_id;
        this.theme_name = theme_name;
        this.theme_time = theme_time;
    }

    public int getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(int theme_id) {
        this.theme_id = theme_id;
    }

    public String getTheme_name() {
        return theme_name;
    }

    public void setTheme_name(String theme_name) {
        this.theme_name = theme_name;
    }

    public Date getTheme_time() {
        return theme_time;
    }

    public void setTheme_time(Date theme_time) {
        this.theme_time = theme_time;
    }

    public List<LiveRoom> getLiveList() {
        return liveList;
    }

    public void setLiveList(List<LiveRoom> liveList) {
        this.liveList = liveList;
    }
}
