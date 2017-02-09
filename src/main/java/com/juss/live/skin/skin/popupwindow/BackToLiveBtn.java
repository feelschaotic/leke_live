package com.juss.live.skin.skin.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.juss.live.skin.skin.BaseView;
import com.lecloud.leutils.ReUtils;

/**
 * 返回直播按钮
 * 
 * @author pys
 *
 */
public class BackToLiveBtn extends BaseView {

    private PopupWindow popupWindow;

    public BackToLiveBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BackToLiveBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackToLiveBtn(Context context) {
        super(context);
    }

    @Override
    protected void initPlayer() {

    }

    @Override
    protected void initView(Context context) {

    }

    public void show(View anchor) {
        if (popupWindow == null) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(ReUtils.getDrawableId(context, "letv_skin_v4_back_to_live"));
            popupWindow=new PopupWindow(imageView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
            popupWindow.setOutsideTouchable(false);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
        }

        int[] location = new int[2];
        anchor.getLocationInWindow(location);
        popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0]+anchor.getWidth(), location[1]);
    }

    public void dismiss() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

}
