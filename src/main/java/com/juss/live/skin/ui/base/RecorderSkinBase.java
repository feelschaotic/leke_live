package com.juss.live.skin.ui.base;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.juss.live.skin.ui.RecorderBottomView;
import com.juss.live.skin.ui.RecorderDialogBuilder;
import com.juss.live.skin.ui.RecorderTopFloatView;
import com.juss.live.skin.ui.RecorderView;
import com.letv.recorder.bean.CameraParams;
import com.letv.recorder.callback.LetvPublishListener;
import com.letv.recorder.callback.VideoRecorderDeviceListener;
import com.letv.recorder.controller.Publisher;
import com.letv.recorder.ui.logic.RecorderConstance;
import com.letv.recorder.util.LeLog;
import com.letv.recorder.util.NetworkUtils;


import java.util.List;

public abstract class RecorderSkinBase implements SurfaceHolder.Callback,VideoRecorderDeviceListener {
	protected static final String TAG = "CameraView2_RecorderSkin";
	
	protected final static int RE_TRY_NUM = 0;
	protected boolean isReTry = false;
	protected int reTry = 0;

	/**
	 * UI模块(顶部栏，底部栏,机位选择dialog,matherBoard视图)
	 */
	protected RecorderView rv;
	private RecorderTopFloatView topFloatView;
	private RecorderBottomView bottomView;
	private Dialog dialog;
	private Dialog loadDialog;

	protected Publisher publisher;

	protected Handler handler = new Handler();
	protected Context context;
	protected String streamName;

	/**
	 * 人数统计
	 */
	// TODO: 暂时没有实现，所以关闭
	// private CountPeopleTimer mCountPeopleTimer;

	protected int orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

	/**
	 * 构建一个默认皮肤
	 * 
	 * @param context
	 *            上下文
	 * @param rv
	 *            皮肤rootView
	 */
	public void build(Context context, RecorderView rv, int orientation) {
		this.rv = rv;
		this.context = context;
		this.orientation = orientation;
		setCameraView(new SurfaceView(context));
		initCameraPreView2(getCameraView());
		initStartCountTimer();
		initObserver();

		startCountPeople();// 开始定时获取在线人数
	}

	/**
	 * 初始化人数统计类
	 */
	private void initStartCountTimer() {
		// //TODO: 暂时没有实现，所以关闭
		// mCountPeopleTimer = new CountPeopleTimer();
	}

	/**
	 * 开始统计人数
	 */
	private void startCountPeople() {
		// TODO: 暂时没有实现，所以关闭
		// if (mCountPeopleTimer != null) {
		// mCountPeopleTimer.startCountPeople();
		// }
	}

	/**
	 * 结束统计人数
	 */
	private void stopCountPeople() {
		// TODO: 暂时没有实现，所以关闭
		// if (mCountPeopleTimer != null) {
		// mCountPeopleTimer.stopCountPeople();
		// }
	}

	/**
	 * 于推流器绑定
	 * 
	 * @param publisher
	 */
	public void BindingPublisher(Publisher publisher) {
		this.publisher = publisher;
		rv.buildPublisher(publisher);
		setPublisherListerner();
		/**
		 * 上层按钮点击事件
		 */
		getTopFloatView().getTopSubject().addObserver(publisher.getVideoRecordDevice());
		getTopFloatView().getTopSubject().addObserver(publisher.getAudioRecordDevice());
		getBottomView().getBottomSubject().addObserver(publisher.getVideoRecordDevice());
		getBottomView().getBottomSubject().addObserver(publisher.getAudioRecordDevice());
		getBottomView().buildPublisher(publisher);
		getTopFloatView().buildPublisher(publisher);
		publisher.getVideoRecordDevice().setVideoRecorderDeviceListener(this);
	}

