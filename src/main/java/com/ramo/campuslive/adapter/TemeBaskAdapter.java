package com.ramo.campuslive.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.juss.mediaplay.entity.AnchorLive;
import com.ramo.campuslive.R;
import com.ramo.campuslive.ThemeLiveActivity_;
import com.ramo.campuslive.utils.L;

import java.util.List;

/**
 * Created by ramo on 2016/6/20.
 */
public class TemeBaskAdapter extends BaseAdapter {
    public static final int ACTIVITY = 1;
    public static final int FRAGMENT = 2;
    private Context context;
    private List<AnchorLive> themeList;
    private int type;

    public TemeBaskAdapter() {
    }

    public TemeBaskAdapter(Context context, List<AnchorLive> themeList, int type) {
        this.context = context;
        this.themeList = themeList;
        this.type = type;
    }

    @Override
    public int getCount() {
        return themeList.size();
    }

    @Override
    public Object getItem(int position) {
        return themeList.get(position);
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
            if (type == ACTIVITY)
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.teme_bask_activity_item, null);
            else
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.teme_bask_activity_item, null);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }
        hold.head = (ImageView) convertView.findViewById(R.id.fans_head);
        hold.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ThemeLiveActivity_.class));
            }
        });
        hold.name = (TextView) convertView.findViewById(R.id.contribution_name);
        hold.desc = (TextView) convertView.findViewById(R.id.fans_desc);
        hold.contribution_level = (ImageView) convertView.findViewById(R.id.contribution_level);


        AnchorLive teme = themeList.get(position);
        L.e(themeList.size()+"");

        hold.head.setImageResource(R.drawable.head);
        hold.name.setText(teme.getUserNickname());

        hold.desc.setText(teme.getSchool());
        /*switch (u.getLevel()) {
            case 1:
                hold.contribution_level.setImageResource(R.drawable.v1);
                break;
            case 2:
                hold.contribution_level.setImageResource(R.drawable.v2);
                break;
            case 3:
                hold.contribution_level.setImageResource(R.drawable.v3);
                break;
            case 4:
                hold.contribution_level.setImageResource(R.drawable.v4);
                break;
            case 5:
                hold.contribution_level.setImageResource(R.drawable.v5);
                break;
            case 6:
                hold.contribution_level.setImageResource(R.drawable.v6);
                break;
            default:
                hold.contribution_level.setImageResource(R.drawable.v1);
                break;
        }*/
        return convertView;
    }

    class ViewHold {
        public TextView name;
        public TextView desc;
        public ImageView head;
        public ImageView contribution_level;
    }
}
