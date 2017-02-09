package com.juss.live.skin.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.letv.recorder.bean.LivesInfo;
import com.letv.recorder.ui.logic.RecorderConstance;
import com.letv.recorder.ui.logic.UiObservable;
import com.letv.recorder.util.ReUtils;

import java.util.ArrayList;

/**
 * 机位选择
 * @author pys
 *
 */
public class RecorderMachineView extends FrameLayout {

	private Context context;
	private GridView gridView;
	private HorizontalScrollView container;
	private ArrayList<LivesInfo> livesInfos;
	private UiObservable oberverable = new UiObservable();

	public RecorderMachineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public RecorderMachineView(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	public UiObservable getMachineObserable() {
		return oberverable;
	}

	private void initView() {
		LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_recorder_machine_layout"), this);
		gridView = (GridView) findViewById(ReUtils.getId(context, "letv_recorder_machine_view"));
		container = (HorizontalScrollView) findViewById(ReUtils.getId(context, "letv_recorder_grid_view_container"));

		GridViewAdapter adapter = new GridViewAdapter();
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putInt("flag", RecorderConstance.selected_angle);
				bundle.putInt("numFlag", position);
				oberverable.notifyObserverPlus(bundle);
			}
		});
//		initGridView();
	}

	private void initGridView() {
		container.setVisibility(View.VISIBLE);

		// List<Bitmap> images = getEditingBitmaps();
		// mGridViewAdapter.setBitmaps(images);
//		int size = Integer.valueOf(recorderInfo.liveNum);
		int size = livesInfos.size();

		int rowNum = 1; // set how many row by yourself
		int gridItemWidth = getResources().getDimensionPixelSize(ReUtils.getDimen(context, "letv_recorder_machine_item_width"));
		int gridItemHeight = getResources().getDimensionPixelSize(ReUtils.getDimen(context, "letv_recorder_machine_item_width"));
		int gap = getResources().getDimensionPixelSize(ReUtils.getDimen(context, "letv_recorder_machine_item_gap"));

		// get how many columns
		int columnNum = size / rowNum;
		if (columnNum % rowNum != 0) {
			columnNum++;
		}

		// get GridView's height and width
		int gridViewWidth = (gridItemWidth + gap) * columnNum;
		int gridViewHeight = (gridItemHeight + gap) * rowNum;

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridViewWidth, gridViewHeight);
		gridView.setLayoutParams(params);
		gridView.setColumnWidth(gridItemWidth);
		gridView.setHorizontalSpacing(gap);
		gridView.setVerticalSpacing(gap);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setNumColumns(columnNum);
	}

	public void setRecorderInfo(ArrayList<LivesInfo> livesInfos) {
		this.livesInfos = livesInfos;
		initGridView();
	}

	private class GridViewAdapter extends BaseAdapter {

		private ViewHolder holder;

		@Override
		public int getCount() {
			return livesInfos != null ? livesInfos.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return livesInfos != null ? livesInfos.get(position) : null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_recorder_machine_item"), parent, false);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView.findViewById(ReUtils.getId(context, "letv_recorder_angle_i"));
				holder.txt = (TextView) convertView.findViewById(ReUtils.getId(context, "letv_recorder_machine_num"));
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			LivesInfo livesInfo = livesInfos.get(position);
			if (livesInfo.status == 0) {
				holder.img.setImageResource(ReUtils.getDrawableId(context, "letv_recorder_angle_blue"));
				holder.txt.setTextColor(ReUtils.getColorId(context, "letv_recorder_text_blue_color"));
			} else {

			}
			holder.txt.setText(position+1+"");

			return convertView;
		}

		class ViewHolder {
			ImageView img;
			TextView txt;
		}
	}

}
