package com.juss.live;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.letv.controller.PlayProxy;
import com.letv.universal.iplay.EventPlayProxy;
import com.ramo.campuslive.R;

public class SeePlayActivity extends Activity {

    private EditText play_path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_play);
        play_path= (EditText) findViewById(R.id.play_path);
    }

    public void shar(View view){
    /*    OnekeyShare shar = new OnekeyShare();
        shar.setTitle("校园乐视直播");
        shar.setText("欢迎广大学生参与");
        shar.setTitleUrl("http://blog.csdn.net/shifuhetudi/article/details/45006605");
        shar.show(SeePlayActivity.this);
  */
    }


    public void seePlay(View view){
/*        String tm = format.format(new Date());
        String streamName="";
        String domainName="4315.mpush.live.lecloud.com";
        String appkey="GNLT19K7UYD4W5ME2O00";
        // 生成播放 的sign 值，把流名称，时间，签名密钥，和"lecloud" 通过MD5 算法加密
        String sign = MD5Utls.stringToMD5(streamName + tm + appkey + "lecloud");
        // 获取到播放域名。现在播放域名的获取规则是 把推流域名中的 push 替换为pull
        domainName = domainName.replaceAll("push", "pull");*/
        String playUrl= play_path.getText().toString().trim();
        Intent intent = new Intent(SeePlayActivity.this,LiveClassPlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_LIVE);
        bundle.putString("path",playUrl);
        intent.putExtra("data", bundle);
        startActivity(intent);

    }

}
