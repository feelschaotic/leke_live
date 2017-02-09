package com.ramo.campuslive;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.yuntu.demo.core.PushDataListener;
import com.juss.live.RecorderActivity;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.listener.MyOnclickStener;
import com.juss.mediaplay.utils.Constants;
import com.juss.mediaplay.utils.JuheDataUtils;
import com.juss.mediaplay.utils.NetUtil;
import com.juss.mediaplay.view.PremissionDialog;
import com.juss.mediaplay.view.Type2Dialog;
import com.letv.recorder.util.MD5Utls;
import com.ramo.campuslive.asynctask.InviteHostHeadLoadAsyncTask;
import com.ramo.campuslive.bean.LiveRoomMapBean;
import com.ramo.campuslive.handler.LiveMapHandler;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;
import com.ramo.campuslive.utils.T;
import com.ramo.campuslive.view.FlowLayout;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ramo on 2016/7/3.
 */
@EActivity(R.layout.activity_begin_live_setting)
public class BeginLiveSettingActivity extends LiveSettingParent implements PushDataListener {
    @ViewById
    EditText live_title;

    @ViewById
    ImageView begin_live_back_iv;
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
    TextView live_permissions_open_tv;
    @ViewById
    LinearLayout live_permissions_open_ll;
    @ViewById
    LinearLayout live_type_ll;
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
    @ViewById
    LinearLayout live_permissions_doubleLive_view_ll;
    @ViewById
    GridView live_theme_head_gv;
    @ViewById
    Button begin_live_btn;

    private TextView id_typename_tv;

    private String userid;
    private String longitude;
    private String latitude;
    private EditText tv_money;
    private EditText live_permissions_tv_pass1;
    private EditText live_permissions_tv_pass2;
    private EditText live_permissions_tv_pass3;
    private EditText live_permissions_tv_pass4;



    private LocationManager locationManager;
    private String provider;
    private LiveMapHandler drawInMapHandler;
    private Location location;
    private TextView tv_address;


