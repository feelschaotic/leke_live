package com.juss.live.skin.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.letv.recorder.controller.Publisher;
import com.letv.recorder.ui.logic.RecorderConstance;
import com.letv.recorder.ui.logic.UiObservable;
import com.letv.recorder.util.LeLog;
import com.letv.recorder.util.ReUtils;

import java.util.Observable;
import java.util.Observer;

public class RecorderTopFloatView extends RelativeLayout implements Observer {
	private static final String TAG = "CameraView2";
	protected Context context;
	private ImageView leftArrayBtn;
	private TextView topTitle;
	private ImageView chCamBtn;
	private ImageView micBtn;
	private ImageView flashBtn;
	protected Publisher publisher;

	private boolean useMic = true;

	public boolean isUserMic() {
		return useMic;
	}

	public void setUserMic(boolean userMic) {
		this.useMic = userMic;
	}

	private boolean useBackCam = true;

	public boolean isUseBackCam() {
		return useBackCam;
	}

	public void setUseBackCam(boolean useBackCam) {
		this.useBackCam = useBackCam;
	}

	private boolean useFlash = false;

	public boolean isUseFlash() {
		return useFlash;
	}

	public void setUseFlash(boolean useFlash) {
		this.useFlash = useFlash;
	}

	private UiObservable topSubject = new UiObservable();

	public UiObservable getTopSubject() {
		return topSubject;
	}

	public RecorderTopFloatView(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	public RecorderTopFloatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	protected void initView() {
		LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_recorder_top_float_layout"), this);
		leftArrayBtn = (ImageView) findViewById(ReUtils.getId(context, "letv_recorder_left_arraw"));
		leftArrayBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putInt("flag", RecorderConstance.top_float_back);
				getTopSubject().notifyObserverPlus(bundle);
			}
		});

		topTitle = (TextView) findViewById(ReUtils.getId(context, "letv_recorder_top_title"));
		// 切换摄像头
		chCamBtn = (ImageView) findViewById(ReUtils.getId(context, "letv_recorder_top_change_cam"));
		chCamBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();

				if (useBackCam) {
					bundle.putInt("flag", RecorderConstance.use_front_cam);

					getTopSubject().notifyObserverPlus(bundle);
					useBackCam = false;
					
					// 使用前置摄像头的时候关闭闪光灯
					flashBtn.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_flash_gray"));
					useFlash = false;

				} else {
					flashBtn.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_flash_white"));
					
					bundle.putInt("flag", RecorderConstance.use_back_cam);
					getTopSubject().notifyObserverPlus(bundle);
					useBackCam = true;
				}
			}
		});

		// 麦克风
		micBtn = (ImageView) findViewById(ReUtils.getId(context, "letv_recorder_top_mic"));
		micBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();

				if (useMic) {
					bundle.putInt("flag", RecorderConstance.disable_mic);
					getTopSubject().notifyObserverPlus(bundle);
					micBtn.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_mic_white"));
					useMic = false;
				} else {

					bundle.putInt("flag", RecorderConstance.enable_mic);

					getTopSubject().notifyObserverPlus(bundle);
					micBtn.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_mic_blue"));
					useMic = true;
				}
			}
		});

		// 闪光灯
		flashBtn = (ImageView) findViewById(ReUtils.getId(context, "letv_recorder_flash"));
		flashBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();

				if (useFlash) {
					bundle.putInt("flag", RecorderConstance.disable_flash);
					getTopSubject().notifyObserverPlus(bundle);
					flashBtn.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_flash_white"));
					useFlash = false;
				} else {
					if(useBackCam){
						bundle.putInt("flag", RecorderConstance.enable_flash);
						getTopSubject().notifyObserverPlus(bundle);
						flashBtn.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_flash_blue"));
						useFlash = true;
					}else{
						Toast.makeText(context, "该摄像头不支持闪光灯", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	public void buildPublisher(Publisher publisher){
		this.publisher = publisher;
	}
	public void updateTitle(String title) {
		topTitle.setText(title);
	}

	/**
	 * 观察者
	 */
	@Override
	public void update(Observable observable, Object data) {
		Bundle bundle = (Bundle) data;
		int flag = bundle.getInt("flag");
		if (RecorderConstance.recorder_start == flag) {
			LeLog.d(TAG, "[oberver] recorder_start |recorderTopFlatView");
//			this.setVisibility(View.GONE);
		} else if (RecorderConstance.recorder_stop == flag) {

			LeLog.d(TAG, "[oberver] recorder_stop |recorderTopFlatView");
//			this.setVisibility(View.VISIBLE);
		}
	}

}
