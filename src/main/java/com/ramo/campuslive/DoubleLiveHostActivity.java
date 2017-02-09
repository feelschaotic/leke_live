package com.ramo.campuslive;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.juss.live.LivePathUtil;
import com.juss.live.LivePlayBase;
import com.juss.live.skin.ui.RecorderView;
import com.juss.live.skin.ui.mobile.RecorderSkinMobile;
import com.letv.controller.PlayProxy;
import com.letv.recorder.controller.Publisher;
import com.letv.recorder.util.LeLog;
import com.letv.universal.iplay.EventPlayProxy;


public class DoubleLiveHostActivity extends Activity {


    private RelativeLayout view1;
    private View view2;

    private ImageButton double_live_support2;
    private TextView double_live_agree1;
    private TextView double_live_agree2;
    private boolean support = false;

    private String rootPath = Environment.getExternalStorageDirectory().getPath();

    private Animation supportAnim;
    private ImageView double_live_close_iv;

    private RelativeLayout live_view1;

    /*主播变量*/
    private RecorderView rv;
    private RecorderSkinMobile recorderSkinMobile;
    private ImageView focusView;
    private boolean isVertical = true;
    private Publisher publisher;

    private String pushSteamUrl;
    private String pushName;

    /*end 主播变量*/
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_live_host);
        view1 = (RelativeLayout)findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);


        double_live_support2 = (ImageButton) view2.findViewById(R.id.double_live_support);

        double_live_agree1 = (TextView) findViewById(R.id.double_live_agree1);
        double_live_agree2 = (TextView) findViewById(R.id.double_live_agree2);

        double_live_close_iv = (ImageView) findViewById(R.id.double_live_close_iv);
        supportAnim = AnimationUtils.loadAnimation(this, R.anim.double_live_support_anim);
        initListener();
        setHost();
        setLive();
    }

    private void setHost() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_recorder, null);
        view1.addView(inflate);

        focusView = (ImageView) findViewById(R.id.focusView);
        rv = (RecorderView) findViewById(R.id.rv);//获取rootView

        isVertical = getIntent().getBooleanExtra("isVertical", false);

       /* pushSteamUrl = getIntent().getStringExtra("streamUrl");
        pushSteamUrl = "rtmp://4315.mpull.live.lecloud.com/live/1111?tm=20160808105355&sign=ef38647b2eb6c71271796b50dc653600";
        pushName = getIntent().getStringExtra("pushName");*/
        pushName="100010";

        String[] path = LivePathUtil.createStreamUrl(pushName);
        pushSteamUrl = path[0];
        if(pushSteamUrl == null || "".equals(publisher)){
            Toast.makeText(this, "不能传入空的推流地址", Toast.LENGTH_LONG).show();
        }
        LeLog.w("推流地址是:" + pushSteamUrl);

        /**
         *  1、 初始化推流器，在移动直播中使用的是Publisher 对象
         */
        initPublish();
        /**
         *  2、初始化皮肤,在移动直播中使用的是RecorderSkinMobile 对象，并且需要传入不同的参数
         */
        initSkin(isVertical);
        /**
         * 3、绑定推流器
         */
        bindingPublish();
    }

    private void initListener() {

        double_live_support2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                support = supportBtnStyle(v, support);
            }
        });

        double_live_close_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoubleLiveHostActivity.this, DoubleLiveResultActivity_.class));
                finish();
            }
        });
    }


    private boolean supportBtnStyle(View v, boolean support) {
        ImageButton support_ib = (ImageButton) v;
        if (!support) {
            support_ib.startAnimation(supportAnim);
            double_live_agree1.startAnimation(supportAnim);
            double_live_agree2.startAnimation(supportAnim);
            support_ib.setImageResource(R.drawable.bottom_nav_recommend_red);
            return true;
        } else
            support_ib.setImageResource(R.drawable.bottom_nav_recommend);
        return false;

    }

    private String playUrl2 = "rtmp://4315.mpull.live.lecloud.com/live/1111?tm=20160806193209&sign=0495686b48e72fc4245c45574efe5b60";
    private Bundle mBundle;

    public void setLive() {
        live_view1 = (RelativeLayout) view2.findViewById(R.id.live_view1);

        mBundle = new Bundle();
        mBundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_LIVE);
        mBundle.putString("path", playUrl2);
        new LivePlayBase(DoubleLiveHostActivity.this, playUrl2, mBundle, live_view1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 皮肤于推流器关联
     */
    private void bindingPublish() {
        recorderSkinMobile.BindingPublisher(publisher);
        publisher.setCameraView(recorderSkinMobile.getCameraView());
        publisher.setFocusView(focusView);
    }

    /**
     * 初始化皮肤
     */
    private void initSkin(boolean isScreenOrientation) {
        recorderSkinMobile = new RecorderSkinMobile(LetvFlag.Skin_No_Bottom);
        recorderSkinMobile.setPushSteamUrl(pushSteamUrl);
        recorderSkinMobile.setStreamName(pushName);
        if(isScreenOrientation){
            recorderSkinMobile.build(this, rv, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            recorderSkinMobile.build(this, rv,ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    public void change(boolean isScreenOrientation){
        if(isScreenOrientation){
            recorderSkinMobile.build(this, rv,ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            recorderSkinMobile.build(this, rv,ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }


    /**
     * 初始化推流器
     */
    private void initPublish() {
        publisher = Publisher.getInstance();
        if(isVertical){
            /// 竖屏状态
            publisher.getRecorderContext().setUseLanscape(false);
        }else{
            /// 横屏状态
            publisher.getRecorderContext().setUseLanscape(true);
        }
        publisher.initPublisher(this);
    }
}
