package com.ramo.campuslive.listener;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.widget.SeekBar;

public class DoubleLiveBufferListener implements OnBufferingUpdateListener {
    private SeekBar sb;

    public DoubleLiveBufferListener(SeekBar sb) {
        this.sb = sb;
    }

    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        sb.setSecondaryProgress(mp.getDuration() / 100 * percent); // 显示缓冲数据的进度
    }
}
