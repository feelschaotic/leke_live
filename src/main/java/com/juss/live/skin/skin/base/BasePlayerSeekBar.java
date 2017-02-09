package com.juss.live.skin.skin.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.juss.live.skin.skin.BaseView;
import com.juss.live.skin.skin.utils.PlayerTimer;
import com.lecloud.leutils.ReUtils;
import com.lecloud.leutils.TimerUtils;
import com.letv.universal.iplay.IPlayer;
import com.letv.universal.iplay.ISplayer;
import com.letv.universal.notice.UIObserver;

import java.util.Observable;

/**
 * 
 * @author pys
 *
 */
public abstract class BasePlayerSeekBar extends BaseView implements UIObserver {

    private static final String TAG = "PlayerSeekbar";
    private static final int timeout = 0;
    private PlayerTimer playerTimer;
    protected SeekBar seekBar;
    protected boolean isdragging = false;

    /**
     * 判断是否拖动状态
     */
    protected boolean isTrackingTouch = false;

    public BasePlayerSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BasePlayerSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePlayerSeekBar(Context context) {
        super(context);
    }

    public void pauseSeek() {
        isdragging = true;
    }

    public void resumeSeek() {
        isdragging = false;
    }

    /**
     * 展示seekbar
     * 
     * @param timeout
     *            单位：秒，几秒后会隐藏seekbar
     */
    public void showSeekbar(int timeout) {
        Log.d(TAG, "[skin][seekbar] show and start timer!!");
        setVisibility(View.VISIBLE);
        getPlayerTimer().sendEmptyMessage(PlayerTimer.TIMER_HANDLER_SHOW_PROGRESS);
        if (timeout > 0) {
            getPlayerTimer().removeMessages(PlayerTimer.TIMER_HANDLER_HIDE_PROGRESS);
            Message msg = getPlayerTimer().obtainMessage(PlayerTimer.TIMER_HANDLER_HIDE_PROGRESS);
            getPlayerTimer().sendMessageDelayed(msg, timeout);
        }
    }

    /**
     * 隐藏seekbar,同时会取消获取播放时间
     */
    public void hideSeekbar() {
        if (isShown()) {
            try {
                setVisibility(View.GONE);
                getPlayerTimer().reset();
            } catch (Exception e) {

            }
        }
    }

    public abstract String getLayout();


    /**
     * init view
     * 
     * @param context
     */
    @Override
    protected void initView(Context context) {
        seekBar = (SeekBar) LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, getLayout()), null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(seekBar, params);

        initSeekbar();
        reset();
    }

    private void initPlayTimer() {
        setPlayerTimer(new PlayerTimer(player) {
            @Override
            public void hideProgress() {
                hideSeekbar();
            }
        });
        getPlayerTimer().getObserver().addObserver(this);
    }

    private void initSeekbar() {
        if (seekBar != null) {
            seekBar.setMax(1000);
            seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    stopTrackingTouch();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    startTrackingTouch();
                }
				
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                }
            });
        }
    }
    private int progress;
    
    public void startTrackingTouch() {
    	if (isTrackingTouch) {
			return;
		}
		isTrackingTouch = true;
        progress = seekBar.getProgress();
	}

    public void stopTrackingTouch() {
		isTrackingTouch = false;
        if (player != null) {
            long duration = 0;
            duration = player.getDuration();
            seekTo(seekBar.getProgress() * duration / 1000);
        } else {
            seekBar.setProgress(this.progress);
        }
	}

    /**
     * 重置seekbar
     */
    public void reset() {
        seekBar.setProgress(0);
        if (getPlayerTimer() != null) {
            getPlayerTimer().reset();
        }
        resumeSeek();
    }

    private long getDuration() {
        if (player == null) {
            return 0;
        }
        return player.getDuration();
    }

    private void seekTo(long sec) {
        if (player != null) {
        	if (sec >= player.getDuration()) {
        		sec = sec - 5;
			}
        	if (sec > 0) {
        		player.seekTo(sec);
			}
			if (player.isPlayCompleted()) {
				player.resetPlay();
			} else if (!player.isPlaying()) {
				player.start();
			}
        	
            
            
        }
    }

    @Override
    protected void initPlayer() {
        player.attachObserver(this);
        initPlayTimer();
        if (uiPlayContext != null && uiPlayContext.isSaveInstanceState() && !player.isPlayCompleted()) {
            showSeekbar(timeout);
        }
    };

    //////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////

    @Override
    public void update(Observable observable, Object data) {
        Bundle bundle = (Bundle) data;
        int state = bundle.getInt("state");
        Log.d(TAG, "[ui] seekbar state:" + state);
        switch (state) {
        case IPlayer.MEDIA_EVENT_PLAY_COMPLETE:
            reset();
            break;
        case IPlayer.PLAYER_EVENT_START:
            showSeekbar(timeout);
            break;
        case IPlayer.PLAYER_EVENT_PAUSE:
            break;
        case PlayerTimer.TIMER_HANDLER_PER_TIME:

            if (isTrackingTouch) {
                return;
            }
            int duration = bundle.getInt(PlayerTimer.key_duration);
            int percentage = bundle.getInt(PlayerTimer.key_bufferpercentage);
            int position = bundle.getInt(PlayerTimer.key_position);
            seekBar.setProgress((int) (1000L * position / duration));
            // if(percentage>0){
            // seekBar.setSecondaryProgress((int) (1000L*percentage/duration));
            // }
            break;
        case IPlayer.MEDIA_EVENT_SEEK_COMPLETE:
        case ISplayer.MEDIA_EVENT_FIRST_RENDER:
             getPlayerTimer().resume();
            break;
        case ISplayer.PLAYER_EVENT_SEEK:
        case ISplayer.PLAYER_EVENT_RATE_TYPE_CHANGE:
            /**
             * seek 操作之后，会概率性的获取到当前时间为0的情况，造成seekbar回退到0的情况,需要暂停定时器
             */
             getPlayerTimer().pause();
            break;
        case ISplayer.MEDIA_ERROR_DECODE_ERROR:
        case ISplayer.MEDIA_ERROR_NO_STREAM:
        case ISplayer.PLAYER_PROXY_ERROR:
        case ISplayer.PLAYER_EVENT_STOP:
        case ISplayer.MEDIA_ERROR_UNKNOWN:
            reset();
            break;
        default:
            break;
        }
    }

    public PlayerTimer getPlayerTimer() {
        return playerTimer;
    }

    public void setPlayerTimer(PlayerTimer playerTimer) {
        this.playerTimer = playerTimer;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.w(TAG, "[seekbar] removew from window");
        if (getPlayerTimer() != null) {
            getPlayerTimer().reset();
        }
    }
    
	public void setProgress(int progress) {
		if (seekBar != null) {
			seekBar.setProgress(this.progress+progress);
		}
	}
	
	public CharSequence getPlayerProgress() {
		String progress = TimerUtils.stringForTime((int)(seekBar.getProgress() * player.getDuration()/1000000));
		return progress;
	}
	
	public CharSequence getPlayerDuration() {
		String duration = TimerUtils.stringForTime((int) (player.getDuration()/1000));
		return duration;
	}

}
