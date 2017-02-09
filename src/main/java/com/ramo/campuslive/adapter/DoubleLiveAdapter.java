package com.ramo.campuslive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramo.campuslive.R;
import com.ramo.campuslive.bean.LiveRoom;

import java.util.List;

public class DoubleLiveAdapter extends BaseAdapter {
    private Context context;
    private List<LiveRoom> lives;

    public DoubleLiveAdapter(Context context, List<LiveRoom> lives) {
        this.context = context;
        this.lives = lives;
    }

    @Override
    public int getCount() {
        return lives.size();
    }

    @Override
    public Object getItem(int position) {
        return lives.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold hold;
        if (convertView == null) {
            hold = new ViewHold();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.activity_double_live_list_item, null);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }


        LiveRoom room = lives.get(position);


        return convertView;
    }

    class ViewHold {
        public TextView live_title;
        public TextView live_userName1;
        public TextView live_userName2;
        public TextView live_view1;
        public TextView live_view2;
        public ImageView live_userHead1;
        public ImageView live_userHead2;
    }
}
