package com.juss.live.skin.skin.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.lecloud.leutils.ReUtils;

public class DefaultLoadingView extends BaseLoadingView {

    public DefaultLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DefaultLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultLoadingView(Context context) {
        super(context);
    }

    @Override
    protected void onInitView(Context context) {
      LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_skin_loading_layout"), this);
    }

	@Override
	protected void setLetvVisibility(int gone) {
		// TODO Auto-generated method stub
		
	}

}
