package com.juss.live.skin.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.letv.recorder.controller.Publisher;
import com.letv.recorder.ui.logic.RecorderConstance;
import com.letv.recorder.ui.logic.UiObservable;
import com.letv.recorder.util.LeLog;
import com.letv.recorder.util.NetworkUtils;
import com.letv.recorder.util.ReUtils;
import com.ramo.campuslive.utils.L;

import java.util.Observer;

public class RecorderView extends RelativeLayout implements Callback {

    private static final String TAG = "CameraView2";
    private Context context;
    private FrameLayout topContainer;
    private FrameLayout bottomContainer;

    private CaptureBtn startBtn;// 开始按钮

    private SurfaceView surfaceView;
    private RelativeLayout surfaceContainer;
    private Publisher publisher;


    public RecorderView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public RecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_recorder_main_layout"), this);
        topContainer = (FrameLayout) findViewById(ReUtils.getId(context, "letv_recorder_top_container"));
        bottomContainer = (FrameLayout) findViewById(ReUtils.getId(context, "letv_recorder_bottom_container"));
        surfaceContainer = (RelativeLayout) findViewById(ReUtils.getId(context, "letv_recorder_surface_container"));
        attachStartBtn();
        // 初始化日志模块，保证日志放在android/data/包名/push_stream 下面
        LeLog.init(context);

    }


    /**
     * 加入surfaceView
     *
     * @param surfaceView
     */

    public void attachSurfaceView(SurfaceView surfaceView) {
        if (this.surfaceView != null) {
            surfaceContainer.removeView(this.surfaceView);
            this.surfaceView.getHolder().removeCallback(this);
        }

        this.surfaceView = surfaceView;
        this.surfaceView.getHolder().addCallback(this);
        surfaceContainer.addView(surfaceView);
    }

    /**
     * 添加头部浮层
     *
     * @param topView
     */
    public void attachTopFloatView(View topView) {
        topContainer.removeAllViews();
        topContainer.addView(topView);
    }

    /**
     * 添加底部浮层
     *
     * @param bottomView
     */
    public void attachBomttomView(View bottomView) {
        bottomContainer.removeAllViews();
        if (bottomView instanceof Observer) {
            startBtn.getStartSubject().deleteObserver((Observer) bottomView);
        }

        //LayoutParams lp= (LayoutParams) startBtn.getLayoutParams();

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        bottomContainer.addView(bottomView, params);


        if (bottomView instanceof Observer) {// 添加观察者
            startBtn.getStartSubject().addObserver((Observer) bottomView);
        }
    }

    public void buildPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    /**
     * 添加中部浮层
     *
     * @param centerView
     */
    public void attachCenterView(View centerView) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(centerView, params);
        centerView.setVisibility(View.GONE);

//		if(centerView instanceof RecorderAngleView){
//			this.angleView=(RecorderAngleView) centerView;
//			this.angleView.addObserver(this);
//		}
    }

    public void reSetStartBtn(boolean falg) {
        if (startBtn != null) {
            if (falg) {
                startBtn.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_stop"));
            } else {
                startBtn.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_open"));
            }
        }
    }

    /**
     * 加入播放按钮
     */
    public void attachStartBtn() {

        startBtn = new CaptureBtn(context);
        startBtn.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_open"));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        //TODO:在新的UI中，按钮是靠左放置的，所以这里改动一下
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        this.addView(startBtn, params);


        //设置大小
        android.view.ViewGroup.LayoutParams layoutParams = startBtn.getLayoutParams();
        layoutParams.height = dip2px(context, 56);
        layoutParams.width = dip2px(context, 56);
        startBtn.setLayoutParams(layoutParams);
    }


    public void stopAuto() {
        startBtn.stopRecorder();
    }

    public UiObservable getStartSubject() {
        return startBtn.getStartSubject();
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public void startRecorder() {
        startBtn.startRecorder();
    }

    public void stopRecorder() {
        startBtn.stopRecorder();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * 开始按钮
     *
     * @author pys
     */
    private class CaptureBtn extends ImageView implements OnClickListener {
        private UiObservable startSubject = new UiObservable();
        private Dialog mobileNetworkDialog;

        public CaptureBtn(Context context, AttributeSet attrs) {
            super(context, attrs);
            initView();
        }

        public CaptureBtn(Context context) {
            super(context);
            initView();
        }

        private void initView() {
            setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (publisher == null || !publisher.isRecording()) {
//				this.startRecorder();
                selectAngle();
            } else {
                this.stopRecorder();
            }
        }

        /**
         * 选择机位
         */
        private void selectAngle() {
            final Bundle bundle = new Bundle();

            /**
             * wifi环境下
             */
            if (NetworkUtils.isWifiNetType(context)) {
                bundle.putInt("flag", RecorderConstance.angle_request);
                getStartSubject().notifyObserverPlus(bundle);
            } else {
                if (NetworkUtils.getNetType(context) != null && !NetworkUtils.isWifiNetType(context)) {//mobile网络环境下
                    mobileNetworkDialog = RecorderDialogBuilder.showMobileNetworkWarningDialog(context, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mobileNetworkDialog != null) {
                                mobileNetworkDialog.dismiss();
                                if (publisher != null && !publisher.isRecording()) {
                                    bundle.putInt("flag", RecorderConstance.angle_request);
                                    getStartSubject().notifyObserverPlus(bundle);
                                }
                            }

                        }
                    }, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mobileNetworkDialog != null) {
                                mobileNetworkDialog.dismiss();
                            }
                        }
                    });

                } else {
                    mobileNetworkDialog = RecorderDialogBuilder.showCommentDialog(context, "网络连接失败,请检查后重试", "我知道了", null, new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mobileNetworkDialog.dismiss();
                            mobileNetworkDialog = null;
                        }
                    }, null);

                }
            }


        }

        public void startRecorder() {
            startBtn.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_stop"));
            Bundle bundle = new Bundle();
            bundle.putInt("flag", RecorderConstance.recorder_start);
            getStartSubject().notifyObserverPlus(bundle);
        }

        public void stopRecorder() {
            L.e("oooooooo");
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("真的要关闭直播吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startBtn.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_open"));
                    Bundle bundle = new Bundle();
                    bundle.putInt("flag", RecorderConstance.recorder_stop);
                    getStartSubject().notifyObserverPlus(bundle);

                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
           /* startBtn.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_open"));
            Bundle bundle = new Bundle();
            bundle.putInt("flag", RecorderConstance.recorder_stop);
            getStartSubject().notifyObserverPlus(bundle);// 通知观察者*/
        }

        public UiObservable getStartSubject() {
            return startSubject;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


//	/**
//	 * 观察者
//	 */
//	@Override
//	public void update(Observable observable, Object data) {
//		Bundle bundle=(Bundle) data;
//		int flag=bundle.getInt("flag");
//		if(RecorderConstance.selected_angle==flag){
//			Log.d(TAG, "[oberver] selected_angle |recorderView");
//		}
//	}

}
