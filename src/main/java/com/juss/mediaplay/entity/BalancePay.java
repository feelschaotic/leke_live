package com.juss.mediaplay.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by lenovo on 2016/7/26.
 */
public class BalancePay implements Parcelable {
    /*
    * recordDate":"2016-07-12 16:29:11","recordNum":50.0,"balanceState":1,"introduction":"充值"*/
    private Date recordDate;
    private float recordNum;
    private int balanceState;
    private String introduction;

    protected BalancePay(Parcel in) {
        recordDate= (Date) in.readSerializable();
        recordNum = in.readFloat();
        balanceState = in.readInt();
        introduction = in.readString();
    }

    public static final Creator<BalancePay> CREATOR = new Creator<BalancePay>() {
        @Override
        public BalancePay createFromParcel(Parcel in) {
            return new BalancePay(in);
        }

        @Override
        public BalancePay[] newArray(int size) {
            return new BalancePay[size];
        }
    };

    public Date getRecordDate() {
        return recordDate;
    }

    public float getRecordNum() {
        return recordNum;
    }

    public int getBalanceState() {
        return balanceState;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public void setRecordNum(float recordNum) {
        this.recordNum = recordNum;
    }

    public void setBalanceState(int balanceState) {
        this.balanceState = balanceState;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(recordDate);
        dest.writeFloat(recordNum);
        dest.writeInt(balanceState);
        dest.writeString(introduction);
    }
}
