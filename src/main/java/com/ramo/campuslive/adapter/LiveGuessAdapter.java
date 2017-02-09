package com.ramo.campuslive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.juss.mediaplay.entity.GuessingVO;
import com.ramo.campuslive.R;

import java.util.List;

/**
 * Created by ramo on 2016/8/6.
 */
public class LiveGuessAdapter extends BaseAdapter {
    private List<GuessingVO> list;
    private Context context;

    public LiveGuessAdapter(Context context, List<GuessingVO> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyGuessingHolder hold;
        if (convertView == null) {
            hold = new MyGuessingHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.live_guess_item, null);
            convertView.setTag(hold);
        } else {
            hold = (MyGuessingHolder) convertView.getTag();
        }

        hold.guess_title = (TextView) convertView.findViewById(R.id.guess_title);
        hold.guess_state = (TextView) convertView.findViewById(R.id.guess_state);
        hold.guess_joinNum = (TextView) convertView.findViewById(R.id.guess_joinNum);
        hold.guess_time = (TextView) convertView.findViewById(R.id.guess_time);
        GuessingVO guessing = list.get(position);

        hold.guess_title.setText(guessing.getTitle());
        hold.guess_time.setText(guessing.getStopTime().toLocaleString());
        hold.guess_joinNum.setText(String.valueOf(guessing.getBetNum()));
        //  int dateResult = DateUtil.compare_date(guessing.getTime(), guessing.getStop_time());
        if (0 == guessing.getState())
            hold.guess_state.setText("竞猜中");
        else
            hold.guess_state.setText("正在开奖");
        return convertView;
    }

    class MyGuessingHolder {
        TextView guess_state;
        TextView guess_joinNum;
        TextView guess_time;
        TextView guess_title;
    }

}
