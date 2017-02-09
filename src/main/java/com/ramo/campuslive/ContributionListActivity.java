package com.ramo.campuslive;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ramo.campuslive.adapter.ContributionListAdapter;
import com.ramo.campuslive.bean.ContributionList;
import com.ramo.campuslive.bean.User;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramo on 2016/6/30.
 */
@EActivity(R.layout.activity_contribution_list)
public class ContributionListActivity extends SwipeBackActivity {
    @ViewById
    ListView contribution_lv;
    List<ContributionList> contributionList;
    @ViewById
    RelativeLayout contribution_top1;
    @ViewById
    RelativeLayout contribution_top2;
    @ViewById
    RelativeLayout contribution_top3;
    RelativeLayout[] topArry;
    @AfterViews
    public void init() {
        initData();
        initListener();
        contribution_lv.setFocusable(false);
        setListViewHeightBasedOnChildren(contribution_lv);
    }

    private void initData() {
        contributionList=new ArrayList<>();
        ContributionList con1=new ContributionList(new User("江湖感觉乱",null,"年轻人没有双下巴啦，恭喜啊"),101223230);
        ContributionList con2=new ContributionList(new User("钟小喵爱大大大大太阳",null,"从上了知乎，现在无法正视看待体验二字"),99999);
        ContributionList con3=new ContributionList(new User("狂月",null,"没有什么问题是重启解决不了的 如果有 那就是 多喝点水"),88888);
        ContributionList con4=new ContributionList(new User("ramo",null,"上天让我遇到你 所以我格外珍惜你"),10000);
        ContributionList con5=new ContributionList(new User("机器人哈哈哈哈",null,"快人快语 慢人不语"),5233);
        ContributionList con6=new ContributionList(new User("哈哈",null,"慢人不语"),1333);
        ContributionList con7=new ContributionList(new User("机器人哈哈哈哈",null,"快人快语 慢人不语"),1212);
        contributionList.add(con1);
        contributionList.add(con2);
        contributionList.add(con3);
        contributionList.add(con4);
        contributionList.add(con5);
        contributionList.add(con6);
        contributionList.add(con7);


        topArry=new RelativeLayout[]{contribution_top1,contribution_top2,contribution_top3};
        for(int i=0;i<topArry.length;i++){
            TextView name = (TextView) topArry[i].findViewById(R.id.contribution_name);
            TextView money=(TextView) topArry[i].findViewById(R.id.contribution_money);
            name.setText(contributionList.get(i).getUser().getName());
            money.setText(String.valueOf(contributionList.get(i).getMoney()));
            if(i==topArry.length-1){
                for(int j=0;j<topArry.length;j++)
                contributionList.remove(contributionList.get(0));
            }
        }
        contribution_lv.setAdapter(new ContributionListAdapter(this, contributionList));
    }

    private void initListener() {
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

}
