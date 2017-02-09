package com.juss.mediaplay.entity;

import java.util.Date;
import java.util.List;

public class GuessingVO {
    Integer liveId;
    String title;
    Date stopTime;
    int betNum;
    int state;
    List<GuessingOption> options;


    public Integer getLiveId() {
        return liveId;
    }

    public void setLiveId(Integer liveId) {
        this.liveId = liveId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public List<GuessingOption> getOptions() {
        return options;
    }

    public void setOptions(List<GuessingOption> options) {
        this.options = options;
    }

    public int getBetNum() {
        return betNum;
    }

    public void setBetNum(int betNum) {
        this.betNum = betNum;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
