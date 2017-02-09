package com.juss.live.skin.simple.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceHolder;

import com.lecloud.dispatcher.play.entity.Config;
import com.lecloud.dispatcher.state.LiveStatusManager;
import com.lecloud.entity.LiveStatusCallback;
import com.lecloud.leutils.LeLog;
import com.letv.controller.LetvPlayer;
import com.letv.controller.PlayContext;
import com.letv.universal.iplay.ISplayer;
import com.letv.universal.iplay.OnPlayStateListener;

public class PlayerAssistant {

    private static final String TAG = "PlayerAssistant";

    /**
     * 检测活动直播状态信息
     * 
     * @param player
     * @param liveStateCallback
     */
    public static void checkActionLiveStatus(ISplayer player, LiveStatusCallback liveStateCallback) {
        PlayContext context = (PlayContext) player.getPlayContext();
        if (context != null && context.getActionInfo() != null) {
            LiveStatusManager.getActiveLiveStatus(context.getActionInfo().getActivityId(), context.getLiveId(), context.getStreamId(), context.getContext(), context.getJsProxy(), liveStateCallback);
        }
    }

    /**
     * 活动直播时，创建小播放器
     */
    public static ISplayer createActionLivePlayer(Context context, SurfaceHolder holder, String path, final OnPlayStateListener playStateListener) {
        PlayContext tmPlayContext = new PlayContext(context);
        tmPlayContext.setUsePlayerProxy(false);
        final ISplayer mISplayer = new LetvPlayer();
        mISplayer.setPlayContext(tmPlayContext);
        mISplayer.init();
        mISplayer.setOnPlayStateListener(new OnPlayStateListener() {
            @Override
            public void videoState(int state, Bundle bundle) {
                if (playStateListener != null) {
                    playStateListener.videoState(state, bundle);
                }
                switch (state) {
                case ISplayer.MEDIA_EVENT_PREPARE_COMPLETE:
                    mISplayer.setVolume(0, 0);
                    mISplayer.start();
                    break;
                case ISplayer.MEDIA_EVENT_PLAY_COMPLETE:
                    mISplayer.resetPlay(ISplayer.PLAYER_REPLAY);
                    break;
                }
            }
        });
        mISplayer.setDisplay(holder.getSurface());
        mISplayer.setDataSource(path);
        mISplayer.prepareAsync();
        return mISplayer;
    }

    public static long loadLastPosition(PlayContext playContext, String uuid, String vuid) {
        VideoDBHelper dbHelper = VideoDBHelper.getInstance(playContext.getContext());
        //续播表字段  去数据库查询uuid,vuid对应的在续播表里是否有记录 ，查询出播放时间，播放码率
        Config mConfig = dbHelper.getVideoConfig(uuid, vuid);
        if (mConfig != null) {
            LeLog.d(TAG,"seekTime:"+mConfig.getSeek());
            playContext.setLastPosition(mConfig.getSeek());
            return mConfig.getSeek();
        }
        return 0;
    }

    public static void saveLastPosition(Context mContext, String uuid, String vuid, int position, int defination) {
        VideoDBHelper dbHelper = VideoDBHelper.getInstance(mContext);
        dbHelper.setVideoConfig(uuid, vuid, position, defination + "");
        LeLog.d(TAG,"uuid:" + uuid + ",vuid:" + vuid + ",position:" + position + ",defination:" + defination);
    }

}
