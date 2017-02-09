package com.ramo.campuslive.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.juss.mediaplay.entity.MyCommunity;
import com.juss.mediaplay.entity.MyCommunityJson;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.utils.NetUtil;
import com.ramo.campuslive.R;
import com.ramo.campuslive.adapter.PersonalMyCommunityAdapter;
import com.ramo.campuslive.bean.Community;
import com.ramo.campuslive.bean.LiveBase;
import com.ramo.campuslive.bean.User;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramo on 2016/6/20.
 */
@EFragment(R.layout.fragment_my_community)
public class PersonalMyCommunityFragment extends Fragment {
    @ViewById
    RecyclerView community_rv;
//    private List<LiveBase> communityList;
    private List<MyCommunity> communityList;
    private int pagenum=1;
    private int pageall =1;
    private PersonalMyCommunityAdapter myCommunityAdapter;
    @AfterViews
    protected void init() {
        initData();

        community_rv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//       community_rv.setAdapter(myCommunityAdapter);
    }

    private Handler MyCommunityHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1:
                    L.e("MyCommunityHandler");
                    String resquest = (String) msg.obj;
                    Gson gson = new Gson();
                    MyCommunityJson myCommunityJson = gson.fromJson(resquest,MyCommunityJson.class);
                    communityList = myCommunityJson.getList();
                    pagenum=myCommunityJson.getPagenum();
                    pageall=myCommunityJson.getTotalrecord();
                    myCommunityAdapter=new PersonalMyCommunityAdapter(communityList);
                    community_rv.setAdapter(myCommunityAdapter);
                    break;
            }

            super.handleMessage(msg);
        }
    };

    private void initData() {
       /* communityList=new ArrayList<>();
        Community comm1=new Community(null,new User("江湖感觉乱",null,"年轻人没有双下巴啦，恭喜啊"),"吉大圆梦联盟社团会议",12313);
        Community comm2=new Community(null,new User("钟小喵爱大大大大太阳",null,"从上了知乎，现在无法正视看待体验二字"),"吉大书法协会",313);
        Community comm3=new Community(null,new User("ramo",null,"上天让我遇到你 所以我格外珍惜你"),"二次元社团招募仪式启动",213);
        Community comm4=new Community(null,new User("机器人哈哈哈哈",null,"快人快语 慢人不语"),"平面设计找人啦！！",23);
        Community comm5=new Community(null,new User("ramo",null,"上天让我遇到你 所以我格外珍惜你"),"动漫讨论区。。。",13);
        communityList.add(comm1);
        communityList.add(comm2);
        communityList.add(comm3);
        communityList.add(comm4);
        communityList.add(comm5);*/


        String[] name = new String[]{"userId","pagenum"};
        String[] val = new String[]{"10003",pagenum+""};
        NetUtil.sendRequestToUrl(ServerConstants.MyCommunityURL, name, val, new HttpCallbackListener() {
            @Override
            public void onSucc(String response) {
                L.e(response);
                Message msg = Message.obtain();
                msg.obj=response;
                msg.what=1;
                MyCommunityHandler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                L.e(e.getMessage());
            }
        });

    }
}
