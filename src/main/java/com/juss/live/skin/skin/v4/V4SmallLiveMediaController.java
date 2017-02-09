package com.juss.live.skin.skin.v4;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.TextView;

import com.juss.live.skin.skin.base.BaseLiveSeekBar;
import com.juss.live.skin.skin.base.BasePlayBtn;
import com.juss.live.skin.skin.controller.BaseMediaController;
import com.lecloud.leutils.ReUtils;

public class V4SmallLiveMediaController extends BaseMediaController {
	private BaseLiveSeekBar seekbar;
	private TextView seekTime;
    public V4SmallLiveMediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4SmallLiveMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4SmallLiveMediaController(Context context) {
        super(context);
    }

    @Override
    protected void onSetLayoutId() {
        layoutId = "letv_skin_v4_controller_live_layout";
        childId.add("vnew_play_btn");
        childId.add("vnew_chg_btn");
        childId.add("vnew_seekbar");
    }

    @Override
    protected void onInitView() {
    	
    	seekbar = (BaseLiveSeekBar) childViews.get(2);
        seekTime = (TextView) findViewById(ReUtils.getId(context, "vnew_time_text"));
        seekbar.setOnSeekChangeListener(new BaseLiveSeekBar.OnSeekChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!seekbar.isShown()) {
                    return;
                }
                if (seekTime != null) {
                    if (seekTime.getVisibility() != VISIBLE) {
                        seekTime.setVisibility(VISIBLE);
                    }
                    LayoutParams params = (LayoutParams) seekTime.getLayoutParams();
                    int right = seekBar.getRight() - seekBar.getWidth() * progress / seekBar.getMax();
                    if (fromUser) {
                        seekTime.setText("正在播放：" + seekbar.getSeekToTime());
                    } else {
                        seekTime.setText("正在播放：" + seekbar.getCurrentTime());
                    }
                    int leftMargin = seekBar.getRight() - right - seekTime.getMeasuredWidth();
                    if (leftMargin > 0) {
                        params.rightMargin = right;
                        seekTime.setLayoutParams(params);
                    }
                }
            }
        });

    }

    @Override
    protected void initPlayer() {
        BasePlayBtn playBtn = (BasePlayBtn) childViews.get(0);
        playBtn.setPlayBtnType(BasePlayBtn.play_btn_type_vod);// 设置按钮模式
    }

}
