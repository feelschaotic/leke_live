package com.juss.mediaplay.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by lenovo on 2016/7/26.
 */
public class GsonDateUtil {
    private Gson gson;
    public Gson getGson(){
        gson = new Gson();
        return gson;
    }
    public Gson getGsonDate(){
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson;
    }
}
