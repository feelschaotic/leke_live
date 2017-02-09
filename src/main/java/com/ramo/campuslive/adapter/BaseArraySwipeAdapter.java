package com.ramo.campuslive.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;

/**
 * Created by ramo on 2016/8/7.
 */
public abstract class BaseArraySwipeAdapter extends BaseSwipeAdapter {
    @Override
    public int getSwipeLayoutResourceId(int position) {
        return 0;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return null;
    }

    @Override
    public void fillValues(int position, View convertView) {

    }

}
