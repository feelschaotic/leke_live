package com.ramo.campuslive.fragment;

import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.ramo.campuslive.R;
import com.ramo.campuslive.adapter.PersonalAttentionAdapter;
import com.ramo.campuslive.bean.Attention;
import com.ramo.campuslive.bean.User;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramo on 2016/6/20.
 */
@EFragment(R.layout.fragment_personal_attention)
public class PersonalAttentionFragment extends Fragment {
    @ViewById
    ListView attention_listview;
    private List attentions;

    @AfterViews
    protected void init() {
        initData();
        attention_listview.setAdapter(new PersonalAttentionAdapter(getActivity(), attentions));
    }

    private void initData() {
        attentions=new ArrayList();
        //String userName, byte[] userHead, String liveName, byte[] cover, int likeNum, int type
        Attention attention =new Attention(new User("ramo",null,null),"第二届教职工会议",null,134,1);
        Attention attention2 =new Attention(new User("江湖感觉乱",null,null),"创新创业大讲堂",null,4,-1);
        Attention attention3 =new Attention(new User("rdd阿瑟",null,null),"机房监控",null,304,1);
        attentions.add(attention);
        attentions.add(attention2);
        attentions.add(attention3);
        initListener();
    }

    private void initListener() {

    }

}
