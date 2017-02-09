package com.ramo.campuslive;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.dd.CircularProgressButton;
import com.google.gson.Gson;
import com.juss.mediaplay.entity.BalancePay;
import com.juss.mediaplay.entity.BalancePayJson;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.utils.GsonDateUtil;
import com.juss.mediaplay.utils.NetUtil;
import com.ramo.campuslive.adapter.BalanceTimeLineAdapter;
import com.ramo.campuslive.bean.PaymentsBalance;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by ramo on 2016/6/19.
 */
@EActivity(R.layout.activity_payments_balance)
public class BalancePaymentsActivity extends SwipeBackActivity {

    ListView balance_lv;
    List<PaymentsBalance> balance_datas;
    @ViewById
    CircularProgressButton top_up_btn;
    private int pagenum=1;
    private BalanceTimeLineAdapter mAdapter;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if(mAdapter==null){
                        mAdapter =new BalanceTimeLineAdapter(BalancePaymentsActivity.this, balance_datas);
                        balance_lv.setAdapter(mAdapter);
                    }
                    else{
                        balance_lv.deferNotifyDataSetChanged();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @AfterViews
    public void init() {
        balance_lv= (ListView) findViewById(R.id.balance_listview);
        initData();
    }

    private void initData() {
        NetUtil.sendRequestToUrl(ServerConstants.BalenceURL, new String[]{"userId","pagenum"}, new String[]{"10002",pagenum+""}, new HttpCallbackListener() {
            @Override
            public void onSucc(String response) {
                L.e(response);
                balance_datas = new ArrayList<>();
                Gson gson = new GsonDateUtil().getGsonDate();
                BalancePayJson balancePay = gson.fromJson(response,BalancePayJson.class);
                for (BalancePay balance: balancePay.getList() ) {
                    PaymentsBalance b = new PaymentsBalance(balance.getRecordNum(),balance.getBalanceState(),balance.getIntroduction(),balance.getRecordDate());
                    balance_datas.add(b);
                }
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onError(Exception e) {
                L.e(e.getMessage());
            }
        });

     /*

        PaymentsBalance b1=new PaymentsBalance(100,1,"登录送金币",new Date());
        PaymentsBalance b2=new PaymentsBalance(50,1,"充值",new Date());
        PaymentsBalance b3=new PaymentsBalance(80,-1,"送给主播，消费",new Date());
        balance_datas = new ArrayList<>();
        balance_datas.add(b1);
        balance_datas.add(b2);
        balance_datas.add(b3);
        balance_datas.add(b1);
        balance_datas.add(b2);
        balance_datas.add(b3);
        balance_lv.setAdapter(new BalanceTimeLineAdapter(this, balance_datas));*/
        initListener();
    }

    private void initListener() {
        top_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BalancePaymentsActivity.this,TopUpActivity_.class));
            }
        });
    }
}
