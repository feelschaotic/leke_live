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


/**
 * 下载按钮
 * @author dengjiaping
 */
public abstract class BaseDownloadBtn extends BaseView implements OnClickListener {
	
	private ImageView downloadBtn;
	
	public BaseDownloadBtn(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public BaseDownloadBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseDownloadBtn(Context context) {
		super(context);
	}
	
	@Override
	protected void initView(Context context){
		this.context=context;
		downloadBtn=(ImageView) LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, getLayout()), null);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		this.addView(downloadBtn, params);
		reset();
	}
	
	public void setOnDownloadClickListener(OnClickListener l) {
		downloadBtn.setOnClickListener(l);
	}

	@Override
	public void onClick(View v) {
		downloadBtn.setEnabled(false);
	}

	@Override
	protected void initPlayer() {
		
	}
	
	protected abstract String getLayout();

}
