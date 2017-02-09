package com.juss.mediaplay.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.juss.mediaplay.po.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/7/22.
 */
public class ProvetSchoolJson implements Parcelable {
    private List<Province> list;
    private int totalpage;
    private int pagesize;
    private int totalrecord;
    private int startindex;
    private int pagenum;
    private int startpage;
    private int endpage;

    public int getEndpage() {
        return endpage;
    }

    public int getPagenum() {
        return pagenum;
    }

    public int getPagesize() {
        return pagesize;
    }

    public int getStartindex() {
        return startindex;
    }

    public int getStartpage() {
        return startpage;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public int getTotalrecord() {
        return totalrecord;
    }

    public List<Province> getList() {
        return list;
    }

    public void setEndpage(int endpage) {
        this.endpage = endpage;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public void setList(List<Province> list) {
        this.list = list;
    }

    public void setStartindex(int startindex) {
        this.startindex = startindex;
    }

    public void setStartpage(int startpage) {
        this.startpage = startpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public void setTotalrecord(int totalrecord) {
        this.totalrecord = totalrecord;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.list);
        dest.writeInt(this.totalpage);
        dest.writeInt(this.pagesize);
        dest.writeInt(this.totalrecord);
        dest.writeInt(this.startindex);
        dest.writeInt(this.pagenum);
        dest.writeInt(this.startpage);
        dest.writeInt(this.endpage);
    }

    public ProvetSchoolJson() {
    }

    protected ProvetSchoolJson(Parcel in) {
        this.list = new ArrayList<Province>();
        in.readList(this.list, Province.class.getClassLoader());
        this.totalpage = in.readInt();
        this.pagesize = in.readInt();
        this.totalrecord = in.readInt();
        this.startindex = in.readInt();
        this.pagenum = in.readInt();
        this.startpage = in.readInt();
        this.endpage = in.readInt();
    }

    public static final Parcelable.Creator<ProvetSchoolJson> CREATOR = new Parcelable.Creator<ProvetSchoolJson>() {
        @Override
        public ProvetSchoolJson createFromParcel(Parcel source) {
            return new ProvetSchoolJson(source);
        }

        @Override
        public ProvetSchoolJson[] newArray(int size) {
            return new ProvetSchoolJson[size];
        }
    };
}
