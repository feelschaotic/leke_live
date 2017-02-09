package com.ramo.campuslive.adapter;

/**
 * Created by ramo on 2016/8/3.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.juss.mediaplay.entity.GuessingVO;
import com.ramo.campuslive.R;

import java.util.List;

/**
 * Created by ramo on 2016/7/6.
 */
public class GuessListAdapter extends BaseAdapter {
    private Context context;
    private List<GuessingVO> guessingList;

    public GuessListAdapter(Context context, List<GuessingVO> guessingList) {
        this.context = context;
        this.guessingList = guessingList;
    }

    @Override
    public int getCount() {
        return guessingList.size();
    }

    @Override
    public Object getItem(int position) {
        return guessingList.get(position);
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
                    R.layout.activity_my_guessing_item, null);
            convertView.setTag(hold);
        } else {
            hold = (MyGuessingHolder) convertView.getTag();
        }

        hold.my_guessimg_title = (TextView) convertView.findViewById(R.id.my_guessimg_title);
        hold.my_guess_state = (TextView) convertView.findViewById(R.id.my_guess_state);
        hold.my_guessimg_joinNum = (TextView) convertView.findViewById(R.id.my_guessimg_joinNum);
        hold.my_guessimg_time = (TextView) convertView.findViewById(R.id.my_guessimg_time);
        hold.my_guessimg_img = (ImageView) convertView.findViewById(R.id.my_guessimg_img);
        GuessingVO guessing = guessingList.get(position);

        hold.my_guessimg_title.setText(guessing.getTitle());
        hold.my_guessimg_time.setText(guessing.getStopTime().toLocaleString());
       /* hold.my_guessimg_joinNum.setText(String.valueOf(guessing.get().size()));*/
        //  int dateResult = DateUtil.compare_date(guessing.getTime(), guessing.getStop_time());
        if (0 == guessing.getState())
            hold.my_guess_state.setText("竞猜中");
        else
            hold.my_guess_state.setText("正在开奖");
        return convertView;
    }

    class MyGuessingHolder {
        ImageView my_guessimg_img;
        TextView my_guessimg_title;
        TextView my_guess_state;
        TextView my_guessimg_joinNum;
        TextView my_guessimg_time;

    }
}

