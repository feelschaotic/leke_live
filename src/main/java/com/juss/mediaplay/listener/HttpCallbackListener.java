package com.juss.mediaplay.listener;

/**
 * Created by lenovo on 2016/7/18.
 */
public interface HttpCallbackListener {
    void onSucc(String response);
    void onError(Exception e);
}
