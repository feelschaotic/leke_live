package com.juss.mediaplay.po;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Theme implements Parcelable {
    private int themeId;
    private String themeName;
    private Date themeTime;


    protected Theme(Parcel in) {
        themeId = in.readInt();
        themeName = in.readString();
        themeTime = (Date) in.readSerializable();
    }

    public static final Creator<Theme> CREATOR = new Creator<Theme>() {
        @Override
        public Theme createFromParcel(Parcel in) {
            return new Theme(in);
        }

        @Override
        public Theme[] newArray(int size) {
            return new Theme[size];
        }
    };

    /*
        protected Theme(Parcel in) {
            themeId = in.readInt();
            themeName = in.readString();
            themeTime=(Date)in.readSerializable();
        }
    */
    public Integer getThemeId() {
        return themeId;
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public Date getThemeTime() {
        return themeTime;
    }

    public void setThemeTime(Date themeTime) {
        this.themeTime = themeTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(themeId);
        dest.writeString(themeName);
        dest.writeSerializable(themeTime);
    }

   /* @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(themeId);
        dest.writeString(themeName);
        dest.writeSerializable(themeTime);

    }*/
}