package com.ramo.campuslive.adapter;

/**
 * Created by ramo on 2016/8/5.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramo.campuslive.R;
import com.ramo.campuslive.bean.Gift;
import com.ramo.campuslive.utils.ImageManageUtil;

import java.util.List;


/**
 * Created by ramo on 2016/3/24.
 */
public class LiveGiftAdapter extends BaseAdapter {
    private List<Gift> giftList;
    private Context context;

    public LiveGiftAdapter(Context context, List<Gift> giftList) {
        this.context = context;
        this.giftList = giftList;
    }

    @Override
    public int getCount() {
        return giftList.size();
    }

    @Override
    public Object getItem(int position) {
        return giftList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.select_gift_item, null);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.iv = (ImageView) convertView.findViewById(R.id.select_gift_img);
        holder.price = (TextView) convertView.findViewById(R.id.select_gift_price);
        holder.name = (TextView) convertView.findViewById(R.id.select_gift_name);
        holder.select_gift_check = (ImageView) convertView.findViewById(R.id.select_gift_check);


        Gift gift = giftList.get(position);
        holder.select_gift_check.setVisibility(View.GONE);

        if (gift.getImg() == null)
            holder.iv.setImageResource(R.mipmap.ic_launcher);
        else
            holder.iv.setImageBitmap(ImageManageUtil.Byte2Bitmap(gift.getImg()));

        holder.price.setText(String.valueOf(gift.getPrice()));
        holder.name.setText(gift.getName());
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.iv.setTag(position);
        return convertView;
    }

    class Holder {
        ImageView iv;
        TextView price;
        TextView name;
        ImageView select_gift_check;
    }

}



