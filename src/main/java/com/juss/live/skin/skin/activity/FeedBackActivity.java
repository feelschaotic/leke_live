package com.juss.live.skin.skin.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.lecloud.leutils.ReUtils;
import com.letv.controller.interfacev1.IPlayerRequestController;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by heyuekuai on 16/1/7.
 */
public class FeedBackActivity extends Activity {
    public static IPlayerRequestController requestController;
    private ViewGroup feedCheckGroup;
    private EditText editContent;
    private EditText editPhone;
    private View feedCancel;
    private View feedBtnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ReUtils.getLayoutId(this, "letv_feed_back_activity_layout"));
        initReportView();
    }

    private void initReportView() {
        feedCheckGroup = (ViewGroup) findViewById(ReUtils.getId(this, "check_group"));
        editContent = (EditText) findViewById(ReUtils.getId(this, "edit_feed_content"));
        editPhone = (EditText) findViewById(ReUtils.getId(this, "edit_phone"));
        feedCancel = findViewById(ReUtils.getId(this, "btn_back"));
        feedCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postFeedBack();
            }
        });
        feedBtnOk = findViewById(ReUtils.getId(this, "btn_submit"));
        feedBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    editPhone.setHint("填写您的联系方式，方便我们和您联系");
                    return;
                }
                postFeedBack();
            }
        });
    }

    private void postFeedBack() {
        if (requestController != null) {
            String feedContent = getFeedCheckText().trim() + "\n" + editContent.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();
            JSONObject param = new JSONObject();
            try {
                param.put("usercontact", phone);
                param.put("userfeedback", feedContent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            requestController.feedBack(param);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestController = null;
    }

    private String getFeedCheckText() {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < feedCheckGroup.getChildCount(); i++) {
            if (feedCheckGroup.getChildAt(i) instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) feedCheckGroup.getChildAt(i);
                for (int j = 0; j < group.getChildCount(); j++) {
                    if (group.getChildAt(j) instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) group.getChildAt(j);
                        if (checkBox.isChecked()) {
                            sb.append("[");
                            sb.append(checkBox.getText().toString().trim());
                            sb.append("]");
                        }
                    }
                }
            }
            if (feedCheckGroup.getChildAt(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) feedCheckGroup.getChildAt(i);
                if (checkBox.isChecked()) {
                    sb.append(checkBox.getText().toString().trim());
                }
            }
        }

        return sb.toString();
    }
}
