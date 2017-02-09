package com.juss.mediaplay.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class GuessingOption implements Parcelable {
    private Integer optionsId;

    private Integer guessingId;

    private String optionsName;

    private Float optionsCompensate;

    public Integer getOptionsId() {
        return optionsId;
    }

    public void setOptionsId(Integer optionsId) {
        this.optionsId = optionsId;
    }

    public Integer getGuessingId() {
        return guessingId;
    }

    public void setGuessingId(Integer guessingId) {
        this.guessingId = guessingId;
    }

    public String getOptionsName() {
        return optionsName;
    }

    public void setOptionsName(String optionsName) {
        this.optionsName = optionsName;
    }

    public Float getOptionsCompensate() {
        return optionsCompensate;
    }

    public void setOptionsCompensate(Float optionsCompensate) {
        this.optionsCompensate = optionsCompensate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.optionsId);
        dest.writeValue(this.guessingId);
        dest.writeString(this.optionsName);
        dest.writeValue(this.optionsCompensate);
    }

    public GuessingOption() {
    }

    protected GuessingOption(Parcel in) {
        this.optionsId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.guessingId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.optionsName = in.readString();
        this.optionsCompensate = (Float) in.readValue(Float.class.getClassLoader());
    }

    public static final Creator<GuessingOption> CREATOR = new Creator<GuessingOption>() {
        @Override
        public GuessingOption createFromParcel(Parcel source) {
            return new GuessingOption(source);
        }

        @Override
        public GuessingOption[] newArray(int size) {
            return new GuessingOption[size];
        }
    };
}