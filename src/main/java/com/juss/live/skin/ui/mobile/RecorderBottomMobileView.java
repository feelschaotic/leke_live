package com.juss.live.skin.ui.mobile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juss.live.LiveConsole;
import com.juss.live.skin.ui.RecorderBottomView;
import com.letv.recorder.ui.logic.RecorderConstance;
import com.letv.recorder.util.ReUtils;

import java.util.Observable;

public class RecorderBottomMobileView extends RecorderBottomView {

    private ImageView settingView;
    private TextView rateView;
    private ImageView flashlightView;
    private ImageView voiceView;
    private ImageView chageCamera;
    private View inflate;

    private boolean useFlash = false;
    private boolean useBackCamera = true;
    private boolean useMic = true;

    private String letv_recorder_bottom_default_layout = "letv_recorder_bottom_float_mobile_layout";
    private String letv_recorder_bottom_no_bottom_layout = "activity_double_live_host_base";

    public RecorderBottomMobileView(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
    }

    public void init(boolean isSkinDefult) {
        if (isSkinDefult) {
            inflate = LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, letv_recorder_bottom_default_layout), this);
            initBottomCustom();
        } else {
            inflate = LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, letv_recorder_bottom_no_bottom_layout), this);
        }
     /*   settingView = (ImageView) findViewById(ReUtils.getId(context, "imgV_setting"));
        rateView = (TextView) findViewById(ReUtils.getId(context, "tv_rate"));*/
        flashlightView = (ImageView) findViewById(ReUtils.getId(context, "imgV_flashlight"));

        //   voiceView = (ImageView) findViewById(ReUtils.getId(context, "imgV_voice"));
        chageCamera = (ImageView) findViewById(ReUtils.getId(context, "imgV_postposition_camera"));

        initListener();
    }

    public void initBottomCustom() {
        LiveConsole console = new LiveConsole(getContext(), inflate);
        console.initMyDanMuView();
    }

    private void initListener() {

        flashlightView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if (useFlash) {
                    useFlash = false;
                    bundle.putInt("flag", RecorderConstance.disable_flash);
                    flashlightView.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_flash_light_close"));
                } else {
                    useFlash = true;
                    bundle.putInt("flag", RecorderConstance.enable_flash);
                    flashlightView.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_flash_light_open"));
                }
                getBottomSubject().notifyObserverPlus(bundle);
            }
        });
    /*	voiceView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				if(useMic){
					useMic = false;
					bundle.putInt("flag", RecorderConstance.disable_mic);
					voiceView.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_voise_close"));
				}else{
					useMic = true;
					bundle.putInt("flag", RecorderConstance.enable_mic);
					voiceView.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_voise_open"));
				}
				getBottomSubject().notifyObserverPlus(bundle);
			}
		});*/
        chageCamera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Rotate3dAnimation animation = new Rotate3dAnimation(0, 180, chageCamera.getWidth() / 2f, chageCamera.getHeight() / 2f, 0f, true);
                animation.setDuration(500);
                animation.setFillAfter(true);
                chageCamera.startAnimation(animation);
                Bundle bundle = new Bundle();
                if (useBackCamera) {
                    useBackCamera = false;
                    bundle.putInt("flag", RecorderConstance.use_front_cam);
                    useFlash = false;
                    flashlightView.setVisibility(View.INVISIBLE);
                } else {
                    useBackCamera = true;
                    bundle.putInt("flag", RecorderConstance.use_back_cam);
                    useFlash = false;
                    flashlightView.setVisibility(View.VISIBLE);
                    flashlightView.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_flash_light_close"));
                }
                getBottomSubject().notifyObserverPlus(bundle);
            }
        });

      /*  settingView.setVisibility(View.GONE);
        rateView.setVisibility(View.GONE);*/
    }


    @Override
    public void update(Observable observable, Object data) {

    }


    @Override
    public void reset() {
    }

    @Override
    public void reSetRunnable() {
    }

    @Override
    public void clear() {
    }


}
