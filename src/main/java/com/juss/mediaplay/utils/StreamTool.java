package com.juss.mediaplay.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by lenovo on 2016/7/27.
 */
public class StreamTool {
    /**
     * 读取流中的数据
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static byte[] readInStream(InputStream inputStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len=0;
        while((len=inputStream.read(buffer))!=-1){
            outStream.write(buffer,0,len);
        }
        inputStream.close();
        return outStream.toByteArray();

    }
}
