package com.ramo.campuslive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juss.mediaplay.entity.GuessingOption;
import com.ramo.campuslive.R;
import com.ramo.campuslive.bean.MyGuessing;
import com.ramo.campuslive.utils.L;

import java.util.List;

/**
 * Created by ramo on 2016/7/6.
 */
public class MyGuessingDetailsOptionsAdapter extends BaseAdapter {
    private Context context;
    private List<GuessingOption> optionsList;
    private MyGuessing myGuessing;
    private int guess_num;

    private String guess_name;
    public MyGuessingDetailsOptionsAdapter(Context context, MyGuessing myGuessing) {
        this.context = context;
//        this.optionsList = myGuessing.getGuessing().getOptionses();
        this.myGuessing = myGuessing;
        L.e("myGuessing.getGuessingOptions():"+myGuessing.getGuessingOptions().getOptions_name());
//        guess_num = optionsList.indexOf(myGuessing.getGuessingOptions());
//        L.e("guess_num:"+guess_num);
    }

    public MyGuessingDetailsOptionsAdapter(Context context, List<GuessingOption> list,String guess_name) {
        this.context = context;
        this.optionsList = list;
        this.guess_name=guess_name;
//        guess_num = optionsList.indexOf(myGuessing.getGuessingOptions());
//        L.e("guess_num:"+guess_num);
    }

    @Override
    public int getCount() {
        return optionsList.size();
    }

    @Override
    public Object getItem(int position) {
        return optionsList.get(position);
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
                    R.layout.activity_my_guessing_details_options, null);
            convertView.setTag(hold);
        } else {
            hold = (MyGuessingHolder) convertView.getTag();
        }

        hold.guessing_options_num = (TextView) convertView.findViewById(R.id.guessing_options_num);
        hold.guessing_options_tv = (TextView) convertView.findViewById(R.id.guessing_options_tv);
        hold.guessing_options_odds_tv = (TextView) convertView.findViewById(R.id.guessing_options_odds_tv);
        GuessingOption options = optionsList.get(position);

        //设置我下注的蓝色 其他灰色
//        if (position == guess_num) {
        if (options.getOptionsName().equals(guess_name)) {
            ((RelativeLayout) hold.guessing_options_num.getParent()).setBackgroundResource(R.drawable.guessing_option_shape);
            hold.guessing_options_num.setBackgroundResource(R.drawable.guessing_et_left);
        } else {
            ((RelativeLayout) hold.guessing_options_num.getParent()).setBackgroundResource(R.drawable.guessing_option_shape_grey);
            hold.guessing_options_num.setBackgroundResource(R.drawable.guessing_et_left_grey);
        }
        //如果和结果一样 则设置绿色  否则设置红色 正确的选项设置橘色
        //  guessing_options_num
        hold.guessing_options_num.setText("选项" + (position + 1));
        hold.guessing_options_tv.setText(options.getOptionsName());
        hold.guessing_options_odds_tv.setText(options.getOptionsCompensate()+"");
        return convertView;
    }

    class MyGuessingHolder {
        TextView guessing_options_num;
        TextView guessing_options_tv;
        TextView guessing_options_odds_tv;

    }
}
