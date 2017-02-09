package com.ramo.campuslive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ramo.campuslive.R;
import com.ramo.campuslive.bean.Theme;

import java.util.List;

/**
 * Created by ramo on 2016/6/20.
 */
public class LiveThemeAdapter extends BaseAdapter {
    private Context context;
    private List<Theme> themes;

    public LiveThemeAdapter(Context context, List<Theme> themes) {
        this.context = context;
        this.themes = themes;
    }

    @Override
    public int getCount() {
        return themes.size();
    }

    @Override
    public Object getItem(int position) {
        return themes.get(position);
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
                    R.layout.activity_live_theme_item, null);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }

        hold.theme_title = (TextView) convertView.findViewById(R.id.theme_title);
        hold.theme_liveNum = (TextView) convertView.findViewById(R.id.theme_liveNum);
        hold.theme_img = (RelativeLayout) convertView.findViewById(R.id.theme_img_rl);
        Theme theme = themes.get(position);
        List liveRooms = theme.getLiveList();
        hold.theme_title.setText(theme.getTheme_name());
        hold.theme_liveNum.setText(String.valueOf(liveRooms.size()));
        if(position==0)
            hold.theme_img.setBackgroundResource(R.drawable.community2);
        else if(position==1)
            hold.theme_img.setBackgroundResource(R.drawable.community3);
        else
            hold.theme_img.setBackgroundResource(R.drawable.demo);
       /* if (liveRooms.get(0) > 0)
            hold.theme_img.setImageResource(R.drawable.live_state_live);
        else
            hold.theme_img.setImageResource(R.drawable.live_state_rest);*/
        return convertView;
    }

    class ViewHold {
        public TextView theme_title;
        public TextView theme_liveNum;
        public RelativeLayout theme_img;
    }
}
