package com.ramo.campuslive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ramo.campuslive.R;
import com.ramo.campuslive.bean.ContributionList;
import com.ramo.campuslive.bean.User;
import com.ramo.campuslive.utils.ImageManageUtil;
import com.ramo.campuslive.view.CircleImageView;

import java.util.List;

/**
 * Created by ramo on 2016/6/20.
 */
public class ContributionListAdapter extends BaseAdapter {
    private Context context;
    private List<ContributionList> contributionLists;

    public ContributionListAdapter(Context context, List<ContributionList> contributionLists) {
        this.context = context;
        this.contributionLists = contributionLists;
    }

    @Override
    public int getCount() {
        return contributionLists.size();
    }

    @Override
    public Object getItem(int position) {
        return contributionLists.get(position);
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
                    R.layout.activity_contribution_list_item, null);

            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }
        hold.name = (TextView) convertView.findViewById(R.id.contribution_name);
        hold.head = (CircleImageView) convertView.findViewById(R.id.contribution_head);
        hold.money = (TextView) convertView.findViewById(R.id.contribution_money);
        hold.contribution_sort_num = (TextView) convertView.findViewById(R.id.contribution_sort_num);

        ContributionList con = contributionLists.get(position);
        User u = con.getUser();
        if (u.getHead() == null)
            hold.head.setImageResource(R.drawable.head);
        else
            hold.head.setImageDrawable(ImageManageUtil.Byte2Drawable(u.getHead()));

        hold.contribution_sort_num.setText("NO."+(position + 4));
        hold.name.setText(u.getName());
        hold.money.setText(String.valueOf(con.getMoney()));

        return convertView;
    }

    class ViewHold {
        public TextView name;
        public TextView money;
        public TextView contribution_sort_num;
        public CircleImageView head;
    }
}
