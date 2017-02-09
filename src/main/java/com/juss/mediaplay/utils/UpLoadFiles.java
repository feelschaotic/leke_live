package com.juss.mediaplay.utils;

import android.content.Entity;

import com.juss.mediaplay.listener.HttpCallbackListener;
import com.ramo.campuslive.utils.L;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
//import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by lenovo on 2016/7/18.
 */
public class UpLoadFiles {
    private static final int OUTTIME=10*800;

    public boolean uploadGet(String urlString,String[] name,String val,HttpCallbackListener listener){
        URL url;
        HttpURLConnection conn;
        try {
            url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(OUTTIME);
            conn.setReadTimeout(OUTTIME);
            //获取输入流
            InputStream response = conn.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 用于上传有普通字段和图片或文件的网络通信
     * @param url
     * @param params
     * @param paramskey
     * @param files
     * @param filesNames
     * @return
     */
    public boolean uploadFiles(String url,String[] params,String[] paramskey,ArrayList<File> files,String[] filesNames){
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            L.i("1");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            int count=0;
            L.i("2");
            for (File file: files) {
                FileBody fb = new FileBody(files.get(count));
                builder.addPart(filesNames[count],fb);
                count++;
            }
            L.i("3");
            for (int i = 0; i < paramskey.length ; i++) {
                builder.addTextBody(paramskey[i],params[i]);
            }
            L.i("4");
            HttpEntity entity = builder.build();
            L.i("5");
            post.setEntity(entity);
            L.i("6");
            HttpResponse response = client.execute(post);
            L.i("发送成功");
            if(response.getStatusLine().getStatusCode()==200){

                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void uploadFiles(final String url, final String[] params, final String[] paramskey, final File files,String filesName, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(url);
                    L.i("1");
                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                    int count=0;
                    L.i("2");
                    FileBody fb = new FileBody(files);
                    L.i("3");
                    for (int i = 0; i < paramskey.length ; i++) {
                        builder.addTextBody(paramskey[i],params[i]);
                    }
                    L.i("4");
                    HttpEntity entity = builder.build();
                    L.i("5");
                    post.setEntity(entity);
                    L.i("6");
                    HttpResponse response = client.execute(post);
                    L.i("发送成功");
                    if(response.getStatusLine().getStatusCode()==200){

                        if (listener != null) {
                            listener.onSucc(EntityUtils.toString(
                                    response.getEntity(),"utf-8"));
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    /**
     * @param url servlet的地址
     * @param params 要传递的参数
     * @param files 要上传的文件
     * @return true if upload success else false
     * @throws ClientProtocolException
     * @throws IOException
     */
    public boolean uploadFiles(String url, Map<String, String> params, ArrayList<File> files) {
        try {
            HttpClient client = new DefaultHttpClient();// 开启一个客户端 HTTP 请求
            HttpPost post = new HttpPost(url);//创建 HTTP POST 请求
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            L.i("1");
		   // builder.setCharset(Charset.forName("uft-8"));//设置请求的编码格式
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
            L.i("11");
            int count = 0;
        /*for (File file:files) {
			FileBody fileBody = new FileBody(file);//把文件转换成流对象FileBody
//			builder.addPart("file"+count, fileBody);
//            builder.addBinaryBody("file"+count, file);
//            count++;
        }
*/

            FileBody fileBody = new FileBody(files.get(0));//把文件转换成流对象FileBody
            builder.addPart("IdCardImg", fileBody);
            FileBody fileBody2 = new FileBody(files.get(1));//把文件转换成流对象FileBody
            builder.addPart("liveImg", fileBody2);
            L.i("111" + fileBody.getFilename());
       /* builder.addBinaryBody("IdCardImg", files.get(0));
        builder.addBinaryBody("liveImg", files.get(1));*/
            L.i("2" + fileBody2.getFilename());

            builder.addTextBody("userId", params.get("userId"));//设置请求参数
            builder.addTextBody("IdCard", params.get("IdCard"));//设置请求参数
            builder.addTextBody("realName", params.get("realName"));//设置请求参数
            L.i("3" + builder.toString());
            HttpEntity entity = builder.build();// 生成 HTTP POST 实体
            L.i("4" + entity.toString());
            post.setEntity(entity);//设置请求参数
            L.i("5");
            HttpResponse response =client.execute(post);// 发起请求 并返回请求的响应
            L.i("6");
            L.i("发送成功");
            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            L.e(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            L.e(e.getMessage());
        }

        return false;
    }
}
