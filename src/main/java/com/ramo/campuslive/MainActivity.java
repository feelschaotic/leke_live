package com.ramo.campuslive;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juss.live.PlayNoSkinActivity;
import com.juss.mediaplay.entity.AnchorLive;
import com.juss.mediaplay.listener.HttpCallbackListener;
import com.juss.mediaplay.utils.Constants;
import com.juss.mediaplay.utils.JuheDataUtils;
import com.juss.mediaplay.utils.NetUtil;
import com.juss.mediaplay.utils.PreferencesUtil;
import com.ramo.campuslive.adapter.EuclidListAdapter;
import com.ramo.campuslive.adapter.OnClickMainBtnListener;
import com.ramo.campuslive.application.MyApplication;
import com.ramo.campuslive.handler.LiveMapHandler;
import com.ramo.campuslive.server.ServerConstants;
import com.ramo.campuslive.utils.L;
import com.ramo.campuslive.utils.LivePermissionType;
import com.ramo.campuslive.utils.T;
import com.ramo.campuslive.view.LivePermissionPwdDialog;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;
import com.tim.shadow.ball.BallActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends EuclidActivity {
    private ImageView main_top_left_menu;
    private DrawerLayout drawerlayout;
    private ImageView main_top_left_begin_live;
    private List<Map<String, Object>> liveList;
    private EuclidListAdapter euclidListAdapter = null;

    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private int pagenum = 1;

    private String activityTag;
    private String[] dataurl = {ServerConstants.SortAddrLiveURL, ServerConstants.RecommendLiveURL, ServerConstants.HotLiveURL};
    private TextView main_topbar_title;

    private List<AnchorLive> AllLiveList;

    private Handler dataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                liveList.clear();
                List<AnchorLive> l = (List) msg.obj;
                setListData(l);
            } else {
                T.showShort(MainActivity.this, getResources().getString(R.string.load_error));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.e("MainActivity");
        if (activityTag != null && activityTag.equals(MyApplication.ACTIVITY_NEAR)) {
            initLocation();
            //存儲進SharePreferences
        }
        init();
        initListener();
        lightBottomMenu();
    }

    private void getData(String activityTag) {
        if (activityTag == null) {
            initRecommendData();
            return;
        }
        switch (activityTag) {
            case MyApplication.ACTIVITY_RECOMMEND:
                initRecommendData();
                break;
            case MyApplication.ACTIVITY_HOT:
                initHotData();
                break;
            case MyApplication.ACTIVITY_NEAR:
                initNearData();
                break;
        }


    }

    private void lightBottomMenu() {
        TextView tv;
        LinearLayout ll = null;
        ImageView iv;
        if (activityTag == null) {
            ll = (LinearLayout) findViewById(R.id.main_bottom_recommend);
            iv = (ImageView) ll.getChildAt(0);
            iv.setImageResource(R.drawable.bottom_nav_recommend);
        } else {
            switch (activityTag) {
                case MyApplication.ACTIVITY_NEAR:
                    ll = (LinearLayout) findViewById(R.id.main_bottom_location);
                    iv = (ImageView) ll.getChildAt(0);
                    iv.setImageResource(R.drawable.bottom_nav_local);
                    break;
                case MyApplication.ACTIVITY_HOT:
                    ll = (LinearLayout) findViewById(R.id.main_bottom_hot);
                    iv = (ImageView) ll.getChildAt(0);
                    iv.setImageResource(R.drawable.bottom_nav_hot);
                    break;
                case MyApplication.ACTIVITY_RECOMMEND:
                default:
                    ll = (LinearLayout) findViewById(R.id.main_bottom_recommend);
                    iv = (ImageView) ll.getChildAt(0);
                    iv.setImageResource(R.drawable.bottom_nav_recommend);
                    break;
            }
        }
        tv = (TextView) ll.getChildAt(1);
        tv.setTextColor(getResources().getColor(R.color.white));
    }

    private void initHotData() {
        String[] key = {"num"};
        String[] val = {"2"};
        NetUtil.sendRequestToUrl(dataurl[2], key, val, new HttpCallbackListener() {
            @Override
            public void onSucc(String response) {
                onDataSucc(response);
            }

            @Override
            public void onError(Exception e) {
                onDataError(e);
            }
        });

    }

    private void initRecommendData() {
        //获取userId
        String key[] = {"userId"};
        String val[] = {"10003"};
        NetUtil.sendRequestToUrl(dataurl[1], key, val, new HttpCallbackListener() {
            @Override
            public void onSucc(String response) {
                onDataSucc(response);
            }

            @Override
            public void onError(Exception e) {
                onDataError(e);
            }
        });
    }

    private void initNearData() {
        String[] key = {LONGITUDE, LATITUDE, "pagenum"};
        String[] val = {PreferencesUtil.getFloat(LONGITUDE, 0.0f) + "", PreferencesUtil.getFloat(LATITUDE, 0.0f) + "", pagenum + ""};
        L.e(PreferencesUtil.getFloat(LONGITUDE, 0.0f) + "");
        L.e("" + PreferencesUtil.getFloat(LATITUDE, 0.0f));
        NetUtil.sendRequestToUrl(dataurl[0], key, val, new HttpCallbackListener() {
            @Override
            public void onSucc(String response) {
                onDataSucc(response);
            }

            @Override
            public void onError(Exception e) {
                onDataError(e);
            }
        });
    }


    private void onDataSucc(String response) {
        // L.e(response);
        Gson gson = new Gson();
        AllLiveList = gson.fromJson(response, new TypeToken<List<AnchorLive>>() {
        }.getType());
        Message msg = new Message();
        msg.what = 1;
        msg.obj = AllLiveList;
        dataHandler.sendMessage(msg);
    }

    private void onDataError(Exception e) {
        L.e(e.getMessage());
        dataHandler.sendEmptyMessage(0);
    }

    private void setListData(List<AnchorLive> liveNears) {
        Map<String, Object> profileMap;
        for (AnchorLive a : liveNears) {
            profileMap = new HashMap<>();
            //头像
            profileMap.put(EuclidListAdapter.KEY_AVATAR, a.getLiveCover());
            //直播间的名字
            profileMap.put(EuclidListAdapter.KEY_NAME, a.getLiveTitle());
            profileMap.put(EuclidListAdapter.KEY_TYPE, a.getLiveType());
            //距离
            if (a.getDistance() != null)
                profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_SHORT, a.getDistance());
            else
                profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_SHORT, a.getFans());

            //签名
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL, "主播：" + a.getUserNickname());
            profileMap.put(EuclidListAdapter.KEY_SCHOOL,  a.getSchool());
            liveList.add(profileMap);
        }
        if (euclidListAdapter != null) {
            euclidListAdapter.notifyDataSetChanged();
            euclidListAdapter.newImageLoader(liveList);
        }
    }

    private void initListener() {
        mButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                judgePermission(nowLivePos);
            }
        });
        main_top_left_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerlayout.openDrawer(Gravity.LEFT);
            }
        });
        main_top_left_begin_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BeginLiveSettingActivity_.class));
            }
        });
    }

    private void judgePermission(int nowLivePos) {
        AnchorLive anchorLive = AllLiveList.get(nowLivePos);

        String permissions = anchorLive.getPermissions();
      //  if (permissions == null || permissions.equals("")) {
            String livePushAddress = anchorLive.getLivePushAddress();
            liveIsPublic(livePushAddress);
       /* } else {
            liveIsNoPublic(permissions);
        }*/


    }

    private void liveIsNoPublic(String permissions) {
        switch (Integer.parseInt(permissions)) {
            case LivePermissionType.PASSWORD:
                liveNeedPwd();
                break;
            case LivePermissionType.MONEY:
                liveNeedMoney();
                break;
        }
    }

    private void liveNeedMoney() {
    }

    private void liveNeedPwd() {
        LivePermissionPwdDialog.Builder builder = new LivePermissionPwdDialog.Builder(this);
        builder.setTitle(R.string.tip);
        builder.setNegativeButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void liveIsPublic(String livePushAddress) {
        Intent intent = new Intent(MainActivity.this, PlayNoSkinActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("path", livePushAddress);
        intent.putExtra("data", mBundle);
        startActivity(intent);
    }

    private void init() {
        main_top_left_menu = (ImageView) findViewById(R.id.main_top_left_menu);
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        main_top_left_begin_live = (ImageView) findViewById(R.id.main_top_left_begin_live);
        main_topbar_title = (TextView) findViewById(R.id.main_topbar_title);
        if (activityTag == null)
            main_topbar_title.setText(MyApplication.ACTIVITY_RECOMMEND);
        else
            main_topbar_title.setText(activityTag);

    }

    @Override
    protected BaseAdapter getAdapter() {
        initDefaultData();

        activityTag = getIntent().getStringExtra(MyApplication.ACTIVITY_EXTRA_NAME);
        getData(activityTag);

        if (activityTag == null || activityTag.equals(MyApplication.ACTIVITY_RECOMMEND))
            euclidListAdapter = new EuclidListAdapter(this, R.layout.list_item_recommend, liveList, mListView);
        else if (activityTag.equals(MyApplication.ACTIVITY_NEAR)) {
            euclidListAdapter = new EuclidListAdapter(this, R.layout.list_item_near, liveList, mListView);
//          EuclidList2Adapter euclidListAdapter2 = new EuclidList2Adapter(this, R.layout.list_item_near, liveList2,mListView);
        } else if (activityTag.equals(MyApplication.ACTIVITY_HOT))
            euclidListAdapter = new EuclidListAdapter(this, R.layout.list_item_hot, liveList, mListView);

        euclidListAdapter.setOnClickMainBtnListener(new OnClickMainBtnListener() {
            @Override
            public void onClick(View v, Integer pos) {
                switch (v.getId()) {
                    case R.id.main_delete_iv:
                        deleteLive(v, pos);
                        break;
                    case R.id.main_like_iv:
                        payAttentionToLive(v, pos);
                        break;
                    case R.id.main_addInTheme_iv:
                        payAttentionToLive(v, pos);
                        break;
                }
            }
        });
        return euclidListAdapter;
    }


    @NonNull
    private void initDefaultData() {
        liveList = new ArrayList<>();
        Map<String, Object> profileMap;

        String[] avatars = {"/live/upload/livecover/default.jpg", "/live/upload/livecover/default.jpg"};
        String[] names = {"闇的直播间", "ramo的直播间"};

        String[] like_nums = {"1400", "0"};

        for (int i = 0; i < avatars.length; i++) {
            profileMap = new HashMap<>();
            profileMap.put(EuclidListAdapter.KEY_AVATAR, avatars[i]);
            profileMap.put(EuclidListAdapter.KEY_NAME, names[i]);
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_SHORT, like_nums[i]);
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL, getString(R.string.lorem_ipsum_long));
            profileMap.put(EuclidListAdapter.KEY_TYPE, "课堂");
            profileMap.put(EuclidListAdapter.KEY_SCHOOL, "--大学");
            liveList.add(profileMap);
        }
    }


    public void onClickLeftMenu(View v) {

       /* SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String data = sharedPreferences.getString("userInfo", "");
        Gson gson = new Gson();
        User user = gson.fromJson(data, User.class);
        int userId=user.getUserId();*/
        switch (v.getId()) {
            case R.id.left_nav_user_rl:
                Intent intent1 = new Intent(MainActivity.this, PersonalCenterActivity_.class);
                intent1.putExtra("userId", 10003);
                startActivity(intent1);
                break;
            case R.id.left_nav_my_community:
                Intent intent2 = new Intent(MainActivity.this, PersonalCenterActivity_.class);
                intent2.putExtra(MyApplication.ACTIVITY_EXTRA_NAME, MyApplication.FRAGMENT_community);
//                intent2.putExtra("userId",userId);
                startActivity(intent2);
                break;
            case R.id.left_nav_my_attention:
                Intent intent3 = new Intent(MainActivity.this, PersonalCenterActivity_.class);
                intent3.putExtra(MyApplication.ACTIVITY_EXTRA_NAME, MyApplication.FRAGMENT_attention);
                startActivity(intent3);
                break;
            case R.id.left_nav_message:
                break;
            case R.id.left_nav_apply_host:
                startActivity(new Intent(MainActivity.this, ApplyForHostActivity_.class));
                break;
            case R.id.left_nav_payments_balance:
                startActivity(new Intent(MainActivity.this, BalancePaymentsActivity_.class));
                break;
            case R.id.left_nav_set:
                startActivity(new Intent(MainActivity.this, SetActivity_.class));
                break;
            case R.id.left_nav_menu_my_guessing:
                startActivity(new Intent(MainActivity.this, MyGuessingActivity_.class));
                break;
            case R.id.left_nav_menu_mapLive:
                startActivity(new Intent(MainActivity.this, MapLiveActivity_.class));
                break;
            case R.id.left_nav_menu_loveWall:
                startActivity(new Intent(MainActivity.this, LoveWallActivity.class));
                break;
            case R.id.left_nav_menu_school_360:
                startActivity(new Intent(MainActivity.this, BallActivity.class));
                break;
            default:
                break;
        }
      //  drawerlayout.closeDrawer(Gravity.LEFT);

    }

    public void onClickBottomMenu(View v) {
        resetAllBottomMenuStyle();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        switch (v.getId()) {
            case R.id.main_bottom_classify:
                startActivity(new Intent(MainActivity.this, ClassifyActivity_.class));
                return;
            case R.id.main_bottom_location:
                intent.putExtra(MyApplication.ACTIVITY_EXTRA_NAME, MyApplication.ACTIVITY_NEAR);
                break;
            case R.id.main_bottom_recommend:
                intent.putExtra(MyApplication.ACTIVITY_EXTRA_NAME, MyApplication.ACTIVITY_RECOMMEND);
                break;
            case R.id.main_bottom_hot:
                intent.putExtra(MyApplication.ACTIVITY_EXTRA_NAME, MyApplication.ACTIVITY_HOT);
                break;

            default:
                break;
        }
        startActivity(intent);
        finish();
    }

    private void resetAllBottomMenuStyle() {

    }


    /*The custom:click event of main btn */
    private void payAttentionToLive(View v, Integer pos) {
        T.showShort(this, "收藏成功");
    }

    private void addInThemeLive(View v, Integer pos) {
        T.showShort(this, "请等待主题管理员审核");
    }

    private void deleteLive(View v, Integer pos) {
        L.e("v:" + v);
        liveList.remove(liveList.get(pos));
        ((SwipeLayout) v.getParent().getParent()).close();
        euclidListAdapter.notifyDataSetChanged();
        T.showShort(this, "反馈成功");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    /**
     * 直播加入主题
     */
    private void addTheme() {
        //liveId themeId
        String[] name = new String[]{"liveId", "themeId"};
        String[] val = new String[]{"liveId", "themeId"};

        NetUtil.sendRequestToUrl(ServerConstants.AddLiveThemeURL, name, val, listener);
    }

    private HttpCallbackListener listener = new HttpCallbackListener() {
        @Override
        public void onSucc(String response) {
            L.i("直播加入主题成功");
        }

        @Override
        public void onError(Exception e) {
            L.e(e.getMessage());
        }
    };



    /*
    登录成功定位
     */

    private String longitude;
    private String latitude;
    private LocationManager locationManager;
    private String provider;
    private LiveMapHandler drawInMapHandler;
    private Location location;
    private TextView tv_address;
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    addTheme();
                    break;
            }
            super.handleMessage(msg);

        }
    };

    private Handler handlerGeo = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    L.i(msg.obj.toString());
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        longitude = object.getString("lng");
                        latitude = object.getString("lat");
                        String add = object.getString("ext");
                        PreferencesUtil.setFloat("longitude", Float.valueOf(longitude));
                        PreferencesUtil.setFloat("latitude", Float.valueOf(latitude));
                        PreferencesUtil.setString("ext", add);
                        L.e("添加成功");
                        //handler.sendEmptyMessage(1);
