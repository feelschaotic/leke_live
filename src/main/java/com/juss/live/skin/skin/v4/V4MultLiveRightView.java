package com.juss.live.skin.skin.v4;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juss.live.skin.skin.BaseViewGourp;
import com.juss.live.skin.skin.interfacev1.IActionCallback;
import com.juss.live.skin.skin.utils.ScreenUtils;
import com.juss.live.skin.skin.utils.UIPlayContext;
import com.lecloud.entity.LiveInfo;
import com.lecloud.leutils.ReUtils;
import com.letv.controller.interfacev1.ILetvPlayerController;
import com.letv.universal.iplay.IPlayer;
import com.letv.universal.iplay.ISplayer;
import com.letv.universal.iplay.OnPlayStateListener;
import com.letv.universal.widget.ReSurfaceView;
import com.letv.universal.widget.VideoViewSizeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 多路窗口
 * 
 * @author dengjiaping
 */

@TargetApi(Build.VERSION_CODES.ECLAIR)
public class V4MultLiveRightView extends BaseViewGourp implements UIPlayContext.MultLiveStateChangeCallback {

    public static char[] numArray = {'一', '二', '三', '四', '五', '六', '七', '八', '九'};
    public LinearLayout mMultLivelayout;
    public FrameLayout mMultParentLayout;
    public ImageView mMultLiveBtn;
    public boolean isShowSubLiveView = false;
    public boolean isFirstShowSubLive = true;
    /**
     * 这个是保存每次弹出的小视屏
     */
    public List<MultLivePlayHolder> actionPlays;
    public LinearLayout.LayoutParams multLiveParams;
    protected OnClickListener showMultLiveViewClick;
    /**
     * 需要参数
     */
    private int mCurrentIndex;
    private LinearLayout.LayoutParams multLiveViewParams;

    private ArrayList<ReSurfaceView> mVideoViews;
    private ArrayList<ISplayer> mISplayers;
    
    private IActionCallback mIActionCallback;

    public V4MultLiveRightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4MultLiveRightView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4MultLiveRightView(Context context) {
        super(context);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void initView(Context context) {
        LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_skin_v4_large_mult_live_layout"), this);
        actionPlays = new ArrayList<MultLivePlayHolder>();
        mVideoViews = new ArrayList<ReSurfaceView>();
        mISplayers = new ArrayList<ISplayer>();

        showMultLiveViewClick = new OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleSubMultLiveView();
            }
        };

        multLiveViewParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        multLiveViewParams.gravity = Gravity.CENTER;
        multLiveViewParams.setMargins(5, 5, 5, 5);
    }

    @Override
    protected void initPlayer() {

    }

    @Override
    protected void onAttachUIPlayControl(ILetvPlayerController playerControl) {
    }

    @Override
    protected void onAttachUIPlayContext(UIPlayContext uiPlayContext) {
    	if (uiPlayContext == null || uiPlayContext.getActionInfo() == null) {
            return;
        }
        List<LiveInfo> liveInfos = uiPlayContext.getActionInfo().getLiveInfos();
        if (liveInfos.size() <= 1) {
        	hideMultLiveLayout();
        }
    }

    @Override
    public void attachUIContext(UIPlayContext playContext) {
        super.attachUIContext(playContext);
        uiPlayContext.registerMultLiveStateChangeListener(this);
        initCurrentIndex(uiPlayContext.getMultCurrentLiveId());
    }

    protected void showMultLiveViewBtn() {
        mMultParentLayout = (FrameLayout) findViewById(ReUtils.getId(context, "mutl_live_lay"));
        mMultLivelayout = (LinearLayout) findViewById(ReUtils.getId(context, "floating_right_mutl_live_lay"));
        mMultLiveBtn = (ImageView) findViewById(ReUtils.getId(context, "mutl_live_btn"));

        showMultLiveLayout();
        mMultLiveBtn.setOnClickListener(showMultLiveViewClick);
    }

    public void toggleSubMultLiveView() {
        if (isShowSubLiveView) {
            mMultLivelayout.setVisibility(View.GONE);
            mMultLiveBtn.setImageResource(ReUtils.getDrawableId(context, "letv_skin_v4_large_mult_live_action_on"));
            isShowSubLiveView = false;
        } else {
            mMultLivelayout.setVisibility(View.VISIBLE);
            if (isFirstShowSubLive) {
                // addLiveView();
                addLiveView();
                isFirstShowSubLive = false;

            } else {
                mMultLivelayout.setVisibility(View.VISIBLE);
                // resumeMultLiveView();
            }
            mMultLiveBtn.setImageResource(ReUtils.getDrawableId(context, "letv_skin_v4_large_mult_live_action_off"));
            isShowSubLiveView = true;
        }
    }

    private void showMultLiveLayout() {
        mMultLiveBtn.setVisibility(View.VISIBLE);
        if (isShowSubLiveView) {
            mMultLivelayout.setVisibility(View.VISIBLE);
        } else {
            mMultLivelayout.setVisibility(View.GONE);
        }
    }

    private void hideMultLiveLayout() {
        mMultLivelayout.setVisibility(View.GONE);
        mMultLiveBtn.setVisibility(View.GONE);
    }

    /*
     * 是否显示多路子机位View
     */
    public void setVisiableActiveSubLiveView(boolean b) {
        if (b) {
            showMultLiveLayout();
        } else {
            hideMultLiveLayout();
        }
    }

    public void addLiveView() {

        if (uiPlayContext.getActionInfo() == null) {
            return;
        }
        List<LiveInfo> liveInfos = uiPlayContext.getActionInfo().getLiveInfos();
        if (liveInfos.size() <= 1) {
            return;
        }
        actionPlays.clear();
        mMultLivelayout.removeAllViews();

        for (int i = 0; i < liveInfos.size(); i++) {
            final MultLivePlayHolder holder = new MultLivePlayHolder();
            final String url = liveInfos.get(i).getPreviewStreamPlayUrl();

            final View layout = View.inflate(getContext(), ReUtils.getLayoutId(context, "letv_skin_v4_large_mult_live_action_sub_live_lay"), null);
            TextView title = (TextView) layout.findViewById(ReUtils.getId(context, "jiwei"));
            final FrameLayout framelayout = (FrameLayout) layout.findViewById(ReUtils.getId(context, "jiwei_lay"));
            final ProgressBar loading = (ProgressBar) layout.findViewById(ReUtils.getId(context, "pb_loading"));
            holder.loading = loading;

            initVideoView(framelayout, url, holder);
            holder.location = i;
            holder.url = url;
            title.setText("机位" + numArray[i]);

            holder.layout = layout;
            MyOnClickListener onClick = new MyOnClickListener();
            layout.setOnClickListener(onClick);
            //
            int width = (ScreenUtils.getWight(getContext()) - dip2px(getContext(), 40)) / 4;
            // int height = dip2px(getContext(), 135);
            multLiveParams = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
            mMultLivelayout.addView(layout, multLiveParams);

            holder.framelayout = framelayout;
            holder.textview = title;
            if (i == mCurrentIndex) {// 默认第一机位选 中
                actionSelected(holder, true);
            }
            actionPlays.add(holder);

            mVideoViews.get(i).getHolder().setFormat(PixelFormat.TRANSPARENT);
            mVideoViews.get(i).setZOrderOnTop(true);
        }
    }

    private void initVideoView(final ViewGroup rootView, final String url, final MultLivePlayHolder mMultLivePlayHolder) {
        final ReSurfaceView videoView = new ReSurfaceView(getContext());
        videoView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        videoView.setVideoContainer(rootView);
        rootView.addView(videoView, multLiveViewParams);
        videoView.getHolder().addCallback(new Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                stopAndRelease();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // createOnePlayer(holder, url);
                if (mIActionCallback != null) {
                    ISplayer mISplayer = mIActionCallback.createPlayerCallback(holder, url, new OnPlayStateListener() {

                        @Override
                        public void videoState(int state, Bundle bundle) {
                            Log.d("V4MultLiveLargeController", "-----state is : " + state);
                            switch (state) {
                                case IPlayer.MEDIA_ERROR_NO_STREAM:
                                case IPlayer.MEDIA_ERROR_DECODE_ERROR:
                                case IPlayer.MEDIA_ERROR_UNKNOWN:
                                    if (mMultLivePlayHolder.no_video_layout != null) {
                                        rootView.removeView(mMultLivePlayHolder.no_video_layout);
                                    }
                                    View view = View.inflate(context, ReUtils.getLayoutId(context, "letv_skin_v4_large_mult_live_action_no_video_layout"), null);
                                    rootView.addView(view, multLiveViewParams);
                                    mMultLivePlayHolder.videoState = false;
                                    mMultLivePlayHolder.no_video_layout = view;
                                    break;
                                case IPlayer.MEDIA_EVENT_VIDEO_SIZE:
                                    // 设置视频比例
                                    videoView.setVideoLayout(VideoViewSizeHelper.VIDEO_LAYOUT_STRETCH, 0);
                                    break;
                                case IPlayer.MEDIA_EVENT_FIRST_RENDER:
                                    mMultLivePlayHolder.videoState = true;
                                    break;

                            }
                        }
                    });
                    mISplayers.add(mISplayer);
                } else {
                    Log.e("V4MultLiveLargeController", " mCreatePlayerCallback is null");
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
        videoView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        videoView.setZOrderOnTop(true);
        mVideoViews.add(videoView);
    }

    /**
     * 下面的小视屏选中和取消选中
     */
    private void actionSelected(MultLivePlayHolder holder, boolean selected) {
        Drawable drawable1 = getContext().getResources().getDrawable(ReUtils.getDrawableId(context, "letv_skin_v4_large_mult_live_action_v_1"));
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        Drawable drawable2 = getContext().getResources().getDrawable(ReUtils.getDrawableId(context, "letv_skin_v4_large_mult_live_action_v_2"));
        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
        if (selected) {
            holder.framelayout.setBackgroundResource(ReUtils.getDrawableId(context, "letv_skin_v4_large_mult_live_action_list_onclick_style"));
            holder.textview.setCompoundDrawables(drawable2, null, null, null);
            holder.textview.setTextColor(getContext().getResources().getColor(ReUtils.getColorId(context, "action_mult_live_shape")));
        } else {
            holder.textview.setCompoundDrawables(drawable1, null, null, null);
            holder.textview.setTextColor(0xffffffff);
            holder.framelayout.setBackgroundResource(0);
        }
    }

    /**
     * 停止播放，并且记录最后播放时间
     */
    private void stopAndRelease() {
        for (int i = 0; i < mISplayers.size(); i++) {
            mISplayers.get(i).reset();
            mISplayers.get(i).stop();
            mISplayers.get(i).release();
            mISplayers.remove(i);
        }
    }

    public void setActionInfoDone() {
        showMultLiveViewBtn();
        // mMultLivelayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCurrentMultLive(String liveId) {
        initCurrentIndex(liveId);
        if (actionPlays != null && mCurrentIndex != -1 && actionPlays.size() > mCurrentIndex) {
            actionSelected(actionPlays.get(mCurrentIndex), true);
        }
    }

    private void initCurrentIndex(String liveId) {
        if (uiPlayContext.getActionInfo() != null) {
            mCurrentIndex = uiPlayContext.getActionInfo().IndexOfLiveInfo(liveId);
        }
    }

//    /**
//     * 创建播放器接口
//     */
//    public interface CreatePlayerCallback {
//        public ISplayer createPlayerCallback(SurfaceHolder holder, String path, OnPlayStateListener playStateListener);
//    }
//
//    private CreatePlayerCallback mCreatePlayerCallback;
//
//    public void setCreatePlayerCallback(CreatePlayerCallback mCreatePlayerCallback) {
//        this.mCreatePlayerCallback = mCreatePlayerCallback;
//    };
//
//    /**
//     * 切换直播窗口
//     */
//    public interface SwitchMultLiveCallback {
//        public void switchMultLive(String liveId);
//    }
//
//    private SwitchMultLiveCallback mSwitchMultLiveCallback;
//
//    public void setSwitchMultLiveCallbackk(SwitchMultLiveCallback mSwitchMultLiveCallback) {
//        this.mSwitchMultLiveCallback = mSwitchMultLiveCallback;
//    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        uiPlayContext.unRegisterMultLiveChangeListener(this);
    }

	public IActionCallback getIActionCallback() {
		return mIActionCallback;
	}

	public void setIActionCallback(IActionCallback mIActionCallback) {
		this.mIActionCallback = mIActionCallback;
	}

    class MultLivePlayHolder {
        FrameLayout framelayout;
        TextView textview;
        ProgressBar loading;
        View no_video_layout;
        View layout;
        /**
         * 这个表示视屏是不是正常。如果正常就为true。否则为false
         */
        boolean videoState = true;
        int location;
        String url;
    }

    class MyOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            if (actionPlays.size() > 0) {
                // long time = System.currentTimeMillis();
                for (final MultLivePlayHolder holder : actionPlays) {
                    if (holder.layout.equals(v)) {
                        if (holder.videoState && mCurrentIndex != holder.location) {
                            actionSelected(holder, true);
                            mCurrentIndex = holder.location;
                            if (mIActionCallback != null) {
                                mIActionCallback.switchMultLive(uiPlayContext.getActionInfo().getLiveInfo(mCurrentIndex).getLiveId());
                            }

                        } else {
                            if (mISplayers != null && mISplayers.size() > holder.location) {
                                mISplayers.get(holder.location).resetPlay(holder.url);
                                actionSelected(actionPlays.get(mCurrentIndex), true);
                                if (holder.loading != null) {
                                    holder.framelayout.removeView(holder.loading);
                                }
                                if (holder.no_video_layout != null) {
                                    holder.framelayout.removeView(holder.no_video_layout);
                                }
                                if (holder.loading != null) {
                                    holder.framelayout.addView(holder.loading);
                                }

                                return;
                            }
                        }
                    } else {
                        actionSelected(holder, false);
                    }
                }
            }
            toggleSubMultLiveView();
        }
    }

}
