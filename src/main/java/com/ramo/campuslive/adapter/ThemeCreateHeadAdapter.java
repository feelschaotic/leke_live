package com.ramo.campuslive.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ramo.campuslive.R;
import com.ramo.campuslive.utils.ImageManageUtil;

import java.util.List;

/**
 * Created by ramo on 2016/7/8.
 */
public class ThemeCreateHeadAdapter extends BaseAdapter {
    private Context context;
    private List<byte[]> imgList;

    public ThemeCreateHeadAdapter(Context context, List<byte[]> imgList) {
        this.context = context;
        this.imgList = imgList;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public Object getItem(int position) {
        return imgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ThemeCreateHeadHolder hold;
        if (convertView == null) {
            hold = new ThemeCreateHeadHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.invite_host_head_item, null);
            convertView.setTag(hold);
        } else {
            hold = (ThemeCreateHeadHolder) convertView.getTag();
        }
        convertView.setLayoutParams(new GridView.LayoutParams(100, 100));

        hold.create_theme_head = (ImageView) convertView.findViewById(R.id.create_theme_head);

        if (imgList.get(position)==null)
            hold.create_theme_head.setImageResource(R.drawable.head2);
        else
            hold.create_theme_head.setImageDrawable(ImageManageUtil.Byte2Drawable(imgList.get(position)));
        return convertView;
    }

    class ThemeCreateHeadHolder {
        ImageView create_theme_head;
    }
}
