package com.juss.live.skin.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Toast;

import com.juss.live.skin.ui.base.RecorderSkinBase;
import com.juss.live.skin.ui.mobile.RecorderBottomMobileView;
import com.juss.live.skin.ui.mobile.RecorderTopFloatMobileView;
import com.letv.recorder.bean.LivesInfo;
import com.letv.recorder.callback.LetvRecorderCallback;
import com.letv.recorder.controller.LetvPublisher;
import com.letv.recorder.ui.logic.RecorderConstance;
import com.letv.recorder.util.LeLog;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 *  这个是乐视云推流所需要的皮肤，乐视云推流需要走调度，使用乐视云默认推流，需要使用LetvPublisher
 */
public class RecorderSkin extends RecorderSkinBase {
	private RecorderDialog machineDialog;
	@Override
	protected void handleMachine() {
		LeLog.d(TAG, "[observer] angle_request 机位请求");

		if (publisher != null && publisher instanceof LetvPublisher) {
			((LetvPublisher)publisher).handleMachine(new LetvRecorderCallback<ArrayList<LivesInfo>>() {
						@Override
						public void onSucess(final ArrayList<LivesInfo> data) {
							int num = data.size();

							switch (num) {
							case 0:// 当前无机位
								handler.post(new Runnable() {
									public void run() {
										Toast.makeText(context, "当前无机位", Toast.LENGTH_SHORT).show();
									}
								});
								break;
							case 1:// 只有一个机位信息
								handler.post(new Runnable() {
									@Override
									public void run() {
										dimssLoadDialog();
										selectMachine(0);
									}
								});
								break;
							default:// 多机位
								handler.post(new Runnable() {
									@Override
									public void run() {
										dimssLoadDialog();
										if (machineDialog!= null&& machineDialog.isShowing()) {// 机位已经显示
											return;
										}

										Bundle dialogBundle = new Bundle();
										dialogBundle.putSerializable(RecorderDialogBuilder.key_machine,data);
										machineDialog = RecorderDialogBuilder.showMachineDialog(context,dialogBundle);
										machineDialog.getObservable().addObserver(machineObserver);// 添加监听者
									}
								});

								break;
							}
						}

						@Override
						public void onFailed(final int code, final String msg) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									dimssLoadDialog();
									Toast.makeText(context,"接口请求失败,错误码:" + code + "," + msg,Toast.LENGTH_SHORT).show();
								}
							});
						}
					});
		}
	}
	
	@Override
	protected void selectMachine(int numFlag) {
		if ( publisher instanceof LetvPublisher && ((LetvPublisher)publisher).selectMachine(numFlag)) {
			rv.startRecorder();
			if (machineDialog != null && machineDialog.isShowing()) {
				machineDialog.dismiss();
			}
		} else {
			Toast.makeText(context, "该机位正在直播", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void initObserver() {
		/**
		 * 开始按钮
		 */
		rv.getStartSubject().addObserver(new Observer() {
			@Override
			public void update(Observable observable, Object data) {
				Bundle bundle = (Bundle) data;
				int flag = bundle.getInt("flag");

				switch (flag) {
				case RecorderConstance.recorder_start:
					LeLog.d(TAG, "[observer] recorder_start 开始推流");
					if (publisher != null) {
						publisher.publish();
					}
					break;
				case RecorderConstance.recorder_stop:
					LeLog.d(TAG, "[observer] recorder_stop 停止推流");
					if (publisher != null) {
						publisher.stopPublish();// 停止推流
					}
					break;

				case RecorderConstance.angle_request:
					showLoadDialog();
					handleMachine();
					break;
				default:
					break;
				}
			}
		});

		rv.getStartSubject().addObserver(getTopFloatView());

		/**
		 * 返回按钮
		 */
		getTopFloatView().getTopSubject().addObserver(new Observer() {
			@Override
			public void update(Observable observable, Object data) {
				Bundle bundle = (Bundle) data;

				int flag = bundle.getInt("flag");
				if (RecorderConstance.top_float_back == flag) {
					LeLog.d(TAG, "[observer] top_float_back 返回按钮");
					if (context instanceof Activity) {
						((Activity) context).finish();
					}
				}
			}
		});

		/**
		 * 在线人数
		 */
		// TODO: 暂时没有实现，所以关闭
		// mCountPeopleTimer.getCountObservable().addObserver(bottomView);
	}
	@Override
	public void build(Context context, RecorderView rv, int orientation) {
		this.rv = rv;
		this.context = context;
		this.orientation = orientation;
		setCameraView(new SurfaceView(context));
		initCameraPreView(getCameraView());
		initObserver();
	}
	/**
	 * 绑定Sufaceview 
	 * @param cameraView
	 */
	private void initCameraPreView(SurfaceView cameraView) {
		setBottomView(new RecorderBottomMobileView(context));
		setTopFloatView(new RecorderTopFloatMobileView(context));
		
		rv.attachTopFloatView(getTopFloatView());
		rv.attachBomttomView(getBottomView());
		rv.attachSurfaceView(cameraView);
		cameraView.getHolder().addCallback(this);
		((RecorderTopFloatMobileView)getTopFloatView()).setTitle(streamName);
	}
	/**
	 * 机位选择按钮
	 */
	private Observer machineObserver = new Observer() {

		@Override
		public void update(Observable observable, Object data) {
			Bundle bundle = (Bundle) data;
			int flag = bundle.getInt("flag");
			switch (flag) {
			case RecorderConstance.selected_angle:
				LeLog.d(TAG, "[observer] selected_angle 选择推流");
				int numFlag = bundle.getInt("numFlag");// 第几个机位
				selectMachine(numFlag);
				break;
			default:
				break;
			}
		}
	};
}
