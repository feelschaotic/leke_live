package com.juss.mediaplay.po;

public class BrowsingHistory extends BrowsingHistoryKey {
    private Integer browsingDuration;

    private Integer browsingNum;

    public Integer getBrowsingDuration() {
        return browsingDuration;
    }

    public void setBrowsingDuration(Integer browsingDuration) {
        this.browsingDuration = browsingDuration;
    }

    public Integer getBrowsingNum() {
        return browsingNum;
    }

    public void setBrowsingNum(Integer browsingNum) {
        this.browsingNum = browsingNum;
    }
}