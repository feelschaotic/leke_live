package com.juss.mediaplay.po;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class BalanceRecord implements Parcelable{
    private Integer recordId;

    private Integer userId;

    private Integer introductionId;

    private Date recordDate;

    private Float recordNum;

    private Integer balanceState;

    protected BalanceRecord(Parcel in) {
    }

    public static final Creator<BalanceRecord> CREATOR = new Creator<BalanceRecord>() {
        @Override
        public BalanceRecord createFromParcel(Parcel in) {
            return new BalanceRecord(in);
        }

        @Override
        public BalanceRecord[] newArray(int size) {
            return new BalanceRecord[size];
        }
    };

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIntroductionId() {
        return introductionId;
    }

    public void setIntroductionId(Integer introductionId) {
        this.introductionId = introductionId;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public Float getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(Float recordNum) {
        this.recordNum = recordNum;
    }

    public Integer getBalanceState() {
        return balanceState;
    }

    public void setBalanceState(Integer balanceState) {
        this.balanceState = balanceState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}