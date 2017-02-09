package com.juss.live.skin.ui.mobile;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juss.live.skin.ui.RecorderTopFloatView;
import com.letv.recorder.ui.logic.RecorderConstance;
import com.letv.recorder.util.ReUtils;


import java.util.Formatter;
import java.util.Locale;
import java.util.Observable;

public class RecorderTopFloatMobileView extends RecorderTopFloatView {
	private ImageView backButton;
	private TextView titleView;
	private TextView timeView;
	private ImageView recordeView;
	private TextView recView;
	private int time = 0;
	private StringBuilder mFormatBuilder = new StringBuilder();
	private Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
	private Handler handler = new Handler();
	private String title;
	private boolean useRec = true;

	public RecorderTopFloatMobileView(Context context) {
		this(context,null);
	}
	public RecorderTopFloatMobileView(Context context, AttributeSet attrs){
		super(context,attrs);
	}

	protected void initView() {
		LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_recorder_top_float_mobile_layout"), this);
		backButton = (ImageView) findViewById(ReUtils.getId(context, "imgB_back"));
		titleView = (TextView) findViewById(ReUtils.getId(context, "tv_title"));
		titleView.setText("");
		timeView = (TextView) findViewById(ReUtils.getId(context, "tv_time"));
		recordeView = (ImageView) findViewById(ReUtils.getId(context, "imgV_thumd"));
		recView = (TextView) findViewById(ReUtils.getId(context, "tv_rec"));
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putInt("flag", RecorderConstance.top_float_back);
				getTopSubject().notifyObserverPlus(bundle);
			}
		});
	}
	
	@Override
	public void update(Observable observable, Object data) {
		Bundle bundle = (Bundle) data;
		int flag = bundle.getInt("flag");
		switch (flag) {
		case RecorderConstance.recorder_start:
			startRecorderView();
			break;
		case RecorderConstance.recorder_stop:
			stopRecorderView();
			break;
		case RecorderConstance.enable_rec:
			useRec = true;
			break;
		case RecorderConstance.disable_rec:
			useRec = false;
			recView.setVisibility(View.INVISIBLE);
			break;

		default:
			break;
		}
	}
	/**
	 * 结束推流时视图展示
	 */
	private void stopRecorderView() {
		timeView.setVisibility(View.INVISIBLE);
		recordeView.setVisibility(View.INVISIBLE);
		recView.setVisibility(View.INVISIBLE);	
		handler.removeCallbacks(timeRunnable);
		timeView.setText("00:00:00");
		time = 0;
	}
	/**
	 * 开始推流时视图展示
	 */
	private void startRecorderView() {
		timeView.setVisibility(View.VISIBLE);
		recordeView.setVisibility(View.VISIBLE);
		if(useRec){
			recView.setVisibility(View.VISIBLE);	
		}
		handler.postDelayed(timeRunnable,1000);
	}
	
	Runnable timeRunnable = new Runnable() {
		@Override
		public void run() {
			if(publisher != null && publisher.isRecording()){
				time++;
				timeView.setText(stringForTime(time));
				if (time % 2 == 0) {
					recordeView.setVisibility(View.VISIBLE);
				} else {
					recordeView.setVisibility(View.INVISIBLE);
				}
			}
			handler.postDelayed(this, 1000);
		}
	};
	/**
	 * 格式化时间
	 * 
	 * @param timeMs
	 * @return
	 */
	private String stringForTime(int timeMs) {
		int totalSeconds = timeMs;
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;
		mFormatBuilder.setLength(0);
		return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
	}
	public void setTitle(String title) {
		this.title = title;
		if(this.title != null){
			titleView.setText(title);
		}
	}
}
