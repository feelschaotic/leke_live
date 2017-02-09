package com.juss.live.skin.skin;

import android.content.Context;
import android.util.AttributeSet;

import com.juss.live.skin.skin.interfacev1.IBaseParentView;
import com.juss.live.skin.skin.utils.UIPlayContext;
import com.letv.controller.interfacev1.ILetvPlayerController;


/**
 * 在baseView的基础上，增加了遍历子view的逻辑
 * 
 * @author pys
 *
 */
public abstract class BaseViewGourp extends BaseView implements IBaseParentView {

    private static final String TAG = "BaseViewGourp";

    public BaseViewGourp(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseViewGourp(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseViewGourp(Context context) {
        super(context);
    }

    @Override
    public void attachUIPlayControl(ILetvPlayerController playerControl) {
        onAttachUIPlayControl(playerControl);
        super.attachUIPlayControl(playerControl);
    }

    @Override
    public void attachUIContext(UIPlayContext playContext) {
        onAttachUIPlayContext(playContext);
        super.attachUIContext(playContext);
    }

    protected abstract void onAttachUIPlayControl(ILetvPlayerController playerControl);

    protected abstract void onAttachUIPlayContext(UIPlayContext uiPlayContext);

}
