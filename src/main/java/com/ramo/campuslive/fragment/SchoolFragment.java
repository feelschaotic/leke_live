package com.ramo.campuslive.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.juss.mediaplay.entity.MyCommunity;
import com.ramo.campuslive.AddCommunityActivity_;
import com.ramo.campuslive.R;
import com.ramo.campuslive.adapter.PersonalMyCommunityAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_monitor)
public class SchoolFragment extends Fragment {
    @ViewById
    RecyclerView classify_rv;
    @ViewById
    ImageView add_iv;
    private List<MyCommunity> data;

    @AfterViews
    public void init() {
        initData();
        PersonalMyCommunityAdapter adapter = new PersonalMyCommunityAdapter(data);
        classify_rv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        classify_rv.setAdapter(adapter);
        // initListener();
    }

    private void initListener() {
        add_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddCommunityActivity_.class));
            }
        });
    }

    private void initData() {
        data = new ArrayList<>();
        MyCommunity l1 = new MyCommunity(1, "捐贈採訪", "", R.drawable.interview, 0);
/*        MyCommunity l2 = new MyCommunity(2, "ppt制作培训", "", R.drawable.meeting1, 4);
        // MyCommunity l3 = new MyCommunity(3,"学术交流大会","", R.drawable.meeting2,  1);
        MyCommunity l4 = new MyCommunity(4, "经验分享会议", "", R.drawable.meeting3, 2);
        MyCommunity l5 = new MyCommunity(5, "开个小会", "", R.drawable.community1, 3);
        MyCommunity l6 = new MyCommunity(6, "党员培训大会", "", R.drawable.commutiy, 2);*/
        data.add(l1);
       /* data.add(l2);
        //   data.add(l3);
        data.add(l4);
        data.add(l5);
        data.add(l6);*/
    }
}
