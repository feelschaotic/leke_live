package com.juss.live.skin.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.letv.recorder.bean.LivesInfo;
import com.letv.recorder.bean.RecorderInfo;
import com.letv.recorder.ui.logic.RecorderConstance;
import com.letv.recorder.ui.logic.UiObservable;
import com.letv.recorder.util.LeLog;
import com.letv.recorder.util.ReUtils;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class RecorderAngleView extends RelativeLayout implements Observer{
	private static final String TAG = "CameraView2";
	private Context context;
	public RecorderAngleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public RecorderAngleView(Context context) {
		super(context);
		initView(context);
	}

	public void addObserver(Observer oberver) {
		angle1.addObserver(oberver);
		angle2.addObserver(oberver);
		angle3.addObserver(oberver);
		angle4.addObserver(oberver);
	}

	private AngleBtn angle1 = new AngleBtn(1);
	private AngleBtn angle2 = new AngleBtn(2);
	private AngleBtn angle3 = new AngleBtn(3);
	private AngleBtn angle4 = new AngleBtn(4);
	private ArrayList<AngleBtn> angles;

	@Override
	public void setVisibility(int visibility) {
		if(View.GONE==visibility){
			reset();
		}
		
		super.setVisibility(visibility);
	}

	public void initView(Context context) {
		this.context=context;
		
		LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_recorder_angle_layout"), this);
		angles=new ArrayList<AngleBtn>();

		angle1.angle = findViewById(ReUtils.getId(context, "letv_recorder_angle_1"));
		angle1.angleI = (ImageView) findViewById(ReUtils.getId(context, "letv_recorder_angle_1_i"));
		angle1.setOnClickListenner();
		angle1.setEnable(false);
		angles.add(angle1);

		angle2.angle = findViewById(ReUtils.getId(context, "letv_recorder_angle_2"));
		angle2.angleI = (ImageView) findViewById(ReUtils.getId(context, "letv_recorder_angle_2_i"));
		angle2.setOnClickListenner();
		angle2.setEnable(false);
		angles.add(angle2);

		angle3.angle = findViewById(ReUtils.getId(context, "letv_recorder_angle_3"));
		angle3.angleI = (ImageView) findViewById(ReUtils.getId(context, "letv_recorder_angle_3_i"));
		angle3.setOnClickListenner();
		angle3.setEnable(false);
		angles.add(angle3);

		angle4.angle = findViewById(ReUtils.getId(context, "letv_recorder_angle_4"));
		angle4.angleI = (ImageView) findViewById(ReUtils.getId(context, "letv_recorder_angle_4_i"));
		angle4.setOnClickListenner();
		angle4.setEnable(false);
		angles.add(angle4);
	}

	public void updataAngleStatus(RecorderInfo mRecorderInfo) {

		ArrayList<LivesInfo> livesInfos = mRecorderInfo.livesInfos;
		for (int i = 0; i < livesInfos.size(); i++) {
			LivesInfo livesInfo = livesInfos.get(i);
			
			int machine = livesInfo.machine;
			int status = livesInfo.status;
			
			for (int j = 0; j < angles.size(); j++) {
				AngleBtn angleBtn = angles.get(j);
				if(machine==angleBtn.numFlag){
					angleBtn.status=status;
					if(status==0){
						angleBtn.angleI.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_angle_blue"));
//						angleBtn.angleI.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_angle_white"));
					}else{
//						angleBtn.angleI.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_angle_blue"));
					}
					angleBtn.setEnable(true);
				}
			}
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 机位
	 * 
	 * @author pys
	 *
	 */
	class AngleBtn extends UiObservable {
		public View angle;
		public ImageView angleI;
		public boolean angleFlag = false;
		public int numFlag;
		public int status;
		

		public AngleBtn(int numFlag) {
			this.numFlag = numFlag;
		}
		
		public void setEnable(boolean enabled){
			angle.setEnabled(enabled);
		}
		
		public void setVisibility(int visibility){
			angle.setVisibility(visibility);
		}

		public void setOnClickListenner() {
			if (angle != null) {
				angle.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						Bundle bundle=new Bundle();
						bundle.putInt("flag", RecorderConstance.selected_angle);
						bundle.putInt("numFlag", numFlag);
						bundle.putInt("status", status);
						notifyObserverPlus(bundle);
					}
				});
			}
		}
	}
	
	public void reset(){
		if(angles!=null){
			for (int i = 0; i < angles.size(); i++) {
				AngleBtn angleBtn = angles.get(i);
				angleBtn.setEnable(false);
				angleBtn.angleI.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_angle_gray"));
			}
		}
	}
	
	/**
	 * 观察者
	 */
	@Override
	public void update(Observable observable, Object data) {
		int flag=(Integer) data;
		
		switch (flag) {
		case RecorderConstance.recorder_stop:
//			reset();
			LeLog.d(TAG, "[observer] recorder_stop angleView");
			break;

		default:
			break;
		}
	};
}
