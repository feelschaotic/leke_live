package com.juss.live.skin.ui.mobile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceView;

import com.juss.live.skin.ui.RecorderView;
import com.juss.live.skin.ui.base.RecorderSkinBase;
import com.letv.recorder.ui.logic.RecorderConstance;
import com.letv.recorder.util.LeLog;
import com.ramo.campuslive.LetvFlag;


import java.util.Observable;
import java.util.Observer;

/**
 * 新的移动推流，不使用流ID，只需要推流地址,，使用乐视云默认推流，需要直接使用Publisher
 */
public class RecorderSkinMobile extends RecorderSkinBase {

    private String pushSteamUrl;
    private boolean isSkinDefult;

    /*是否使用默认的布局
        否：去掉底部自定义布局*/
    public RecorderSkinMobile(String flag) {
        if (flag.equals(LetvFlag.Skin_DEFULT))
            isSkinDefult = true;
        else
            isSkinDefult = false;
    }


    @Override
    public void build(Context context, RecorderView rv, int orientation) {
        this.rv = rv;
        this.context = context;
        this.orientation = orientation;
        setCameraView(new SurfaceView(context));
        initCameraPreView(getCameraView());
        initObserver();
    }

    /**
     * 绑定Sufaceview
     *
     * @param cameraView
     */
    private void initCameraPreView(SurfaceView cameraView) {
        RecorderBottomMobileView bottomView = new RecorderBottomMobileView(context);
        bottomView.init(isSkinDefult);
        setBottomView(bottomView);

        setTopFloatView(new RecorderTopFloatMobileView(context));

        rv.attachTopFloatView(getTopFloatView());
        rv.attachBomttomView(getBottomView());
        rv.attachSurfaceView(cameraView);
        cameraView.getHolder().addCallback(this);
        ((RecorderTopFloatMobileView) getTopFloatView()).setTitle(streamName);
    }

    @Override
    protected void initObserver() {
        /**
         * 开始按钮
         */
        rv.getStartSubject().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                Bundle bundle = (Bundle) data;
                int flag = bundle.getInt("flag");

                switch (flag) {
                    case RecorderConstance.recorder_start:
                        LeLog.d(TAG, "[observer] recorder_start 开始推流");
                        if (publisher != null) {
                            publisher.publish();
                        }
                        break;
                    case RecorderConstance.recorder_stop:
                        LeLog.d(TAG, "[observer] recorder_stop 停止推流");
                        if (publisher != null) {
                            publisher.stopPublish();// 停止推流
                        }
                        break;
                    case RecorderConstance.angle_request:
                        if (pushSteamUrl != null || pushSteamUrl.equals("")) {
                            publisher.setUrl(pushSteamUrl);
                        }
                        showLoadDialog();
                        rv.startRecorder();
                        break;
                }
            }
        });

        rv.getStartSubject().addObserver(getTopFloatView());

        /**
         * 返回按钮
         */
        getTopFloatView().getTopSubject().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                Bundle bundle = (Bundle) data;
                int flag = bundle.getInt("flag");
                if (RecorderConstance.top_float_back == flag) {
                    LeLog.d(TAG, "[observer] top_float_back 返回按钮");
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                }
            }
        });

        /**
         * 在线人数
         */
        // TODO: 暂时没有实现，所以关闭
        // mCountPeopleTimer.getCountObservable().addObserver(bottomView);
    }

    public void setPushSteamUrl(String pushSteamUrl) {
        this.pushSteamUrl = pushSteamUrl;
    }

    @Override
    protected void handleMachine() {

    }

    @Override
    protected void selectMachine(int numFlag) {

    }
}
