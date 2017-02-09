package com.juss.live.skin.skin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.juss.live.skin.skin.BaseView;
import com.lecloud.leutils.ReUtils;
import com.lecloud.leutils.TimerUtils;

public class TextTimerView extends BaseView {

    private TextView positionView;
    private TextView durationView;

    public TextTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TextTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextTimerView(Context context) {
        super(context);
    }

    @Override
    protected void initPlayer() {

    }

    @Override
    protected void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_skin_controller_text_timer"), null);
        positionView = (TextView) view.findViewById(ReUtils.getId(context, "skin_txt_position"));
        durationView = (TextView) view.findViewById(ReUtils.getId(context, "skin_txt_duration"));
        addView(view);
    }

    public void reset() {
        positionView.setText("00:00");
        //durationView.setText("00:00");
    }

    public void setTextTimer(int position, int duration) {
        positionView.setText(TimerUtils.stringForTime(position / 1000));
        durationView.setText(TimerUtils.stringForTime(duration / 1000));
    }

}
