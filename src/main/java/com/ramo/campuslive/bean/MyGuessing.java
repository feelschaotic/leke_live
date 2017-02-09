package com.ramo.campuslive.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by ramo on 2016/7/6.
 */
public class MyGuessing implements Parcelable {
    private Date time;
    private float money;
    private GuessingOptions guessingOptions;
    private Guessing guessing;

    public MyGuessing() {
        super();
    }

    protected MyGuessing(Parcel in) {
        time = (Date) in.readSerializable();
        money = in.readFloat();
        guessingOptions = in.readParcelable(GuessingOptions.class.getClassLoader());
        guessing = in.readParcelable(Guessing.class.getClassLoader());
    }

    public static final Creator<MyGuessing> CREATOR = new Creator<MyGuessing>() {
        @Override
        public MyGuessing createFromParcel(Parcel in) {
            return new MyGuessing(in);
        }

        @Override
        public MyGuessing[] newArray(int size) {
            return new MyGuessing[size];
        }
    };

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(time);
        dest.writeFloat(money);
        dest.writeParcelable(guessingOptions, flags);
        dest.writeParcelable(guessing, flags);
    }

    public GuessingOptions getGuessingOptions() {
        return guessingOptions;
    }

    public void setGuessingOptions(GuessingOptions guessingOptions) {
        this.guessingOptions = guessingOptions;
    }

    public Guessing getGuessing() {
        return guessing;
    }

    public void setGuessing(Guessing guessing) {
        this.guessing = guessing;
    }
}
