package com.ramo.campuslive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.juss.mediaplay.entity.MyGuess;
import com.ramo.campuslive.R;
import com.ramo.campuslive.bean.Guessing;
import com.ramo.campuslive.bean.MyGuessing;
import com.ramo.campuslive.utils.DateUtil;

import java.util.List;

/**
 * Created by ramo on 2016/7/6.
 */
public class MyGuessingAdapter extends BaseAdapter {
    private Context context;
    private List<MyGuess> myGuessingList;

    public MyGuessingAdapter(Context context, List<MyGuess> myGuessingList) {
        this.context = context;
        this.myGuessingList = myGuessingList;
    }

    @Override
    public int getCount() {
        return myGuessingList.size();
    }

    @Override
    public Object getItem(int position) {
        return myGuessingList.get(position);
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
        MyGuess guessing = myGuessingList.get(position);

        hold.my_guessimg_title.setText(guessing.getGuessingTitle());
        hold.my_guessimg_time.setText(guessing.getBetTime().toLocaleString());
      //  hold.my_guessimg_joinNum.setText(String.valueOf(guessing.getPunters().size()));
        String result=guessing.getGuessingResult();
        if(result==null)
        hold.my_guess_state.setText("竞猜中");
        else
            hold.my_guess_state.setText("结果："+result);

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