    //推流地址
    private String pustPath;
    //播放地址
    private String pullPath;
/*
    private List<Type> typeList;
    private List<Permissions> permissionsList;*/

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {
            reverseGeocoding(location);
        }
    };

    private Handler handlerGeo = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1:
                    L.i(msg.obj.toString());
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        longitude = object.getString("lng");
                        latitude = object.getString("lat");
                        String add = object.getString("ext");
                        JSONObject addr = new JSONObject(add);
                        L.e(addr.getString("city"));
                        tv_address.setText(addr.getString("city"));

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
    private Integer typeId;

    @AfterViews
    public void init() {
        super.init();

        changeDoubleLiveStyle(View.GONE);
        drawInMapHandler = new LiveMapHandler(this);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_money = (EditText) findViewById(R.id.tv_money);
        live_permissions_tv_pass1 = (EditText) findViewById(R.id.live_permissions_tv_pass1);
        live_permissions_tv_pass2 = (EditText) findViewById(R.id.live_permissions_tv_pass2);
        live_permissions_tv_pass3 = (EditText) findViewById(R.id.live_permissions_tv_pass3);
        live_permissions_tv_pass4 = (EditText) findViewById(R.id.live_permissions_tv_pass4);
        id_typename_tv = (TextView) findViewById(R.id.id_typename_tv);




       /* SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String userinfo = sharedPreferences.getString("userInfo", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userinfo,User.class);
        userid =user.getUserId()+"";
        if(null==userid)*/
            userid ="10003";

        initData();
        initListener();
        initLiveTag();
        initLocation();

    }

    private void testDrawInMap() {
        LiveRoomMapBean bean = new LiveRoomMapBean();
        bean.set_name("XX的直播间");
        bean.set_address("中国湖南省张家界市永定区教场路");
        bean.setPermissions("公开");
        bean.setTheme("挑战杯");
        bean.setState("直播中");
        bean.setTag("第一手资讯、独家报道、体育运动");
        L.e(location.toString());
        if (location != null) {
            bean.set_location(location.getLongitude() + "," + location.getLatitude());
        }

        drawInMapHandler.addTask(bean);
    }

    private void initLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = locationManager.getProviders(true);
        for (String p:providerList) {
            L.i(p);
        }
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            // 当没有可用的位置提供器时，弹出Toast提示用户
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }
        location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            // 反向地理位置编码
            reverseGeocoding(location);
        } else {
            T.showShort(this, "location==null");
        }
        locationManager.requestLocationUpdates(provider, 5000, 1,
                locationListener);
    }


    private void initData() {

        new InviteHostHeadLoadAsyncTask(this, live_theme_head_gv).execute();
       /* final Gson gson = new Gson();
        //获取直播类型已经直播权限
        NetUtil.getHttpUrlConnection(ServerConstants.ProUrl+ServerConstants.TypeListURL,new HttpCallbackListener(){

            @Override
            public void onSucc(String response) {
                L.i(response);
                typeList = gson.fromJson(response,new TypeToken<List<Type>>(){}.getType());
            }

            @Override
            public void onError(Exception e) {
                L.e(e.getMessage());
            }
        });
        NetUtil.getHttpUrlConnection(ServerConstants.ProUrl+ServerConstants.LivePremissionURL,new HttpCallbackListener(){

            @Override
            public void onSucc(String response) {
                L.i(response);
               permissionsList = gson.fromJson(response,new TypeToken<List<Permissions>>(){}.getType());
               *//* live_permissions_text=new String[permissionsList.size()];
                for (int i = 0; i < permissionsList.size() ; i++) {
                    live_permissions_text.[i] = permissionsList.get(i).getPermissionsName();
                }*//*
//                live_permissions_text.l
            }

            @Override
            public void onError(Exception e) {
                L.e(e.getMessage());
            }
        });*/
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

    private void initListener() {
        begin_live_exchange_permissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                begin_live_permissions_iv.setImageResource(live_permissions_img[permissionNum]);
                begin_live_permissions_tv.setText(live_permissions_text[permissionNum]);
//                begin_live_permissions_tv.setText(live_permissions_list.get(permissionNum).getPermissionsName());
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
                /*CustomDialog.Builder builder = new CustomDialog.Builder(BeginLiveSettingActivity.this);
                builder.setTitle("社团");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();*/
                PremissionDialog.Builder builder= new PremissionDialog.Builder(BeginLiveSettingActivity.this);
                if(live_permissions_list!=null)
                   /* builder.setData(live_permissions_list,new MyOnclickStener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            L.e(position + "");


                        }
                    });*/
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
                builder.create().show();
            }
        });
        live_type_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Type2Dialog.Builder builder1 = new Type2Dialog.Builder(BeginLiveSettingActivity.this);
                builder1.setDataone( R.layout.school_dialog2, R.layout.item_test, new String[]{"name"}, new int[]{R.id.id_name_tv}, new MyOnclickStener() {
                    @Override
                    public void getName(Integer id,String name) {
                        id_typename_tv.setText(name);
                        typeId=id;
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

            }
        });

        begin_live_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        live_theme_head_gv.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
        live_theme_head_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View check = view.findViewById(R.id.create_theme_head_check);
                if (check.getVisibility() == View.GONE)
                    check.setVisibility(View.VISIBLE);
                else
                    check.setVisibility(View.GONE);
            }
        });

        begin_live_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //testDrawInMap();
                //要添加一个播放地址发送给服务器，同时生成一个推流地址给直播节目
                /*String streamName = "10003";
                String[] path = LivePathUtil.createStreamUrl(streamName);
                //推流地址
                pustPath=path[0];
                //播放地址
                pullPath = path[1];*/