	/**
	 * 设置推流回调
	 */
	private void setPublisherListerner() {
		if (publisher != null) {
			publisher.setPublishListener(new LetvPublishListener() {
				@Override
				public boolean OnLetvPublish(int code, final Object... msgs) {
					LeLog.d(TAG, "[callback] code:" + code);
					switch (code) {
					// case RecorderConstance.recorder_start_ok:
					case RecorderConstance.recorder_open_url_sucess:
						LeLog.d("RTMP 地址打开成功");
						break;
					case RecorderConstance.recorder_open_url_failed:
						handler.post(new Runnable() {
							@Override
							public void run() {
								dimssLoadDialog();
								if(isReTry){
									reTryPublisher();
								}else{
									RecorderSkinBase.this.getBottomView().reset();
									LeLog.w("无法连接到推流地址");
									if(dialog != null && dialog.isShowing()){
										dialog.dismiss();
										dialog = null;
									}
									dialog = RecorderDialogBuilder.showCommentDialog(context, "无法连接推流服务器", "我知道了", null, new OnClickListener() {

										@Override
										public void onClick(View v) {
											dialog.dismiss();
											dialog = null;
										}
									}, null);
									stopPublishWithUI();
								}
							}
						});
						break;
					case RecorderConstance.recorder_push_first_size:
						handler.post(new Runnable() {
							@Override
							public void run() {
								LeLog.d("第一针推流成功");
								reTry = 0;
								dimssLoadDialog();
							}
						});
						break;
					case RecorderConstance.recorder_push_error:
						handler.post(new Runnable() {
							@Override
							public void run() {
								dimssLoadDialog();
								isReTry = true;
								LeLog.w("推流错误");
							    reTryPublisher();
							}
						});
						break;
						
					case RecorderConstance.LIVE_STATE_NEED_RECORD:
						handler.post(new Runnable() {
							@Override
							public void run() {
								boolean isNeedRecord = (Boolean) msgs[1];
								Bundle bundle=new Bundle();
								if(isNeedRecord){
									bundle.putInt("flag", RecorderConstance.enable_rec);
								}else{
									bundle.putInt("flag", RecorderConstance.disable_rec);
								}
								rv.getStartSubject().notifyObserverPlus(bundle);// 通知观察者
							}
						});
						break;
					case RecorderConstance.LIVE_STATE_CANCEL_ERROR:
					case RecorderConstance.LIVE_STATE_END_ERROR:
					case RecorderConstance.LIVE_STATE_NOT_STARTED_ERROR:
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(context, msgs[0].toString(), 0).show();
								LeLog.w("直播已到结束时间");
								dimssLoadDialog();
							}
						});
						break;
					case RecorderConstance.LIVE_STATE_TIME_REMAINING:
					case RecorderConstance.recorder_push_audio_packet_loss_rate:
						LeLog.w(msgs[0].toString());
						break;
					case RecorderConstance.recorder_push_video_packet_loss_rate:
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(context, "网络不稳定", 0) .show();
								if(Double.valueOf(msgs[1].toString()) >= 0.80){
									LeLog.w("推流失败大于80%，所以关闭推流重试");
									publisher.stopPublish();
									isReTry = true;
									reTryPublisher();
								}
							}
						});
						break;
					case RecorderConstance.LIVE_STATE_PUSH_COMPLETE:
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(context, msgs[0].toString(), 0) .show();
								Bundle bundle = new Bundle();
								bundle.putInt("flag", RecorderConstance.recorder_stop);
								rv.getStartSubject().notifyObserverPlus(bundle);
								rv.reSetStartBtn(false);
								RecorderSkinBase.this.getBottomView().reset();
								dimssLoadDialog();
							}
						});
						break;
					case RecorderConstance.recorder_change_video_rate_warn:
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(context, msgs[0].toString(), 0) .show();
							}
						});
						break;
					}
					return false;
				}
			});
		}
	}
	/**
	 * 重试推流
	 */
	protected void reTryPublisher(){
		LeLog.d("是否开启重试:" + isReTry + "当前重试次数:" + reTry + ",当前网络状态:" + NetworkUtils.getNetType(context));
		Bundle bundle = new Bundle();
		reTry++;
		if(!isReTry || reTry > RE_TRY_NUM || NetworkUtils.getNetType(context) == null){
			isReTry = false;
			reTry = 0;
			bundle.putInt("flag", RecorderConstance.recorder_stop);
			rv.getStartSubject().notifyObserverPlus(bundle);
			rv.reSetStartBtn(false);
			RecorderSkinBase.this.getBottomView().reset();
			dimssLoadDialog();
			if(dialog != null && dialog.isShowing()){
				dialog.dismiss();
				dialog = null;
			}
			dialog = RecorderDialogBuilder.showCommentDialog(context, ((NetworkUtils.getNetType(context) == null)?"网络异常,":"")+"无法连接推流服务器","我知道了",null, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					dialog = null;
				}
			}, null);
		}else{
			RecorderSkinBase.this.getBottomView().reSetRunnable();
			LeLog.w("开始重试，当前重试次数:" + reTry);
			bundle.putInt("flag", RecorderConstance.angle_request);
			rv.getStartSubject().notifyObserverPlus(bundle);
		}
	}
	/**
	 * 组装ui
	 * 
	 * @param cameraView
	 */
	private void initCameraPreView2(SurfaceView cameraView) {
		setBottomView(new RecorderBottomView(context));
		setTopFloatView(new RecorderTopFloatView(context));

		/**
		 * title
		 */
		// if (mRecorderInfo != null) {
		// topFloatView.updateTitle(mRecorderInfo.activityName);
		// }

		rv.attachTopFloatView(getTopFloatView());
		rv.attachBomttomView(getBottomView());
		rv.attachSurfaceView(cameraView);
		cameraView.getHolder().addCallback(this);
	}

	protected void showLoadDialog() {
		loadDialog = RecorderDialogBuilder.showLoadDialog(context,"正在急速加载中，请稍等");
	}

	protected void dimssLoadDialog() {
		if (loadDialog != null && loadDialog.isShowing()) {
			loadDialog.dismiss();
		}
	}

	/**
	 * UI响应注册
	 */
	protected abstract void initObserver();

	/**
	 * 对几位进行处理
	 */
	protected abstract void handleMachine();

	/**
	 * activity(fragment)生命周期onResume时调用次方法
	 */
	public void onResume() {
		registerNetworkChange();
	}

	/**
	 * activity(fragment)生命周期onPause时调用次方法
	 */
	public void onPause() {
		unregisterNetworkChange();
		stopPublishWithUI();
	}

	/**
	 * activity(fragment)生命周期onDestroy时调用次方法
	 */
	public void onDestroy() {
		stopCountPeople();
		if (publisher != null) {
			publisher.release();
		}
	}

	/**
	 * 停止推流，同时更新UI
	 */
	private void stopPublishWithUI() {
		if (rv != null) {
			rv.stopAuto();
		}
	}

	protected RecorderBottomView getBottomView() {
		return bottomView;
	}

	protected void setBottomView(RecorderBottomView bottomView) {
		this.bottomView = bottomView;
	}

	protected RecorderTopFloatView getTopFloatView() {
		return topFloatView;
	}

	protected void setTopFloatView(RecorderTopFloatView topFloatView) {
		this.topFloatView = topFloatView;
	}

	// ///////////////////////////////////////////////////////////////////////////////////

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		if (publisher.getVideoRecordDevice() != null) {
			publisher.getVideoRecordDevice().bindingSurface(arg0);
			publisher.getVideoRecordDevice().start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		if (publisher.getVideoRecordDevice() != null) {
			publisher.getVideoRecordDevice().stop();
		}
	}

	/**
	 * 选择机位
	 */
	protected abstract void selectMachine(int numFlag);
	/**
	 * 注册网络监听
	 */
	private void registerNetworkChange() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		filter.setPriority(1000);
		context.registerReceiver(networkChangeReceiver, filter);
	}

	/**
	 * 取消网络监听
	 */
	private void unregisterNetworkChange() {
		context.unregisterReceiver(networkChangeReceiver);
	}

	public SurfaceView getCameraView() {
		return cameraView;
	}

	public void setCameraView(SurfaceView cameraView) {
		this.cameraView = cameraView;
	}

	private NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();

	private SurfaceView cameraView;

	private class NetworkChangeReceiver extends BroadcastReceiver {
		private Bundle bundle = new Bundle();

		@Override
		public void onReceive(final Context context, Intent intent) {
			//TODO: 这里只监听一种情况，就是 WIFI转3G 4G的情况。如果此时推流还没有断掉，那么我就需要手动掐断，然后给用户提示
			if(!publisher.isRecording() || (NetworkUtils.getNetType(context) != null && !NetworkUtils.isWifiNetType(context))) return;
			
			if(!NetworkUtils.isWifiNetType(context)){
				publisher.stopPublish();
				rv.reSetStartBtn(false);
			}
			if(dialog != null && dialog.isShowing()){
				dialog.dismiss();
				dialog = null;
			}
			dialog = RecorderDialogBuilder.showMobileNetworkWarningDialog(context,new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(publisher != null && !publisher.isRecording() && !NetworkUtils.isWifiNetType(context)){
						bundle.putInt("flag", RecorderConstance.recorder_start);
						rv.getStartSubject().notifyObserverPlus(bundle);
						rv.reSetStartBtn(true);
					}
					dialog.dismiss();
					dialog = null;
				}
			}, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					dialog = null;
				}
			});
		}
	}

	@Override
	public void onSetFps(int cameraId, List<int[]> range,CameraParams cameraParams) {
		if (range != null && range.size() > 0) {
			cameraParams.setFps(range.get(0));
		}
	}

	@Override
	public void onSetPreviewSize(int cameraId, List<Size> previewSizes,CameraParams cameraParams) {
		// float ratio =
		// ((float)RecorderUtils.getScreenWidth(context))/RecorderUtils.getScreenHeight(context);
		float ratio = 16.0f / 9.0f;
		float appropriate = 100;
		Size s = null;
		for (Size size : previewSizes) {
//			 Log.d(TAG,"可选择选择录制的视频有：宽为:"+size.width+",高："+size.height+"；比例："+((float)size.width)/size.height);
			if (size.width <= 1000) { // / 选择比较低的分辨率，保证推流不卡
				if (Math.abs(((float) size.width) / size.height - ratio) < appropriate) {
					appropriate = Math.abs(((float) size.width) / size.height - ratio);
					s = size;
				}
			}
		}
		cameraParams.setWidth(s.width);
		cameraParams.setHeight(s.height);
		LeLog.d(TAG, "选择录制的视频宽为:" + s.width + ",高：" + s.height);
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}
	
}
