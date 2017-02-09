package com.juss.mediaplay.po;

import android.os.Parcel;
import android.os.Parcelable;

public class Admin implements Parcelable {
    private Integer adminId;

    private String adminName;

    private String adminPassword;

    private Integer adminState;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public Integer getAdminState() {
        return adminState;
    }

    public void setAdminState(Integer adminState) {
        this.adminState = adminState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.adminId);
        dest.writeString(this.adminName);
        dest.writeString(this.adminPassword);
        dest.writeValue(this.adminState);
    }

    public Admin() {
    }

    protected Admin(Parcel in) {
        this.adminId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.adminName = in.readString();
        this.adminPassword = in.readString();
        this.adminState = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Admin> CREATOR = new Parcelable.Creator<Admin>() {
        @Override
        public Admin createFromParcel(Parcel source) {
            return new Admin(source);
        }

        @Override
        public Admin[] newArray(int size) {
            return new Admin[size];
        }
    };
}