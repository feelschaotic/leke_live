package com.juss.live.skin.skin.v4;

import android.content.Context;
import android.util.AttributeSet;

import com.juss.live.skin.skin.base.BaseRateTypeBtn;


public class V4RateTypeBtn extends BaseRateTypeBtn {

    public V4RateTypeBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4RateTypeBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4RateTypeBtn(Context context) {
        super(context);
    }

	@Override
	protected String getLayout() {
		return "letv_skin_v4_ratetype_button_layout";
	}


}
