package com.juss.live.skin.simple.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.juss.live.skin.skin.v4.V4PlaySkin;
import com.letv.universal.widget.ReSurfaceView;

public class LetvNormalVideoHelper extends LetvBaseHelper {
    @Override
    public void init(Context mContext, Bundle mBundle, V4PlaySkin skin) {
        super.init(mContext, mBundle, skin);
        initVideoView();
    }

    private void initVideoView() {
        if (videoView == null || !(videoView instanceof ReSurfaceView)) {
            ReSurfaceView videoView = new ReSurfaceView(mContext);
            videoView.getHolder().addCallback(surfaceCallback);
            videoView.setVideoContainer(null);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            skin.addVideoView(videoView, params);
            this.videoView = videoView;
        }
        playContext.setVideoContentView(videoView);
    }




}
