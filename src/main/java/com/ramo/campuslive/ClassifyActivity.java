package com.ramo.campuslive;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ramo.campuslive.fragment.CharityFragment_;
import com.ramo.campuslive.fragment.Class1Fragment_;
import com.ramo.campuslive.fragment.ClassifyFragment_;
import com.ramo.campuslive.fragment.DoubleLiveListFragment_;
import com.ramo.campuslive.fragment.MeetingFragment;
import com.ramo.campuslive.fragment.MeetingFragment_;
import com.ramo.campuslive.fragment.MonitorFragment_;
import com.ramo.campuslive.fragment.SchoolFragment;
import com.ramo.campuslive.fragment.SchoolFragment_;
import com.ramo.campuslive.fragment.ThemeListFragment_;
import com.ramo.campuslive.view.ViewPagerIndicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ramo on 2016/7/11.
 */
@EActivity(R.layout.activity_classify)
public class ClassifyActivity extends FragmentActivity {

    @ViewById(R.id.classifyViewPagerIndicator)
    ViewPagerIndicator classifyViewPagerIndicator;
    @ViewById(R.id.classifyViewPager)
    ViewPager classifyViewPager;
    private List<String> titleList = Arrays.asList("双人pk","主题", "课堂","公益","找座","社团","会议","校园采访", "运动", "游戏", "生活",
             "拍卖");
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private FragmentPagerAdapter pagerAdapter;

    @AfterViews
    public void init() {
        initFragment();
        initTab();
        initListener();
        classifyViewPager.setAdapter(pagerAdapter);
    }

    private void initTab() {
        classifyViewPagerIndicator.setVisibleTagCount(4);
        classifyViewPagerIndicator.setTabItemTitles(titleList);
        classifyViewPagerIndicator.setView_pager(classifyViewPager);
    }

    private void initFragment() {

        fragmentList.add(new DoubleLiveListFragment_());//双人pk
        fragmentList.add(new ThemeListFragment_());//主题
        fragmentList.add(new Class1Fragment_());//课堂
        fragmentList.add(new CharityFragment_());//公益
        fragmentList.add(new MonitorFragment_());//找座
        fragmentList.add(new ClassifyFragment_());//社团
        fragmentList.add(new MeetingFragment_());//会议
        fragmentList.add(new SchoolFragment_());//会议
        //
       /* for (int i = 0; i < titleList.size() - 7; i++) {
            fragmentList.add(new ClassifyFragment_());
        }*/

        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }
        };


    }

    private void initListener() {

        classifyViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                classifyViewPagerIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

}
