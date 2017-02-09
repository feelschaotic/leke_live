package com.ramo.campuslive.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramo.campuslive.GiftShopActivity;
import com.ramo.campuslive.R;
import com.ramo.campuslive.bean.Gift;
import com.ramo.campuslive.utils.ImageManageUtil;

import java.util.List;

/**
 * Created by ramo on 2016/3/24.
 */
public class SelectGiftAdapter extends RecyclerView.Adapter<SelectGiftAdapter.SelectGiftHolder> {
    private List<Gift> giftList;
    private OnClickGiftListener onItemClickListener;

    public SelectGiftAdapter(List<Gift> giftList) {
        this.giftList = giftList;
    }

    @Override
    public SelectGiftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_gift_item, parent, false);
        return new SelectGiftHolder(v);
    }

    @Override
    public void onBindViewHolder(final SelectGiftHolder holder, final int position) {
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
        if (GiftShopActivity.check_tag[position] == 1)
            holder.select_gift_check.setVisibility(View.VISIBLE);

    }

    public void setOnItemClickListener(OnClickGiftListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return giftList.size();
    }

    class SelectGiftHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv;
        TextView price;
        TextView name;
        ImageView select_gift_check;

        public SelectGiftHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.select_gift_img);
            price = (TextView) itemView.findViewById(R.id.select_gift_price);
            name = (TextView) itemView.findViewById(R.id.select_gift_name);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setPadding(5, 5, 5, 5);
            iv.setOnClickListener(this);
            select_gift_check = (ImageView) itemView.findViewById(R.id.select_gift_check);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null)
                onItemClickListener.onCheckClick(view, (Integer) view.getTag());
        }
    }

}


