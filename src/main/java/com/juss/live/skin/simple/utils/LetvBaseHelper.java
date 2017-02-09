package com.juss.live.skin.simple.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.juss.live.skin.skin.interfacev1.IActionCallback;
import com.juss.live.skin.skin.utils.UIPlayContext;
import com.juss.live.skin.skin.v4.V4PlaySkin;
import com.lecloud.download.control.DownloadCenter;
import com.lecloud.entity.ActionInfo;
import com.lecloud.entity.LiveInfo;
import com.lecloud.entity.LiveStatus;
import com.letv.controller.LetvPlayer;
import com.letv.controller.PlayContext;
import com.letv.controller.PlayProxy;
import com.letv.controller.imp.LetvPlayerControllerImp;
import com.letv.controller.interfacev1.ILetvPlayerController;
import com.letv.universal.iplay.EventPlayProxy;
import com.letv.universal.iplay.ISplayer;
import com.letv.universal.iplay.OnPlayStateListener;
import com.letv.universal.play.util.PlayerParamsHelper;
import com.letv.universal.widget.ReSurfaceView;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by heyuekuai on 16/3/25.
 */
public class LetvBaseHelper {
    private static final boolean USE_PLAYER_PROXY = true;

    protected Context mContext;
    private String path = "";
    private Bundle mBundle;
    private boolean isContinue = true;
    private int skinBuildType;
    private UIPlayContext uicontext;
    protected V4PlaySkin skin;
    private int playMode;
    private long lastPosition;
    protected ISplayer player;
    protected PlayContext playContext;
    protected SurfaceView videoView;
    private ILetvPlayerController playerController;

    public void init(Context mContext, Bundle mBundle, V4PlaySkin skin) {
        this.mContext = mContext;
        this.skin = skin;
        this.mBundle = mBundle;
        playMode = mBundle.getInt(PlayProxy.PLAY_MODE, -1);
        switch (playMode) {
            case EventPlayProxy.PLAYER_VOD:
                skinBuildType = V4PlaySkin.SKIN_TYPE_A;
                break;
            case EventPlayProxy.PLAYER_LIVE:
                skinBuildType = V4PlaySkin.SKIN_TYPE_B;
                break;
            case EventPlayProxy.PLAYER_ACTION_LIVE:
                skinBuildType = V4PlaySkin.SKIN_TYPE_C;
                break;
            default:
                break;
        }
        path = mBundle.getString("path");
        initPlayContext();
        initPlayerSkin();
        if (TextUtils.isEmpty(path) && playMode == EventPlayProxy.PLAYER_VOD) {
            //如果在续播表里有记录，则得到上次播放的位置，如果没有，则返回0
            PlayerAssistant.loadLastPosition(playContext, mBundle.getString(PlayProxy.PLAY_UUID), mBundle.getString(PlayProxy.PLAY_VUID));
            /**
             * 1、初始化下载模块
             * 2、加入下载队列
             * 3、下载过程中各种状态 进行回调（interface LeDownloadObserver）
             * */
            initDownload();
        }
    }

    /**
     * 1、创建jsProxy
     * 2、设置皮肤view
     * 3、设置播放代理true
     * 4、把playContext传递给playerController中
     */
    private void initPlayContext() {
        playContext = new PlayContext(mContext);
        playContext.setVideoContainer(skin);
        playContext.setUsePlayerProxy(USE_PLAYER_PROXY);
        playerController = new LetvPlayerControllerImp();
        playerController.setPlayContext(playContext);
    }

    /**
     * 初始化播放器皮肤
     */
    private void initPlayerSkin() {
        if (playMode == EventPlayProxy.PLAYER_ACTION_LIVE) {// 活动直播
            PlayerSkinFactory.initActionLivePlaySkin(skin, V4PlaySkin.SKIN_TYPE_C, mBundle.getString(PlayProxy.PLAY_ACTIONID), new IActionCallback() {
                @Override
                public void switchMultLive(String liveId) {
                    ((LetvPlayer) player).switchMultLive(liveId);
                }

                @Override
                public ISplayer createPlayerCallback(SurfaceHolder holder, String path, OnPlayStateListener playStateListener) {
                    return PlayerAssistant.createActionLivePlayer(mContext, holder, path, playStateListener);
                }
            });
        } else {
            PlayerSkinFactory.initPlaySkin(skin, skinBuildType);
        }
        /**
         * 获取皮肤的上下文
         */
        uicontext = skin.getUIPlayContext();
    }

