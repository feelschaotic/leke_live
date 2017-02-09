package com.juss.mediaplay.po;

import android.os.Parcel;
import android.os.Parcelable;

public class Permissions implements Parcelable {
    private Integer permissionsId;

    private String permissionsName;

    public Integer getPermissionsId() {
        return permissionsId;
    }

    public void setPermissionsId(Integer permissionsId) {
        this.permissionsId = permissionsId;
    }

    public String getPermissionsName() {
        return permissionsName;
    }

    public void setPermissionsName(String permissionsName) {
        this.permissionsName = permissionsName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.permissionsId);
        dest.writeString(this.permissionsName);
    }

    public Permissions() {
    }

    protected Permissions(Parcel in) {
        this.permissionsId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.permissionsName = in.readString();
    }

    public static final Parcelable.Creator<Permissions> CREATOR = new Parcelable.Creator<Permissions>() {
        @Override
        public Permissions createFromParcel(Parcel source) {
            return new Permissions(source);
        }

        @Override
        public Permissions[] newArray(int size) {
            return new Permissions[size];
        }
    };
}