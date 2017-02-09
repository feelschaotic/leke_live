package com.ramo.campuslive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramo.campuslive.GiftShopActivity;
import com.ramo.campuslive.R;
import com.ramo.campuslive.bean.Gift;
import com.ramo.campuslive.utils.ImageManageUtil;

import java.util.List;

/**
 * Created by ramo on 2016/4/8.
 */
public class DialogAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Gift> giftList;


    public DialogAdapter(Context context, List<Gift> giftList) {
        this.giftList = giftList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return giftList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return giftList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        DialogHolder holder;
        View v;
        if (convertView == null) {
            v = inflater.inflate(R.layout.dialog_gift_shopping_cart_item, parent, false);
            holder = new DialogHolder();
            holder.gift_img = (ImageView) v.findViewById(R.id.gift_img);
            holder.gift_img.setScaleType(ImageView.ScaleType.CENTER);
            holder.gift_name = (TextView) v.findViewById(R.id.gift_name);
            holder.gift_prices = (TextView) v.findViewById(R.id.gift_prices);
            holder.deleteIt = (ImageView) v.findViewById(R.id.send_file_delete_iv);
            // 把容器和View 关系保存起来
            v.setTag(holder);
        } else {
            v = convertView;
            holder = (DialogHolder) convertView.getTag();
        }
        final Gift gift = giftList.get(position);

        if (gift.getImg() == null)
            holder.gift_img.setImageResource(R.mipmap.ic_launcher);
        else
            holder.gift_img.setImageBitmap(ImageManageUtil.Byte2Bitmap(gift.getImg()));

        holder.gift_name.setText(gift.getName());
        holder.gift_prices.setText(String.valueOf(gift.getPrice()));
        holder.deleteIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giftList.remove(position);
                notifyDataSetChanged();
                GiftShopActivity.cartEvent(gift);

            }
        });

        return v;
    }
}

class DialogHolder {
    ImageView gift_img;
    TextView gift_name;
    TextView gift_prices;
    ImageView deleteIt;
}
