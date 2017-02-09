package com.ramo.campuslive.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.juss.live.skin.handler.CrashHandler;
import com.lecloud.config.LeCloudPlayerConfig;
import com.letv.proxy.LeCloudProxy;
import com.thinkland.sdk.android.JuheSDKInitializer;

import java.util.List;

/**
 * Created by ramo on 2016/3/25.
 */
public class MyApplication extends Application {
    public static final String ACTIVITY_EXTRA_NAME = "activityTag";
    public static final String MY_GUESS_DETAILS_EXTRA = "guessing_details";
    private static Context context;
    public static final String ACTIVITY_NEAR = "本地";
    public static final String ACTIVITY_RECOMMEND = "推荐";
    public static final String ACTIVITY_HOT = "热门";

    public static final String FRAGMENT_attention = "attention";
    public static final String FRAGMENT_community = "community";

    public static String []themePath={
            "rtmp://4315.mpull.live.lecloud.com/live/10001?tm=20160919013856&sign=acb15d288156079a2b7dcbaadc1fa1dc",
    "rtmp://4315.mpull.live.lecloud.com/live/10011?tm=20160919021422&sign=ee0666b949ca77028b510df3b7a7accc",
            "rtmp://216.mpull.live.lecloud.com/live/111111?tm=20160919013752&sign=c6186b6c5212485586abe74795aea9c0"};

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null) {
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        JuheSDKInitializer.initialize(getApplicationContext());

        String processName = getProcessName(this, android.os.Process.myPid());
        if (getApplicationInfo().packageName.equals(processName)) {
            //TODO CrashHandler是一个抓取崩溃log的工具类（可选）
            CrashHandler.getInstance(this);
            LeCloudPlayerConfig.getInstance().setPrintSdcardLog(true).setIsApp().setUseLiveToVod(true);//setUseLiveToVod 使用直播转点播功能 (直播结束后按照点播方式播放)
            LeCloudProxy.init(getApplicationContext());

        }

    }

    public static Context getContext() {
        return context;
    }


}
