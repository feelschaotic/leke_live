package com.ramo.campuslive;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juss.live.LivePlayBase;
import com.letv.controller.PlayProxy;
import com.letv.universal.iplay.EventPlayProxy;
import com.ramo.campuslive.adapter.ThemeLiveListAdapter;
import com.ramo.campuslive.application.MyApplication;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ramo on 2016/7/12.
 */
@EActivity(R.layout.activity_theme_live)
public class ThemeLiveActivity extends SwipeBackActivity {
    @ViewById
    Button theme_exchange_view;
    @ViewById(R.id.theme_live_list)
    ListView theme_live_list;
    private Bundle mBundle;
    @ViewById(R.id.view1)
    RelativeLayout view;
    @ViewById(R.id.beginPIP)
    TextView beginPIP;
    @ViewById(R.id.live_theam_list_rl)
    RelativeLayout live_theam_list_rl;
    private LivePlayBase live_play;
    private String path;
    public static  int pos;
    ThemeLiveListAdapter adapter;

    @AfterViews
    public void init() {
        loadDataFromIntent();
        initData();

        live_play = new LivePlayBase(this, path, mBundle, view);
        theme_exchange_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator.ofFloat(live_theam_list_rl, "translationX", live_theam_list_rl.getTranslationX(), live_theam_list_rl.getTranslationX() - live_theam_list_rl.getWidth()).setDuration(300).start();
            }
        });
    }

    private void getNextTheme(int position) {
        Intent intent = new Intent(ThemeLiveActivity.this, ThemeLiveActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_LIVE);
        if (pos >= MyApplication.themePath.length)
            pos = 0;

        bundle.putInt("pos", position);
        bundle.putString("path", MyApplication.themePath[pos]);
        intent.putExtra("data", bundle);
        startActivity(intent);
        finish();
    }

    private void initData() {
        String themeTitle[] = {"篮球赛1班直播", "篮球赛2班直播", "篮球赛前采访"};
        adapter = new ThemeLiveListAdapter(this, themeTitle);
        theme_live_list.setAdapter(adapter);
        initListener();
    }

    private void initListener() {
      /*  theme_live_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/
        beginPIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThemeLiveActivity.this,Pip2Activity.class));
            }
        });
    }

    private void loadDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mBundle = intent.getBundleExtra("data");
            if (mBundle == null) {
                mBundle = new Bundle();
                pos = 0;
                path = MyApplication.themePath[pos];
                mBundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_LIVE);
                mBundle.putString("path", path);
                mBundle.putInt("pos", pos);
            } else {
                pos = mBundle.getInt("pos");
                path = mBundle.getString("path");
            }

        }
    }

}
