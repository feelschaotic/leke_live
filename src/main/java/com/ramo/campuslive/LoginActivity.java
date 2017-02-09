package com.ramo.campuslive;

import android.app.Activity;
import android.content.Entity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.po.User;
import com.juss.mediaplay.utils.NetUtil;
import com.juss.mediaplay.utils.PreferencesUtil;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;
import com.ramo.campuslive.utils.StartAnimUtil;
import com.ramo.campuslive.utils.T;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Set;


public class LoginActivity extends SwipeBackActivity implements View.OnClickListener {

	private TextView tv_register;
	private ImageView back;
	private Button login_btn;
	private EditText et_account;
	private EditText et_password;
	private LinearLayout log_LL;

	private String account;
	private String password;
	private String lastaccount = "";
	private String lastpassword="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		tv_register = (TextView) findViewById(R.id.tv_register);
		back = (ImageView) findViewById(R.id.back);
		login_btn = (Button) findViewById(R.id.login_btn);
		log_LL = (LinearLayout) findViewById(R.id.log_LL);

		back.setOnClickListener(this);
		login_btn.setOnClickListener(this);
		tv_register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.back:
				LoginActivity.this.finish();
				break;

			case R.id.login_btn:
				et_account = (EditText) findViewById(R.id.login_account);
				et_password = (EditText) findViewById(R.id.login_password);
				account = et_account.getText().toString().trim();
				password = et_password.getText().toString().trim();

				if (account.equals("")) {
					Toast.makeText(LoginActivity.this, "用户名为空！", Toast.LENGTH_SHORT).show();
					return;
				} else if (password.equals("")) {
					Toast.makeText(LoginActivity.this, "密码为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				if(account.equals(lastaccount) && password.equals(lastaccount)){
					return ;
				}
				else {
					lastaccount=account;
					lastpassword=password;
				}
				HttpCallbackListener listener = new HttpCallbackListener() {
					@Override
					public void onSucc(String response) {
						L.i(response);
						try {
							JSONObject object = new JSONObject(response);
							int state = object.getInt("state");
							if(state==0){
								JSONObject data = object.getJSONObject("data");
								L.e(data.toString());
								/*SharedPreferences spference = getSharedPreferences("user",MODE_PRIVATE);
								SharedPreferences.Editor editor = spference.edit();
								editor.putString("userInfo",data.toString());
								editor.commit();*/
								PreferencesUtil.setString("userInfo",data.toString());
								startActivity(new Intent(LoginActivity.this, MainActivity.class));
							}else if(state==10001){
								JSONObject data = object.getJSONObject("errMsg");
								T.showLong(LoginActivity.this, data.toString());
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					public void onError(Exception e) {
						L.e(e.toString());
						L.e(e.getMessage());
					}
				};
//				NetUtil.sendRequestToUrl(ServerConstants.ProUrl+ServerConstants.LoginURL,new String[]{"username","password"},new String[]{account,password},listener);
				NetUtil.sendRequestToUrl( ServerConstants.LoginURL, new String[]{"username", "password"}, new String[]{account, password}, listener);

				/*LoadNewsAsyncTask loadNewsAsyncTask = new LoadNewsAsyncTask();
				loadNewsAsyncTask.execute(account, password, 0);*/
				break;

			case R.id.tv_register:
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.in_from_right, R.anim.keep);
				break;
		}

	}


	/**
	 * 异步访问网络数据 刚进入页面加载数据
	 *
	 */
	public class LoadNewsAsyncTask extends AsyncTask<Object, Object, Integer> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Integer doInBackground(Object... params) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post= new HttpPost(ServerConstants.LoginURL);
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				builder.addTextBody("username","用户名");
				builder.addTextBody("password","用户密码");
				HttpEntity entity = builder.build();
				post.setEntity(entity);
				client.execute(post);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == 1  /*&&user != null*/) {
				log_anim();//登录动画
				save_message();//保存信息
			} else {
				Toast.makeText(LoginActivity.this, "登陆失败！", Toast.LENGTH_LONG).show();
			}
		}

		private void save_message() {
			/**
			 * 把用户名和密码保存 用于换肤之后的自动重登
			 */
//			pre.save(new String[] { "username", "password"}, new String[] { account, password });
		//	pre.saveInt("log_count", pre.getAPreferenceInt("log_count") + 1);//保存登录次数
		}

		private void log_anim() {
			StartAnimUtil.startAnim(et_account, 1);
			StartAnimUtil.startAnim(et_password, 0);
			StartAnimUtil.startAnim(login_btn, 0);
		}
	}


}
