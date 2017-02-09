package com.ramo.campuslive;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dd.CircularProgressButton;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.utils.NetUtil;
import com.ramo.campuslive.application.MyApplication;
import com.ramo.campuslive.fragment.PersonalAttentionFragment_;
import com.ramo.campuslive.fragment.PersonalFansFragment_;
import com.ramo.campuslive.fragment.PersonalMyCommunityFragment_;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;
import com.ramo.campuslive.utils.SimulateBtnUtil;
import com.ramo.campuslive.utils.T;
import com.ramo.campuslive.view.CircleImageView2;
import com.ramo.campuslive.view.ViewPagerIndicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ramo on 2016/6/20.
 */
@EActivity(R.layout.activity_personal_center)
public class PersonalCenterActivity extends BaseFragmentActivity {
    /*viewPager*/
    @ViewById(R.id.ViewPagerIndicator)
    ViewPagerIndicator ViewPagerIndicator;
    @ViewById(R.id.viewPager)
    ViewPager viewPager;
    @ViewById
    CircularProgressButton personal_attention_btn;
    @ViewById
    LinearLayout personal_user_gift;
    @ViewById
    CircleImageView2 personal_user_head;
    @ViewById
    ImageView personal_send_letter_iv;
    @ViewById
    ImageView personal_room_setting;

    private Intent intent;

    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private List<String> titleList = Arrays.asList("关注3", "社团4", "粉丝12");
    private FragmentPagerAdapter adapter;
    private HttpCallbackListener followListener = new HttpCallbackListener() {
        @Override
        public void onSucc(String response) {
            try {
                JSONObject object = new JSONObject(response);
                int state = object.getInt("state");

                if(0==state){
                    T.showLong(PersonalCenterActivity.this,"成功");
                    followSuccess(state, "");
                }else{
                    String errMsg = object.getString("errMsg");
                    followSuccess(state,errMsg);
                    T.showShort(PersonalCenterActivity.this,"请稍后重试");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(Exception e) {

        }
    };
    private final int SUCCESS = 0;
    private final int F1=40001;//用户不存在
    private final int F2=40002;//直播间不存在
    private final int F3=40003;//已取消关注



    /**
     * 判断关注是否成功
     * @param state
     */
    private void followSuccess(int state,String errMsg) {
        switch (state){
            case SUCCESS:
                L.e("关注成功");
                break;
            default:
                L.e(errMsg);
                break;

        }
    }

    @AfterViews
    public void init() {
        initFragment();
        initTab();
        intent = getIntent();
//         获取上一个页面传过来的用户id
        int userid=intent.getIntExtra("userId", 0);
//        从SharedPreferences中获取用户信息
      /*  SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String data = sharedPreferences.getString("userInfo", "");
        Gson gson = new Gson();
        User user = gson.fromJson(data, User.class);
        int userId=user.getUserId();*/

        viewPager.setAdapter(adapter);
        String stringExtra = getIntent().getStringExtra(MyApplication.ACTIVITY_EXTRA_NAME);
        if(stringExtra!=null){
            if(stringExtra.equals(MyApplication.FRAGMENT_community))
                viewPager.setCurrentItem(1);
            else if(stringExtra.equals(MyApplication.FRAGMENT_attention))
                viewPager.setCurrentItem(0);
        }



        int userId=10003;
        if(userid==userId){
            personal_attention_btn.setVisibility(View.GONE);
            personal_send_letter_iv.setVisibility(View.GONE);
        }else {
            personal_room_setting.setVisibility(View.GONE);
        }

    }
    private void initTab() {
        ViewPagerIndicator.setVisibleTagCount(3);
        ViewPagerIndicator.setTabItemTitles(titleList);
        ViewPagerIndicator.setView_pager(viewPager);
    }
    private void initFragment() {

        Fragment fragment1 = new PersonalAttentionFragment_();
        Fragment fragment2 = new PersonalMyCommunityFragment_();
        Fragment fragment3 = new PersonalFansFragment_();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);

        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragmentList.size();
            }
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }
        };

        initListener();
    }
    private void initListener() {

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ViewPagerIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        personal_attention_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] name = new String[]{"userId","liveId"};
                String[] val = new String[]{"10003",""};
                if (personal_attention_btn.getProgress() == 0) {//关注
                    SimulateBtnUtil.simulateSuccessProgress(personal_attention_btn);

                    NetUtil.sendRequestToUrl(ServerConstants.FollowURL,name,val,followListener);

                } else {//取关
                    personal_attention_btn.setProgress(0);
                    NetUtil.sendRequestToUrl(ServerConstants.UnFollowURL, name, val, followListener);
                }
            }
        });

        personal_user_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalCenterActivity.this,ContributionListActivity_.class));
            }
        });

        personal_user_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalCenterActivity.this,EditPersonalInformation_.class));
            }
        });

        personal_send_letter_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalCenterActivity.this, ChatActivity_.class));
            }
        });
        personal_room_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalCenterActivity.this,LiveRoomSettingActivity_.class));
            }
        });
    }

}
