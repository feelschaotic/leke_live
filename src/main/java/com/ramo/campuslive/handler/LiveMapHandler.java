package com.ramo.campuslive.handler;

/**
 * Created by ramo on 2016/7/12.
 */

import android.os.Handler;
import android.os.Message;

import com.amap.yuntu.demo.core.ProtocalHandler;
import com.amap.yuntu.demo.core.PushDataListener;
import com.google.gson.Gson;
import com.ramo.campuslive.bean.LiveRoomMapBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CopyOnWriteArrayList;

public class LiveMapHandler extends ProtocalHandler {
    private PushDataListener mListener;
    private boolean isPush = true;
    private CopyOnWriteArrayList<LiveRoomMapBean> mDataList = new CopyOnWriteArrayList<LiveRoomMapBean>();

    public LiveMapHandler(PushDataListener listener) {
        this.mListener = listener;
        upload.start();
    }

    /**
     * 加入上传列表
     *
     * @param bean
     */
    public void addTask(LiveRoomMapBean bean) {
        mDataList.add(bean);
    }

    /**
     * 销毁上传线程
     */
    public void destroy() {
        isPush = false;
    }

    /**
     * 用户建立的数据表id http://yuntu.amap.com/datamanager/
     */
    @Override
    protected String getTableID() {
        return "5784aaeaafdf5247972434dd";
    }

    /**
     * 用户申请且绑定的key，需要在网站开启云存储功能
     */
    @Override
    protected String getKEY() {
        return "23673fc32919c2cc091eb1c0165271aa";
    }

    /**
     * 获取上传单条数据
     */
    @Override
    protected String getUserJSONString() {
        if (mDataList.size() > 0) {
            Gson gson = new Gson();
            LiveRoomMapBean bean = mDataList.get(0);
            mDataList.remove(0);
            return gson.toJson(bean);
        }
        return new String();
    }

    /**
     * 接收上传数据结果
     */
    Handler upHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String result = (String) msg.obj;
                boolean succeed = false;
                String info = "";
                try {
                    JSONObject jobj = new JSONObject(result);
                    int status = jobj.getInt("status");
                    info = jobj.getString("info");
                    if (status == 0) {
                        succeed = false;
                    } else {
                        succeed = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (mListener != null) {
                        mListener.onPushFinish(succeed, info);
                    }
                }

            }
        }

    };
    /**
     * 上传数据线程
     */
    Thread upload = new Thread() {
        @Override
        public void run() {
            while (isPush) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mDataList.size() <= 0) {
                    continue;
                }
                String result = getData();
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                upHandler.sendMessage(msg);

            }

        }
    };
}

