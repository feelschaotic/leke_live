package com.juss.live.skin.skin;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.juss.live.skin.skin.utils.UIPlayContext;
import com.letv.controller.interfacev1.ILetvPlayerController;
import com.letv.controller.interfacev1.IPlayerRequestController;
import com.letv.controller.interfacev1.ISplayerController;


/**
 * 皮肤基础类
 * 
 * @author pys
 *
 */
public abstract class BaseView extends RelativeLayout {
    protected Context context;
    protected ISplayerController player;
    protected IPlayerRequestController requestController;
    protected UIPlayContext uiPlayContext;
    private boolean isMark;

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    public BaseView(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    /**
     * view初始化完毕之后，需要获取到ui的一些上下文，父类初始化完毕之后，会调用此方法
     * 
     * @param playContext
     */
    public void attachUIContext(UIPlayContext playContext) {
        this.uiPlayContext = playContext;
    }

    public void attachUIPlayControl(ILetvPlayerController playerControl) {
        this.player = playerControl.getIsPlayerController();
        this.requestController = playerControl.getRequestController();
        initPlayer();
    }

    /**
     * 播放器已经初始化完毕了，在子类可以对播放器做一些处理
     */
    protected abstract void initPlayer();

    /**
     * 初始化view
     * 
     * @param context
     */
    protected abstract void initView(Context context);

    /**
     * 将view恢复初始化状态
     */
    protected void reset() {

    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public boolean isMark() {
        return isMark;
    }

    public void setMark(boolean isMark) {
        this.isMark = isMark;
    }
}
