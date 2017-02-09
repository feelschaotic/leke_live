package com.juss.mediaplay.po;

import java.util.Date;
import java.util.List;

public class Guessing {
    private Integer guessingId;

    private Integer liveId;

    private String guessingResult;

    private String guessingTitle;

    private Date guessingTime;

    private Date stopTime;

    private List<GuessingOptions> options;


    public Integer getGuessingId() {
        return guessingId;
    }

    public void setGuessingId(Integer guessingId) {
        this.guessingId = guessingId;
    }

    public Integer getLiveId() {
        return liveId;
    }

    public void setLiveId(Integer liveId) {
        this.liveId = liveId;
    }

    public String getGuessingResult() {
        return guessingResult;
    }

    public void setGuessingResult(String guessingResult) {
        this.guessingResult = guessingResult;
    }

    public String getGuessingTitle() {
        return guessingTitle;
    }

    public void setGuessingTitle(String guessingTitle) {
        this.guessingTitle = guessingTitle;
    }

    public Date getGuessingTime() {
        return guessingTime;
    }

    public void setGuessingTime(Date guessingTime) {
        this.guessingTime = guessingTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public List<GuessingOptions> getOptions() {
        return options;
    }

    public void setOptions(List<GuessingOptions> options) {
        this.options = options;
    }
}