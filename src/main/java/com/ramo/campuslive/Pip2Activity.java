package com.ramo.campuslive;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juss.live.LivePlayBase;
import com.juss.live.skin.simple.utils.LetvNormalAndPanoHelper;
import com.letv.controller.PlayContext;
import com.letv.controller.PlayProxy;
import com.letv.universal.iplay.EventPlayProxy;
import com.letv.universal.iplay.ISplayer;
import com.ramo.campuslive.application.MyApplication;

public class Pip2Activity extends Activity{

    private final static String DATA="data";
    private ISplayer player;
    private String path = "";
    private PlayContext playContext;
    private SurfaceView videoView;
    private Bundle mBundle;
    private long lastPosition;
    private TextView console;

    // surfaceView的生命周期
    private LetvNormalAndPanoHelper playHelper;
    RelativeLayout layout1;
    RelativeLayout layout2;
    RelativeLayout layout3;

    RelativeLayout view1;


    View view;

    private String playUrl1= MyApplication.themePath[2];
    private String playUrl2=MyApplication.themePath[1];
    private String playUrl3=MyApplication.themePath[0];

//    private String playUrl2="rtmp://4315.mpull.live.lecloud.com/live/1111?tm=20160809193430&sign=5867a84aa1b8091260c6f150161f0f48";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
     //   getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pip2);


        RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.layout1);
        RelativeLayout layout2 = (RelativeLayout) findViewById(R.id.layout2);
        RelativeLayout layout3 = (RelativeLayout) findViewById(R.id.layout3);



        mBundle = new Bundle();
        mBundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_LIVE);
        mBundle.putString("path", playUrl1);
//        view1 =(RelativeLayout) findViewById(R.id.view1);
        new LivePlayBase(this,playUrl1,mBundle,layout1);

        Bundle mBundle2 = new Bundle();
        mBundle2.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_LIVE);
        mBundle2.putString("path", playUrl2);
//        layout =(RelativeLayout) findViewById(R.id.view2);

        Bundle mBundle3 = new Bundle();
        mBundle2.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_LIVE);
        mBundle2.putString("path", playUrl3);
        new LivePlayBase(this,playUrl1,mBundle,layout1);
        new LivePlayBase(this,playUrl2,mBundle2,layout2);
        new LivePlayBase(this,playUrl3,mBundle3,layout3);



    }

}
