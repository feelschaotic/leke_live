package com.juss.mediaplay.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.util.Log;

import com.juss.mediaplay.listener.HttpCallbackListener;
import com.ramo.campuslive.utils.L;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/7/18.
 */
public class NetUtil {


    /**
     * 仅仅获取服务器数据 不携带请求参数
     *
     * @param url
     * @param listener
     * @return
     */
    public static void getIOFromUrl(final String url,
                                    final HttpCallbackListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet get = new HttpGet(url);
                    HttpResponse response = client.execute(get);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        if (listener != null)
                            listener.onSucc(EntityUtils.toString(response
                                    .getEntity()));

                    }
                } catch (Exception e) {
                    Log.e("net conn", "请求失败");
                    if (listener != null)
                        listener.onError(e);
                }
            }
        }).start();
    }


    /**
     * 向服务器发送请求 带参数
     *
     * @param url
     * @param name
     * @param val
     * @param listener
     * @return
     */

    public static void sendRequestToUrl(final String url, final String[] name,
                                        final String[] val, final HttpCallbackListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(url);

                    // 通过NameValuePair集合保存待提交的数据
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    for (int i = 0; i < name.length; i++)
                        params.add(new BasicNameValuePair(name[i], val[i]));

                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
                            params, "utf-8");
                    post.setEntity(entity);

                    HttpResponse response = client.execute(post);
                    // 如果返回状态码是200 则表示请求成功
                    L.i("ok" + response.getStatusLine().getStatusCode());
                    if (response.getStatusLine().getStatusCode() == 200) {
                        if (listener != null)
                            listener.onSucc(EntityUtils.toString(
                                    response.getEntity(), "utf-8"));
                        // 把数据放在回调函数里
                    }

                } catch (Exception e) {
                    if (listener != null)
                        listener.onError(e);
                }
            }
        }).start();
    }

    public static void getHttpUrlConnection(final String url,
                                            final HttpCallbackListener listener) {

        try {
            new Thread(new Runnable() {
                private URL mUrl = new URL(url);
                private HttpURLConnection connection = null;

                @Override
                public void run() {
                    try {
                        connection = (HttpURLConnection) mUrl.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);

                        InputStream in = connection.getInputStream();
                        BufferedReader read = new BufferedReader(
                                new InputStreamReader(in));

                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = read.readLine()) != null) {
                            response.append(line);
                        }

                        if (listener != null) {
                            listener.onSucc(response.toString());
                        }
                    } catch (IOException e) {
                        listener.onError(e);
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (MalformedURLException e) {
            listener.onError(e);
            e.printStackTrace();
        }
    }

    //获取当前网络状态
    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_WIFI;
        }

        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }

    public static void sendJsonToServer(final String url, final String json, final String key, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                HttpParams httpParams = new BasicHttpParams();
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                try {
                    nameValuePair.add(new BasicNameValuePair(key, json));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                    httpPost.setParams(httpParams);
                    httpClient.execute(httpPost);
                    HttpResponse response = httpClient.execute(httpPost);

                    StatusLine statusLine = response.getStatusLine();
                    if (statusLine != null && statusLine.getStatusCode() == 200) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            listener.onSucc(response.toString());
                        }
                    }
                } catch (Exception e) {
                    listener.onError(e);
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private static String readInputStream(InputStream is) throws IOException {
        if (is == null)
            return null;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = is.read(buf)) != -1) {
            bout.write(buf, 0, len);
        }
        is.close();
        return URLDecoder.decode(new String(bout.toByteArray()), "utf-8");
    }

}