//                L.e(pustPath);
                pustPath = createStreamUrl(true);
                pullPath=createStreamUrl(false);
                L.e("pustPath=================="+pustPath);
                L.e("pullpath======================="+pullPath);
                String[] name={"userId","typeId","longitude","latitude","permissionsId","money","password","liveUrl"};
                String money =tv_money.getText().toString().trim();
                StringBuffer password = new StringBuffer();
                password.append(live_permissions_tv_pass1.getText().toString());
                password.append(live_permissions_tv_pass2.getText().toString());
                password.append(live_permissions_tv_pass3.getText().toString());
                password.append(live_permissions_tv_pass4.getText().toString());
                String[] val = {userid,typeId+"",longitude,latitude,"1000",money,password.toString(),pullPath};
                NetUtil.sendRequestToUrl(ServerConstants.OpenLiveURL, name, val, listener);
            }
        });
    }
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    public String createStreamUrl(boolean isPush) {
        String tm = format.format(new Date());
        // 获取无推流地址时 流名称，推流域名，签名密钥 三个参数
        String streamName;
        String domainName;
        String appkey;
        domainName = "4315.mpush.live.lecloud.com";
        streamName = userid;
        appkey = "GNLT19K7UYD4W5ME2O00";
        if(streamName==null)
            streamName="20162014";
        String sign;
        if(isPush){
            sign= MD5Utls.stringToMD5(streamName + tm + appkey);

        }else{
            sign=MD5Utls.stringToMD5(streamName + tm + appkey + "lecloud");
            domainName = domainName.replaceAll("push", "pull");
        }
        return "rtmp://"+domainName+"/live/"+streamName+"?tm="+tm+"&sign="+sign;
    }
    private final int SUCCESS=1;
    private String liveVerification;
    private Handler beginLiveHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    String resquest = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(resquest);
                        int state = object.getInt("state");
                        if(0==state){
                            String data = object.getString("data");
                            JSONObject d = new JSONObject(data);
                            liveVerification=d.getString("liveVerification");
                            Intent intent = new Intent(BeginLiveSettingActivity.this, RecorderActivity.class);
                            intent.putExtra("pushName", userid);
                            // 生成一个推流地址，并且把推流地址 传递到 RecorderActivity 中去
                            intent.putExtra("streamUrl", pustPath);
                            intent.putExtra("isVertical", true);
                            intent.putExtra("liveVerification", liveVerification);
                            startActivity(intent);
                        }
                        else{
                            T.showShort(BeginLiveSettingActivity.this,"播放失败，请稍后重试！！");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private HttpCallbackListener listener = new HttpCallbackListener() {
        @Override
        public void onSucc(String response) {
            L.i(response);
            Message msg = Message.obtain();
            msg.obj=response;
            msg.what=SUCCESS;
            beginLiveHandler.sendMessage(msg);


        }

        @Override
        public void onError(Exception e) {
            L.e(e.getMessage());
        }
    };


    private void determineCurrentPermissions() {
        changePwInputStyle(View.GONE);
        changeGroupStyle(View.GONE);
        changeMoneyStyle(View.GONE);
        changeDoubleLiveStyle(View.GONE);
        changeOpenStyle(View.VISIBLE);

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
            case 3:
                changeOpenStyle(View.GONE);
                break;
        }
        if (permissionNum >= live_permissions_img.length)
            permissionNum = 0;
    }

    private void changeGroupStyle(int visible) {
        live_permissions_group_ll.setVisibility(visible);
    }

    private void changeOpenStyle(int visible) {
        live_permissions_open_tv.setVisibility(visible);
        live_permissions_open_ll.setVisibility(visible);
        live_permissions_open_hr.setVisibility(visible);
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
        live_permissions_doubleLive_view_ll.setVisibility(state);
    }

    @Override
    public void onPushFinish(boolean succeed, String errorDes) {
        L.e("上传：" + succeed + "  " + errorDes);
    }







    // 把经纬度转为地理位置
    /**
     * 请求的方法 参数: 第一个参数 当前请求的context 第二个参数 接口id 第三二个参数 接口请求的url 第四个参数 接口请求的方式
     * 第五个参数 接口请求的参数,键值对com.thinkland.sdk.android.Parameters类型; 第六个参数
     * 请求的回调方法,com.thinkland.sdk.android.DataCallBack;
     *
     */

    private void reverseGeocoding(Location location) {
        L.e("地理编码..");
        Parameters parame = new Parameters();
        parame.add("dtype", "json");
        parame.add("type", 1);
        parame.add("lat", location.getLatitude());
        parame.add("lng", location.getLongitude());

        JuheData.executeWithAPI(BeginLiveSettingActivity.this,
                JuheDataUtils.DATANUM1, Constants.JUHE_GEOAPI, JuheData.GET,
                parame, new DataCallBack() {

                    JSONObject jsonObject;
                    Message msg = new Message();

                    @Override
                    public void onSuccess(int statusCode, String responseString) {
                        try {
                            jsonObject = new JSONObject(responseString);
                            if (jsonObject.getString("resultcode")
                                    .equals("200")) {
                                JSONObject result = jsonObject
                                        .getJSONObject("result");
                                L.e(result + "!");
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
                            Toast.makeText(BeginLiveSettingActivity.this,
                                    "网络异常，请刷新重试", Toast.LENGTH_SHORT).show();
                        else if (statusCode == 30003)
                            Toast.makeText(BeginLiveSettingActivity.this, "没有初始化",
                                    Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
