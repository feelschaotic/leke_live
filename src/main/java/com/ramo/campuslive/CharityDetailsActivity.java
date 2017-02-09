package com.ramo.campuslive;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramo.campuslive.bean.Charity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ramo on 2016/9/8.
 */
@EActivity(R.layout.activity_charity_details)
public class CharityDetailsActivity extends SwipeBackActivity {
    @ViewById
    ImageView charity_cover;
    @ViewById
    TextView charity_attentionNum;
    @ViewById
    TextView charity_shareNum;
    @ViewById
    TextView charity_communityName;
    @ViewById
    TextView charity_schoolName;

    @AfterViews
    public void init() {
        Intent intent = getIntent();
        if (intent != null) {
            Charity charity = intent.getParcelableExtra("charity");
            if (charity != null) {
                charity_attentionNum.setText(charity.getFans().toString());
                charity_shareNum.setText(charity.getShareNum().toString());
                charity_communityName.setText(charity.getCommunityName());
                charity_schoolName.setText(charity.getSchool());
            }
        }
    }
}
