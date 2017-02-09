package com.ramo.campuslive;


import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.ramo.campuslive.utils.L;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ramo on 2016/6/30.
 */
@EActivity(R.layout.activity_top_up)
public class TopUpActivity extends SwipeBackActivity {
    @ViewById
    GridLayout top_up_gl;
    @AfterViews
    public void init() {
        initListener();
    }

    private void initListener() {

    }

    public void btnEvent(View v) {
        L.e("View light");
        resetAll();
        v.setBackgroundResource(R.drawable.top_up_money_selected_shape);
        ((TextView) v).setTextColor(0xffffffff);
    }

    private void resetAll() {
        for(int i=0;i<top_up_gl.getChildCount();i++) {
            View v=top_up_gl.getChildAt(i);
            v.setBackgroundResource(R.drawable.top_up_money_shape);
            ((TextView) v).setTextColor(0x8eFFFFFF);
        }
    }
}
