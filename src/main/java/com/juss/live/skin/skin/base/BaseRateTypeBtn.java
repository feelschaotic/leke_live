package com.juss.live.skin.skin.base;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.juss.live.skin.skin.BaseViewGourp;
import com.juss.live.skin.skin.model.RateTypeItem;
import com.juss.live.skin.skin.utils.UIPlayContext;
import com.juss.live.skin.skin.v4.V4RateTypePopupWindow;
import com.lecloud.leutils.ReUtils;
import com.letv.controller.interfacev1.ILetvPlayerController;
import com.letv.controller.interfacev1.ISplayerController;
import com.letv.universal.iplay.ISplayer;
import com.letv.universal.notice.UIObserver;

import java.util.Observable;

/**
 * 码率切换按钮
 * 
 * @author pys
 *
 */
public abstract class BaseRateTypeBtn extends BaseViewGourp implements UIObserver {

    private static final String TAG = "RateTypeBtn";
    private Button rateTypeBtn;
    private V4RateTypePopupWindow popupWindow;

    public BaseRateTypeBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseRateTypeBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRateTypeBtn(Context context) {
        super(context);
    }

    @Override
    protected void initPlayer() {
        player.attachObserver(this);
        // TODO 初始化的时候设置码率
        Log.d(TAG, "[rateType] init :" + uiPlayContext.getCurrentRateType());
        setRateType(uiPlayContext.getCurrentRateType());
    }
    
    protected abstract String getLayout();

    @Override
    protected void initView(Context context) {
        rateTypeBtn = (Button) LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, getLayout()), null);
        popupWindow = new V4RateTypePopupWindow(context);
        rateTypeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && !popupWindow.isShown()) {
                    popupWindow.show(v);
                }
            }
        });

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(rateTypeBtn, lp);
        rateTypeBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void update(Observable observable, Object data) {
        Bundle bundle = (Bundle) data;
        int state = bundle.getInt("state");
        switch (state) {
        case ISplayer.MEDIA_EVENT_PREPARE_COMPLETE:
            setRateType(uiPlayContext.getCurrentRateType());
            break;
        case ISplayer.PLAYER_EVENT_RATE_TYPE_CHANGE:
            
            int rateType=bundle.getInt(String.valueOf(ISplayer.PLAYER_EVENT_RATE_TYPE_CHANGE));
            uiPlayContext.setCurrentRateType(rateType);
            setRateType(rateType);
            break;
            case ISplayerController.SCREEN_ORIENTATION_PORTRAIT:
            case ISplayerController.SCREEN_ORIENTATION_REVERSE_PORTRAIT:
            case ISplayerController.SCREEN_ORIENTATION_USER_PORTRAIT:
            case ISplayerController.SCREEN_ORIENTATION_LANDSCAPE:
            case ISplayerController.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
            case ISplayerController.SCREEN_ORIENTATION_USER_LANDSCAPE:
            if (popupWindow != null) {
                popupWindow.hide();
            }
            break;
        default:
            break;
        }
    }

    private void setRateType(int state) {
        if (uiPlayContext != null) {
            RateTypeItem item = uiPlayContext.getRateTypeItemById(state);
            if (item != null) {
                String name = item.getName();
                rateTypeBtn.setText(name);
                rateTypeBtn.setVisibility(View.VISIBLE);
            } else {
                rateTypeBtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onAttachUIPlayControl(ILetvPlayerController playerControl) {
        if (popupWindow != null) {
            popupWindow.attachUIPlayControl(playerControl);
        }
    }

    @Override
    protected void onAttachUIPlayContext(UIPlayContext uiPlayContext) {
        if (popupWindow != null) {
            popupWindow.attachUIContext(uiPlayContext);
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        switch (visibility) {
        case View.GONE:
        case View.INVISIBLE:
            if (popupWindow != null) {
                popupWindow.hide();
            }
            break;
        case View.VISIBLE:
            break;

        default:
            break;
        }
    }
}
