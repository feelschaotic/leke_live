package com.juss.mediaplay.po;

import java.util.Date;

public class Answer {
    private Integer answerId;

    private Integer ansAnswerId;

    private Integer questionId;

    private Integer userId;

    private String answerContent;

    private Date answerTime;

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public Integer getAnsAnswerId() {
        return ansAnswerId;
    }

    public void setAnsAnswerId(Integer ansAnswerId) {
        this.ansAnswerId = ansAnswerId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public Date getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }
}