package com.juss.mediaplay.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;

import com.ramo.campuslive.R;
import com.ramo.campuslive.application.MyApplication;

import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

/**
 * Created by ramo on 2016/6/9.
 */
public class BarrageUtil {
    private IDanmakuView mDanmakuView;
    private BaseDanmakuParser mParser;
    private DanmakuContext danmakuContext;

    public BarrageUtil(IDanmakuView mDanmakuView, BaseDanmakuParser mParser,DanmakuContext danmakuContext) {
        this.mDanmakuView = mDanmakuView;
        this.mParser = mParser;
        this.danmakuContext = danmakuContext;
    }

    public void hideBarrage() {
        mDanmakuView.hide();
    }

    public void showBarrage() {
        mDanmakuView.show();
    }

    // 添加文本弹幕
    public void myaddDanmaku(String content,int color,boolean islive) {
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }

        danmaku.text = content;
        danmaku.padding = 5;
        danmaku.priority = 1;  //0 表示可能会被各种过滤器过滤并隐藏显示 //1 表示一定会显示, 一般用于本机发送的弹幕
        danmaku.isLive = islive; //是否是直播弹幕
        //danmaku.time = mDanmakuView.getCurrentTime() + 1200; //显示时间
        danmaku.time = mDanmakuView.getCurrentTime(); //显示时间
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = color;
        danmaku.textShadowColor = Color.BLACK; //阴影/描边颜色
        // danmaku.borderColor = Color.GREEN; //边框颜色，0表示无边框
        danmaku.borderColor = 0; //边框颜色，0表示无边框
        //////////////
        danmaku.userId=1107;
        danmaku.userHash="ramo";
        danmaku.isGuest=false;

        mDanmakuView.addDanmaku(danmaku);
    }



    // 添加文本弹幕
    public void addDanmaku(boolean islive) {
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }

        danmaku.text = "这是一条弹幕!" + System.nanoTime();
        danmaku.padding = 5;
        danmaku.priority = 1;  //0 表示可能会被各种过滤器过滤并隐藏显示 //1 表示一定会显示, 一般用于本机发送的弹幕
        danmaku.isLive = islive; //是否是直播弹幕
        //danmaku.time = mDanmakuView.getCurrentTime() + 1200; //显示时间
        danmaku.time = mDanmakuView.getCurrentTime(); //显示时间
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.WHITE;
        danmaku.textShadowColor = Color.BLACK; //阴影/描边颜色
        // danmaku.borderColor = Color.GREEN; //边框颜色，0表示无边框
        danmaku.borderColor = 0; //边框颜色，0表示无边框
        //////////////
        danmaku.userId=1107;
        danmaku.userHash="ramo";
        danmaku.isGuest=false;

        mDanmakuView.addDanmaku(danmaku);
    }

    //添加图文混排弹幕
    public void addDanmaKuShowTextAndImage(boolean islive) {
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        Drawable drawable = MyApplication.getContext().getResources().getDrawable(R.mipmap.ic_launcher);
        drawable.setBounds(0, 0, 100, 100);
        SpannableStringBuilder spannable = createSpannable(drawable);
        danmaku.text = spannable;
        danmaku.padding = 5;
        danmaku.priority = 1;  // 一定会显示, 一般用于本机发送的弹幕
        danmaku.isLive = islive;
        danmaku.time = mDanmakuView.getCurrentTime();
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.WHITE;
        danmaku.textShadowColor = 0;

        // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        // danmaku.underlineColor = Color.GREEN;/下划线颜色
        mDanmakuView.addDanmaku(danmaku);
    }

    /**
     * 创建图文混排模式
     *
     * @param drawable
     * @return
     */
    public SpannableStringBuilder createSpannable(Drawable drawable) {
        String text = "bitmap";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ImageSpan span = new ImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append("图文混排");
        //spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#8A2233B1")), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#8A000000")), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }
}
