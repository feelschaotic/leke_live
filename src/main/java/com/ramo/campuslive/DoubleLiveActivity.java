package com.ramo.campuslive;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juss.live.LivePlayBase;
import com.letv.controller.PlayProxy;
import com.letv.universal.iplay.EventPlayProxy;
import com.ramo.campuslive.utils.L;
import com.ramo.campuslive.utils.T;


public class DoubleLiveActivity extends Activity {



    private View view1;
    private View view2;

    private ImageButton double_live_support1;
    private ImageButton double_live_support2;
    private TextView double_live_agree1;
    private TextView double_live_agree2;
    private boolean support1 = false;
    private boolean support2 = false;

    private String rootPath = Environment.getExternalStorageDirectory().getPath();

    private Animation supportAnim;
    private ImageView double_live_close_iv;

    private RelativeLayout live_view1;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_live);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);


//        sv = (SurfaceView) view1.findViewById(R.id.sv);
        double_live_support1 = (ImageButton) view1.findViewById(R.id.double_live_support);
//        sv2 = (SurfaceView) view2.findViewById(R.id.sv);
        double_live_support2 = (ImageButton) view2.findViewById(R.id.double_live_support);

        double_live_agree1 = (TextView) findViewById(R.id.double_live_agree1);
        double_live_agree2 = (TextView) findViewById(R.id.double_live_agree2);

        double_live_close_iv = (ImageView) findViewById(R.id.double_live_close_iv);
        supportAnim = AnimationUtils.loadAnimation(this, R.anim.double_live_support_anim);
        initListener();
        setLive();
    }

    private void initListener() {

        double_live_support1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                support1 = supportBtnStyle(v, support1);
            }
        });
        double_live_support2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                support2 = supportBtnStyle(v, support2);
            }
        });
        double_live_close_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoubleLiveActivity.this, DoubleLiveResultActivity_.class));
                finish();
            }
        });
    }

    boolean isSupport = false;

    private boolean supportBtnStyle(View v, boolean support) {
        ImageButton support_ib = (ImageButton) v;
        L.e(support1 + " : " + support2 + " : " + support);
        if (!support) {
            if (!isSupport) {
                support_ib.startAnimation(supportAnim);
                double_live_agree1.startAnimation(supportAnim);
                double_live_agree2.startAnimation(supportAnim);
                support_ib.setImageResource(R.drawable.bottom_nav_recommend_red);
                isSupport = true;
            } else {
                T.showShort(this, "不要贪心，只能点赞1位喔");
                return false;
            }

        } else {
            support_ib.setImageResource(R.drawable.bottom_nav_recommend);
            isSupport = false;
        }
        return isSupport;
    }
    private String playUrl1="rtmp://4315.mpull.live.lecloud.com/live/10011?tm=20160919021422&sign=ee0666b949ca77028b510df3b7a7accc";
    private String playUrl2="rtmp://216.mpull.live.lecloud.com/live/111111?tm=20160919013752&sign=c6186b6c5212485586abe74795aea9c0";
    private Bundle mBundle;

    public void setLive(){
        live_view1 = (RelativeLayout) view1.findViewById(R.id.live_view1);
        RelativeLayout live_view2= (RelativeLayout) view2.findViewById(R.id.live_view1);

        mBundle = new Bundle();
        mBundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_LIVE);
        mBundle.putString("path", playUrl1);
        Bundle mBundle2 = new Bundle();
        mBundle2.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_LIVE);
        mBundle2.putString("path", playUrl2);
        new LivePlayBase(DoubleLiveActivity.this,playUrl1,mBundle,live_view1);
        new LivePlayBase(DoubleLiveActivity.this,playUrl2,mBundle2,live_view2);
    }
}
