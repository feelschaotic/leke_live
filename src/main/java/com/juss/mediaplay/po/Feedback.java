package com.juss.mediaplay.po;

import java.util.Date;

public class Feedback {
    private Integer feedbackId;

    private Integer userId;

    private String feedbackContent;

    private Date feedbackTime;

    private Integer feedbackState;

    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public Integer getFeedbackState() {
        return feedbackState;
    }

    public void setFeedbackState(Integer feedbackState) {
        this.feedbackState = feedbackState;
    }
}