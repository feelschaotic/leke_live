package com.juss.live.skin.skin.loading;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.lecloud.leutils.ReUtils;
import com.lecloud.volley.RequestQueue;
import com.lecloud.volley.toolbox.ImageLoader;
import com.lecloud.volley.toolbox.NetworkImageView;
import com.lecloud.volley.toolbox.Volley;
import com.lecloud.volley.toolbox.image.ImageCacheUtil;

public class MaterialLoadingView extends BaseLoadingView {
	private ImageLoader imageLoader;
    private RequestQueue mRequestQueue;
    
    public MaterialLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MaterialLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialLoadingView(Context context) {
        super(context);
    }

    @Override
    protected void onInitView(Context context) {
      LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_skin_bonus_loading_layout"), this);
      mRequestQueue = Volley.newRequestQueue(context);
      imageLoader = new ImageLoader(mRequestQueue, ImageCacheUtil.getInstance());
      setLetvVisibility(VISIBLE);
    }

	@Override
	protected void setLetvVisibility(int visibility) {
		View loading = findViewById(ReUtils.getId(context, "letv_skin_v4_letv_rl_loading"));
		if (loading != null) {
			loading.setVisibility(visibility);
		}
		if (uiPlayContext == null || uiPlayContext.getCoverConfig() == null ||uiPlayContext.getCoverConfig().getLoadingConfig() == null||uiPlayContext.getCoverConfig().getLoadingConfig().getPicUrl() == null) {
			return;
		}
		View ll_loading = findViewById(ReUtils.getId(context, "letv_skin_v4_letv_ll_loading"));
		final ImageView loadingLine = (ImageView) findViewById(ReUtils.getId(context, "letv_skin_v4_letv_iv_loading_line"));
		final NetworkImageView loadingImageView = (NetworkImageView) findViewById(ReUtils.getId(context, "letv_skin_v4_letv_iv_loading"));
		if (loadingImageView != null && ll_loading != null && loadingLine != null && loadingLine.getDrawable() instanceof AnimationDrawable) {
			ll_loading.setVisibility(View.VISIBLE);
			loadingImageView.setHeightWrapContent(true);
			loadingImageView.setImageUrl(uiPlayContext.getCoverConfig().getLoadingConfig().getPicUrl(), imageLoader);
			post(new Runnable() {
				
				@Override
				public void run() {
					((AnimationDrawable) loadingLine.getDrawable()).start();
				}
			});
		}
	}

}
