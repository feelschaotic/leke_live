package com.juss.live.skin.skin.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.juss.live.skin.skin.BaseView;
import com.lecloud.leutils.ReUtils;
import com.letv.controller.interfacev1.ISplayerController;


/**
 * 改变屏幕播放尺寸按钮
 * 
 * @author pys
 *
 */
public abstract class BaseChgScreenBtn extends BaseView implements OnClickListener {

    private ImageView chgScreenBtn;
//    protected String zoomInStyle = "";
//    protected String zoomOutStyle = "";

    public BaseChgScreenBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseChgScreenBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseChgScreenBtn(Context context) {
        super(context);
    }

    protected abstract String getLayoutId();
    
    protected abstract String getZoomInStyle();
    
    protected abstract String getZoomOutStyle();

    @Override
    protected void initView(Context context) {
        this.context = context;

        chgScreenBtn = (ImageView) LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, getLayoutId()), null);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(chgScreenBtn, params);
        setOnClickListener(this);
        reset();
    }

    @Override
    public void onClick(View v) {
        if (player != null && uiPlayContext != null && !uiPlayContext.isLockFlag()) {
            switch (uiPlayContext.getScreenResolution(context)) {
                case ISplayerController.SCREEN_ORIENTATION_PORTRAIT:
                    player.setScreenResolution(ISplayerController.SCREEN_ORIENTATION_USER_LANDSCAPE);
                break;
                case ISplayerController.SCREEN_ORIENTATION_LANDSCAPE:
                    player.setScreenResolution(ISplayerController.SCREEN_ORIENTATION_USER_PORTRAIT);
                break;
            default:
//            	player.setScreenResolution(UIPlayerControl.SCREEN_ORIENTATION_USER_PORTRAIT);
                break;
            }
        }
    }

    /**
     * 展示放大状态
     */
    public void showZoomInState() {
        int drawableId = ReUtils.getDrawableId(context, getZoomInStyle());
        chgScreenBtn.setImageResource(drawableId);
    }

    /**
     * 展示缩小状态
     */
    public void showZoomOutState() {
        int drawableId = ReUtils.getDrawableId(context, getZoomOutStyle());
        chgScreenBtn.setImageResource(drawableId);
    }

    /**
     * 恢复初始状态
     */
    public void reset() {
        showZoomInState();
    }

    @Override
    protected void initPlayer() {

    }

}
