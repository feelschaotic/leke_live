package com.juss.mediaplay.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.juss.mediaplay.po.Province;

import java.util.List;

/**
 * Created by lenovo on 2016/7/22.
 */
public class BalancePayJson implements Parcelable {
    private List<BalancePay> list;
    private int totalpage;
    private int pagesize;
    private int totalrecord;
    private int startindex;
    private int pagenum;
    private int startpage;
    private int endpage;

    protected BalancePayJson(Parcel in) {
        list = in.createTypedArrayList(BalancePay.CREATOR);
        totalpage = in.readInt();
        pagesize = in.readInt();
        totalrecord = in.readInt();
        startindex = in.readInt();
        pagenum = in.readInt();
        startpage = in.readInt();
        endpage = in.readInt();
    }

    public static final Creator<BalancePayJson> CREATOR = new Creator<BalancePayJson>() {
        @Override
        public BalancePayJson createFromParcel(Parcel in) {
            return new BalancePayJson(in);
        }

        @Override
        public BalancePayJson[] newArray(int size) {
            return new BalancePayJson[size];
        }
    };

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

    public List<BalancePay> getList() {
        return list;
    }

    public int getTotalrecord() {
        return totalrecord;
    }

    public void setList(List<BalancePay> list) {
        this.list = list;
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
        dest.writeTypedList(list);
        dest.writeInt(totalpage);
        dest.writeInt(pagesize);
        dest.writeInt(totalrecord);
        dest.writeInt(startindex);
        dest.writeInt(pagenum);
        dest.writeInt(startpage);
        dest.writeInt(endpage);
    }
}
