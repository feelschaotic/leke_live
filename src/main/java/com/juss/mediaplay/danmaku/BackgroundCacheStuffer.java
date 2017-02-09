package com.juss.mediaplay.danmaku;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;

/**
 * Created by ramo on 2016/6/9.
 */

/**
 * 自定义弹幕背景和边距
 */
public class BackgroundCacheStuffer extends SpannedCacheStuffer {
    // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
    final Paint paint = new Paint();
    @Override
    public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
        danmaku.padding = 10;  // 在背景绘制模式下增加padding
        super.measure(danmaku, paint, fromWorkerThread);
    }


    @Override
    public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
        paint.setColor(0x8A000000);  //弹幕背景颜色

        //画圆角矩形
        paint.setStyle(Paint.Style.FILL);//充满
        paint.setAntiAlias(true);// 设置画笔的锯齿效果
        RectF oval = new RectF(left + 2, top + 2, left + danmaku.paintWidth - 2, top + danmaku.paintHeight - 2);// 设置个新的长方形
        canvas.drawRoundRect(oval, 20, 15, paint);//第二个参数是x半径，第三个参数是y半径
    }


    @Override
    public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
        // 禁用描边绘制
    }
}
