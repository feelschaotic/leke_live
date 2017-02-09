package com.ramo.campuslive;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.juss.mediaplay.entity.GuessingOption;
import com.juss.mediaplay.entity.GuessingVO;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.utils.NetUtil;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;
import com.ramo.campuslive.utils.T;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Date;

/**
 * Created by ramo on 2016/7/2.
 */
@EActivity(R.layout.activity_guessing)
public class GuessingActivity extends SwipeBackActivity {
    @ViewById
    ImageView guessing_add_options_btn;
    @ViewById
    Button guessing_submit_btn;
    @ViewById
    EditText guessing_title_et;
    @ViewById
    EditText guessing_bet_et1;
    @ViewById
    EditText guessing_time_et1;
    @ViewById
    CheckBox guessing_agree_deal_checkBox;
    private int optionsNum = 2;

    @ViewById(R.id.guessing_options_ll)
    LinearLayout guessing_options_ll;
    private android.os.Handler addGuessHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                T.showShort(GuessingActivity.this, "添加成功");
            } else {
                T.showShort(GuessingActivity.this, "添加失败，请稍候重试");
            }
            finish();
        }
    };

    @AfterViews
    public void init() {
        initListener();
    }

    private void initListener() {
        guessing_add_options_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = LayoutInflater.from(GuessingActivity.this);
                LinearLayout ll = (LinearLayout) inflater.inflate(
                        R.layout.activity_guessing_options, null);
                TextView guessing_options_tv = (TextView) ll.findViewById(R.id.guessing_options_tv);
                guessing_options_tv.setText("选项" + optionsNum++);
                guessing_options_ll.addView(ll);
                delEvent((ImageView) ll.findViewById(R.id.guessing_del_options));
            }
        });
        delEvent((ImageView) findViewById(R.id.guessing_del_options));

        guessing_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataToVo();
            }
        });
    }

    private void dataToVo() {
        int time = Integer.parseInt(guessing_time_et1.getText().toString());
        String title = guessing_title_et.getText().toString();

        GuessingVO guessingVO = new GuessingVO();
        guessingVO.setStopTime(new Date(System.currentTimeMillis() + time));
        guessingVO.setLiveId(1005);
        guessingVO.setTitle(title);
        for (int i = 0; i < guessing_options_ll.getChildCount(); i++) {
            LinearLayout ll = (LinearLayout) guessing_options_ll.getChildAt(i);
            String optionName = ((EditText) ll.findViewById(R.id.guessing_options_et)).getText().toString();
            float compensate = Float.parseFloat(((EditText) ll.findViewById(R.id.guessing_options_et2)).getText().toString());
            L.e("optionName :" + optionName + ":" + compensate);
            GuessingOption option = new GuessingOption();
            option.setOptionsName(optionName);
            option.setOptionsCompensate(compensate);
            guessingVO.getOptions().add(option);
        }
        Gson gson = new Gson();
        String json = gson.toJson(guessingVO);
        L.e(json);
        sendToServer(json);
    }

    private void sendToServer(final String json) {
        String key = "Guessing";
        NetUtil.sendJsonToServer(ServerConstants.AddGuessingURL, json, key, new HttpCallbackListener() {
            @Override
            public void onSucc(String response) {
                L.e("成功");
                addGuessHandler.sendEmptyMessage(1);
            }

            @Override
            public void onError(Exception e) {
                addGuessHandler.sendEmptyMessage(0);
            }
        });
    }


    private void delEvent(ImageView guessing_del_options) {
        guessing_del_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsNum--;
                guessing_options_ll.removeView((View) v.getParent());
            }
        });
    }


}
