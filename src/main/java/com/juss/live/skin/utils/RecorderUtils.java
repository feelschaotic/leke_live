package com.juss.live.skin.utils;

import android.app.Activity;
import android.content.Context;

public class RecorderUtils {

	public static int getScreenWidth(Context context){
		return ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
	}
	public static int getScreenHeight(Context context){
		return ((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
	}
}
