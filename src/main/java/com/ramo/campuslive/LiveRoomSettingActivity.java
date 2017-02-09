package com.ramo.campuslive;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ramo.campuslive.utils.L;
import com.ramo.campuslive.view.CustomDialog;
import com.ramo.campuslive.view.FlowLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ramo on 2016/7/4.
 */
@EActivity(R.layout.activity_live_room_setting)
public class LiveRoomSettingActivity extends LiveSettingParent {

    @ViewById
    ImageView begin_live_exchange_permissions;
    @ViewById
    ImageView begin_live_permissions_iv;
    @ViewById
    TextView begin_live_permissions_tv;
    @ViewById
    TextView live_permissions_pw_input_tv;
    @ViewById
    LinearLayout live_permissions_pw_input_ll;
    @ViewById
    View live_permissions_open_hr;
    @ViewById
    LinearLayout live_permissions_group_ll;
    @ViewById
    LinearLayout live_permissions_money_ll;
    @ViewById
    FlowLayout begin_live_flowLayout_tag;
    @ViewById
    LinearLayout live_permissions_doubleLive_ll;

    @AfterViews
    public void init() {
        super.init();
        initListener();
        initLiveTag();
    }

    private void initListener() {
        begin_live_exchange_permissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                begin_live_permissions_iv.setImageResource(live_permissions_img[permissionNum]);
                begin_live_permissions_tv.setText(live_permissions_text[permissionNum]);
                permissionNum++;
                determineCurrentPermissions();
            }
        });
        final boolean[] pwIsNotNull = new boolean[live_permissions_tvs.length];
        for (int i = 0; i < live_permissions_tvs.length; i++) {
            final int j = i + 1;
            live_permissions_tvs[i].addTextChangedListener(new TextWatcher() {
                private CharSequence temp;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    temp = s;
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    L.e("temp.length()" + temp.length());
                    if (temp.length() == 1) {
                        pwIsNotNull[j - 1] = true;
                        if (j < live_permissions_tv_ids.length)
                            setFocus(j);
                    } else
                        pwIsNotNull[j - 1] = false;
                }

            });
            live_permissions_tvs[i].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DEL
                            && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (pwIsNotNull[j - 1] && j - 1 >= 0)
                            resetEditText(j - 1);
                        else if (!pwIsNotNull[j - 1] && j - 2 >= 0)
                            resetEditText(j - 2);

                        return true;
                    }
                    return false;
                }

                private void resetEditText(int num) {
                    live_permissions_tvs[num].setText("");
                    setFocus(num);
                    pwIsNotNull[num] = false;
                }
            });
        }

        live_permissions_group_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.Builder builder = new CustomDialog.Builder(LiveRoomSettingActivity.this);
                builder.setMessage(" ");
                builder.setTitle("社团");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

    }

    private void initLiveTag() {
        TextView tv;
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < btns.length; i++) {
            tv = (TextView) inflater.inflate(R.layout.tag, begin_live_flowLayout_tag, false);
            tv.setText(btns[i]);
            begin_live_flowLayout_tag.addView(tv);

            tv.setOnClickListener(new View.OnClickListener() {
                boolean selected = false;

                @Override
                public void onClick(View v) {
                    if (!selected)
                        v.setBackgroundResource(R.drawable.flowlayout_tag_bg_selected);
                    else
                        v.setBackgroundResource(R.drawable.flowlayout_tag_bg);
                    selected = !selected;
                }
            });
        }
    }

    private void determineCurrentPermissions() {
        changePwInputStyle(View.GONE);
        changeGroupStyle(View.GONE);
        changeMoneyStyle(View.GONE);
        changeDoubleLiveStyle(View.GONE);

        switch (permissionNum) {
            case 1:
                changeGroupStyle(View.VISIBLE);
                break;
            case 2:
                changePwInputStyle(View.VISIBLE);
                break;
            case 4:
                changeMoneyStyle(View.VISIBLE);
                break;
            case 5:
                changeDoubleLiveStyle(View.VISIBLE);
                break;
        }
        if (permissionNum >= live_permissions_img.length)
            permissionNum = 0;
    }
    private void changeGroupStyle(int visible) {
        live_permissions_group_ll.setVisibility(visible);
    }


    private void changePwInputStyle(int state) {
        live_permissions_pw_input_ll.setVisibility(state);
        live_permissions_pw_input_tv.setVisibility(state);
    }

    private void changeMoneyStyle(int state) {
        live_permissions_money_ll.setVisibility(state);
    }
    private void changeDoubleLiveStyle(int state) {
        live_permissions_doubleLive_ll.setVisibility(state);
    }
}
