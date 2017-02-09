package com.ramo.campuslive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.juss.mediaplay.entity.ProvetSchoolJson;
import com.juss.mediaplay.entity.SchoolJson;
import com.juss.mediaplay.entity.SchoolVo;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.listener.LocationCallbackListener;
import com.juss.mediaplay.listener.MyOnclickStener;
import com.juss.mediaplay.po.Province;
import com.juss.mediaplay.utils.Constants;
import com.juss.mediaplay.utils.JuheDataUtils;
import com.juss.mediaplay.utils.LocationUtil;
import com.juss.mediaplay.utils.NetUtil;
import com.juss.mediaplay.view.SchoolDialog;
import com.juss.mediaplay.view.SimgleDialog;
import com.ramo.campuslive.application.MyApplication;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;
import com.ramo.campuslive.utils.StartAnimUtil;
import com.ramo.campuslive.utils.T;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_reg)
public class RegisterActivity extends SwipeBackActivity implements OnClickListener {
	@ViewById
	 ImageView register_back;
	@ViewById
	 Button register_btn;
	@ViewById
	EditText account;
	@ViewById
	 EditText password;
	@ViewById
	 EditText password_again;
	@ViewById
	 LinearLayout goToLogin;
	@ViewById
	 LinearLayout reg_LL;

	private EditText user_phone;
	private String str_a;
	private String str_p;

	private Location location;
	private String provider;
	String address="";
	private LocationUtil locationUtil;

	private LinearLayout shcool_list_dalog;
	private LinearLayout provice_list_dalog;
	private int pagenum=1;
	private int lastItem=0;
	private String lng="110.456851";
	private String lat="29.138601";
	private ProvetSchoolJson provectList;

	private int schoolId;
	private List<Map<String,Object>> list2 = new ArrayList<>();
	/*
	* {"citycode":312,
	* "lng":"110.456851",
	* "type":"1",
	* "address":"湖南省张家界市永定区子午路",
	* "ext":{"distance":"","direction":"","street":"子午路","province":"湖南省","adcode":"430802","street_number":"","district":"永定区","country_code":0,"country":"中国","city":"张家界市"},
	* "business":"",
	* "lat":"29.138601"}
	*
	 */
	private Handler handlerGeo = new Handler(){
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what){
				/*case 2:
					SchoolDialog.Builder builder = new SchoolDialog.Builder(RegisterActivity.this);
					builder.setDataone(list2,R.layout.school_dialog2, R.layout.item_test, new String[]{"name"}, new int[]{R.id.id_name_tv}, new MyOnclickStener() {
						@Override
						public void getName(Integer id,String name) {
							school_name_tv.setText(name);
							schoolId=id;
						}
					});
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int i) {
							dialog.dismiss();

						}
					});
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});
					Dialog dialog=builder.create();
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();
					break;*/
				case 1:
					L.i(msg.obj.toString());
					JSONObject result = (JSONObject) msg.obj;
					try {
						String ad = result.getString("address");
						if(ad.contains("省")){
							address=ad.substring(0, ad.indexOf('省'));
						}
						else {
							address = ad.substring(0, ad.indexOf('市'));
						}
						L.e(address);
						provice_name_tv.setText(address);
						/*address = result.getString("ext");

						JSONObject object = new JSONObject(address);
						String pro=object.getString("province");
						String city=object.getString("city");
						L.e("-----"+pro+"-------"+city);*/
						//T.showShort(RegisterActivity.this,"定位成功");
					} catch (JSONException e) {
						e.printStackTrace();
					}

					break;
				case 0:
					L.i("出错了");
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};
	private LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			reverseGeocoding(location);
		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}
	};
	private List<Province> list;
	private TextView school_name_tv;
	private TextView provice_name_tv;
	private List<Map<String,Object>> mapProvince=new ArrayList<>();
	HttpCallbackListener getSchoolCallBackListener = new HttpCallbackListener() {
		@Override
		public void onSucc(String response) {
			L.e(response);
			Gson gson = new Gson();
			provectList = gson.fromJson(response,ProvetSchoolJson.class);
			list = provectList.getList();

		}
		@Override
		public void onError(Exception e) {
			L.e(e.getMessage());
		}
	};
 	@AfterViews
	protected void init() {
		register_back.setOnClickListener(this);
		register_btn.setOnClickListener(this);
		goToLogin.setOnClickListener(this);
		shcool_list_dalog = (LinearLayout) findViewById(R.id.shcool_list_dalog);
		provice_list_dalog = (LinearLayout) findViewById(R.id.provice_list_dalog);
		school_name_tv = (TextView) findViewById(R.id.school_name_tv);
		provice_name_tv = (TextView) findViewById(R.id.provice_name_tv);

		user_phone = (EditText) findViewById(R.id.register_calphone);

		shcool_list_dalog.setOnClickListener(this);
		provice_list_dalog.setOnClickListener(this);
		location();
	}


	public void location(){
		LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = manager.getProviders(true);
		for (String p:providers) {
			L.i(p);
		}
		if(providers.contains(LocationManager.GPS_PROVIDER)){
			provider = LocationManager.GPS_PROVIDER;
		}else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
			provider = LocationManager.NETWORK_PROVIDER;
		}else {
			T.showShort(RegisterActivity.this,"没有可用的位置提供器");
			return ;
		}
		location = manager.getLastKnownLocation(provider);
		if (location != null) {
			reverseGeocoding(location);
		}
		else {
			T.showShort(RegisterActivity.this,"定位失败");
		}
