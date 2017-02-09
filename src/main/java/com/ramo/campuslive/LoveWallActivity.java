package com.ramo.campuslive;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.ramo.campuslive.fragment.LoveWallTemplateFragment;

/**
 * Created by ramo on 2016/7/7.
 */
public class LoveWallActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_love_wall);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.love_wall_container, new LoveWallTemplateFragment()).commit();
        }
    }
}
