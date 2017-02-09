package com.juss.live.skin.skin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.juss.live.skin.skin.interfacev1.OnNetWorkChangeListener;
import com.juss.live.skin.skin.utils.NetworkUtils;
import com.juss.live.skin.skin.utils.UIPlayContext;
import com.letv.controller.interfacev1.ILetvPlayerController;
import com.letv.controller.interfacev1.ISplayerController;


/**
 * 网络监听
 * 
 * @author pys
 *
 */
public class NetworkConnectionReceiver extends BroadcastReceiver {

    private Context context;
    private OnNetWorkChangeListener netWorkChangeListener;
    private ISplayerController uiplayer;
    private UIPlayContext uiPlayContext;
    public static final String NET_ACTION = "com.letv.skin.receiver.NET_ACTION";

    public NetworkConnectionReceiver(Context context) {
        this.context = context;
    }

    public void setNetWorkListener(OnNetWorkChangeListener netWorkChangeListener) {
        this.netWorkChangeListener = netWorkChangeListener;
    }

    public void AttachUIPlayControl(ILetvPlayerController playerControl) {
        uiplayer = playerControl.getIsPlayerController();
    }

    public void AttachUIPlayContext(UIPlayContext uiPlayContext) {
        this.uiPlayContext = uiPlayContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == context || null == intent) {
            return;
        }
        if (uiPlayContext != null && uiPlayContext.isUseNetWorkNotice()) {
            netWorkChange(NetworkUtils.isWifiConnect(context), "");
        }
    }

    /**
     * 网络改变
     * 
     * @param state
     *            是不是存在网络
     * @param message
     *            当前是什么网络
     */
    public void netWorkChange(boolean state, String message) {
        if (netWorkChangeListener != null) {
            netWorkChangeListener.onNetWorkChange(state, message);
        }
        if (!state && !uiPlayContext.isNoWifiContinue()) {
            if (uiplayer != null) {
                uiplayer.pause();
            }
        }
    }

    /**
     * 取消网络监听广播
     */
    public void unregisterReceiver() {
        try {
            context.unregisterReceiver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册网络监听广播
     */
    public void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(NET_ACTION);
        context.registerReceiver(this, filter);
    }
}
