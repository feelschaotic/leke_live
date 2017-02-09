package com.juss.mediaplay.utils;

import android.location.Location;
import android.os.Message;
import android.provider.SyncStateContract;

import com.ramo.campuslive.utils.L;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Formatter;
import java.util.Locale;


public class Utils {

	private StringBuilder mFormatBuilder;
	private Formatter mFormatter;

	public Utils() {
		// 转换成字符串的时间
		mFormatBuilder = new StringBuilder();
		mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

	}

	/**
	 * 把毫秒转换成：1:20:30这里形式
	 * @param timeMs
	 * @return
	 */
	public String stringForTime(int timeMs) {
		int totalSeconds = timeMs / 1000;
		int seconds = totalSeconds % 60;

		int minutes = (totalSeconds / 60) % 60;

		int hours = totalSeconds / 3600;

		mFormatBuilder.setLength(0);
		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
					.toString();
		} else {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}

	public static boolean getIsIdcar(String text){
		String regx0 = "[0-9]{15}";
		String regx1 = "[0-9]{18}";
		String regx2 = "[0-9]{17}x";
		return text.matches(regx1) || text.matches(regx2) || text.matches(regx0);
	}




}
