package com.ramo.campuslive.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ramo.campuslive.DoubleLiveActivity;
import com.ramo.campuslive.R;
import com.ramo.campuslive.adapter.DoubleLiveAdapter;
import com.ramo.campuslive.bean.LiveRoom;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramo on 2016/7/11.
 */
@EFragment(R.layout.activity_double_live_list)
public class DoubleLiveListFragment extends Fragment {
    @ViewById
    ListView double_live_listview;
    private List<LiveRoom> doubleList;

    @AfterViews
    public void init(){
        initData();
    }
    private void initData() {
        doubleList=new ArrayList<>();
        doubleList.add(new LiveRoom());

        LiveRoom lr = new LiveRoom("开播有必要实名认证吗？");
        doubleList.add(lr);
        DoubleLiveAdapter doubleLiveAdapter=new DoubleLiveAdapter(getActivity(),doubleList);
        double_live_listview.setAdapter(doubleLiveAdapter);
        initListener();
    }

    private void initListener() {
        double_live_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(),DoubleLiveActivity.class));
            }
        });
    }
}