//		manager.requestLocationUpdates(provider, 5000, 1, locationListener);
	}
// 把经纬度转为地理位置
	/**
	 * 请求的方法 参数: 第一个参数 当前请求的context 第二个参数 接口id 第三二个参数 接口请求的url 第四个参数 接口请求的方式
	 * 第五个参数 接口请求的参数,键值对com.thinkland.sdk.android.Parameters类型; 第六个参数
	 * 请求的回调方法,com.thinkland.sdk.android.DataCallBack;
	 *
	 */
	public int myposition;
	private void reverseGeocoding(Location location) {
		L.i("地理编码..");
		Parameters parame = new Parameters();
		parame.add("dtype", "json");
		parame.add("type", 1);
		parame.add("lat", location.getLatitude());
		parame.add("lng", location.getLongitude());

		JuheData.executeWithAPI(RegisterActivity.this,
				JuheDataUtils.DATANUM1, Constants.JUHE_GEOAPI, JuheData.GET,
				parame, new DataCallBack() {

					JSONObject jsonObject;
					Message msg = new Message();

					@Override
					public void onSuccess(int statusCode, String responseString) {
						L.e(responseString);
						try {
							jsonObject = new JSONObject(responseString);
							if (jsonObject.getString("resultcode")
									.equals("200")) {
								JSONObject result = jsonObject
										.getJSONObject("result");
//								.L.e(result + "!定位成功");

								String ad = result.getString("address");
								String address=ad.substring(0, ad.indexOf('省'));
//								L.e(address);
								if(ad.contains("省")){
									address=ad.substring(0, ad.indexOf('省'));
								}else {
									address=ad.substring(0,ad.indexOf('市'));
								}
//								L.e(address);
								NetUtil.sendRequestToUrl(ServerConstants.GetSchoolURL,new String[]{"province"},new String[]{address},getSchoolCallBackListener);
								msg.obj = result;
								msg.what = 1;
								handlerGeo.sendMessage(msg);
							}
						} catch (JSONException e) {
							msg.what = 0;
							handlerGeo.sendMessage(msg);
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						L.i("juhe finish" + "调用结束");
					}

					public void onFailure(int statusCode,
										  String responseString, Throwable throwable) {
//                        L.e("juhe error", throwable + "");
						L.e("juhe error" + throwable + "");
						if (statusCode == 30002)
							Toast.makeText(MyApplication.getContext(),
									"网络异常，请刷新重试", Toast.LENGTH_SHORT).show();
						else if (statusCode == 30003)
							Toast.makeText(MyApplication.getContext(), "没有初始化",
									Toast.LENGTH_SHORT).show();
					}
				});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.goToLogin:
				startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
				RegisterActivity.this.finish();
				overridePendingTransition(R.anim.in_from_right, R.anim.keep);
				break;
			case R.id.register_back:
				RegisterActivity.this.finish();
				break;

			case R.id.register_btn:
				str_a = account.getText().toString().trim();
				str_p = password.getText().toString().trim();
				String str_p2 = password_again.getText().toString().trim();
				String phone = user_phone.getText().toString().trim();
				if (str_a.equals("")) {
					Toast.makeText(RegisterActivity.this, "用户名为空！", Toast.LENGTH_LONG).show();
					return;
				}else if(phone.equals("")){
					T.showShort(RegisterActivity.this,"手机号为空！");
					return;
				}else if(!phone.matches("[1][358]\\d{9}")){
					L.i(phone.matches("[1][358]\\d{9}")+"");
					T.showShort(RegisterActivity.this,"手机号码输入错误");
				}
				else if (str_p.equals("")) {
					Toast.makeText(RegisterActivity.this, "密码为空！", Toast.LENGTH_LONG).show();
					return;
				} else if (!str_p.equals(str_p2)) {
					Toast.makeText(RegisterActivity.this, "两次输入密码不一致！", Toast.LENGTH_LONG).show();
					return;
				}

//				String params = "account=" + str_a + "&password=" + str_p + "&location=-1" + "&picture=/image/action_personal.png" + "&gender=0" + "&nick=未填写";
//				new putServerAsyncTask().execute(params, ServerConstants.RegisterURL);
				String[] name= new String[]{"username","password","userphone","school"};
				String[] val=new String[]{str_a,str_p,phone,schoolId+""};
				NetUtil.sendRequestToUrl(ServerConstants.RegisterURL, name, val, new HttpCallbackListener() {
					@Override
					public void onSucc(String response) {
						L.i(response);
						L.e("注册成功！跳转到登录界面");
						startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
					}

					@Override
					public void onError(Exception e) {
						L.e(e.getMessage());
					}
				});
				break;
			case R.id.shcool_list_dalog:
				L.e(address);
				SchoolDialog.Builder builder = new SchoolDialog.Builder(RegisterActivity.this);
				builder.setDataone(address, R.layout.school_dialog2, R.layout.item_test, new String[]{"name"}, new int[]{R.id.id_name_tv}, new MyOnclickStener() {
					@Override
					public void getName(Integer id, String name) {
						school_name_tv.setText(name);
						schoolId = id;
					}
				});
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int i) {
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
				Dialog dialog=builder.create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();


				/*NetUtil.sendRequestToUrl(ServerConstants.GetSchoolURL, new String[]{"province","pagenum"}, new String[]{address,1+""}, new HttpCallbackListener() {
					@Override
					public void onSucc(String response) {
						Gson gson = new Gson();
						SchoolJson provectList = gson.fromJson(response, SchoolJson.class);
						for (SchoolVo p : provectList.getList()) {
							Map<String, Object> map = new HashMap<>();
							map.put("name", p.getSchoolName());
							list2.add(map);
						}
						handlerGeo.sendEmptyMessage(2);
					}

					@Override
					public void onError(Exception e) {

					}
				});
*/
			break;
			case R.id.provice_list_dalog:
				List<Map<String,Object>> list1 = new ArrayList<>();
				SimgleDialog.Builder builder1 = new SimgleDialog.Builder(RegisterActivity.this);
				builder1.setDataone( R.layout.school_dialog2, R.layout.item_test, new String[]{"name"}, new int[]{R.id.id_name_tv}, new MyOnclickStener() {

					@Override
					public void getName(Integer id,String name) {
						provice_name_tv.setText(name);
						address=name;
					}
				});
				builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int i) {
						dialog.dismiss();
					}
				});
				builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				Dialog dialog1 = builder1.create();
				dialog1.setCanceledOnTouchOutside(false);
				dialog1.show();
			break;
		}
	}


	/**
	 * 异步访问网络数据 刚进入页面加载数据
	 *
	 */
	public class putServerAsyncTask extends AsyncTask<Object, Object, Integer> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Integer doInBackground(Object... params) {
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == 1) {
				Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_LONG).show();
				StartAnimUtil.startAnim(account, 1);
				StartAnimUtil.startAnim(password, 0);
				StartAnimUtil.startAnim(password_again, 0);
				StartAnimUtil.startAnim(register_btn, 0);
				new Handler().postDelayed(new Runnable() {

					public void run() {
						RegisterActivity.this.finish();

					}
				}, 3000);

			} else if (result == 2) {
				Toast.makeText(RegisterActivity.this, "账号已被注册！", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(RegisterActivity.this, "联网失败！", Toast.LENGTH_LONG).show();
			}
		}
	}
}
