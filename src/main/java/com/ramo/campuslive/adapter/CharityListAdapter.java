package com.ramo.campuslive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramo.campuslive.R;
import com.ramo.campuslive.bean.Charity;

import java.util.List;

/**
 * Created by ramo on 2016/9/8.
 */
public class CharityListAdapter extends RecyclerView.Adapter<CharityListAdapter.Holder> {
    private Context context;
    private List<Charity> data;
    private OnClickMainBtnListener onClickMainBtnListener;

    public CharityListAdapter(Context context, List data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public CharityListAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_charity_list_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(CharityListAdapter.Holder holder, final int position) {
        Charity charity = data.get(position);
        holder.attentionNum.setText(charity.getFans().toString());
        holder.shareNum.setText(charity.getShareNum().toString());
        holder.communityName.setText(charity.getCommunityName());
        holder.schoolName.setText(charity.getSchool());
        switch (position) {
            case 0:
                holder.cover.setImageResource(R.drawable.charity1);
                break;
            case 1:
                holder.cover.setImageResource(R.drawable.charity2);
                break;
            case 2:
                holder.cover.setImageResource(R.drawable.charity3);
                break;

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickMainBtnListener != null)
                    onClickMainBtnListener.onClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnClickMainBtnListener(OnClickMainBtnListener onClickMainBtnListener) {
        this.onClickMainBtnListener = onClickMainBtnListener;
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView attentionNum;
        TextView shareNum;
        TextView communityName;
        TextView schoolName;

        public Holder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.charity_cover);
            attentionNum = (TextView) itemView.findViewById(R.id.charity_attentionNum);
            shareNum = (TextView) itemView.findViewById(R.id.charity_shareNum);
            communityName = (TextView) itemView.findViewById(R.id.charity_communityName);
            schoolName = (TextView) itemView.findViewById(R.id.charity_schoolName);

        }

    }
}
