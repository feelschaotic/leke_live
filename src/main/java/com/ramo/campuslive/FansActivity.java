package com.ramo.campuslive;

import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.google.gson.Gson;
import com.juss.mediaplay.entity.MyFansJson;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.po.UserFans;
import com.juss.mediaplay.utils.NetUtil;
import com.ramo.campuslive.adapter.PersonalFansAdapter;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramo on 2016/6/30.
 */
@EActivity(R.layout.activity_fans)
public class FansActivity extends SwipeBackActivity {
    @ViewById
    ListView fans_listview;
    List<UserFans> userList;
    PersonalFansAdapter adapter;
    private List<UserFans> fansList = new ArrayList<>();
    private final int SUCCESS = 1;

    private int pagenum = 1;
    private int pageall = 1;
    private Handler getFansListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    String response = (String) msg.obj;
                    Gson gson = new Gson();
                    MyFansJson myFans = gson.fromJson(response, MyFansJson.class);
                    fansList = myFans.getList();
                    pageall = myFans.getTotalrecord();
                    pagenum = myFans.getPagenum();
                    L.e(fansList.size() + "");
                    adapter = new PersonalFansAdapter(FansActivity.this, fansList, PersonalFansAdapter.FRAGMENT);
                    fans_listview.setAdapter(adapter);

            }
            super.handleMessage(msg);


        }
    };

    @AfterViews
    public void init() {
        initData();
        adapter = new PersonalFansAdapter(this, userList, PersonalFansAdapter.ACTIVITY);
        fans_listview.setAdapter(adapter);
    }

    private void initData() {
/*        userList=new ArrayList<>();
        User u=new User("江湖感觉乱",null,"年轻人没有双下巴啦，恭喜啊");
        User u2=new User("钟小喵爱大大大大太阳",null,"从上了知乎，现在无法正视看待体验二字");
        User u3=new User("狂月",null,"没有什么问题是重启解决不了的 如果有 那就是 多喝点水");
        User u4=new User("ramo",null,"上天让我遇到你 所以我格外珍惜你");
        User u5=new User("机器人哈哈哈哈",null,"快人快语 慢人不语");
        userList.add(u);
        userList.add(u2);
        userList.add(u3);
        userList.add(u4);
        userList.add(u5);*/

        String[] name = new String[]{"userId", "pagenum"};
        String[] val = new String[]{"10003", 1 + ""};
        NetUtil.sendRequestToUrl(ServerConstants.MyFansURL, name, val, new HttpCallbackListener() {
            @Override
            public void onSucc(String response) {
                L.e(response);
                Message msg = Message.obtain();
                msg.obj = response;
                msg.what = SUCCESS;
                getFansListHandler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                L.e(e.getMessage());
            }
        });
    }
}
