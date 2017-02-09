package com.juss.live.skin.skin.v4;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juss.live.skin.skin.BaseView;
import com.juss.live.skin.skin.utils.StatusUtils;
import com.juss.live.skin.skin.utils.UIPlayContext;
import com.juss.live.skin.skin.widget.MarqueeTextView;
import com.lecloud.leutils.ReUtils;
import com.letv.controller.interfacev1.ISplayerController;
import com.letv.universal.iplay.ISplayer;
import com.letv.universal.notice.UIObserver;

import java.util.Observable;

/**
 * 顶部浮层
 * 
 * @author pys
 *
 */
public class V4TopTitleView extends BaseView implements UIObserver {

    private MarqueeTextView textView;
    private ImageView netStateView;
    private ImageView batteryView;
    private TextView timeView;
    private ImageView videoLock;
    /**
     * lockFlag 为false 表示不加锁
     */
    private boolean lockFlag = false;

    public V4TopTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4TopTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4TopTitleView(Context context) {
        super(context);
    }

    @Override
    protected void initPlayer() {
        player.attachObserver(this);
        setState();
    }

    @Override
    public void attachUIContext(UIPlayContext playContext) {
        super.attachUIContext(playContext);
        if (uiPlayContext != null) {
            if (uiPlayContext.isPlayingAd()) {
                setVisibility(View.GONE);
            } else {
                setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 设置状态栏
     */
    private void setState() {
        /**
         * 播放器可用的时候试着获取标题
         */
        if (uiPlayContext != null) {
            /**
             * 设置标题
             */
            String title = uiPlayContext.getVideoTitle();
            if (title != null) {
                textView.setText(title);
            }

            /**
             * 获取网络状态
             */
            netStateView.setImageLevel(StatusUtils.getWiFistate(context));
            /**
             * 电池
             */
            batteryView.setImageLevel(StatusUtils.getBatteryStatus(context));
            /**
             * 时间
             */
            timeView.setText(StatusUtils.getCurrentTime(context));
        }
    }

    @Override
    protected void initView(final Context context) {
        LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_skin_v4_top_layout"), this);
        videoLock = (ImageView) findViewById(ReUtils.getId(context, "iv_video_lock"));
        /**
         * 返回键
         */
        findViewById(ReUtils.getId(context, "full_back")).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uiPlayContext != null) {
                    /**
                     * 返回键是否恢复竖屏状态
                     */
                    if (uiPlayContext.isReturnback()) {
                        if (player != null) {
                            // 返回竖屏状态
                            player.setScreenResolution(ISplayerController.SCREEN_ORIENTATION_USER_PORTRAIT);
                        }
                    } else {
                        ((Activity) context).finish();
                    }
                }
            }
        });
        
        videoLock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lockFlag = !lockFlag;
                if (lockFlag) {
                    videoLock.setImageResource(ReUtils.getDrawableId(context, "letv_skin_v4_large_mult_live_action_lock"));
                } else {
                    videoLock.setImageResource(ReUtils.getDrawableId(context, "letv_skin_v4_large_mult_live_action_unlock"));
                }
                uiPlayContext.setLockFlag(lockFlag);
            }
        });
        
        textView = (MarqueeTextView) findViewById(ReUtils.getId(context, "full_title"));
        netStateView = (ImageView) findViewById(ReUtils.getId(context, "full_net"));
        batteryView = (ImageView) findViewById(ReUtils.getId(context, "full_battery"));
        timeView = (TextView) findViewById(ReUtils.getId(context, "full_time"));
    }

    @Override
    public void update(Observable observable, Object data) {
        Bundle bundle = (Bundle) data;
        switch (bundle.getInt("state")) {
        case ISplayer.MEDIA_EVENT_PREPARE_COMPLETE:
            setState();
            break;

        default:
            break;
        }
    }

}
