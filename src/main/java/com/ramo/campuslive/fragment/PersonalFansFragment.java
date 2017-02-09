package com.ramo.campuslive.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.juss.mediaplay.entity.MyFansJson;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.po.UserFans;
import com.juss.mediaplay.utils.NetUtil;
import com.ramo.campuslive.FansActivity_;
import com.ramo.campuslive.R;
import com.ramo.campuslive.adapter.PersonalFansAdapter;
import com.ramo.campuslive.bean.User;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ram6/6/30.
 */
@EFragment(R.layout.fragment_personal_fans)
public class PersonalFansFragment extends Fragment {
    @ViewById
    ListView fans_listview;
    List<User> userList;
    PersonalFansAdapter adapter;
    @ViewById
    TextView fans_more;

    private List<UserFans> fansList = new ArrayList<>();
    private final int SUCCESS=1;

    private int pagenum=1;
    private int pageall=1;


    @AfterViews
    public void init() {
        initData();
        initListener();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    String response = (String) msg.obj;
                    Gson gson = new Gson();
                    MyFansJson myFans = gson.fromJson(response,MyFansJson.class);
                    fansList = myFans.getList();
                    pageall=myFans.getTotalrecord();
                    pagenum=myFans.getPagenum();
                    L.e(fansList.size()+"");
                    adapter=new PersonalFansAdapter(getActivity(),fansList, PersonalFansAdapter.FRAGMENT);
                    fans_listview.setAdapter(adapter);

            }
            super.handleMessage(msg);


        }
    };
    private void initListener() {
        fans_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FansActivity_.class));
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.keep);
            }
        });
    }

    private void initData() {
        userList=new ArrayList<>();
        User u=new User("江湖感觉乱",null,"年轻人没有双下巴啦，恭喜啊");
        User u2=new User("钟小喵爱大大大大太阳",null,"从上了知乎，现在无法正视看待体验二字");
        User u3=new User("狂月",null,"没有什么问题是重启解决不了的 如果有 那就是 多喝点水");
        User u4=new User("ramo",null,"上天让我遇到你 所以我格外珍惜你");
        User u5=new User("机器人哈哈哈哈",null,"快人快语 慢人不语");
        userList.add(u);
        userList.add(u2);
        userList.add(u3);
        userList.add(u4);


        /*{"list":[
        {"userId":10001,
        "userNickName":"mdzz",
        "userHead":"/live/upload/userhead/2016/08/02/11/b6375fd4-c29c-428f-89f5-308039c1596b.jpg",
        "userSignature":"没有个性签名"},
        {"userId":10002,
        "userNickName":"....",
        "userHead":"/live/upload/userhead/2016/08/02/11/38e0a9e4-7e0b-43ab-98da-8584d2f269f5.jpg",
        "userSignature":"这个人很懒，什么都没有写"},{"userId":10003,"userNickName":"请不要看昵称","userHead":"/live/upload/userhead/2016/08/02/15/84972cd4-fd4f-4269-ae11-4e8b09e9df57.jpg","userSignature":"人生真是芥末如雪啊"},{"userId":10005,"userNickName":"zzz","userHead":"/live/upload/userhead/2016/08/02/15/7a4f1a23-5cd6-487f-be56-b23fce851295.jpg","userSignature":"这个人很懒，什么都没有写"}],"totalpage":1,"pagesize":10,"totalrecord":4,"startindex":0,"pagenum":1,"startpage":1,"endpage":1}
08-04 10:50:01.210 10354-10366/? E/PGPSDownloader.SocketServer: java.io.IOException: Connection refused*/

        String[] name =new String[]{"userId","pagenum"};
        String[] val = new String[]{"10003",1+""};
        NetUtil.sendRequestToUrl(ServerConstants.MyFansURL, name, val, new HttpCallbackListener() {
            @Override
            public void onSucc(String response) {
                L.e(response);
                Message msg = Message.obtain();
                msg.obj=response;
                msg.what=SUCCESS;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                L.e(e.getMessage());
            }
        });
    }
}
