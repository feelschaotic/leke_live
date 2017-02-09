package com.juss.live.skin.skin.interfacev1;

import android.view.SurfaceHolder;

import com.letv.universal.iplay.ISplayer;
import com.letv.universal.iplay.OnPlayStateListener;

public interface IActionCallback {
	
	public void switchMultLive(String liveId);
	
	public ISplayer createPlayerCallback(SurfaceHolder holder, String path, OnPlayStateListener playStateListener);
    
}
