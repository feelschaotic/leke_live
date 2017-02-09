package com.juss.live.skin.skin.v4;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.juss.live.skin.skin.BaseView;
import com.juss.live.skin.skin.activity.FeedBackActivity;
import com.juss.live.skin.skin.interfacev1.OnNetWorkChangeListener;
import com.juss.live.skin.skin.utils.NetworkUtils;
import com.lecloud.entity.LiveStatus;
import com.lecloud.leutils.ReUtils;
import com.letv.universal.iplay.EventPlayProxy;
import com.letv.universal.iplay.ISplayer;
import com.letv.universal.notice.UIObserver;

import java.util.Observable;

/**
 * 提示view
 *
 * @author pys
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class V4NoticeView extends BaseView implements UIObserver, OnNetWorkChangeListener {
    private static final String TAG = "V4NoticeView";
    private TextView codeView;
    private TextView msgView;
    private TextView noteView;
    private TextView btn_error_replay;
    private TextView btn_error_report;
//    private boolean isContinue;
    private boolean isShowNoWifi;

    public V4NoticeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4NoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4NoticeView(Context context) {
        super(context);
    }

    @Override
    protected void initPlayer() {
        player.attachObserver(this);
    }

    @Override
    protected void initView(final Context context) {
        LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_skin_v4_notice_layout"), this);
        codeView = (TextView) findViewById(ReUtils.getId(context, "tv_error_code"));
        msgView = (TextView) findViewById(ReUtils.getId(context, "tv_error_msg"));
        noteView = (TextView) findViewById(ReUtils.getId(context, "tv_error_message"));
        btn_error_replay = (TextView) findViewById(ReUtils.getId(context, "btn_error_replay"));
        btn_error_report = (TextView) findViewById(ReUtils.getId(context, "btn_error_report"));
        msgView.setVisibility(View.GONE);
        btn_error_replay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null) {
                	if (isShowNoWifi) {
                		uiPlayContext.setNoWifiContinue(true);
                		player.start();
                		setVisibility(View.GONE);
                		return;
                	}
                	setVisibility(View.GONE);
                    player.resetPlay();
                }
            }
        });
        btn_error_report.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	if (isShowNoWifi) {
            		setVisibility(View.GONE);
            		uiPlayContext.setNoWifiContinue(false);
					return;
				}
                FeedBackActivity.requestController = requestController;
                Intent intent = new Intent(context, FeedBackActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
//        btnFeed.setVisibility(GONE);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (NetworkUtils.hasConnect(context)) {
            msgView.setVisibility(View.VISIBLE);
            Bundle bundle = (Bundle) data;
            int state = bundle.getInt("state");
            Log.d(TAG, "[errorView] state:" + state);
            switch (state) {
                case ISplayer.MEDIA_ERROR_DECODE_ERROR:
                    setVisibility(View.VISIBLE);
                    showMsg(state, "播放器解码失败");
                    setNoteView();
                case ISplayer.MEDIA_ERROR_NO_STREAM:
                    setVisibility(View.VISIBLE);
                    showMsg(state, "流媒体连接失败");
                    setNoteView();
                    break;
                case ISplayer.MEDIA_ERROR_UNKNOWN:
                    setVisibility(View.VISIBLE);
                    showMsg(state, "播放器内部错误");
                    setNoteView();
                    break;
                case ISplayer.MEDIA_EVENT_FIRST_RENDER:
                    setNoteView();
                    setVisibility(View.GONE);
                    if (!uiPlayContext.isNoWifiContinue()) {
						if (!NetworkUtils.isWifiConnect(context)) {
							player.pause();
							onNetWorkChange(false, "");
						}
					}
                    break;
                case ISplayer.PLAYER_PROXY_ERROR:
                    setNoteView();
                    setVisibility(View.VISIBLE);
                    int errorCode = bundle.getInt("errorCode");
                    String msg = bundle.getString("errorMsg");
                    showMsg(errorCode, msg);
                    break;
                case EventPlayProxy.PLAYER_TIME_SHIRT_SEEK_ERROR:
                    showMsg(state, "时移失败");
                    setVisibility(View.VISIBLE);
                    break;
                case EventPlayProxy.PLAYER_ACTION_LIVE_STATUS:
                    int status = bundle.getInt("status", -1);
                    if (status != -1) {
                        String errMsg = "";
                        if (status == LiveStatus.STATUS_END) {
                            errMsg = "直播已结束";
                        } else if (status == LiveStatus.STATUS_INTERRUPTED) {
                            errMsg = "直播已中断";
                        } else if (status == LiveStatus.SATUS_NOT_START) {
                            errMsg = "直播未开始";
                        }
                        if (!TextUtils.isEmpty(errMsg)) {
                            setNoteView();
                            setVisibility(View.VISIBLE);
                            showMsg(0, errMsg);
                        } else if (status == LiveStatus.STATUS_LIVE_ING) {
                            noteView.setText("直播恢复了，继续播放？");
                            codeView.setVisibility(View.GONE);
                            msgView.setVisibility(View.GONE);

                        }
                    }
                    break;
                default:
                    break;
            }
        } else {
            showNetworkNotice();
        }
    }

    private void setNoteView() {
        noteView.setText(ReUtils.getStringId(context, "letv_notice_message"));
    }

    /**
     * 展示消息
     *
     * @param event
     * @param msg
     */
    private void showMsg(int event, String msg) {
        String errorNote = "错误代码:";
        codeView.setText(errorNote + event);
        codeView.setVisibility(View.VISIBLE);
        if (msg != null && !msg.isEmpty()) {
            msgView.setText("(" + msg + ")");
            msgView.setVisibility(View.VISIBLE);
        } else {
            msgView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setVisibility(int visibility) {
    	isShowNoWifi = false;
        super.setVisibility(visibility);
        switch (visibility) {
            case View.GONE:
            case View.INVISIBLE:
                if (uiPlayContext != null) {
                    uiPlayContext.setNotiveViewShowing(false);
                }
                break;
            case View.VISIBLE:
                if (uiPlayContext != null) {
                    uiPlayContext.setNotiveViewShowing(true);
                }
                bringToFront();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNetWorkChange(boolean state, String message) {
        if (state) {
            // setVisibility(View.GONE);
        } else {
        	if (NetworkUtils.hasConnect(context)) {
//        		3g -- 2g
        		if (uiPlayContext.isNoWifiContinue()) {
					return;
				}
        		showNoWifiNetworkNotice();
			} else {
				showNetworkNotice();
			}
        }
    }

    private void showNoWifiNetworkNotice() {
    	// 解决网络访问提示
    	showMsg(10000, null);
    	noteView.setText("您在使用运营商网络，继续观看会产生流量费用");
    	btn_error_replay.setText("继续观看");
    	btn_error_report.setText("取消观看");
    	setVisibility(View.VISIBLE);
    	isShowNoWifi = true;
    }

    private void showNetworkNotice() {
        // 解决网络访问提示
        showMsg(10000, "网络访问失败");
        noteView.setText("( >﹏< )无法连接到网络，请检查后重试");
        btn_error_replay.setText("重新播放");
    	btn_error_report.setText("提交反馈");
        setVisibility(View.VISIBLE);
    }
}
