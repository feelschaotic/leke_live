package com.juss.mediaplay;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.google.gson.Gson;
import com.juss.mediaplay.entity.AnchorLive;
import com.juss.mediaplay.entity.TemeBaskJson;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.utils.NetUtil;
import com.ramo.campuslive.R;
import com.ramo.campuslive.adapter.TemeBaskAdapter;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;

import java.util.ArrayList;
import java.util.List;

public class ThemeBasketballActivity extends Activity {


    private ListView lv_theme_bask;
    private List<AnchorLive> teme_bask_list;
    private final int SUCCESS=1;
    private final int FAULST=0;
    private TemeBaskAdapter adapter;

    private Handler themeBaskHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int wath = msg.what;
            switch (wath){
                case SUCCESS:
                    String response = (String) msg.obj;
                    onDataSucc(response);
                    break;
                case FAULST:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void onDataSucc(String response) {
         L.e(response);
        Gson gson = new Gson();
        TemeBaskJson json= gson.fromJson(response, TemeBaskJson.class);
        teme_bask_list= json.getList();
        adapter = new TemeBaskAdapter(this,teme_bask_list,1);
        lv_theme_bask.setAdapter(adapter);
        L.e(teme_bask_list.size()+"");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_basketball);
        lv_theme_bask =(ListView)findViewById(R.id.lv_theme_bask);
        getData();
        initListener();
    }

    private void initListener() {

    }

    private void getData() {
        teme_bask_list=new ArrayList<>();

        String[] name = new String[]{"themeId"};
        String[] val = new String[]{"1"};
        NetUtil.sendRequestToUrl(ServerConstants.TemeBarkURL, name, val, new HttpCallbackListener() {
            @Override
            public void onSucc(String response) {
                Message msg = Message.obtain();
                msg.obj = response;
                msg.what=SUCCESS;
                themeBaskHandle.sendMessage(msg);
            }
            @Override
            public void onError(Exception e) {
                themeBaskHandle.sendEmptyMessage(FAULST);

            }
        });
    }


}
