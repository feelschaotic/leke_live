package com.juss.mediaplay.entity;

/**
 * Created by lenovo on 2016/6/10.
 */
public class VideoItem {
    /**
     * 视频名称
     */
    private String name;
    /**
     * 视频的总时长：毫秒
     */
    private long duration;

    /**
     * 视频的文件大小
     */
    private long size;
    /**
     * 视频路径
     */
    private String data;

    public void setData(String data) {
        this.data = data;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public long getSize() {
        return size;
    }

    public String getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "name="+name +"dratuion="+duration+"size="+size+"   data = "+data;
    }
}
