package com.ramo.campuslive.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class MonitorFragment extends Fragment {

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
        MyCommunity l1 = new MyCommunity(1,"图书馆1楼自习室","", R.drawable.monitor1,  0);
        MyCommunity l2 = new MyCommunity(2,"机房自习室","", R.drawable.monitor2,  4);
        MyCommunity l3 = new MyCommunity(3,"3318自习室","", R.drawable.monitor3,  1);
        MyCommunity l4 = new MyCommunity(4,"2109自习室","", R.drawable.monitor4,  2);
        MyCommunity l5 = new MyCommunity(5,"爱学习自习室","", R.drawable.monitor5,  3);
        MyCommunity l6 = new MyCommunity(6,"实验楼","", R.drawable.monitor6,  2);
        data.add(l1);
        data.add(l2);
        data.add(l3);
        data.add(l4);
        data.add(l5);
        data.add(l6);
    }
}
