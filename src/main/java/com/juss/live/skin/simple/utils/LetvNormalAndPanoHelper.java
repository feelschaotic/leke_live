package com.juss.live.skin.simple.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.juss.live.skin.skin.utils.UIPlayContext;
import com.juss.live.skin.skin.v4.V4PlaySkin;
import com.letv.controller.interfacev1.IPanoVideoChangeMode;
import com.letv.pano.ISurfaceListener;
import com.letv.pano.OnPanoViewTapUpListener;
import com.letv.pano.PanoVideoControllerView;
import com.letv.pano.PanoVideoView;
import com.letv.universal.iplay.ISplayer;
import com.letv.universal.widget.ReSurfaceView;

import java.util.List;

public class LetvNormalAndPanoHelper extends LetvBaseHelper {
    private boolean isLocalPano;
    @Override
    public void init(Context mContext, Bundle mBundle, V4PlaySkin skin) {
        super.init(mContext, mBundle, skin);
        isLocalPano = mBundle.getBoolean(LetvParamsUtils.IS_LOCAL_PANO);
        checkSensor();
        createOnePlayer(null);
    }


    private boolean checkSensor() {
        SensorManager sm = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        // 获取全部传感器列表
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ALL);
        boolean supportSensor = false;
        // 打印每个传感器信息
        StringBuilder strLog = new StringBuilder();
        int iIndex = 1;
        //旋转矢量传感器  判断是否支持陀螺仪
        for (Sensor item : sensors) {
            strLog.append(iIndex + ".");
            strLog.append("	Sensor Type - " + item.getType() + "\r\n");
            strLog.append("	Sensor Name - " + item.getName() + "\r\n");
            strLog.append("	Sensor Version - " + item.getVersion() + "\r\n");
            strLog.append("	Sensor Vendor - " + item.getVendor() + "\r\n");
            strLog.append("	Maximum Range - " + item.getMaximumRange() + "\r\n");
            strLog.append("	Minimum Delay - " + item.getMinDelay() + "\r\n");
            strLog.append("	Power - " + item.getPower() + "\r\n");
            strLog.append("	Resolution - " + item.getResolution() + "\r\n");
            strLog.append("\r\n");
            iIndex++;
            if (item.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                supportSensor = true;
            }
        }
        Log.d("sensor", strLog.toString());
        return supportSensor;
    }

    private void initNormalVideoView() {
        if (videoView == null || !(videoView instanceof ReSurfaceView)) {
            videoView = new ReSurfaceView(mContext);
            videoView.getHolder().addCallback(surfaceCallback);
            ((ReSurfaceView)videoView).setVideoContainer(null);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            skin.addVideoView(videoView, params);
            skin.unRegisterPanoVideoChange();
        }
    }

    private void initPanoVideoView() {
        if (videoView == null || !(videoView instanceof PanoVideoView)) {
            final PanoVideoControllerView panoVideoView = new PanoVideoControllerView(mContext);
            panoVideoView.registerSurfacelistener(new ISurfaceListener() {
                @Override
                public void setSurface(Surface surface) {
                    player.setDisplay(surface);
                }
            });

            // 设置手势操作层的touch事件
            // 如果手势不起作用有可能是您的layout把panovideoview的手势事件覆盖 这里也可以设置您的layout中最上层view
            panoVideoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return panoVideoView.onPanoTouch(v, event);
                }
            });

            // 设置video的单击事件 通知上层唤醒播控控件等
            panoVideoView.setTapUpListener(new OnPanoViewTapUpListener() {
                @Override
                public void onSingleTapUp(MotionEvent e) {
                    skin.performClick();
                }
            });
            panoVideoView.init();
            videoView = panoVideoView;
            videoView.getHolder().addCallback(surfaceCallback);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            skin.addVideoView(videoView, params);
            skin.initPanoView();
            skin.registerPanoVideoChange(new IPanoVideoChangeMode() {
                @Override
                public void switchPanoVideoMode(int mode) {
                    //先判断设备是否有旋转矢量传感器
                    PanoVideoControllerView.PanoControllMode controllMode = mode == UIPlayContext.MODE_TOUCH ? PanoVideoControllerView.PanoControllMode.GESTURE : PanoVideoControllerView.PanoControllMode.GESTURE_AND_GYRO;
                    if (controllMode == PanoVideoControllerView.PanoControllMode.GESTURE_AND_GYRO && !checkSensor()) {
                        return;
                    }
                    panoVideoView.switchControllMode(controllMode);
                }
            });
        }
    }

    @Override
    public void handlePlayState(int state, Bundle bundle) {
        super.handlePlayState(state, bundle);
        if (state == ISplayer.PLAYER_EVENT_PREPARE_VIDEO_VIEW) {
            boolean pano = bundle != null ? bundle.getBoolean("pano", false) : false;
            if (isLocalPano || pano) {
                initPanoVideoView();
            } else {
                initNormalVideoView();
            }
            playContext.setVideoContentView(videoView);
        }
    }

}
