package com.juss.live.skin.skin.v4;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juss.live.skin.skin.widget.MarqueeTextView;
import com.lecloud.base.net.BaseCallback;
import com.lecloud.base.net.json.ResultJson;
import com.lecloud.js.action.entity.LinePeople;
import com.lecloud.leutils.LeLog;
import com.lecloud.leutils.PxUtils;
import com.lecloud.leutils.ReUtils;
import com.lecloud.volley.VolleyError;
import com.letv.controller.interfacev1.ILetvPlayerController;
import com.letv.controller.interfacev1.ISplayerController;
import com.letv.universal.iplay.ISplayer;
import com.letv.universal.notice.UIObserver;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 顶部浮层
 * 
 * @author pys
 *
 */
public class V4TopTitleMultLiveView extends V4TopTitleView implements UIObserver {

    private MarqueeTextView textView;
    // private ImageView netStateView;
    // private ImageView batteryView;
    // private TextView timeView;

    /**
     * 横屏状态下的在线人数
     */
    private TextView peopleCount;
    /**
     * 这个是横屏是的加锁。当加锁之后屏幕上所有的操作都无效了。必须解锁才能再次使用
     */
    private ImageView videoLock;
    /**
     * lockFlag 为false 表示不加锁
     */
    private boolean lockFlag = false;
    /**
     * 举报对话框
     */
    private Dialog informDialog;

    /**
     * 定时查询在线人数
     */
    private Timer linePeopleQueryTimer;
    
    /**
	 * 水印
	 */
//	private NetworkImageView watermark;
//	private ActionLiveConfig mActionLiveConfig;
//	private ImageLoader imageLoader;
//	private RequestQueue mRequestQueue;
    

    public V4TopTitleMultLiveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4TopTitleMultLiveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4TopTitleMultLiveView(Context context) {
        super(context);
    }

    @Override
    protected void initPlayer() {
        player.attachObserver(this);
        setState();
    }

    /**
     * 设置状态栏
     */
    private void setState() {
        /**
         * 播放器可用的时候试着获取标题
         */
        if (uiPlayContext != null) {
            /**
             * 设置标题
             */
            String title = uiPlayContext.getVideoTitle();
            if (title != null) {
                textView.setText(title);
            }

            /**
             * 获取网络状态
             */
            // netStateView.setImageLevel(StatusUtils.getWiFistate(context));
            // /**
            // * 电池
            // */
            // batteryView.setImageLevel(StatusUtils.getBatteryStatus(context));
            // /**
            // * 时间
            // */
            // timeView.setText(StatusUtils.getCurrentTime(context));
        }
    }

