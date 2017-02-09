package com.juss.live.skin.skin.v4;


import android.content.Context;
import android.util.AttributeSet;

import com.juss.live.skin.skin.base.BaseLiveSeekBar;

public class V4LiveSeekbar extends BaseLiveSeekBar {

    public V4LiveSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4LiveSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4LiveSeekbar(Context context) {
        super(context);
    }

    @Override
    public String getLayout() {
        return "letv_skin_v4_small_seekbar_layout";
    }
}
