package com.juss.mediaplay.utils;

import com.ramo.campuslive.server.ServerConstants;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 * Created by lenovo on 2016/7/17.
 */
public class UploadFile {

    public  void uploadimg(String file){
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(ServerConstants.ApplyForHostURL);
        /*MultipartEntity
                MultipartEntity multipartEntity = new MultipartEntity();*/

    }
}
