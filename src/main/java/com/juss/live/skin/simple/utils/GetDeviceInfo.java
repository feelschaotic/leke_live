package com.juss.live.skin.simple.utils;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by gaolinhua on 2016/4/17.
 */
public class GetDeviceInfo {
    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }
    public static int getScreenHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

}
