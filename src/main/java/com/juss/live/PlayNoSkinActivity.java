package com.juss.live;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.juss.live.skin.simple.utils.GetDeviceInfo;
import com.juss.live.skin.simple.utils.LetvNormalAndPanoHelper;
import com.juss.live.skin.simple.utils.LetvParamsUtils;
import com.juss.live.skin.simple.utils.PlayerFactory;
import com.lecloud.entity.ActionInfo;
import com.lecloud.entity.LiveInfo;
import com.letv.controller.PlayContext;
import com.letv.controller.PlayProxy;
import com.letv.pano.ISurfaceListener;
import com.letv.pano.OnPanoViewTapUpListener;
import com.letv.pano.PanoVideoControllerView;
import com.letv.pano.PanoVideoView;
import com.letv.universal.iplay.EventPlayProxy;
import com.letv.universal.iplay.ISplayer;
import com.letv.universal.iplay.OnPlayStateListener;
import com.letv.universal.play.util.PlayerParamsHelper;
import com.letv.universal.widget.ReSurfaceView;
import com.ramo.campuslive.PersonalCenterActivity_;
import com.ramo.campuslive.R;
import com.ramo.campuslive.utils.L;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 无皮肤版demo
 */
public class PlayNoSkinActivity extends Activity implements OnPlayStateListener {
    public final static String DATA = "data";

    private ISplayer player;
    private String path = "";
    private PlayContext playContext;
    private SurfaceView videoView;
    private Bundle mBundle;
    private long lastPosition;
    private TextView console;

    private LinearLayout letv_host_info_ll;

    // surfaceView的生命周期
    private LetvNormalAndPanoHelper playHelper;
    private Callback surfaceCallback = new Callback() {
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            stopAndRelease();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (player != null) {
                player.setDisplay(holder.getSurface());
            } else {
                createOnePlayer(holder.getSurface());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (player != null) {
                PlayerParamsHelper.setViewSizeChange(player, width, height);
            }
        }
    };

    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    // 处理activity生命周期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_no_skin);

        initDanmuConsole();

        loadDataFromIntent();// 加载数据
        L.e(path);

        RelativeLayout view = (RelativeLayout) findViewById(R.id.videoContainer);

        new LivePlayBase(this,path,mBundle,view);



