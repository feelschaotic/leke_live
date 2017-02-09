package com.ramo.campuslive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ramo.campuslive.R;
import com.ramo.campuslive.bean.PaymentsBalance;
import com.ramo.campuslive.utils.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by ramo on 2016/6/20.
 */
public class BalanceTimeLineAdapter extends BaseAdapter {
    private Context context;
    private List<PaymentsBalance> balance_datas;

    public BalanceTimeLineAdapter(Context context, List<PaymentsBalance> balance_datas) {
        this.context = context;
        this.balance_datas = balance_datas;
    }

    @Override
    public int getCount() {
        return balance_datas.size();
    }

    @Override
    public Object getItem(int position) {
        return balance_datas.get(position);
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
                    R.layout.balance_listview_item, null);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }

        hold.balance_time_line_month = (TextView) convertView.findViewById(R.id.balance_time_line_month);
        hold.balance_time_line_year = (TextView) convertView.findViewById(R.id.balance_time_line_year);
        hold.balance_time_line_time = (TextView) convertView.findViewById(R.id.balance_time_line_time);
        hold.balance_time_line_balance = (TextView) convertView.findViewById(R.id.balance_time_line_balance);
        hold.balance_time_line_content = (TextView) convertView.findViewById(R.id.balance_time_line_content);
       /* if (position == balance_datas.size() - 1) {
            hold.balance_line = convertView.findViewById(R.id.balance_line);
            ViewGroup.LayoutParams layoutParams = hold.balance_line.getLayoutParams();
            layoutParams.height = layoutParams.height / 2;
        }*/
        PaymentsBalance paymentsBalance = balance_datas.get(position);

        Date record_date = paymentsBalance.getRecord_date();
        DateUtil dateUtil = new DateUtil(record_date);

        hold.balance_time_line_year.setText(dateUtil.getYear());
        hold.balance_time_line_month.setText(dateUtil.getDay());
        hold.balance_time_line_time.setText(dateUtil.getTime());
        StringBuffer moneySB = new StringBuffer();
        if (paymentsBalance.getBalance_state() >= 0)
            moneySB.append("+ ");
        else
            moneySB.append("- ");
        moneySB.append(paymentsBalance.getRecord_num());
        moneySB.append(" 金币");
        hold.balance_time_line_balance.setText(moneySB.toString());
        hold.balance_time_line_content.setText(paymentsBalance.getIntroduction());
        return convertView;
    }

    class ViewHold {
        public TextView balance_time_line_month;
        public TextView balance_time_line_year;
        public TextView balance_time_line_balance;
        public TextView balance_time_line_content;
        public TextView balance_time_line_time;
        private View balance_line;
    }
}
