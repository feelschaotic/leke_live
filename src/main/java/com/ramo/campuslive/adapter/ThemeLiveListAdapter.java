package com.ramo.campuslive.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.letv.controller.PlayProxy;
import com.letv.universal.iplay.EventPlayProxy;
import com.ramo.campuslive.R;
import com.ramo.campuslive.ThemeLiveActivity;
import com.ramo.campuslive.ThemeLiveActivity_;
import com.ramo.campuslive.application.MyApplication;

/**
 * Created by lenovo on 2016/9/18.
 */
public class ThemeLiveListAdapter extends BaseAdapter{
    private Context context;
    private String[] themeTitle;
    public ThemeLiveListAdapter(Context context, String[] themeTitle) {
        this.context=context;
        this.themeTitle=themeTitle;
    }

    @Override
    public int getCount() {
        return themeTitle.length;
    }

    @Override
    public Object getItem(int position) {
        return themeTitle[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

                final ViewHold hold;
        if (convertView == null) {
            hold = new ViewHold();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.live_theme_live_item, null);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }

        hold.themeName = (TextView) convertView.findViewById(R.id.theme_name);
        hold.themeName.setText(themeTitle[position]);
        hold.themeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hold.themeName.setBackgroundResource(R.drawable.btn_shape);
                getNextTheme(position);
            }
        });

        return convertView;
    }

    private void getNextTheme(int position) {
        Intent intent = new Intent(context, ThemeLiveActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_LIVE);
        if (ThemeLiveActivity.pos >= MyApplication.themePath.length)
            ThemeLiveActivity.pos = 0;

        bundle.putInt("pos", position);
        bundle.putString("path", MyApplication.themePath[ThemeLiveActivity.pos]);
        intent.putExtra("data", bundle);
        context.startActivity(intent);
        //finish();
    }

    class ViewHold{
        TextView themeName;
    }

}
