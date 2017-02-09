package com.juss.mediaplay.po;

import java.util.Date;

public class Letter {
    private Integer letterId;

    private Integer userSendId;

    private Integer userReceiveId;

    private String letterContent;

    private Date letterTime;

    public Integer getLetterId() {
        return letterId;
    }

    public void setLetterId(Integer letterId) {
        this.letterId = letterId;
    }

    public Integer getUserSendId() {
        return userSendId;
    }

    public void setUserSendId(Integer userSendId) {
        this.userSendId = userSendId;
    }

    public Integer getUserReceiveId() {
        return userReceiveId;
    }

    public void setUserReceiveId(Integer userReceiveId) {
        this.userReceiveId = userReceiveId;
    }

    public String getLetterContent() {
        return letterContent;
    }

    public void setLetterContent(String letterContent) {
        this.letterContent = letterContent;
    }

    public Date getLetterTime() {
        return letterTime;
    }

    public void setLetterTime(Date letterTime) {
        this.letterTime = letterTime;
    }
}