package com.ramo.campuslive;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

/**
 * Created by ramo on 2016/7/11.
 */
public class BaseFragmentActivity extends FragmentActivity {

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.keep);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        outStyle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        outStyle();
    }

    private void outStyle() {
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
}
