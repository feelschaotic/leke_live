package com.juss.live.skin.skin.v4;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.juss.live.skin.skin.BaseView;
import com.lecloud.base.net.BaseCallback;
import com.lecloud.base.net.json.ResultJson;
import com.lecloud.js.action.entity.ActionLiveConfig;
import com.lecloud.leutils.PxUtils;
import com.lecloud.volley.RequestQueue;
import com.lecloud.volley.VolleyError;
import com.lecloud.volley.toolbox.ImageCache;
import com.lecloud.volley.toolbox.ImageLoader;
import com.lecloud.volley.toolbox.NetworkImageView;
import com.lecloud.volley.toolbox.Volley;
import com.letv.controller.interfacev1.ILetvPlayerController;
import com.letv.universal.notice.UIObserver;

import java.util.Observable;

/**
 * 水印
 * @author dengjiaping
 */
public class V4WaterMarkView extends BaseView implements UIObserver {

	private NetworkImageView watermark;
	private ActionLiveConfig mActionLiveConfig;
	private ImageLoader imageLoader;
	private RequestQueue mRequestQueue;

    public V4WaterMarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4WaterMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4WaterMarkView(Context context) {
        super(context);
    }

    @Override
    protected void initPlayer() {
        player.attachObserver(this);
    }


    @Override
    protected void initView(final Context context) {
    	
    }

    @Override
    public void update(Observable observable, Object data) {
//        Bundle bundle = (Bundle) data;
//        switch (bundle.getInt("state")) {
//        case ISplayer.MEDIA_EVENT_PREPARE_COMPLETE:
//            setState();
//            break;
//
//        default:
//            break;
//        }
    }
    
    @Override
	public void attachUIPlayControl(ILetvPlayerController playerControl) {
		super.attachUIPlayControl(playerControl);

		watermark = new NetworkImageView(context);
        mRequestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(mRequestQueue, new ImageCache() {

			@Override
			public void putBitmap(String url, Bitmap bitmap) {
			}

			@Override
			public Bitmap getBitmap(String url) {
				return null;
			}
		});
        requestActionConfig();
    }
    
    private void requestActionConfig() {
        if (TextUtils.isEmpty(uiPlayContext.getActivityId())) {
            return;
        }
		requestController.requestActionConfig(uiPlayContext.getActivityId(), new BaseCallback<ActionLiveConfig>() {
			
			@Override
			public void onSuccess(ResultJson<ActionLiveConfig> response) {
				if (null == response) {
					return;
				}
				mActionLiveConfig = response.getData();
				if (mActionLiveConfig != null) {
					if ("1".equals(mActionLiveConfig.getWatermarkStatus())) {
						try {
							int pos = Integer.parseInt(mActionLiveConfig.getWatermarkPos());
							setWatermarkLocation(pos, mActionLiveConfig.getWatermarkUrl());
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					} else {
						watermark.setVisibility(View.GONE);
					}
				}
			}
			
			@Override
			public void onFail(VolleyError error) {
				
			}
		});
    }
    
    /**
	 * 设置水印的位置
	 * 
	 * @param location
	 */
	private void setWatermarkLocation(int location, String imgUrl) {
		LayoutParams params = new LayoutParams(PxUtils.dip2px(context, 40), PxUtils.dip2px(context, 26));
		switch (location) {
		case 1: // 左上角
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			break;
		case 2: // 右上角
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

			break;
		case 3: // 左下角
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			break;
		case 4: // 右下角
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			break;
		}
		int margin = PxUtils.dip2px(context, 40);
		params.leftMargin = margin;
		params.rightMargin = margin;
		params.topMargin = margin;
		params.bottomMargin = margin;
		this.watermark.setLayoutParams(params);
		this.watermark.setImageUrl(imgUrl, imageLoader);
		((RelativeLayout)this.getParent()).removeView(watermark);
		((RelativeLayout)this.getParent()).addView(watermark);
	}
}
