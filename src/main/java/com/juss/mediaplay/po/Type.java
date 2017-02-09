package com.juss.mediaplay.po;

import android.os.Parcel;
import android.os.Parcelable;

public class Type implements Parcelable {
    private Integer typeId;

    private String typeName;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.typeId);
        dest.writeString(this.typeName);
    }

    public Type() {
    }

    protected Type(Parcel in) {
        this.typeId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.typeName = in.readString();
    }

    public static final Parcelable.Creator<Type> CREATOR = new Parcelable.Creator<Type>() {
        @Override
        public Type createFromParcel(Parcel source) {
            return new Type(source);
        }

        @Override
        public Type[] newArray(int size) {
            return new Type[size];
        }
    };
}