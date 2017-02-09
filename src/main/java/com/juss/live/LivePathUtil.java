package com.juss.live;

import com.juss.live.skin.utils.MD5Utls;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lenovo on 2016/8/5.
 */
public class LivePathUtil {


    // 移动直播推流域名，在官网移动直播创建应用后可拿到
    public static final String DEFAULT_DOMAINNAME = "4315.mpush.live.lecloud.com";
    // 移动直播推流签名密钥，在官网移动直播创建应用后可拿到
    public static final String DEFAULT_APPKEY = "GNLT19K7UYD4W5ME2O00";
    //播放域名
    public static final String DEFAULT_PLAY="4315.mpull.live.lecloud.com";
    /**
     * **移动直播 **    生成一个 推流地址/播放地址 。这两个地址生成规则特别像
     * @param isPush 当前需要生成的是推流地址还是播放地址，true 推流地址 ，false 播放地址
     * @return 返回生成的地址
     */
    public static String[] createStreamUrl(String stremName) {
        // 格式化，获取时间参数 。注意，当你在创建移动直播应用时，如果开启推流防盗链或播放防盗链 。那么你必须保证这个时间和中国网络时间一致
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String tm = format.format(new Date());
        // 获取无推流地址时 流名称，推流域名，签名密钥 三个参数
        String streamName;
        String domainName;
        String appkey;
        domainName = DEFAULT_DOMAINNAME;
        streamName = stremName;
        appkey = DEFAULT_APPKEY;


        // 生成 sign值,在播放和推流时生成的sign值不一样
        String signpush;
        String signpull ;
        String pushPath,pullPath;
            // 生成推流的 sign 值 。把流名称 ，时间，签名密钥 通过MD5 算法加密
            signpush = MD5Utls.stringToMD5(streamName+tm+appkey);
            pushPath = "rtmp://"+domainName+"/live/"+streamName+"?tm="+tm+"&sign="+signpush;
            // 生成播放 的sign 值，把流名称，时间，签名密钥，和"lecloud" 通过MD5 算法加密
            signpull = MD5Utls.stringToMD5(streamName + tm + appkey + "lecloud");
            // 获取到播放域名。现在播放域名的获取规则是 把推流域名中的 push 替换为pull
            domainName = domainName.replaceAll("push", "pull");
            pullPath = "rtmp://"+domainName+"/live/"+streamName+"?tm="+tm+"&sign="+signpull;

        String[] path = new String[]{pushPath,pullPath};
        return path;
    }
}
