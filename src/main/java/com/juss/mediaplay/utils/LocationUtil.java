package com.juss.mediaplay.utils;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import com.juss.mediaplay.listener.LocationCallbackListener;
import com.ramo.campuslive.application.MyApplication;
import com.ramo.campuslive.utils.L;
import com.ramo.campuslive.utils.T;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Provider;
import java.util.List;

/**
 * Created by lenovo on 2016/7/19.
 */
public class LocationUtil {
    private Context context;
    private String provider;
    private Location location;
    private LocationCallbackListener listener;
    public LocationUtil(Context contex,LocationCallbackListener listener){
        this.context=context;
        this.listener=listener;
        location();
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //reverseGeocoding(location);
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void location(){
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = manager.getProviders(true);
        for (String p:providers) {
            L.i(p);
        }
        if(providers.contains(LocationManager.GPS_PROVIDER)){
            provider = LocationManager.GPS_PROVIDER;
        }else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
            provider = LocationManager.NETWORK_PROVIDER;
        }else {
            listener.error("没有可用的位置提供器");
//            T.showShort(RegisterActivity.this, "没有可用的位置提供器");
            return ;
        }
        location = manager.getLastKnownLocation(provider);
        if (location != null) {
            reverseGeocoding(location);
        }
        else {
            listener.error("定位失败");
//            T.showShort(RegisterActivity.this,"定位失败");
        }
        manager.requestLocationUpdates(provider, 5000, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //reverseGeocoding(location);
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

    }
// 把经纬度转为地理位置
    /**
     * 请求的方法 参数: 第一个参数 当前请求的context 第二个参数 接口id 第三二个参数 接口请求的url 第四个参数 接口请求的方式
     * 第五个参数 接口请求的参数,键值对com.thinkland.sdk.android.Parameters类型; 第六个参数
     * 请求的回调方法,com.thinkland.sdk.android.DataCallBack;
     *
     */

    private void reverseGeocoding(Location location) {
        L.i("地理编码..");
        Parameters parame = new Parameters();
        parame.add("dtype", "json");
        parame.add("type", 1);
        parame.add("lat", location.getLatitude());
        parame.add("lng", location.getLongitude());

        JuheData.executeWithAPI(context,
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
                                String ad = result.getString("address");
                                String address=ad.substring(0, ad.indexOf('省'));
                                L.e(address);
                                if(ad.contains("省")){
                                    address=ad.substring(0, ad.indexOf('省'));
                                }else {
                                    address=ad.substring(0,ad.indexOf('市'));
                                }
                                L.e(address);
                                listener.getResult(address);

                            }
                        } catch (JSONException e) {
                            listener.error(e.getMessage());
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
                            listener.error("网络异常，请刷新重试");
                          /*  Toast.makeText(MyApplication.getContext(),
                                    "网络异常，请刷新重试", Toast.LENGTH_SHORT).show();*/
                        else if (statusCode == 30003)
                            listener.error("没有初始化");
                           /* Toast.makeText(MyApplication.getContext(), "没有初始化",
                                    Toast.LENGTH_SHORT).show();*/
                    }
                });
    }


}
