package com.ramo.campuslive.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ramo on 2016/7/6.
 */
public class Guessing implements Parcelable{
    private String title;
    private Date time;
    private Date stop_time;
    private String result;
    private List<GuessingOptions> optionses =new ArrayList<>();
    private User banker;
    private List<User> punters =new ArrayList<>();

    public Guessing(){
        super();
    }
    protected Guessing(Parcel in) {
        title = in.readString();
        time= (Date) in.readSerializable();
        stop_time= (Date) in.readSerializable();
        result = in.readString();
        optionses = in.createTypedArrayList(GuessingOptions.CREATOR);
        banker = in.readParcelable(User.class.getClassLoader());
        punters = in.createTypedArrayList(User.CREATOR);
    }

    public static final Creator<Guessing> CREATOR = new Creator<Guessing>() {
        @Override
        public Guessing createFromParcel(Parcel in) {
            return new Guessing(in);
        }

        @Override
        public Guessing[] newArray(int size) {
            return new Guessing[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getStop_time() {
        return stop_time;
    }

    public void setStop_time(Date stop_time) {
        this.stop_time = stop_time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<GuessingOptions> getOptionses() {
        return optionses;
    }

    public void setOptionses(List<GuessingOptions> optionses) {
        this.optionses = optionses;
    }

    public User getBanker() {
        return banker;
    }

    public void setBanker(User banker) {
        this.banker = banker;
    }

    public List<User> getPunters() {
        return punters;
    }

    public void setPunters(List<User> punters) {
        this.punters = punters;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeSerializable(time);
        dest.writeSerializable(stop_time);
        dest.writeString(result);
        dest.writeTypedList(optionses);
        dest.writeParcelable(banker, flags);
        dest.writeTypedList(punters);
    }
}
