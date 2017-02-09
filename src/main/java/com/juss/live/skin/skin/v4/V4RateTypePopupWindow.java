package com.juss.live.skin.skin.v4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.juss.live.skin.skin.model.RateTypeItem;
import com.juss.live.skin.skin.popupwindow.BaseRateTypePopupWindow;
import com.lecloud.leutils.ReUtils;

public class V4RateTypePopupWindow extends BaseRateTypePopupWindow {

    private static final String TAG = "V4RateTypePopupWindow";

    @Override
    protected BaseAdapter setAdapter() {
        adapter = new RateTypeAdateper();
        return adapter;
    }

    @Override
    protected String setLayoutId() {
        return "letv_skin_v4_ratetype_layout";
    }

    public V4RateTypePopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4RateTypePopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4RateTypePopupWindow(Context context) {
        super(context);
    }

    private RateTypeAdateper adapter;

    private class RateTypeAdateper extends BaseAdapter {
        private static final String Selected_Text_Color = "#ff00a0e9";
        private int currentIndex;

        @Override
        public int getCount() {
            if (uiPlayContext != null && uiPlayContext.getRateTypeItems() != null) {
                return uiPlayContext.getRateTypeItems().size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_skin_v4_ratetype_item"), null);
            if (uiPlayContext != null) {
                RateTypeItem item = uiPlayContext.getRateTypeItems().get(position);
                view.setText(item.getName());
                if (item.getTypeId() == uiPlayContext.getCurrentRateType()) {
                    view.setTextColor(Color.parseColor(Selected_Text_Color));
                } else {
                    view.setTextColor(Color.WHITE);
                }
            }
            return view;
        }
    }

    @Override
    protected int getPopHeight(View anchor) {
        if(anchor==null||adapter==null){
            return 0;
        }
        return ((anchor.getHeight() + anchor.getPaddingRight()) * adapter.getCount());
    }

    @Override
    protected int getPopWidth(View anchor) {
        return 0;
    }
}
