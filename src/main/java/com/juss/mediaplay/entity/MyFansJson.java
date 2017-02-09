package com.juss.mediaplay.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.juss.mediaplay.po.UserFans;

import java.util.List;

/**
 * Created by lenovo on 2016/8/4.
 */
public class MyFansJson implements Parcelable {
    private List<UserFans> list;
    private int totalpage;
    private int pagesize;
    private int totalrecord;
    private int startindex;
    private int pagenum;
    private int startpage;
    private int endpage;

    public List<UserFans> getList() {
        return list;
    }

    public void setList(List<UserFans> list) {
        this.list = list;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getTotalrecord() {
        return totalrecord;
    }

    public void setTotalrecord(int totalrecord) {
        this.totalrecord = totalrecord;
    }

    public int getStartindex() {
        return startindex;
    }

    public void setStartindex(int startindex) {
        this.startindex = startindex;
    }

    public int getPagenum() {
        return pagenum;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public int getStartpage() {
        return startpage;
    }

    public void setStartpage(int startpage) {
        this.startpage = startpage;
    }

    public int getEndpage() {
        return endpage;
    }

    public void setEndpage(int endpage) {
        this.endpage = endpage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.list);
        dest.writeInt(this.totalpage);
        dest.writeInt(this.pagesize);
        dest.writeInt(this.totalrecord);
        dest.writeInt(this.startindex);
        dest.writeInt(this.pagenum);
        dest.writeInt(this.startpage);
        dest.writeInt(this.endpage);
    }

    public MyFansJson() {
    }

    protected MyFansJson(Parcel in) {
        this.list = in.createTypedArrayList(UserFans.CREATOR);
        this.totalpage = in.readInt();
        this.pagesize = in.readInt();
        this.totalrecord = in.readInt();
        this.startindex = in.readInt();
        this.pagenum = in.readInt();
        this.startpage = in.readInt();
        this.endpage = in.readInt();
    }

    public static final Creator<MyFansJson> CREATOR = new Creator<MyFansJson>() {
        @Override
        public MyFansJson createFromParcel(Parcel source) {
            return new MyFansJson(source);
        }

        @Override
        public MyFansJson[] newArray(int size) {
            return new MyFansJson[size];
        }
    };
}
