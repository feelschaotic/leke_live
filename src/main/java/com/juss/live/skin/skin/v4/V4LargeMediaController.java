package com.juss.live.skin.skin.v4;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.juss.live.skin.skin.base.BaseChangeModeBtn;
import com.juss.live.skin.skin.base.BasePlayerSeekBar;
import com.juss.live.skin.skin.controller.BaseMediaController;
import com.juss.live.skin.skin.utils.PlayerTimer;
import com.juss.live.skin.skin.widget.TextTimerView;
import com.letv.controller.interfacev1.IPanoVideoChangeMode;
import com.letv.controller.interfacev1.ISplayerController;
import com.letv.universal.iplay.IPlayer;
import com.letv.universal.iplay.ISplayer;
import com.letv.universal.notice.UIObserver;

import java.util.Observable;
import java.util.Observer;

public class V4LargeMediaController extends BaseMediaController {
    public static final String PANO_CHANGE_MODE_NAME = "vnew_change_mode";
    private BasePlayerSeekBar seekbar;
	private TextTimerView timerView;

    public V4LargeMediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4LargeMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4LargeMediaController(Context context) {
        super(context);
    }

    public void registerPanoVideoChange(IPanoVideoChangeMode changeModeListener) {
         View view = findViewByResName(PANO_CHANGE_MODE_NAME);
         if (view != null && view instanceof BaseChangeModeBtn) {
             ((BaseChangeModeBtn) view).registerPanoVideoChange(changeModeListener);
         }
    }
    
    private View findViewByResName(String resName) {
        int index = childId.indexOf(resName);
        if (index != -1) {
            return childViews.get(index);
        }
        return null;
    }
    
    @Override
    protected void onSetLayoutId() {
        layoutId = "letv_skin_v4_controller_large_layout";
        childId.add("vnew_seekbar");
        childId.add("vnew_play_btn");
        childId.add("vnew_chg_btn");
        childId.add("vnew_text_duration_ref");
        childId.add("vnew_rate_btn");
        childId.add(PANO_CHANGE_MODE_NAME);
    }

    @Override
    protected void onInitView() {
        seekbar = (BasePlayerSeekBar) childViews.get(0);
        V4ChgScreenBtn chgBtn = (V4ChgScreenBtn) childViews.get(2);
        chgBtn.showZoomOutState();

        timerView = (TextTimerView) childViews.get(3);
        
    }

    @Override
    protected void initPlayer() {
        initTimer(player);
        player.attachObserver(new UIObserver() {
            @Override
            public void update(Observable observable, Object data) {
                Bundle bundle = (Bundle) data;
                int state = bundle.getInt("state");
                switch (state) {
                case ISplayer.PLAYER_EVENT_STOP:
                case IPlayer.MEDIA_EVENT_PLAY_COMPLETE:
                    reset();
                    break;
                default:
                    break;
                }

            }
        });
    }

    private void initTimer(ISplayerController player) {
//        TimerUtils.initTextFormatter();
        seekbar.getPlayerTimer().getObserver().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                Bundle bundle = (Bundle) data;
                if (bundle.getInt("state") == PlayerTimer.TIMER_HANDLER_PER_TIME) {
                    int position = bundle.getInt(PlayerTimer.key_position);
                    int duration = bundle.getInt(PlayerTimer.key_duration);
                    timerView.setTextTimer(position, duration);
                }
            }
        });
    }

    @Override
    protected void reset() {
        super.reset();
        timerView.reset();
    }
    
    public BasePlayerSeekBar getSeekbar() {
		return seekbar;
	}

}
