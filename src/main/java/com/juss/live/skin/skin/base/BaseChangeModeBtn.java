package com.juss.live.skin.skin.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.juss.live.skin.skin.BaseView;
import com.juss.live.skin.skin.utils.UIPlayContext;
import com.lecloud.leutils.ReUtils;
import com.letv.controller.interfacev1.IPanoVideoChangeMode;

import java.util.List;

/**
 * 切换全景使用方式 touch 或者陀螺仪
 * 
 * @author heyuekuai
 */
public abstract class BaseChangeModeBtn extends BaseView implements OnClickListener {
    protected ImageView imageBtn;

    protected IPanoVideoChangeMode changeModelListener;

    public BaseChangeModeBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseChangeModeBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseChangeModeBtn(Context context) {
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private boolean checkSensor() {
        SensorManager sm = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        // 获取全部传感器列表
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ALL);
        // 打印每个传感器信息
        StringBuilder strLog = new StringBuilder();
        int iIndex = 1;
        //旋转矢量传感器  判断是否支持陀螺仪
        for (Sensor item : sensors) {
            strLog.append(iIndex + ".");
            strLog.append("	Sensor Type - " + item.getType() + "\r\n");
            strLog.append("	Sensor Name - " + item.getName() + "\r\n");
            strLog.append("	Sensor Version - " + item.getVersion() + "\r\n");
            strLog.append("	Sensor Vendor - " + item.getVendor() + "\r\n");
            strLog.append("	Maximum Range - " + item.getMaximumRange() + "\r\n");
            strLog.append("	Minimum Delay - " + item.getMinDelay() + "\r\n");
            strLog.append("	Power - " + item.getPower() + "\r\n");
            strLog.append("	Resolution - " + item.getResolution() + "\r\n");
            strLog.append("\r\n");
            iIndex++;
            if (item.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void initView(Context context) {
        this.context = context;
        setVisibility(View.GONE);
        imageBtn = (ImageView) LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, getLayout()), null);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        this.addView(imageBtn, params);
        setOnClickListener(this);
    }


    @Override
    public void attachUIContext(UIPlayContext playContext) {
        super.attachUIContext(playContext);
        reset();
    }

    @Override
    public void onClick(View v) {
        if (checkSensor()) {
            if (uiPlayContext.panoMode == UIPlayContext.MODE_TOUCH) {
                uiPlayContext.panoMode = UIPlayContext.MODE_MOVE;
            } else {
                uiPlayContext.panoMode = UIPlayContext.MODE_TOUCH;
            }
            if (changeModelListener != null) {
                changeModelListener.switchPanoVideoMode(uiPlayContext.panoMode);
            }
            reset();
        }
    }

    public void registerPanoVideoChange(IPanoVideoChangeMode changeModelListener) {
        this.changeModelListener = changeModelListener;
        if (changeModelListener != null) {
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }
    }

    @Override
    protected void reset() {
        String btnResId = uiPlayContext.panoMode == UIPlayContext.MODE_TOUCH ? getTouchStyle() : getMoveStyle();
        imageBtn.setImageResource(ReUtils.getDrawableId(context, btnResId));
    }

    protected abstract String getMoveStyle();

    protected abstract String getTouchStyle();

    protected abstract String getLayout();

    @Override
    protected void initPlayer() {

    }
}
