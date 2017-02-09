package com.juss.mediaplay.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class MyGuess  implements Parcelable {

    private Integer liveId;

    private Integer guessingId;

    private String guessingTitle;

    private String guessingResult;

    private String choice;

    private Date  betTime;

    public Integer getLiveId() {
        return liveId;
    }

    public void setLiveId(Integer liveId) {
        this.liveId = liveId;
    }

    public Integer getGuessingId() {
        return guessingId;
    }

    public void setGuessingId(Integer guessingId) {
        this.guessingId = guessingId;
    }

    public String getGuessingTitle() {
        return guessingTitle;
    }

    public void setGuessingTitle(String guessingTitle) {
        this.guessingTitle = guessingTitle;
    }

    public String getGuessingResult() {
        return guessingResult;
    }

    public void setGuessingResult(String guessingResult) {
        this.guessingResult = guessingResult;
    }


    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public Date getBetTime() {
        return betTime;
    }

    public void setBetTime(Date betTime) {
        this.betTime = betTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.liveId);
        dest.writeValue(this.guessingId);
        dest.writeString(this.guessingTitle);
        dest.writeString(this.guessingResult);
        dest.writeString(this.choice);
        dest.writeLong(this.betTime != null ? this.betTime.getTime() : -1);
    }

    public MyGuess() {
    }

    protected MyGuess(Parcel in) {
        this.liveId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.guessingId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.guessingTitle = in.readString();
        this.guessingResult = in.readString();
        this.choice = in.readString();
        long tmpBetTime = in.readLong();
        this.betTime = tmpBetTime == -1 ? null : new Date(tmpBetTime);
    }

    public static final Creator<MyGuess> CREATOR = new Creator<MyGuess>() {
        @Override
        public MyGuess createFromParcel(Parcel source) {
            return new MyGuess(source);
        }

        @Override
        public MyGuess[] newArray(int size) {
            return new MyGuess[size];
        }
    };
}
