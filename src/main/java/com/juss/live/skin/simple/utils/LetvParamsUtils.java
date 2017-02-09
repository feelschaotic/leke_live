package com.juss.live.skin.simple.utils;

import android.os.Bundle;

import com.letv.controller.PlayProxy;
import com.letv.universal.iplay.EventPlayProxy;

/**
 * 乐视云服务参数设置帮助类
 *
 * @author pys
 */
public class LetvParamsUtils {
    public static final String IS_LOCAL_PANO = "local_pano";

    /**
     * 乐视运直播参数设置
     *
     * @param streamId    直播流ＩＤ，和直播ID二选一
     * @param liveId      直播ID，和直播流ID二选一
     * @param isHls       非必须，是不是HIS协议。默认是RTMP协议
     * @param isLetv      非必须，可选参数，当前视频是不是乐视视频
     * @return
     */
    public static Bundle setLiveParams(String streamId, String liveId, boolean isHls, boolean isLetv) {

        Bundle mBundle = new Bundle();
        mBundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_LIVE);

        /**
         * PlayProxy.PLAY_STREAMID和PlayProxy.PLAY_LIVEID参数说明：两者二选一传入就行
         * 用户申请使用的直播业务中，如果申请使用的是streamID,则传入streamID,streamID示例：20150421300001210
         * 用户申请使用的直播业务中，如果申请使用的是liveID,则传入liveID，liveID示例：201504213000012
         */
        mBundle.putString(PlayProxy.PLAY_STREAMID, streamId);
        mBundle.putString(PlayProxy.PLAY_LIVEID, liveId);
        // mBundle.putBoolean(PlayProxy.PLAY_ISRTMP, rb1.isChecked());
        mBundle.putBoolean(PlayProxy.PLAY_USEHLS, isHls);
        mBundle.putBoolean(PlayProxy.PLAY_ISLETV, isLetv);
        return mBundle;
    }

    /**
     * 乐视云点播参数设置
     * 没有的数值传空字符串""
     *
     * @param uuid      必须
     * @param vuid      必须
     * @param checkCode 非必须，收费视频需要
     * @param userKey   非必须，收费视频需要
     * @return
     */
    public static Bundle setVodParams(String uuid, String vuid, String checkCode, String userKey, String playName) {
        Bundle mBundle = new Bundle();
        mBundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_VOD);
        mBundle.putString(PlayProxy.PLAY_UUID, uuid);
        mBundle.putString(PlayProxy.PLAY_VUID, vuid);
        mBundle.putString(PlayProxy.PLAY_CHECK_CODE, checkCode);
        mBundle.putString(PlayProxy.PLAY_PLAYNAME, playName);
        mBundle.putString(PlayProxy.PLAY_USERKEY, userKey);
       // mBundle.putString(PlayProxy.PLAY_SERVICE_NUMBER, "业务线");
       // mBundle.putString(PlayProxy.PLAY_CUSTOMERID, "用户id");
        return mBundle;
    }

    /**
     * 乐视云活动直播参数设置
     *
     * @param actionId 直播活动ID
     * @param useHls   非必须，当前是HIS协议还是RTMP协议，默认RTMP协议
     * @return
     */
    public static Bundle setActionLiveParams(String actionId, boolean useHls) {
        Bundle mBundle = new Bundle();
        mBundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_ACTION_LIVE);
        mBundle.putString(PlayProxy.PLAY_ACTIONID, actionId);
//        mBundle.putBoolean(PlayProxy.PLAY_ISRTMP, rb1.isChecked());
        mBundle.putBoolean(PlayProxy.PLAY_USEHLS, useHls);
       // mBundle.putString(PlayProxy.PLAY_SERVICE_NUMBER, "业务线");
       // mBundle.putString(PlayProxy.PLAY_CUSTOMERID, "用户id");
        return mBundle;
    }

}
