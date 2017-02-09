package com.juss.mediaplay;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.utils.NetUtil;
import com.juss.mediaplay.utils.UploadService;
import com.ramo.campuslive.R;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;
import com.ramo.campuslive.utils.T;

import org.androidannotations.annotations.rest.Post;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FeedBackActivity extends Activity {

    private ArrayList<File>files;
    private Map<String, String> params;
    private Button btn_feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        btn_feedback = (Button) findViewById(R.id.btn_feedback);

        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbmit();
            }
        });

    }

    private void doSmitfeedback() {

        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(ServerConstants.FeedbackURL);
//                    URL url = new URL(ServerConstants.FollowURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    StringBuffer data = new StringBuffer();
                   // data.append("userId=10002&content=\"测试反馈内容\"");
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setRequestProperty("Charset", "UTF-8");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    //post方式不能缓存数据  需手动设置
                    connection.setUseCaches(false);
                   // connection.connect();
                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes("userId=10002&content=testtest");
                    outputStream.close();
                    T.showShort(FeedBackActivity.this, "反馈成功");
                    if(connection.getResponseCode()==200){
                        T.showShort(FeedBackActivity.this,"反馈成功");
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }


    public void sbmit(){

        String content="feedBackcontent";
        String userid="10002";
        String[] key = {"userId","content"};
        String[] val = {userid,content};
        HttpCallbackListener callbackListener = new HttpCallbackListener() {
            @Override
            public void onSucc(String response) {
                //可以处理返回回来的数据
                T.showShort(FeedBackActivity.this,"反馈成功");
            }
            @Override
            public void onError(Exception e) {
                T.showShort(FeedBackActivity.this,"反馈出错");
            }
        };
        NetUtil.sendRequestToUrl(ServerConstants.ProUrl+ServerConstants.FeedbackURL,key,val,callbackListener);
    }


//添加关注
    public void get(){
        String[] name ={"userId","liveId"};
        String[] val={"10003","1"};
        HttpCallbackListener callbackListener = new HttpCallbackListener() {
            @Override
            public void onSucc(String response) {

            }
            @Override
            public void onError(Exception e) {

            }
        };
        //关注
        NetUtil.sendRequestToUrl(ServerConstants.ProUrl+ServerConstants.FollowURL,name,val,callbackListener);
        //取关
//        NetUtil.sendRequestToUrl(ServerConstants.ProUrl+ServerConstants.UnFollowURL,name,val,callbackListener);


    }
    


}
