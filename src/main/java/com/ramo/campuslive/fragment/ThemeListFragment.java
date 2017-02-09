package com.ramo.campuslive.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.juss.mediaplay.ThemeBasketballActivity;
import com.juss.mediaplay.entity.ThemeJson;
import com.juss.mediaplay.listener.HttpCallbackListener;

import com.juss.mediaplay.utils.GsonDateUtil;
import com.juss.mediaplay.utils.NetUtil;
import com.ramo.campuslive.R;
/*import com.ramo.campuslive.ThemeCreateActivity_;
import com.ramo.campuslive.ThemeLiveActivity;
import com.ramo.campuslive.ThemeLiveActivity_;*/
import com.ramo.campuslive.ThemeCreateActivity_;
import com.ramo.campuslive.ThemeLiveActivity;
import com.ramo.campuslive.ThemeLiveActivity_;
import com.ramo.campuslive.adapter.LiveThemeAdapter;

import com.ramo.campuslive.bean.Theme;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.logging.LogRecord;

/**
 * Created by ramo on 2016/7/12.
 */
@EFragment(R.layout.activity_theme_list)
public class ThemeListFragment extends Fragment {

    @ViewById
    ListView live_theme_listview;
    private List<Theme> themes;
    @ViewById
    ImageView live_theme_add_iv;

    private int pagenum=1;
    private int pageall=2;

    LiveThemeAdapter mAdapter;

    @AfterViews
    public void init() {
        pagenum=1;
        initData();
        initListener();
    }
    private final int SESSCEE=1;
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SESSCEE:
                    if(mAdapter!=null){
                        live_theme_listview.deferNotifyDataSetChanged();

                    }else{
                        mAdapter =new LiveThemeAdapter(getActivity(), themes);
                        live_theme_listview.setAdapter(mAdapter);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void initListener() {
        live_theme_add_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ThemeCreateActivity_.class));
            }
        });

        live_theme_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startActivity(new Intent(getActivity(), ThemeLiveActivity_.class));
                startActivity(new Intent(getActivity(), ThemeBasketballActivity.class));
            }
        });
    }

    private void initData() {
     /*   if(pagenum>pageall){
            pagenum=pageall;
        }*/
        NetUtil.sendRequestToUrl( ServerConstants.ListThemeURL, new String[]{"pagenum"}, new String[]{pagenum+""}, new HttpCallbackListener() {
            @Override
            public void onSucc(String response) {
                L.e(response);
                Gson gson = new GsonDateUtil().getGsonDate();
                ThemeJson theme = gson.fromJson(response,ThemeJson.class);
                pagenum = theme.getPagenum()+1;
                pageall = theme.getTotalrecord();
                themes=new ArrayList<>();
                for (com.juss.mediaplay.po.Theme t: theme.getList()) {
                    L.e(t.getThemeId()+"----");
                    L.e(t.getThemeName()+"--");
                    L.e(""+t.getThemeTime());
                    Theme th = new Theme(t.getThemeId(),t.getThemeName(),t.getThemeTime());
                    themes.add(th);
                }
                mHandle.sendEmptyMessage(SESSCEE);
            }
            @Override
            public void onError(Exception e) {
                L.e(e.toString());
                L.e(e.getMessage());
            }
        });
    }

}
