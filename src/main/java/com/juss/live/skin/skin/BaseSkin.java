package com.juss.live.skin.skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.juss.live.skin.skin.controller.BaseMediaController;
import com.juss.live.skin.skin.interfacev1.IPlaySkin;
import com.juss.live.skin.skin.interfacev1.OnNetWorkChangeListener;
import com.juss.live.skin.skin.orientation.OrientationSensorUtils;
import com.juss.live.skin.skin.receiver.NetworkConnectionReceiver;
import com.juss.live.skin.skin.utils.GestureControl;
import com.juss.live.skin.skin.utils.ScreenUtils;
import com.juss.live.skin.skin.utils.UIPlayContext;
import com.juss.live.skin.skin.v4.V4MultLiveRightView;
import com.letv.controller.interfacev1.ILetvPlayerController;
import com.letv.controller.interfacev1.ISplayerController;
import com.letv.universal.iplay.EventPlayProxy;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public abstract class BaseSkin extends BaseViewGourp implements IPlaySkin, Observer, OnClickListener {

    public static final int ORIENTATION_8 = 1;
    public static final int ORIENTATION_9 = 2;
    public static final int ORIENTATION_0 = 3;
    public static final int ORIENTATION_1 = 4;
    protected static final String TAG = "BaseSkin";
    protected GestureControl mGestureControl;
    protected ArrayList<BaseView> childViews = new ArrayList<BaseView>();
    /**
     * 网络监听
     */
    private NetworkConnectionReceiver connectionReceiver;
    private OrientationSensorUtils mOrientationSensorUtils;
    private Runnable mHideFloatingRunnable;

    private RelativeLayout videoViewContainer;
    private RelativeLayout controllerContainer;
    private Context mContext;
    public BaseSkin(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseSkin(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseSkin(Context context) {
        super(context);
    }

    private void addChildView(View view) {
        if (view instanceof BaseView) {
            childViews.add((BaseView) view);
        }
    }

    private void removewChildView(View view) {
        childViews.remove(view);
    }

    @Override
    public void attachCenterView(View view) {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        controllerContainer.addView(view, lp);
        addChildView(view);
    }

    @Override
    public void attachBottomView(View view) {
        if (view != null) {
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            controllerContainer.addView(view, lp);
            addChildView(view);
        }
    }

    @Override
    public void attachTopFloatMediaView(View view) {
        if (view != null) {
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            controllerContainer.addView(view, lp);
            addChildView(view);
        }
    }

    @Override
    public void attachLeftMediaView(View view) {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT | RelativeLayout.CENTER_VERTICAL);
        controllerContainer.addView(view, lp);
        addChildView(view);
    }

    @Override
    public void attachRightMediaView(View view) {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT | RelativeLayout.CENTER_VERTICAL);
        controllerContainer.addView(view, lp);
        addChildView(view);
    }

    @Override
    public void attachAnyPositionView(View view, ViewGroup.LayoutParams lp) {
        controllerContainer.addView(view, lp);
        addChildView(view);
    }

    @Override
    public void attachGestureController(View view, boolean cancel) {
        if (mGestureControl == null) {
            mGestureControl = new GestureControl(context, view);
        }
        mGestureControl.cancelTouchable(cancel);
    }

    @Override
    protected void initView(Context context) {
        mContext = context;
        mOrientationSensorUtils = new OrientationSensorUtils((Activity) context, new ChangeOrientationHandler());
        addContainer();
    }

    private void addContainer() {
        videoViewContainer = new RelativeLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(videoViewContainer, params);

        controllerContainer = new RelativeLayout(context);
        addView(controllerContainer, params);
    }

    public void addVideoView(View view, ViewGroup.LayoutParams params) {
        videoViewContainer.addView(view, params);
    }

    public void removeVideoView(View view) {
        videoViewContainer.removeView(view);
    }
    /**
     * 对外接口，获取一个uiContext
     */
    @Override
    public void build(UIPlayContext uiContext) {
        this.uiPlayContext = uiContext;
        setOnClickListener(this);
        onBuild(uiContext);

        attachCommonView();
        attachComponentView();

        /**
         * 网络监听
         */
        if (connectionReceiver == null && uiPlayContext.isUseNetWorkNotice()) {
            connectionReceiver = new NetworkConnectionReceiver(context);
            for (int i = 0; i < childViews.size(); i++) {
                BaseView baseView = childViews.get(i);
                if (baseView instanceof OnNetWorkChangeListener) {
                    connectionReceiver.setNetWorkListener((OnNetWorkChangeListener) baseView);
                }
            }
        }
        attachUIContext(uiContext);

        /**
         * 是否在初始化的时候显示mediacontroller
         */
        if (uiPlayContext != null && !uiPlayContext.isShowMediaControllerOnStart()) {
            for (int i = 0; i < childViews.size(); i++) {
                BaseView baseView = childViews.get(i);
                if (baseView instanceof BaseMediaController) {
                    baseView.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 在build的时候将子view创建处理
     */
    protected abstract void onBuild(UIPlayContext uiContext);

    /**
     * 对外接口，获取播放器的具体实例
     */
    @Override
    public void registerController(ILetvPlayerController playerControl) {
        uiPlayContext.registerPlayerController(playerControl);
        playerControl = uiPlayContext.getPlayerController();
        attachUIPlayControl(playerControl);
    }

    @Override
    protected void onAttachUIPlayContext(UIPlayContext uiPlayContext) {
        for (int i = 0; i < childViews.size(); i++) {
            BaseView childView = childViews.get(i);
            childView.attachUIContext(uiPlayContext);
        }

        if (connectionReceiver != null) {
            connectionReceiver.AttachUIPlayContext(uiPlayContext);
        }

        if (mOrientationSensorUtils != null) {
            mOrientationSensorUtils.attachUIContext(uiPlayContext);
        }
    }

    @Override
    protected void onAttachUIPlayControl(ILetvPlayerController playerControl) {
        for (int i = 0; i < childViews.size(); i++) {
            BaseView childView = childViews.get(i);
            childView.attachUIPlayControl(playerControl);
        }

        if (connectionReceiver != null) {
            connectionReceiver.AttachUIPlayControl(playerControl);
        }
    }

    @Override
    public void removeView(View view) {
        controllerContainer.removeView(view);
        removewChildView(view);
    }

    protected void dettachComponentView() {
        if (childViews != null && childViews.size() > 0) {
            for (int i = childViews.size() - 1; i >= 0; i--) {
                BaseView childView = childViews.get(i);
                Log.i(TAG, "[playskin] dettach view:" + childView.toString());
                if (childView.isMark()) {
                    continue;
                }
                // if (childView instanceof BaseLoadingView) {
                // continue;
                // }
                // // TODO
                // if (childView instanceof V3NoticeView) {
                // continue;
                // }
                // // TODO
                // if (childView instanceof V3WaterMarkView) {
                // continue;
                // }
                removeView(childView);
            }
        }
    }

    protected abstract void attachComponentView();

    protected abstract void attachCommonView();

    @Override
    public void update(Observable observable, Object data) {
        Bundle bundle = (Bundle) data;
        int state = bundle.getInt("state");
        switch (state) {
            case ISplayerController.SCREEN_ORIENTATION_PORTRAIT:
            case ISplayerController.SCREEN_ORIENTATION_REVERSE_PORTRAIT:
            case ISplayerController.SCREEN_ORIENTATION_USER_PORTRAIT:
            case ISplayerController.SCREEN_ORIENTATION_LANDSCAPE:
            case ISplayerController.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
            case ISplayerController.SCREEN_ORIENTATION_USER_LANDSCAPE:
                setOrientation(state);
                break;
            default:
                break;
        }
        if (state == EventPlayProxy.PLAYER_PROXY_AD_START  || state == EventPlayProxy.PLAYER_PROXY_AD_POSITION) {
            if (controllerContainer.getVisibility() == View.VISIBLE) {
                controllerContainer.setVisibility(View.GONE);
            }
        } else {
            if (controllerContainer.getVisibility() == View.GONE) {
                controllerContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 处理屏幕变化
     */
    private void initWindow(int state) {
        uiPlayContext.setScreenResolution(state);
        uiPlayContext.setSaveInstanceState(true);

        dettachComponentView();
        /**
         * 将旧的UI移除(保留公用UI)
         */
        player.getObserver().deleteUIObservers();

        Log.d(TAG, "[playskin] childView 2 count:" + childViews.size());

        /**
         * 将新的UI加入
         */

        if (uiPlayContext.getScreenChangeShowDelay() > 0) {// 是否延迟显示
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    attachComponentView();
                    attachUIContext(uiPlayContext);
                    attachUIPlayControl(uiPlayContext.getPlayerController());
                    uiPlayContext.setSaveInstanceState(false);
                }
            }, uiPlayContext.getScreenChangeShowDelay());
        } else {
            attachComponentView();
            attachUIContext(uiPlayContext);
            attachUIPlayControl(uiPlayContext.getPlayerController());
            uiPlayContext.setSaveInstanceState(false);
        }
    }

    public UIPlayContext getUIPlayContext() {
        return uiPlayContext;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void initPlayer() {
        if (player != null) {
            player.getObserver().addObserver(this);
        }
    }

    public void onResume() {
        if (uiPlayContext != null && uiPlayContext.isUseNetWorkNotice() && connectionReceiver != null) {
            connectionReceiver.registerReceiver();
        }
        if (mOrientationSensorUtils != null) {
            mOrientationSensorUtils.onResume();
        }
    }

    public void onPause() {
        if (uiPlayContext != null && uiPlayContext.isUseNetWorkNotice() && connectionReceiver != null) {
            connectionReceiver.unregisterReceiver();
        }
        if (mOrientationSensorUtils != null) {
            mOrientationSensorUtils.onPause();
        }
    }

    public void onDestroy() {
        if (mOrientationSensorUtils != null) {
            mOrientationSensorUtils.destory();
        }
    }

    @Override
    public void onClick(View v) {
        if (uiPlayContext == null) {
            return;
        }
        // 错误提示正在显示的时候
        if (!uiPlayContext.isShowMediaControllerOnError() && uiPlayContext.isNotiveViewShowing()) {
            return;
        }

        if (isShowButtomFloating()) {
            hideFloatingView();
        } else {
            showFloatingView();
            hideFloatingInDelay();
        }
    }

    /**
     * 判断当前底部浮层是否显示
     *
     * @return
     */
    private boolean isShowButtomFloating() {
        // fix bug SDK-158 SDK-158【Android】播放器小屏的时候，点击视频画面，播放器下部进度条一栏不消失
        // if (uiPlayContext.getScreenResolution(getContext()) ==
        // UIPlayerControl.SCREEN_ORIENTATION_LANDSCAPE) {
        for (int i = 0; i < childViews.size(); i++) {
            BaseView baseView = childViews.get(i);
            if (baseView instanceof BaseMediaController && baseView.isShown()) {
                return true;
            }
        }
        // }
        return false;
    }

    private void showFloatingView() {
        cancleHideFloatingTask();
        for (int i = 0; i < childViews.size(); i++) {
            BaseView baseView = childViews.get(i);
            // if (baseView instanceof BaseLoadingView) {
            // continue;
            // }
            // if (baseView instanceof V3NoticeView) {
            // continue;
            // }
            if (baseView.isMark()) {
                continue;
            }
            baseView.setVisibility(View.VISIBLE);
        }
        if (player != null) {
            player.getObserver().notifyObserverState(ISplayerController.SHOW_FLOATING_VIEW);
        }
    }

    private void hideFloatingView() {
        for (int i = 0; i < childViews.size(); i++) {
            BaseView baseView = childViews.get(i);
            if (baseView.isMark()) {
                continue;
            }
            // if (baseView instanceof BaseLoadingView) {
            // continue;
            // }
            // if (baseView instanceof V3NoticeView) {
            // continue;
            // }
            if (baseView instanceof V4MultLiveRightView) {
                continue;
            }
            baseView.setVisibility(View.GONE);
        }
        if (player != null) {
            player.getObserver().notifyObserverState(ISplayerController.HIDE_FLOATING_VIEW);
        }

        if(ScreenUtils.hasNavBar(mContext)){
            ScreenUtils.showFullScreen((Activity)mContext,true);
        }
    }

    /**
     * 延时隐藏浮层
     */
    protected void hideFloatingInDelay() {
        if (mHideFloatingRunnable == null) {
            initHideRunnable();
        }
        removeCallbacks(mHideFloatingRunnable);
        postDelayed(mHideFloatingRunnable, uiPlayContext.getControllerHideTime());
    }

    /**
     * 取消隐藏浮层
     */
    protected void cancleHideFloatingTask() {
        removeCallbacks(mHideFloatingRunnable);
    }

    /**
     * 延时隐藏底部和顶部菜单
     */
    private void initHideRunnable() {
        if (mHideFloatingRunnable != null) {
            return;
        }
        mHideFloatingRunnable = new Runnable() {
            @Override
            public void run() {
                hideFloatingView();
            }
        };
    }

    private void setOrientation(int state) {
        if (player == null) {
            return;
        }
        switch (state) {
            case ISplayerController.SCREEN_ORIENTATION_USER_LANDSCAPE:
                if (mOrientationSensorUtils != null) {
                    mOrientationSensorUtils.getmOrientationSensorListener().lockOnce(((Activity) context).getRequestedOrientation());
                }
            case ISplayerController.SCREEN_ORIENTATION_LANDSCAPE:
                ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                if (uiPlayContext.isNoWindowTitle()) {
                    ScreenUtils.showFullScreen((Activity) context, true);
                }
                initWindow(state);
                break;
            case ISplayerController.SCREEN_ORIENTATION_USER_PORTRAIT:
                if (mOrientationSensorUtils != null) {
                    mOrientationSensorUtils.getmOrientationSensorListener().lockOnce(((Activity) context).getRequestedOrientation());
                }
            case ISplayerController.SCREEN_ORIENTATION_PORTRAIT:
                ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (uiPlayContext.isNoWindowTitle()) {
                    ScreenUtils.showFullScreen((Activity) context, false);
                }
                initWindow(state);
                break;
            case ISplayerController.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
                ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                if (uiPlayContext.isNoWindowTitle()) {
                    ScreenUtils.showFullScreen((Activity) context, true);
                }
                initWindow(state);
                break;
            case ISplayerController.SCREEN_ORIENTATION_REVERSE_PORTRAIT:
                ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                if (uiPlayContext.isNoWindowTitle()) {
                    ScreenUtils.showFullScreen((Activity) context, false);
                }
                initWindow(state);
                break;
            default:
                break;
        }
    }

    public boolean isLock() {
        if (mOrientationSensorUtils != null && mOrientationSensorUtils.getmOrientationSensorListener() != null) {
            return mOrientationSensorUtils.getmOrientationSensorListener().isLock();
        }
        return false;
    }

    public void setLock(boolean isLock) {
        if (mOrientationSensorUtils != null && mOrientationSensorUtils.getmOrientationSensorListener() != null) {
            mOrientationSensorUtils.getmOrientationSensorListener().setLock(isLock);
        }
    }

    public class ChangeOrientationHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (player == null) {
                return;
            }
            switch (msg.what) {
                case ORIENTATION_8:// 反横屏
                    // setOrientation(UIPlayerControl.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    player.setScreenResolution(ISplayerController.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    break;
                case ORIENTATION_9:// 反竖屏
                    // setOrientation(UIPlayerControl.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                    player.setScreenResolution(ISplayerController.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                    break;
                case ORIENTATION_0:// 正横屏
                    // setOrientation(UIPlayerControl.SCREEN_ORIENTATION_LANDSCAPE);
                    player.setScreenResolution(ISplayerController.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                case ORIENTATION_1:// 正竖屏
                    // setOrientation(UIPlayerControl.SCREEN_ORIENTATION_PORTRAIT);
                    player.setScreenResolution(ISplayerController.SCREEN_ORIENTATION_PORTRAIT);
                    break;
            }

            super.handleMessage(msg);
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////

}
