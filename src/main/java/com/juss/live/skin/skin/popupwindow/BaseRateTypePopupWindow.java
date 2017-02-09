package com.juss.live.skin.skin.popupwindow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.juss.live.skin.skin.model.RateTypeItem;
import com.lecloud.leutils.ReUtils;

import java.util.ArrayList;

/**
 * 码率切换弹出框
 * 
 * @author pys
 *
 */
public abstract class BaseRateTypePopupWindow extends BasePopupWindow {

	private PopupWindow popupWindow;
	private ListView listView;
	private BaseAdapter adapter;
	private String layoutId;
	private ArrayList<RateTypeItem> rateTypeItems;

	public BaseRateTypePopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public BaseRateTypePopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseRateTypePopupWindow(Context context) {
		super(context);
	}

	@Override
	protected void initPlayer() {
		adapter = setAdapter();
		listView.setAdapter(adapter);
	}

	protected abstract BaseAdapter setAdapter();

	protected abstract String setLayoutId();

	@Override
	protected void initView(Context context) {
		layoutId = setLayoutId();
		listView = (ListView) LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, layoutId), null);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setVerticalScrollBarEnabled(false);
		listView.setHorizontalScrollBarEnabled(false);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				itemClick(parent,view,position,id);				
			}
		});
	}

	protected View getPopContentView() {
		return listView;
	};


	protected  void itemClick(AdapterView<?> parent, View view, int position, long id){
		if(uiPlayContext!=null&&player!=null){
			RateTypeItem item = uiPlayContext.getRateTypeItems().get(position);
			hide();
			adapter.notifyDataSetChanged();
			player.setDefination(item.getTypeId());
		}
	}

}
