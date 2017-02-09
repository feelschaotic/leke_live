package com.ramo.campuslive.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.juss.mediaplay.CharityAuthenticateActivity;
import com.ramo.campuslive.CharityDetailsActivity_;
import com.ramo.campuslive.R;
import com.ramo.campuslive.adapter.CharityListAdapter;
import com.ramo.campuslive.adapter.OnClickMainBtnListener;
import com.ramo.campuslive.bean.Charity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramo on 2016/9/8.
 */
@EFragment(R.layout.fragment_charity_layout)
public class CharityFragment extends Fragment {
    @ViewById
    RecyclerView classify_rv;
    private List data;
    private CharityListAdapter adapter;

    @ViewById(R.id.charity_authenticate)
    TextView charity_authenticate_tv;

    @AfterViews
    public void init() {
        initData();
        classify_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CharityListAdapter(getActivity(), data);
        classify_rv.setAdapter(adapter);
        onListener();
    }

    private void onListener() {
        adapter.setOnClickMainBtnListener(new OnClickMainBtnListener() {
            @Override
            public void onClick(View v, Integer pos) {
                Charity charity = (Charity) data.get(pos);
                Intent intent = new Intent(getActivity(), CharityDetailsActivity_.class);
                intent.putExtra("charity",charity);
                startActivity(intent);
            }
        });

        charity_authenticate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CharityAuthenticateActivity.class));
            }
        });
    }

    private void initData() {
        data = new ArrayList();
        Charity object = new Charity();
        object.setSchool("中南大学");
        object.setFans(20);
        object.setShareNum(42);
        object.setCommunityName("圆梦联盟");
        data.add(object);
        Charity object1 = new Charity();
        object1.setSchool("工业大学");
        object1.setFans(35);
        object1.setCommunityName("-州梦公益");
        object1.setShareNum(3);
        data.add(object1);
        Charity object2 = new Charity();
        object2.setSchool("农业大学");
        object2.setFans(15);
        object2.setCommunityName("慈济社");
        object2.setShareNum(33);
        data.add(object2);
    }
}