       /* initPlayContext();
        //    initNormalVideoView();//注释
        createOnePlayer(null);*/
    }

    private void initDanmuConsole() {
        letv_host_info_ll = (LinearLayout) findViewById(R.id.letv_host_info_ll);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.live_bottom_console_rl);
        LiveConsole console = new LiveConsole(this, rl);
        console.initMyDanMuView();
        initListener();
    }

    private void initListener() {
        letv_host_info_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayNoSkinActivity.this, PersonalCenterActivity_.class));
            }
        });
    }

    private void loadDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mBundle = intent.getBundleExtra(DATA);
            if (mBundle == null) {
                Toast.makeText(this, "no data", Toast.LENGTH_LONG).show();
                return;
            } else {
                path = mBundle.getString("path");
            }
        }
        //   Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();
      /*  path="rtmp://4315.mpull.live.lecloud.com/live/1111?tm=20160805154842&sign=615ca4beacebc308729a395c313b908b";
        L.e(path);*/
        //  path="rtmp://4315.mpull.live.lecloud.com/live/1111?tm=20160802175045&sign=c2fdde82eabc8fa403ed8917416bd61b";
    }

    private void initPlayContext() {
        playContext = new PlayContext(this);
        playContext.setUsePlayerProxy(TextUtils.isEmpty(path));
    }

    private void initNormalVideoView() {
        if (videoView == null || !(videoView instanceof ReSurfaceView)) {
            ReSurfaceView videoView = new ReSurfaceView(this);
            videoView.setVideoContainer(null);
            this.videoView = videoView;
            addVideoView();
        }
    }


    private void addVideoView() {
        videoView.getHolder().addCallback(surfaceCallback);
        int width = GetDeviceInfo.getScreenWidth(this);
        int height = GetDeviceInfo.getScreenHeight(this);
        //   int height = width * 9/16;
        Log.e("tag", player.getVideoWidth() + "");
        Log.e("tag", player.getVideoHeight() + "");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        if (player.getVideoWidth() != 0 && player.getVideoHeight() == 0) {
            double ratio = Math.min(((double) width / player.getVideoWidth()), ((double) height / player.getVideoHeight()));
            params = new RelativeLayout.LayoutParams((int) ratio * player.getVideoWidth(), (int) ratio * player.getVideoHeight());
        }
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        RelativeLayout videoContainer = (RelativeLayout) findViewById(R.id.videoContainer);
        videoContainer.getLayoutParams().width = width;
        videoContainer.getLayoutParams().height = height;
        videoContainer.addView(videoView, params);


    }
     /*private void addVideoView() {
//    	double ratio = Math.min(((double)GetScreenInfo.getScreenWidth(this))/player.getVideoWidth(), ((double)GetScreenInfo.getScreenHeight(this))/player.getVideoHeight());
        videoView.getHolder().addCallback(surfaceCallback);
        int width = getScreenWidth(this);
        int height = width * 9/16;
        double ratio = Math.min(((double)width/player.getVideoWidth()), ((double)height/player.getVideoHeight()));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)ratio*player.getVideoWidth(),(int)ratio*player.getVideoHeight());
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        RelativeLayout videoContainer = (RelativeLayout) findViewById(R.id.videoContainer);
        videoContainer.addView(videoView.getMysef(), params);
        videoContainer.getLayoutParams().width = width;
        videoContainer.getLayoutParams().height = height;
    }*/


    private void initPanoVideoView() {
        if (videoView == null || !(videoView instanceof PanoVideoView)) {
            final PanoVideoView panoVideoView = new PanoVideoControllerView(this);
            panoVideoView.registerSurfacelistener(new ISurfaceListener() {

                @Override
                public void setSurface(Surface surface) {
                    player.setDisplay(surface);
                }
            });

            //设置手势操作层的touch事件
            //如果手势不起作用有可能是您的layout把panovideoview的手势事件覆盖 这里也可以设置您的layout中最上层view
            panoVideoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return panoVideoView.onPanoTouch(v, event);
                }
            });
            //设置video的单击事件 通知上层唤醒播控控件等
            panoVideoView.setTapUpListener(new OnPanoViewTapUpListener() {
                @Override
                public void onSingleTapUp(MotionEvent e) {

                }
            });
            panoVideoView.init();
            videoView = panoVideoView;
            addVideoView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playContext != null) {
            playContext.destory();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (videoView != null && videoView instanceof ReSurfaceView) {
            /**
             * 当屏幕旋转的时候，videoView需要全屏居中显示, 如果用户使用自己的view显示视频（比如SurfaceView）,
             * 比较简单的方法是：对surfaceView的layourParams()进行设置。 1）竖屏转横屏的时候，可以占满全屏居中显示；
             * 2）横屏转竖屏时，需要设置layoutParams()恢复之前的显示大小
             *
             */
            ((ReSurfaceView) videoView).setVideoLayout(-1, 0);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * 初始化PlayContext PlayContext作为播放器的上下文而存在，既保存了播放器运行时的临时数据，也记录播放器所需要的环境变量。
     */


    /**
     * 停止和释放播放器
     */
    private void stopAndRelease() {
        if (player != null) {
            lastPosition = player.getCurrentPosition();
            player.stop();
            player.reset();
            player.release();
            player = null;
        }
    }

    /**
     * 创建一个新的播放器
     *
     * @param surface
     */
    private void createOnePlayer(Surface surface) {
        player = PlayerFactory.createOnePlayer(playContext, mBundle, this, surface);
        if (!TextUtils.isEmpty(path)) {
            playContext.setUsePlayerProxy(false);
        }
        player.setDataSource(path);
        if (lastPosition > 0 && mBundle.getInt(PlayProxy.PLAY_MODE) == EventPlayProxy.PLAYER_VOD) {
            player.seekTo(lastPosition);
        }
        player.prepareAsync();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * 播放器回调
     */
    @Override
    public void videoState(int state, Bundle bundle) {
        handleADEvent(state, bundle);// 处理广告事件
        handleVideoInfoEvent(state, bundle);// 处理视频信息事件
        handlePlayerEvent(state, bundle);// 处理播放器事件
        handleLiveEvent(state, bundle);// 处理直播类事件,如果是点播，则这些事件不会回调
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * 处理视频信息类事件
     *
     * @param state
     * @param bundle
     */
    private void handleVideoInfoEvent(int state, Bundle bundle) {
        switch (state) {
            case EventPlayProxy.PROXY_WAITING_SELECT_DEFNITION_PLAY:// 获取码率
                /**
                 * 处理码率
                 */
                if (playContext != null) {
                    Map<Integer, String> definationsMap = playContext.getDefinationsMap();// 获取到的码率
                    if (definationsMap != null && definationsMap.entrySet() != null) {
                        Iterator<Entry<Integer, String>> iterator = definationsMap.entrySet().iterator();
                        while (iterator != null && iterator.hasNext()) {
                            Entry<Integer, String> next = iterator.next();
                            Integer key = next.getKey();// 码率所对于的key值,key值用于切换码率时，方法playedByDefination(type)所对于的值
                            String value = next.getValue();// 码率名字，比如：标清，高清，超清
                        }
                    }
                }
                break;
            case EventPlayProxy.PROXY_VIDEO_INFO_REFRESH:// 获取视频信息，比如title等等
                break;
            case ISplayer.PLAYER_PROXY_ERROR:// 请求媒体资源信息失败
                int errorCode = bundle.getInt("errorCode");
                String msg = bundle.getString("errorMsg");
                break;
        }
    }


    /**
     * 处理播放器本身事件
     *
     * @param state
     * @param bundle
     */
    private void handlePlayerEvent(int state, Bundle bundle) {
        switch (state) {

            case ISplayer.MEDIA_EVENT_VIDEO_SIZE:
                if (videoView != null && player != null) {
                    /**
                     * 获取到视频的宽高的时候，此时可以通过视频的宽高计算出比例，进而设置视频view的显示大小。
                     * 如果不按照视频的比例进行显示的话，(以surfaceView为例子)内容会填充整个surfaceView。
                     * 意味着你的surfaceView显示的内容有可能是拉伸的
                     */
                    if (videoView instanceof ReSurfaceView) {
                        ((ReSurfaceView) videoView).onVideoSizeChange(player.getVideoWidth(), player.getVideoHeight());
                    }

                    /**
                     * 获取宽高的另外一种方式
                     */
                    bundle.getInt("width");
                    bundle.getInt("height");
                }
                break;

            case ISplayer.MEDIA_EVENT_PREPARE_COMPLETE:// 播放器准备完成，此刻调用start()就可以进行播放了
                if (player != null) {
                    player.start();
                }
                break;

            case ISplayer.MEDIA_EVENT_FIRST_RENDER:// 视频第一帧数据绘制
                break;
            case ISplayer.MEDIA_EVENT_PLAY_COMPLETE:// 视频播放完成
                break;
            case ISplayer.MEDIA_EVENT_BUFFER_START:// 开始缓冲
                break;
            case ISplayer.MEDIA_EVENT_BUFFER_END:// 缓冲结束
                break;
            case ISplayer.MEDIA_EVENT_SEEK_COMPLETE:// seek完成
                break;
            case ISplayer.MEDIA_ERROR_DECODE_ERROR:// 解码错误
                break;
            case ISplayer.MEDIA_ERROR_NO_STREAM:// 播放器尝试连接媒体服务器失败
                break;
            case ISplayer.PLAYER_PROXY_ERROR:
                int errorCode = bundle.getInt("errorCode");
                String errorMsg = bundle.getString("errorMsg");
                if (errorCode == EventPlayProxy.CDE_ERROR_OVERLOAD_PROTECTION) {//过载保护处理
                }
                break;
            case ISplayer.PLAYER_EVENT_PREPARE_VIDEO_VIEW:
                boolean pano = bundle != null ? bundle.getBoolean("pano", false) : false;
                if (pano || mBundle.getBoolean(LetvParamsUtils.IS_LOCAL_PANO)) {
                    initPanoVideoView();
                } else {
                    initNormalVideoView();
                }
                playContext.setVideoContentView(videoView);
                break;
            default:
                break;
        }
    }

    /**
     * 处理直播类事件
     */
    private void handleLiveEvent(int state, Bundle bundle) {
        switch (state) {
            case EventPlayProxy.PROXY_SET_ACTION_LIVE_CURRENT_LIVE_ID:// 获取当前活动直播的id
                String liveId = bundle.getString("liveId");
                break;
            case EventPlayProxy.PROXY_WATING_SELECT_ACTION_LIVE_PLAY:// 当收到该事件后，用户可以选择优先播放的活动直播
                ActionInfo actionInfo = playContext.getActionInfo();
                // 查找正在播放的直播 或者 可以秒转点播的直播信息
                LiveInfo liveInfo = actionInfo.getFirstCanPlayLiveInfo();
                if (liveInfo != null) {
                    playContext.setLiveId(liveInfo.getLiveId());
                }
                break;
            default:
                break;
        }
    }

    /**
     * 处理广告事件
     *
     * @param state
     * @param bundle
     */
    private void handleADEvent(int state, Bundle bundle) {
        switch (state) {
            case EventPlayProxy.PLAYER_PROXY_AD_START:// 广告开始
                break;
            case EventPlayProxy.PLAYER_PROXY_AD_END:// 广告播放结束
                break;
            case EventPlayProxy.PLAYER_PROXY_AD_POSITION:// 广告倒计时
                int position = bundle.getInt(String.valueOf(EventPlayProxy.PLAYER_PROXY_AD_POSITION));// 获取倒计时
                break;
            default:
                break;
        }
    }

}