    /**
     * 初始化下载模块
     */
    private void initDownload() {
        final String uuid = mBundle.getString(PlayProxy.PLAY_UUID);
        final String vuid = mBundle.getString(PlayProxy.PLAY_VUID);
        final DownloadCenter downloadCenter = DownloadCenter.getInstances(mContext);
        if (downloadCenter != null && downloadCenter.isDownloadCompleted(vuid)) {
            path = downloadCenter.getDownloadFilePath(vuid);
        }
        skin.setOnDownloadClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 1、是否显示下载过程
                 * 2、讲当前播放码率设置
                 * 3、根据uuid,vuid去下载(调用下载模块，下载时候需要传递参数有4个userkey(可有可无),uuid,vuid,rateText码率)
                 * */
                downloadCenter.allowShowMsg(false);
                downloadCenter.setDownloadRateText(playContext.getDefinationIdByType(uicontext.getCurrentRateType()));
                downloadCenter.downloadVideo("", uuid, vuid);

            }
        });
    }

//    private void savePamaraters() {
//        SharedPreferenceUtil.getInstance(mContext).putInt("waterMarkMinTime", 200);
//        SharedPreferenceUtil.getInstance(mContext).putInt("waterMarkMaxTime", 700);
//        SharedPreferenceUtil.getInstance(mContext).putInt("preCacheTime", 500);
//        SharedPreferenceUtil.getInstance(mContext).putInt("maxCacheTime", 10000);
//        SharedPreferenceUtil.getInstance(mContext).putInt("maxDelayTime", 1000);
//    }

    /**
     * 创建一个新的播放器
     *
     * @param surface
     */
    protected void createOnePlayer(Surface surface) {
        if (mBundle != null) {
            mBundle.putInt("waterMarkMinTime", SharedPreferenceUtil.getInstance(mContext).getInt("waterMarkMinTime",200));
            mBundle.putInt("waterMarkMaxTime", SharedPreferenceUtil.getInstance(mContext).getInt("waterMarkMaxTime",800));
            mBundle.putInt("preCacheTime", SharedPreferenceUtil.getInstance(mContext).getInt("preCacheTime",500));
            mBundle.putInt("maxCacheTime", SharedPreferenceUtil.getInstance(mContext).getInt("maxCacheTime",10000));
            mBundle.putInt("maxDelayTime", SharedPreferenceUtil.getInstance(mContext).getInt("maxDelayTime",1000));
            mBundle.putInt("playMode", playMode);
        }
        player = PlayerFactory.createOnePlayer(playContext, mBundle, playStateListener, surface);

        /**
         * 皮肤关联播放器
         */
        playerController.setPlayer(player);
        skin.registerController(playerController);

        if (!TextUtils.isEmpty(path)) {
            playContext.setUsePlayerProxy(false);
        }
        player.setDataSource(path);
        if (lastPosition > 0 && mBundle.getInt(PlayProxy.PLAY_MODE) == EventPlayProxy.PLAYER_VOD) {
            player.seekTo(lastPosition);
        }
        player.prepareAsync();
    }

    // surfaceView生命周期
    protected SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            stopAndRelease();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (player == null) {
                createOnePlayer(holder.getSurface());
            } else {
                player.setDisplay(holder.getSurface());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (player != null) {
                PlayerParamsHelper.setViewSizeChange(player, width, height);
            }
        }
    };

    /**
     * 处理播放器的回调事件
     */
    protected OnPlayStateListener playStateListener = new OnPlayStateListener() {
        @Override
        public void videoState(int state, Bundle bundle) {
            handleADState(state, bundle);// 处理广告类事件
            handleVideoInfoState(state, bundle);// 处理视频信息事件
            handlePlayState(state, bundle);// 处理播放器类事件
            handleLiveState(state, bundle);// 处理直播类事件
        }
    };

    /**
     * 处理直播相关信息
     *
     * @param state
     * @param bundle
     */
    private void handleLiveState(int state, Bundle bundle) {
        switch (state) {
            case EventPlayProxy.PROXY_WATING_SELECT_ACTION_LIVE_PLAY:
                /**
                 * 活动直播
                 */
                PlayContext playContextAction = (PlayContext) player.getPlayContext();
                ActionInfo actionInfo = playContextAction.getActionInfo();
                if (actionInfo != null) {
                    // 皮肤层设置活动信息
                    uicontext.setActionInfo(actionInfo);
                    /**
                     * 如果当前活动直播是多个直播窗口， 选择一路可用的活动直播
                     */
                    LiveInfo liveInfo = actionInfo.getFirstCanPlayLiveInfo();
                    if (liveInfo != null) {
                        playContextAction.setLiveId(liveInfo.getLiveId());
                    }
                    // 活动直播中如果包含多个live信息 播放途中调用 LetvPlayer 中
                    // switchMultLive（liveId）切换播放
                }
                break;
            case EventPlayProxy.PROXY_SET_ACTION_LIVE_CURRENT_LIVE_ID:
                // 当前播放的活动直播的liveID
                uicontext.setMultCurrentLiveId(bundle.getString("liveId"));
                break;

            default:
                break;
        }
    }

    /**
     * 处理视频信息
     *
     * @param state
     * @param bundle
     */
    private void handleVideoInfoState(int state, Bundle bundle) {
        switch (state) {
            case EventPlayProxy.PROXY_WAITING_SELECT_DEFNITION_PLAY:
                /**
                 * 皮肤获取码率
                 */
                PlayContext playContext = (PlayContext) player.getPlayContext();
                if (playContext != null) {
                    uicontext.setRateTypeItems(playContext.getDefinationsMap());
                }

                /**
                 * 获取码率
                 */
                Map<Integer, String> definationsMap = playContext.getDefinationsMap();
                Iterator<Map.Entry<Integer, String>> it = definationsMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Integer, String> next = it.next();
                    next.getKey();
                    next.getValue();
                }

                break;
            case EventPlayProxy.PROXY_VIDEO_INFO_REFRESH:
                if (uicontext == null || player == null || player.getPlayContext() == null) {
                    return;
                }
                PlayContext mPlayContext = (PlayContext) player.getPlayContext();
                uicontext.setVideoTitle(mPlayContext.getVideoTitle());
                uicontext.setDownloadable(mPlayContext.isCanbeDownload());
                break;
        }
    }

    /**
     * 处理广告事件
     *
     * @param state
     * @param bundle
     */
    private void handleADState(int state, Bundle bundle) {
        switch (state) {
            case EventPlayProxy.PLAYER_PROXY_AD_START:
                uicontext.setIsPlayingAd(true);// 广告播放时间
                break;
            case EventPlayProxy.PLAYER_PROXY_AD_END:
                uicontext.setIsPlayingAd(false);
                break;
            default:
                break;
        }
    }

    /**
     * 处理播放器事件
     *
     * @param state
     * @param bundle
     */
    protected void handlePlayState(int state, Bundle bundle) {
        switch (state) {
            case ISplayer.MEDIA_EVENT_PREPARE_COMPLETE:
                // TODO 获取当前播放的码率
                uicontext.setCurrentRateType(playContext.getCurrentDefinationType());
                if (uicontext.isClickPauseByUser() && player != null) {
                    player.setVolume(0, 0);
                }
                if (player != null) {
                    player.start();
                }
                break;
            case ISplayer.MEDIA_EVENT_FIRST_RENDER:
                if (renderCallback != null) {
                    renderCallback.onRender();
                }
                if (uicontext.isClickPauseByUser() && player != null) {
                    player.pause();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            player.setVolume(1, 1);
                        }
                    }, 300);
                }
                break;
            case ISplayer.MEDIA_EVENT_VIDEO_SIZE:
                if (videoView != null && player != null && videoView instanceof ReSurfaceView) {
                    ((ReSurfaceView) videoView).onVideoSizeChange(player.getVideoWidth(), player.getVideoHeight());
                }
                break;
            case EventPlayProxy.PLAYER_ACTION_LIVE_STATUS:
                int status = bundle.getInt("status", -1);
                if (status != -1) {
                    if (status == LiveStatus.STATUS_END) {
                        //直播结束
                    } else if (status == LiveStatus.STATUS_INTERRUPTED) {
                        //直播中断
                    } else if (status == LiveStatus.SATUS_NOT_START) {
                        //直播未开始
                    } else if (status == LiveStatus.STATUS_LIVE_ING) {
                        //直播进行中
                    }
                }
                break;
            default:
                break;
        }
    }


    public void onResume() {
        if (skin != null) {
            skin.onResume();
        }
        if (player != null && playContext.getErrorCode() == -1) {
            player.start();
        }
    }

    public void onPause() {
        if (isContinue && skinBuildType == V4PlaySkin.SKIN_TYPE_A && player != null && (int) player.getCurrentPosition() > 0) {
            PlayerAssistant.saveLastPosition(mContext, mBundle.getString(PlayProxy.PLAY_UUID), mBundle.getString(PlayProxy.PLAY_VUID), (int) (player.getCurrentPosition()),
                    playContext.getCurrentDefinationType());
        }

        if (skin != null) {
            skin.onPause();
        }
        if (player != null) {
            player.pause();
        }
    }

    public void onDestroy() {
        stopAndRelease();
        if (skin != null) {
            skin.onDestroy();
        }
        if (playContext != null) {
            playContext.destory();
        }
    }


    public void onConfigurationChanged(Configuration newConfig) {
        if (videoView != null && videoView instanceof ReSurfaceView) {
            ((ReSurfaceView) videoView).setVideoLayout(-1, 0);
        }
    }

    /**
     * 停止播放，并且记录最后播放时间
     */
    public void stopAndRelease() {
        if (player != null) {
            lastPosition = player.getCurrentPosition();
            player.stop();
            player.reset();
            player.release();
            player = null;
        }
    }


    public ISplayer getPlayer() {
        return player;
    }

    public PlayerRenderCallback renderCallback;

    public interface PlayerRenderCallback {
        void onRender();
    }
}
