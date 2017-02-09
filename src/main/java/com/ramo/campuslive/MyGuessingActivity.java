package com.ramo.campuslive;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.juss.mediaplay.entity.MyGuess;
import com.juss.mediaplay.entity.MyGuessJson;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.utils.GsonDateUtil;
import com.juss.mediaplay.utils.NetUtil;
import com.ramo.campuslive.adapter.MyGuessingAdapter;
import com.ramo.campuslive.application.MyApplication;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.T;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ramo on 2016/7/6.
 */
@EActivity(R.layout.activity_my_guessing)
public class MyGuessingActivity extends SwipeBackActivity {
    @ViewById
    ListView my_guessing_lv;
    private MyGuessingAdapter myGuessingAdapter;
    private MyGuessJson myGuessingList;
    private Handler myGuessListHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                GsonDateUtil gsonDateUtil=new GsonDateUtil();
                myGuessingList=gsonDateUtil.getGsonDate().fromJson((String)msg.obj,MyGuessJson.class);
                myGuessingAdapter = new MyGuessingAdapter(MyGuessingActivity.this, myGuessingList.getList());
                my_guessing_lv.setAdapter(myGuessingAdapter);
            }else{
                String message=getResources().getString(R.string.load_error);
                T.showShort(MyGuessingActivity.this,message);
            }
        }
    };
    @AfterViews
    public void init() {
        initData();

        initListener();
    }

    private void initListener() {
        my_guessing_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyGuess guessing = myGuessingList.getList().get(position);
                Intent intent=new Intent(MyGuessingActivity.this,MyGuessingDetailsActivity_.class);
                intent.putExtra("gussingId",guessing.getGuessingId());
                intent.putExtra("guess_name",guessing.getChoice());
                intent.putExtra(MyApplication.MY_GUESS_DETAILS_EXTRA, guessing);
                startActivity(intent);
            }
        });
    }

    private void initData() {

        String []key={"userId"};
        String []value={"10003"};
        NetUtil.sendRequestToUrl(ServerConstants.MyGuessListUrl, key, value, new HttpCallbackListener() {
            @Override
            public void onSucc(String response) {
                Message msg=new Message();
                msg.what=1;
                msg.obj=response;
                myGuessListHandler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                myGuessListHandler.sendEmptyMessage(0);
            }
        });
    }
}
