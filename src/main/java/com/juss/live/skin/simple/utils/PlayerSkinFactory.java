package com.juss.live.skin.simple.utils;

import com.juss.live.skin.skin.BaseSkin;
import com.juss.live.skin.skin.interfacev1.IActionCallback;
import com.juss.live.skin.skin.utils.UIPlayContext;
import com.juss.live.skin.skin.v4.V4PlaySkin;
import com.letv.controller.interfacev1.ISplayerController;

public class PlayerSkinFactory {
    
    /**
     * 创建播放器皮肤
     * @param skin
     * @param skinBuildType
     * @return
     */
	/**
	 * 1、点播的时候初始化播放皮肤
	 * 2、playerskin,utils中UIPlayContext
	 * 3、设置皮肤类型，设置竖屏
	 * 4、设置播放界面大小
	 * 5、
	 * */
    public static BaseSkin initPlaySkin(BaseSkin skin, int skinBuildType){
        UIPlayContext uicontext=new UIPlayContext();
        uicontext.setSkinBuildType(skinBuildType);
        uicontext.setScreenResolution(ISplayerController.SCREEN_ORIENTATION_PORTRAIT);
//        uicontext.setScreenResolution(UIPlayerControl.SCREEN_ORIENTATION_LANDSCAPE);
        if(skin instanceof V4PlaySkin){
            ((V4PlaySkin)skin).changeLayoutParams(16, 9);
        }
        skin.build(uicontext);
        return skin;
    }

    /**
     * 创建活动直播皮肤
     * @param skin
     * @param skinBuildType
     * @param activityId
     * @param mIActionCallback
     * @return
     */
    public static BaseSkin initActionLivePlaySkin(BaseSkin skin,int skinBuildType,String activityId,IActionCallback mIActionCallback){
        ((V4PlaySkin)skin).setIActionCallback(mIActionCallback);
        
        UIPlayContext uicontext=new UIPlayContext();
        uicontext.setSkinBuildType(skinBuildType);
        uicontext.setScreenResolution(ISplayerController.SCREEN_ORIENTATION_PORTRAIT);
        if(skin instanceof V4PlaySkin){
            ((V4PlaySkin)skin).changeLayoutParams(16, 9);
        }
        uicontext.setActivityId(activityId);
        skin.build(uicontext);
        
        return skin;
    }

}
