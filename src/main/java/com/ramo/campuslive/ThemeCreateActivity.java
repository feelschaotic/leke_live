package com.ramo.campuslive;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.utils.NetUtil;
import com.ramo.campuslive.asynctask.InviteHostHeadLoadAsyncTask;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;
import com.ramo.campuslive.utils.T;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ramo on 2016/7/8.
 */
@EActivity(R.layout.activity_create_theme)
public class ThemeCreateActivity extends SwipeBackActivity {
    @ViewById
    GridView live_theme_head_gv;

    private Button live_theme_create_btn;
    private EditText live_theme_title;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    T.showShort(getApplicationContext(), "添加成功");
                    //startActivity(new Intent(ThemeCreateActivity.this,MainActivity.class));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @AfterViews
    public void init() {
        live_theme_head_gv.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
        live_theme_title = (EditText) findViewById(R.id.live_theme_title);
        live_theme_create_btn = (Button) findViewById(R.id.live_theme_create_btn);
        initData();
        initListener();
    }

    private void initListener() {
        live_theme_head_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View check = view.findViewById(R.id.create_theme_head_check);
                if (check.getVisibility() == View.GONE)
                    check.setVisibility(View.VISIBLE);
                else
                    check.setVisibility(View.GONE);
            }
        });
        live_theme_create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String themename = live_theme_title.getText().toString().trim();
                L.i(themename);
                if(!"".equals(themename))
                    NetUtil.sendRequestToUrl(ServerConstants.AddThemeURL,new String[]{"theme"},new String[]{themename},listener);
            }
        });

    }
    private HttpCallbackListener listener = new HttpCallbackListener() {
        @Override
        public void onSucc(String response) {
            L.i(response);
            try {
                JSONObject object = new JSONObject(response);
                int state = object.getInt("state");
                if(state==0){
                   // T.showShort(getApplicationContext(), "添加成功");
                    mHandler.sendEmptyMessage(0);
//                    startActivity(new Intent(ThemeCreateActivity.this,ThemeLiveActivity.class));
                }else{
                    //T.showShort(getApplicationContext(),"添加失败");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onError(Exception e) {
            L.e(e.getMessage());
        }
    };

    private void initData() {
      //  new InviteHostHeadLoadAsyncTask(this,live_theme_head_gv).execute();
    }

}
