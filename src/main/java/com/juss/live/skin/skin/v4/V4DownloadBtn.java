package com.juss.live.skin.skin.v4;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.OnClickListener;

import com.juss.live.skin.skin.base.BaseDownloadBtn;


/**
 * 下载按钮
 * @author dengjiaping
 */
public class V4DownloadBtn extends BaseDownloadBtn implements OnClickListener {

	public V4DownloadBtn(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public V4DownloadBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public V4DownloadBtn(Context context) {
		super(context);
	}

	@Override
	protected String getLayout() {
		return "letv_skin_v4_download_button_layout";
	}
	
}
