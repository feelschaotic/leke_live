package com.ramo.campuslive.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ramo on 2016/7/6.
 */
public class GuessingOptions implements Parcelable {
    private String options_name;
    private String options_compensate;

    public GuessingOptions() {
        super();
    }

    protected GuessingOptions(Parcel in) {
        options_name = in.readString();
        options_compensate = in.readString();
    }

    public static final Creator<GuessingOptions> CREATOR = new Creator<GuessingOptions>() {
        @Override
        public GuessingOptions createFromParcel(Parcel in) {
            return new GuessingOptions(in);
        }

        @Override
        public GuessingOptions[] newArray(int size) {
            return new GuessingOptions[size];
        }
    };

    public String getOptions_name() {
        return options_name;
    }

    public void setOptions_name(String options_name) {
        this.options_name = options_name;
    }

    public String getOptions_compensate() {
        return options_compensate;
    }

    public void setOptions_compensate(String options_compensate) {
        this.options_compensate = options_compensate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(options_name);
        dest.writeString(options_compensate);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof GuessingOptions))
            return false;
        if (((GuessingOptions) o).options_name.equals(this.options_name))
            return true;
        return super.equals(o);
    }
}
