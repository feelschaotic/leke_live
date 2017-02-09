package com.ramo.campuslive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.po.User;
import com.juss.mediaplay.utils.UpLoadFiles;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by ramo on 2016/7/3.
 */
@EActivity(R.layout.activity_edit_personal_information)
public class EditPersonalInformation extends SwipeBackActivity {

    @ViewById
    TextView edit_school;
    @ViewById
    ImageView edit_sex_femal;
    @ViewById
    ImageView edit_sex_male;
    private boolean IsMale = false;

    private EditText edit_nickname;
    private EditText edit_account;
    private EditText edit_phone;
    //状态
    private EditText edit_tv_state;

    String[] paName={"userId","nickName","password","userphone","sex"};

    @AfterViews
    public void init() {
        initdata();
        initListener();
    }
    private HttpCallbackListener callbackListener = new HttpCallbackListener() {
        @Override
        public void onSucc(String response) {
            L.e(response);
        }

        @Override
        public void onError(Exception e) {
            L.e(e.getMessage());
        }
    };
    private void initdata(){
        edit_nickname = (EditText) findViewById(R.id.edit_nickname);
        edit_account = (EditText) findViewById(R.id.edit_account);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
       // edit_tv_style = (EditText) findViewById(R.id.edit_tv_style);


        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        String data = sharedPreferences.getString("userInfo", "");

        Gson gson = new Gson();
        User user = gson.fromJson(data,User.class);
        edit_nickname.setText(user.getUserNickName());
        edit_phone.setText(user.getUserPhone());
        if(user.getUserSex()==1){
            IsMale=true;
        }else{
            IsMale=false;
        }




        /*String[] paName={"userId","nickName","password","userphone","sex"};
        String[] paVal ={"10003","思静","123","13574474965","false"};
        File file = new File("/storage/emulated/0/baidu/searchbox/preset/preset4.2/pic","quick_search_widget_night.jpg");
        UpLoadFiles.uploadFiles(ServerConstants.ProUrl+ServerConstants.EditPersonalInformationURL,paVal,paName,file,"userHead",callbackListener);*/
    }

    private void initListener() {
        findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] paName={"userId","nickName","password","userphone","sex"};
                String[] paVal ={"10003","思静","123","13574474965","false"};
                File file = new File("/storage/emulated/0/baidu/searchbox/preset/preset4.2/pic","quick_search_widget_night.jpg");
                UpLoadFiles.uploadFiles(ServerConstants.EditPersonalInformationURL,paVal,paName,file,"userHead",callbackListener);
            }
        });
        edit_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new AlertDialog.Builder(EditPersonalInformation.this)
                        .setTitle("省份")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();
                dialog.show();
            }
        });

        edit_sex_femal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsMale = false;
                choiceSexStyle();
            }
        });
        edit_sex_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsMale = true;
                choiceSexStyle();
            }
        });
    }

    private void choiceSexStyle() {
        if (IsMale) {
            edit_sex_femal.setImageResource(R.drawable.choice_sex_un_femal);
            edit_sex_male.setImageResource(R.drawable.choice_sex_male);
        } else {
            edit_sex_femal.setImageResource(R.drawable.choice_sex_femal);
            edit_sex_male.setImageResource(R.drawable.choice_sex_un_male);
        }
    }
}
