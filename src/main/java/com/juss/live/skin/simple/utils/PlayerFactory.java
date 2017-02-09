package com.juss.live.skin.simple.utils;

import android.os.Bundle;
import android.view.Surface;

import com.letv.controller.LetvPlayer;
import com.letv.controller.PlayContext;
import com.letv.universal.iplay.ISplayer;
import com.letv.universal.iplay.OnPlayStateListener;

public class PlayerFactory {

    /**
     * 创建一个播放器
     * 
     * @param playcontext
     * @param bundle
     * @param playStateListener
     * @param surface
     * @return
     */
    public static ISplayer createOnePlayer(PlayContext playcontext, Bundle bundle, OnPlayStateListener playStateListener, Surface surface) {
        ISplayer player = new LetvPlayer();
        player.setPlayContext(playcontext);
        player.setParameter(player.getPlayerId(), bundle);
        player.init();
        player.setOnPlayStateListener(playStateListener);
        if (surface == null) {
//            throw new RuntimeException("surface is null!");
        }
        player.setDisplay(surface);
        return player;
    }

}
