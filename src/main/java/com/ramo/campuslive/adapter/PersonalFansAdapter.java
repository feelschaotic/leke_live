package com.ramo.campuslive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.juss.mediaplay.po.UserFans;
import com.ramo.campuslive.R;
import com.ramo.campuslive.bean.User;
import com.ramo.campuslive.utils.ImageManageUtil;

import java.util.List;

/**
 * Created by ramo on 2016/6/20.
 */
public class PersonalFansAdapter extends BaseAdapter {
    public static final int ACTIVITY = 1;
    public static final int FRAGMENT = 2;
    private Context context;
//    private List<User> userList;
    private List<UserFans> fansList;
    private int type;

    public PersonalFansAdapter() {
    }

   /* public PersonalFansAdapter(Context context, List<User> userList, int type) {
        this.context = context;
        this.userList = userList;
        this.type = type;
    }*/
    public PersonalFansAdapter(Context context, List<UserFans> userList, int type) {
        this.context = context;
        this.fansList = userList;
        this.type = type;
    }

    @Override
    public int getCount() {
        return fansList.size();
    }

    @Override
    public Object getItem(int position) {
        return fansList.get(position);
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
                        R.layout.fans_activity_item, null);
            else
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.fans_fragment_item, null);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }
        hold.head = (ImageView) convertView.findViewById(R.id.fans_head);
        hold.name = (TextView) convertView.findViewById(R.id.contribution_name);
        hold.desc = (TextView) convertView.findViewById(R.id.fans_desc);
        hold.contribution_level = (ImageView) convertView.findViewById(R.id.contribution_level);

     //   User u = userList.get(position);
        UserFans fans = fansList.get(position);
        if(position==0)
            hold.head.setImageResource(R.drawable.head);
        else
            hold.head.setImageResource(R.drawable.head2);
       /* if (u.getHead() == null)
            hold.head.setImageResource(R.drawable.head);
        else
            hold.head.setImageDrawable(ImageManageUtil.Byte2Drawable(u.getHead()));*/
//        hold.name.setText(u.getName());
        hold.name.setText(fans.getUserNickName());
//        hold.desc.setText(String.valueOf(u.getDesc()));
        hold.desc.setText(fans.getUserSignature());
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
