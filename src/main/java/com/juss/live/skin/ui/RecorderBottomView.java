package com.juss.live.skin.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.letv.recorder.controller.Publisher;
import com.letv.recorder.ui.logic.RecorderConstance;
import com.letv.recorder.ui.logic.UiObservable;
import com.letv.recorder.util.ReUtils;

import java.util.Formatter;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class RecorderBottomView extends RelativeLayout implements Observer {
	private static final String TAG = "RecorderBottomView";
	protected Context context;
	private TextView peopleCount;
	private TextView recorderTime;
	private TextView rec;
	private ImageView thumd;
	private StringBuilder mFormatBuilder = new StringBuilder();
	private Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
	protected Publisher publisher;
	private UiObservable bottomSubject = new UiObservable();

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

		};
	};
	public UiObservable getBottomSubject() {
		return bottomSubject;
	}
	public RecorderBottomView(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	public RecorderBottomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	protected void initView() {
		LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_recorder_bottom_float_layout"), this);
		peopleCount = (TextView) findViewById(ReUtils.getId(context, "letv_recorder_bottom_people"));
		thumd = (ImageView) findViewById(ReUtils.getId(context, "letv_recorder_bottom_thumd"));
		recorderTime = (TextView) findViewById(ReUtils.getId(context, "letv_recorder_bottom_time"));
		rec = (TextView) findViewById(ReUtils.getId(context, "letv_recorder_bomttom_rec"));
		thumd.setVisibility(View.INVISIBLE);
		recorderTime.setVisibility(View.INVISIBLE);
		rec.setVisibility(View.INVISIBLE);
		recorderTime.setText(stringForTime(0));
	}

	/**
	 * 播放观察者
	 */
	@Override
	public void update(Observable observable, Object data) {
		final Bundle bundle = (Bundle) data;
		int flag = bundle.getInt("flag");

		switch (flag) {
		case RecorderConstance.recorder_start:
			Log.d(TAG, "[observer recorder_start |recorderBottomView]");
			startInternal();
			handler.post(timeRunnable);
			break;
		case RecorderConstance.recorder_stop:
			// handler.removeCallbacks(timeRunnable);
			// resetInternal();
			Log.d(TAG, "[observer recorder_stop |recorderBottomView]");
			reset();
			break;
		case RecorderConstance.count_people:

			handler.post(new Runnable() {
				@Override
				public void run() {
					String countPeople = bundle.getString("count");
					peopleCount.setText(countPeople);

				}
			});
		case RecorderConstance.enable_rec:
			setRecVisible(true);
			break;
		case RecorderConstance.disable_rec:
			setRecVisible(false);
			break;
		}
	}

	public void reset() {
		resetInternal();
		reSetRunnable();
	}

	public void reSetRunnable() {
		synchronized (handler) {
			handler.removeCallbacks(timeRunnable);
		}
	}

	/**
	 * 恢复初始状态
	 */
	private void resetInternal() {
		recorderTime.setText(stringForTime(0));
		thumd.setVisibility(View.INVISIBLE);
		recorderTime.setVisibility(View.INVISIBLE);
		rec.setVisibility(View.INVISIBLE);
		time = 0;
	}

	public void startInternal() {
		thumd.setVisibility(View.VISIBLE);
		recorderTime.setVisibility(View.VISIBLE);
	}

	public void setRecVisible(boolean isNeedRecord) {
		if (isNeedRecord) {
			rec.setVisibility(View.VISIBLE);
		} else {
			rec.setVisibility(View.INVISIBLE);
		}
	}

	public void clear() {
		reSetRunnable();
		recorderTime.setText(stringForTime(0));
		thumd.setVisibility(View.VISIBLE);
		recorderTime.setVisibility(View.VISIBLE);
	}

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
		// if (hours > 0) {
		return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
		// } else {
		// return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		// }
	}

	public void buildPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	///////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////
	private int time = 0;

	Runnable timeRunnable = new Runnable() {
		@Override
		public void run() {
			if (publisher != null && publisher.isRecording()) {
				time++;
				recorderTime.setText(stringForTime(time));
				if (time % 2 == 0) {
					thumd.setVisibility(View.VISIBLE);
				} else {
					thumd.setVisibility(View.INVISIBLE);
				}
			}
			synchronized (handler) {
				handler.postDelayed(this, 1000);
			}
		}
	};
}
