package com.juss.mediaplay.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.juss.mediaplay.entity.VideoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/6/10.
 */
public class ScanUtil {
    public List<VideoItem> videoItems;
    private Context content;
    public ScanUtil(Context content){
        this.content=content;
    }
    public List<VideoItem> getVideoList(){
        videoItems = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection ={MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATA};
        Cursor cursor = content.getContentResolver().query(uri,projection,null,null,null);
        while (cursor.moveToNext()){
            VideoItem videoItem = new VideoItem();
            String name=cursor.getString(0);
            videoItem.setName(name);
            long duration = cursor.getLong(1);
            videoItem.setDuration(duration);
            long size = cursor.getLong(2);
            videoItem.setSize(size);
            String data = cursor.getString(3);
            videoItem.setData(data);
            videoItems.add(videoItem);
        }
        cursor.close();
        return videoItems;
    }
}
