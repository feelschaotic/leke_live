package com.ramo.campuslive.server;

/**
 * Created by ramo on 2016/6/19.
 */
public class ServerConstants {
    public static final String ProUrl = "http://192.168.3.10:8080";


    public static final String BalenceURL = ProUrl + "/live/balance/list";
    //获取直播类型
    public static final String TypeListURL = ProUrl + "/live/type/list";
    //获取直播类型
    public static final String LivePremissionURL = ProUrl + "/live/permission/list";
    //开启直播
    public static final String OpenLiveURL = ProUrl + "/live/liveOpen";
    //按地理位置(两地之间距离)排序直播
    public static final String SortAddrLiveURL = ProUrl + "/live/live/position";

    //直播加入主题
    public static final String AddLiveThemeURL = ProUrl + "/live/live/addtheme";
    //添加直播主题
    public static final String AddThemeURL = ProUrl + "/live/livetheme/append";
    //取关
    public static final String EditPersonalInformationURL = ProUrl + "/live/user/changeInfo";
    //聚合数据反向地理编码
    public static final String GeoUrl = ProUrl + "http://apis.juhe.cn/geo/";
    //注册
    public static final String RegisterURL = ProUrl + "/live/user/register";
    //登录
    public static final String LoginURL = ProUrl + "/live/user/login";
    //申请当主播
    public static final String ApplyForHostURL = ProUrl + "/live/user/applicationAsAnchor";
    //意见反馈
    public static final String FeedbackURL = ProUrl + "/live/feedback";
    //关注
    public static final String FollowURL = ProUrl + "/live/viewer/follow";
    //取关
    public static final String UnFollowURL = ProUrl + "/live/viewer/unfollow";
    //发送地址省份或市名 返回本省学校
    public static final String GetSchoolURL = ProUrl + "/live/province/school";
    //列举省市
    public static final String ListProvinceURL = ProUrl + "/live/province/list";
    //列举主题
    public static final String ListThemeURL = ProUrl + "/live/livetheme/list";
    //主播添加标签
    public static final String LiveAddaleURL = ProUrl + "/live/live/addlable";
    //推荐直播
    public static final String RecommendLiveURL = ProUrl + "/live/recommendLive";

    public static final String AddGuessingURL = ProUrl + "/live/guess/bulid";

    public static final String LIVEGuessListUrl = ProUrl + "/live/live/guesses";

    public static final String MyGuessListUrl = ProUrl + "/live/guess/user";
    public static final String AddCommunityURL = ProUrl + "/live/socity/bulid";

    //我的粉丝
    public static final String MyFansURL = ProUrl + "/live/anchor/fansList";
    public static final String MyCommunityURL = ProUrl + "/live/user/mySocity";
    public static final String HotLiveURL = ProUrl + "/live/hotlive";

    public static final String GessingDetailsURL=ProUrl+"/live/guess/live";
    public static final String TemeBarkURL=ProUrl+"/live/theme/live";
}
