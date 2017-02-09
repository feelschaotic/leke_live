package com.ramo.campuslive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.ramo.campuslive.R;
import com.ramo.campuslive.bean.Attention;

import java.util.List;

/**
 * Created by ramo on 2016/6/20.
 */
public class PersonalAttentionAdapter extends BaseSwipeAdapter {
    private Context context;
    private List<Attention> attentions;

    public PersonalAttentionAdapter(Context context, List<Attention> attentions) {
        this.context = context;
        this.attentions = attentions;
    }

    @Override
    public int getCount() {
        return attentions.size();
    }

    @Override
    public Object getItem(int position) {
        return attentions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        ViewHold hold;
        hold  = new ViewHold();
          View  convertView = LayoutInflater.from(context).inflate(
                    R.layout.fragment_personal_attention_listview_item, null);
        convertView.setTag(hold);
        return convertView;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        ViewHold hold;
        hold  = (ViewHold) convertView.getTag();
        hold.attention_live_name = (TextView) convertView.findViewById(R.id.attention_live_name);
        hold.attention_live_userName = (TextView) convertView.findViewById(R.id.attention_live_userName);
        hold.attention_live_likeNum = (TextView) convertView.findViewById(R.id.attention_live_likeNum);
        hold.attention_live_userHead = (ImageView) convertView.findViewById(R.id.attention_live_userHead);
        hold.attention_item_rl = (RelativeLayout) convertView.findViewById(R.id.attention_item_rl);
        hold.attention_live_state_iv = (ImageView) convertView.findViewById(R.id.attention_live_state_iv);

        hold.trash = (ImageView) convertView.findViewById(R.id.trash);
        hold.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attentions.remove(attentions.get(position));
                // notifyDataSetChanged();
                closeItem(position);
                // closeAllItems();
            }
        });
        Attention attention = attentions.get(position);

        hold.attention_live_name.setText(attention.getLiveName());
        hold.attention_live_userName.setText(attention.getUser().getName());
        hold.attention_live_likeNum.setText(String.valueOf(attention.getLikeNum()));
        if (attention.getType() > 0)
            hold.attention_live_state_iv.setImageResource(R.drawable.live_state_live);
        else
            hold.attention_live_state_iv.setImageResource(R.drawable.live_state_rest);

        if(position==0) {
            hold.attention_live_userHead.setImageResource(R.drawable.head2);
            hold.attention_item_rl.setBackgroundResource(R.drawable.meeting);
        }
        else if(position==1) {
            hold.attention_live_userHead.setImageResource(R.drawable.head);
            hold.attention_item_rl.setBackgroundResource(R.drawable.meeting3);
        }
        else {
            hold.attention_live_userHead.setImageResource(R.drawable.head2);
            hold.attention_item_rl.setBackgroundResource(R.drawable.monitor1);
        }
    }

/*    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold hold;
        if (convertView == null) {
            hold = new ViewHold();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.fragment_personal_attention_listview_item, null);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }

        hold.attention_live_name = (TextView) convertView.findViewById(R.id.attention_live_name);
        hold.attention_live_userName = (TextView) convertView.findViewById(R.id.attention_live_userName);
        hold.attention_live_likeNum = (TextView) convertView.findViewById(R.id.attention_live_likeNum);
        hold.attention_live_userHead = (ImageView) convertView.findViewById(R.id.attention_live_userHead);
        hold.attention_item_rl = (RelativeLayout) convertView.findViewById(R.id.attention_item_rl);
        hold.attention_live_state_iv = (ImageView) convertView.findViewById(R.id.attention_live_state_iv);

        hold.trash = (ImageView) convertView.findViewById(R.id.trash);
        hold.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attentions.remove(position);
                notifyDataSetChanged();
            }
        });
        Attention attention = attentions.get(position);

        hold.attention_live_name.setText(attention.getLiveName());
        hold.attention_live_userName.setText(attention.getUser().getName());
        hold.attention_live_likeNum.setText(String.valueOf(attention.getLikeNum()));
        if (attention.getType() > 0)
            hold.attention_live_state_iv.setImageResource(R.drawable.live_state_live);
        else
            hold.attention_live_state_iv.setImageResource(R.drawable.live_state_rest);
        // hold.attention_live_userHead.setImageDrawable();
        //  hold.attention_item_rl.setBackground();
        return convertView;
    }*/

    class ViewHold {
        public TextView attention_live_name;
        public TextView attention_live_userName;
        public TextView attention_live_likeNum;
        public ImageView attention_live_userHead;
        public RelativeLayout attention_item_rl;
        public ImageView attention_live_state_iv;
        public ImageView trash;
    }
}