    @Override
    protected void initView(final Context context) {
        LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_skin_v4_top_mult_live_layout"), this);
        videoLock = (ImageView) findViewById(ReUtils.getId(context, "iv_video_lock"));
        peopleCount = (TextView) findViewById(ReUtils.getId(context, "people_count"));

        /**
         * 返回键
         */
        findViewById(ReUtils.getId(context, "full_back")).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uiPlayContext != null) {
                    /**
                     * 返回键是否恢复竖屏状态
                     */
                    if (uiPlayContext.isReturnback()) {
                        if (player != null) {
                            // 返回竖屏状态
                            player.setScreenResolution(ISplayerController.SCREEN_ORIENTATION_USER_PORTRAIT);
                        }
                    } else {
                        ((Activity) context).finish();
                    }
                }
            }
        });
        textView = (MarqueeTextView) findViewById(ReUtils.getId(context, "full_title"));
        findViewById(ReUtils.getId(context, "iv_video_inform")).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                informFuction();
            }
        });

        videoLock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lockFlag = !lockFlag;
                if (lockFlag) {
                    videoLock.setImageResource(ReUtils.getDrawableId(context, "letv_skin_v4_large_mult_live_action_lock"));
                } else {
                    videoLock.setImageResource(ReUtils.getDrawableId(context, "letv_skin_v4_large_mult_live_action_unlock"));
                }
                uiPlayContext.setLockFlag(lockFlag);
            }
        });
        // netStateView = (ImageView) findViewById(ReUtils.getId(context,
        // "full_net"));
        // batteryView = (ImageView) findViewById(ReUtils.getId(context,
        // "full_battery"));
        // timeView = (TextView) findViewById(ReUtils.getId(context,
        // "full_time"));
    }

    @Override
    public void update(Observable observable, Object data) {
        Bundle bundle = (Bundle) data;
        switch (bundle.getInt("state")) {
        case ISplayer.MEDIA_EVENT_PREPARE_COMPLETE:
            setState();
            break;

        default:
            break;
        }
    }
    
    @Override
    public void attachUIPlayControl(ILetvPlayerController playerControl) {
        super.attachUIPlayControl(playerControl);

        linePeopleQueryTimer = new Timer(true);
        linePeopleQueryTimer.schedule(new MyTimerTask(), 10, 30 * 1000);
        
//        watermark = new NetworkImageView(context);
//        mRequestQueue = Volley.newRequestQueue(context);
//        imageLoader = new ImageLoader(mRequestQueue, new ImageCache() {
//
//			@Override
//			public void putBitmap(String url, Bitmap bitmap) {
//			}
//
//			@Override
//			public Bitmap getBitmap(String url) {
//				return null;
//			}
//		});
//        requestActionConfig();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (linePeopleQueryTimer != null) {
            linePeopleQueryTimer.cancel();
            linePeopleQueryTimer = null;
        }
    }

    /**
     * 举报功能，当点击举报功能的时候先弹出
     */
    private void informFuction() {
        if (informDialog == null) {
            View view = View.inflate(context, ReUtils.getLayoutId(context, "letv_skin_v4_large_mult_live_action_layout_inform_dialog"), null);
            informDialog = new Dialog(context);
            informDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            informDialog.setContentView(view, new LayoutParams(PxUtils.dip2px(context, 230), PxUtils.dip2px(context, 120)));
            // 确认按钮
            view.findViewById(ReUtils.getId(context, "btn_confirm_inform")).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    informDialog.dismiss();
                    requestInformMessage();
                    // mHelper.requestInformMessage("A2015101200017");
                }

            });
            // 取消按钮
            view.findViewById(ReUtils.getId(context, "btn_cancel_inform")).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    informDialog.dismiss();
                }
            });
        }
        if (!informDialog.isShowing()) {
            informDialog.show();
        }
    }

    /**
     * 上报举报信息
     */
    private void requestInformMessage() {
        requestController.tipOff(uiPlayContext.getActivityId(), new BaseCallback<LinePeople>() {

            @Override
            public void onSuccess(ResultJson<LinePeople> response) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                RelativeLayout view = new RelativeLayout(context);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(ReUtils.getDrawableId(context, "letv_skin_v4_large_mult_live_action_jubao_back"));
                TextView text = new TextView(context);
                text.setText("举报成功");
                text.setTextColor(0xffffffff);
                text.setGravity(Gravity.CENTER);
                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                Drawable drawable = context.getResources().getDrawable(ReUtils.getDrawableId(context, "letv_skin_v4_large_mult_live_action_jubaosuccess_3"));
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
                text.setCompoundDrawables(drawable, null, null, null);
                text.setCompoundDrawablePadding(PxUtils.dip2px(context, 14));
                view.addView(text);
                dialog.setContentView(view, new LayoutParams(PxUtils.dip2px(context, 173), PxUtils.dip2px(context, 80)));
                dialog.show();
                postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing())
                            dialog.dismiss();
                    }
                }, 1000);

            }

            @Override
            public void onFail(VolleyError error) {
                LeLog.ePrint("", "举报失败");
            }
        });

    }

    private void requestLinePeople() {
        if (TextUtils.isEmpty(uiPlayContext.getActivityId())) {
            return;
        }
        requestController.getLinePeople(uiPlayContext.getActivityId(), new BaseCallback<LinePeople>() {

            @Override
            public void onSuccess(ResultJson<LinePeople> response) {
                if (response != null && response.getData() != null) {
                    String count = response.getData().getCount();
//                    if ("0".equals(count)) {
//                        count = "1";
//                    }
                    peopleCount.setText(count);
                }
            }

            @Override
            public void onFail(VolleyError error) {

            }
        });
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            requestLinePeople();
        }
    }
    
//    private void requestActionConfig() {
//        if (TextUtils.isEmpty(uiPlayContext.getActivityId())) {
//            return;
//        }
//        mHelper.requestActionConfig(uiPlayContext.getActivityId(), new BaseCallback<ActionLiveConfig>() {
//			
//			@Override
//			public void onSuccess(ResultJson<ActionLiveConfig> response) {
//				if (null == response) {
//					return;
//				}
//				mActionLiveConfig = response.getData();
//				if (mActionLiveConfig != null) {
////					if ("1".equals(mActionLiveConfig.getWatermarkStatus())) {
//						try {
//							int pos = Integer.parseInt(mActionLiveConfig.getWatermarkPos());
//							setWatermarkLocation(pos, mActionLiveConfig.getWatermarkUrl());
//						} catch (NumberFormatException e) {
//							e.printStackTrace();
//						}
////					} else {
////						watermark.setVisibility(View.GONE);
////					}
//				}
//			}
//			
//			@Override
//			public void onFail(VolleyError error) {
//				
//			}
//		});
//    }
    
    /**
	 * 设置水印的位置
	 * 
	 * @param location
	 */
//	private void setWatermarkLocation(int location, String imgUrl) {
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(PxUtils.dip2px(context, 40), PxUtils.dip2px(context, 26));
//		switch (location) {
//		case 1: // 左上角
//			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//			break;
//		case 2: // 右上角
//			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//
//			break;
//		case 3: // 左下角
//			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//			break;
//		case 4: // 右下角
//			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//			break;
//		}
//		
//		this.watermark.setLayoutParams(params);
//		this.watermark.setImageUrl(imgUrl, imageLoader);
////		((RelativeLayout)this.getParent()).addView(watermark);
//	}
}
