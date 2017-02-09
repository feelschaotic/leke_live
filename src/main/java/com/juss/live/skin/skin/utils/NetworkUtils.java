package com.juss.live.skin.skin.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Utils of network
 * 
 * @author renweichao
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class NetworkUtils {
    private static final String TAG = "NetworkUtils";

    /**
     * MAC地址分隔符
     */
    private static final String DELIMITER = "-";

    /**
     * 无线MAC
     */
    public static String WALN_MAC = "wlan0";

    /**
     * 有线MAC
     */
    public static String ETH_MAC = "eth0";

    private NetworkUtils() {

    }

    /**
     * Judge the network state.
     * 
     * @param context
     * @return has connect
     */
    public static boolean hasConnect(Context context) {

        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (null == connectivity) {
                return false;
            }

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null == info || !info.isAvailable()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
    
    public static boolean isWifiConnect(Context context) {
    	try {
    		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    		if (info != null) {
    			return info.isConnected();
    		}
    	} catch (Exception e) {
    	}
    	return false;
    }

    /**
     * 获取无线mac
     */
    public static String getWlanMac() {
        try {
            for (Enumeration<?> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements();) {
                NetworkInterface item = (NetworkInterface) e.nextElement();
                byte[] mac = item.getHardwareAddress();
                if (null != mac && mac.length > 0 && WALN_MAC.equalsIgnoreCase(item.getName())) {
                    return bytesToHexString(mac);
                }
            }
        } catch (SocketException e) {
        }

        return "";
    }

    /**
     * 获取有线mac
     */
    public static String getEthMac() {
        try {
            for (Enumeration<?> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements();) {
                NetworkInterface item = (NetworkInterface) e.nextElement();
                byte[] mac = item.getHardwareAddress();
                if (null != mac && mac.length > 0 && ETH_MAC.equalsIgnoreCase(item.getName())) {
                    return bytesToHexString(mac);
                }
            }
        } catch (SocketException e) {
        }

        return "";
    }

    public static Map<String, String> getAllMac() {
        try {
            Map<String, String> macMap = new HashMap<String, String>();
            for (Enumeration<?> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements();) {
                NetworkInterface item = (NetworkInterface) e.nextElement();
                byte[] mac = item.getHardwareAddress();
                if (null != mac && mac.length > 0) {
                    if (ETH_MAC.equalsIgnoreCase(item.getName()) || WALN_MAC.equalsIgnoreCase(item.getName())) {
                        macMap.put(item.getName(), bytesToHexWithDelimiter(mac));
                    }
                }
            }
            return macMap;
        } catch (SocketException e) {
        }

        return null;
    }

    /**
     * Get IP
     * 
     * @return
     */
    public static String getIP() {
        try {
            for (Enumeration<?> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements();) {
                NetworkInterface item = (NetworkInterface) e.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = item.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
        }

        return "";
    }

    /**
     * bytes cast to String
     */
    @SuppressLint("DefaultLocale")
    private static String bytesToHexString(byte[] b) {
        if (null == b) {
            return null;
        }
        String hs = "";
        String stmp;
        for (int i = 0; i < b.length; i++) {
            stmp = (Integer.toHexString(b[i] & 0XFF));
            if (1 == stmp.length()) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }

        return hs.toUpperCase();
    }

    /**
     * 转化成冒号分割形式
     * 
     * @param b
     * @return
     */
    @SuppressLint("DefaultLocale")
    private static String bytesToHexWithDelimiter(byte[] b) {
        if (null == b) {
            return null;
        }
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int i = 0; i < b.length; i++) {
            if (hs.length() > 0) {
                hs.append(DELIMITER);
            }
            stmp = (Integer.toHexString(b[i] & 0XFF));
            if (1 == stmp.length()) {
                hs.append("0" + stmp);
            } else {
                hs.append(stmp);
            }
        }

        return hs.toString().toUpperCase();
    }
}
