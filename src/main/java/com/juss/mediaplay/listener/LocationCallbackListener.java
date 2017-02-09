package com.juss.mediaplay.listener;

/**
 * Created by lenovo on 2016/7/21.
 */
public interface LocationCallbackListener {
    /**
     * 定位成功返回省或市
     * @param proder
     */
    public void getResult(String proder);

    /**
     * 定位失败返回错误
     * @param error
     */
    public void error(String error);

}
