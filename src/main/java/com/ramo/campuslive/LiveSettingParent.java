package com.ramo.campuslive;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.po.Permissions;
import com.juss.mediaplay.po.Type;
import com.juss.mediaplay.utils.NetUtil;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;

import java.util.List;

/**
 * Created by ramo on 2016/7/4.
 */
public class LiveSettingParent extends SwipeBackActivity {

    protected String[] live_permissions_text = {"社团", "密码", "课堂", "付费","双人","公开"};
    protected String[] live_permissions_text1 = new String[]{};
    protected List<Permissions> live_permissions_list;
    protected List<Type> type_list;

    protected int[] live_permissions_img = {R.drawable.live_permissions_ic_group,
            R.drawable.live_permissions_ic_password,
            R.drawable.launch_lock_off,
            R.drawable.live_permissions_ic_money, R.drawable.double_live_icon_pk, R.drawable.launch_lock_on};
    protected int permissionNum = 0;

    protected int[] live_permissions_tv_ids = {
            R.id.live_permissions_tv_pass1,
            R.id.live_permissions_tv_pass2,
            R.id.live_permissions_tv_pass3,
            R.id.live_permissions_tv_pass4};

    protected EditText[] live_permissions_tvs;

    protected String[] btns = {"#毕业季#", "#旅游#", "#美食#", "#吃货聊一聊#",
            "#做菜技巧大公开#", "#旅游#", "#音乐分享#", "#大神直播敲代码#",
            "#膜拜。。。。#", "#无聊(￣▽￣)#"};

    public void init() {
        live_permissions_tvs = new EditText[live_permissions_tv_ids.length];
        for (int i = 0; i < live_permissions_tv_ids.length; i++) {
            live_permissions_tvs[i] = (EditText) findViewById(live_permissions_tv_ids[i]);
        }
    }

    public void setFocus(int j) {
        live_permissions_tvs[j].setFocusable(true);
        live_permissions_tvs[j].setFocusableInTouchMode(true);
        live_permissions_tvs[j].requestFocus();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        final Gson gson = new Gson();
        //获取直播类型已经直播权限
        NetUtil.getHttpUrlConnection(ServerConstants.TypeListURL, new HttpCallbackListener() {

            @Override
            public void onSucc(String response) {
                L.i(response);
                type_list = gson.fromJson(response, new TypeToken<List<Type>>() {
                }.getType());
                for (int i = 0; i < type_list.size(); i++) {
                    live_permissions_text[i]=type_list.get(i).getTypeName();
                }
            }

            @Override
            public void onError(Exception e) {
                L.e(e.getMessage());
            }
        });
        NetUtil.getHttpUrlConnection( ServerConstants.LivePremissionURL, new HttpCallbackListener() {

            @Override
            public void onSucc(String response) {
                L.i(response);
                live_permissions_list = gson.fromJson(response, new TypeToken<List<Permissions>>() {
                }.getType());
            }

            @Override
            public void onError(Exception e) {
                L.e(e.getMessage());
            }
        });
    }
}
