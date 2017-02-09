package com.juss.mediaplay.utils;

import android.os.Handler;

import com.ramo.campuslive.utils.L;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by lenovo on 2016/7/18.
 */
public class UploadService {

    private static String url="https://www.baidu.com";
//    private static String url="http://10.219.57.16:8080/ServerForUpload/ServletForUpload";
    //	private static String url="http://10.110.6.58:8080/ServerForUpload/ServletForUpload";
    public static final int UPLOAD_SUCCESS=0x123;
    public static final int UPLOAD_FAIL=0x124;
    private Handler handler;
    public UploadService(Handler handler) {
        // TODO Auto-generated constructor stub
        this.handler=handler;
    }
    /**
     * @param params 请求参数，包括请求的的方法参数method如：“upload”，
     * 请求上传的文件类型fileTypes如：“.jpg.png.docx”
     * @param files 要上传的文件集合
     */
    public void uploadFileToServer(final Map<String, String> params, final ArrayList<File> files) {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    if (uploadFiles(url,params,files)) {
                        handler.sendEmptyMessage(UPLOAD_SUCCESS);//通知主线程数据发送成功
                    }else {
                        //将数据发送给服务器失败
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
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
    private boolean uploadFiles(String url,Map<String, String>params,ArrayList<File>files) throws ClientProtocolException, IOException {
        L.i("1");
        HttpClient client=new DefaultHttpClient();// 开启一个客户端 HTTP 请求
        HttpPost post = new HttpPost(url);//创建 HTTP POST 请求
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//		builder.setCharset(Charset.forName("uft-8"));//设置请求的编码格式
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
        int count=0;
        for (File file:files) {
//			FileBody fileBody = new FileBody(file);//把文件转换成流对象FileBody
//			builder.addPart("file"+count, fileBody);
            builder.addBinaryBody("file"+count, file);
            count++;
        }
        builder.addTextBody("method", params.get("method"));//设置请求参数
        builder.addTextBody("fileTypes", params.get("fileTypes"));//设置请求参数

        HttpEntity entity = builder.build();// 生成 HTTP POST 实体
        post.setEntity(entity);//设置请求参数
        HttpResponse response = client.execute(post);// 发起请求 并返回请求的响应
        L.i("ok");
        if (response.getStatusLine().getStatusCode()==200) {
            return true;
        }
        return false;
    }
}
/*
*
* Error:Execution failed for task ':CampusLive:transformResourcesWithMergeJavaResForDebug'.
> com.android.build.api.transform.TransformException: com.android.builder.packaging.DuplicateFileException: Duplicate files copied in APK META-INF/NOTICE
	File1: I:\studing\Teee\CampusLive\libs\httpcore-4.3.2.jar
	File2: I:\studing\Teee\CampusLive\libs\httpmime-4.3.4.jar
*
* */