//                        hander.send
                        // NetUtil.sendRequestToUrl(ServerConstants.ProUrl+ServerConstants.SortAddrLiveURL);
                       /* JSONObject addr = new JSONObject(add);
                        L.e(addr.getString("city"));*/
                       /* tv_address.setText(addr.getString("city"));*/

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

    private void initLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = locationManager.getProviders(true);
        for (String p : providerList) {
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
    // 把经纬度转为地理位置

    /**
     * 请求的方法 参数: 第一个参数 当前请求的context 第二个参数 接口id 第三二个参数 接口请求的url 第四个参数 接口请求的方式
     * 第五个参数 接口请求的参数,键值对com.thinkland.sdk.android.Parameters类型; 第六个参数
     * 请求的回调方法,com.thinkland.sdk.android.DataCallBack;
     */

    private void reverseGeocoding(Location location) {
        L.e("地理编码..");
        Parameters parame = new Parameters();
        parame.add("dtype", "json");
        parame.add("type", 1);
        parame.add("lat", location.getLatitude());
        parame.add("lng", location.getLongitude());

        JuheData.executeWithAPI(MainActivity.this,
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
                            Toast.makeText(MainActivity.this,
                                    "网络异常，请刷新重试", Toast.LENGTH_SHORT).show();
                        else if (statusCode == 30003)
                            Toast.makeText(MainActivity.this, "没有初始化",
                                    Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
