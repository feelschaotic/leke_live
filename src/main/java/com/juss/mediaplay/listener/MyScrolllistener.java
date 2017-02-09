package com.juss.mediaplay.listener;

import android.widget.AbsListView;

/**
 * Created by lenovo on 2016/7/31.
 */
public interface MyScrolllistener extends AbsListView.OnScrollListener {
    @Override
    void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);

    @Override
    void onScrollStateChanged(AbsListView view, int scrollState);
}
