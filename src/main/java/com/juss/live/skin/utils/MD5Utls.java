package com.juss.live.skin.utils;

/**
 * Created by lenovo on 2016/8/2.
 */

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utls {
    public MD5Utls() {
    }

    public static String stringToMD5(String var0) {
        byte[] var7;
        try {
            var7 = MessageDigest.getInstance("MD5").digest(var0.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
            return null;
        }

        StringBuilder var1 = new StringBuilder(var7.length * 2);
        byte[] var4 = var7;
        int var3 = var7.length;

        for(int var2 = 0; var2 < var3; ++var2) {
            byte var8;
            if(((var8 = var4[var2]) & 255) < 16) {
                var1.append("0");
            }

            var1.append(Integer.toHexString(var8 & 255));
        }

        return var1.toString();
    }

    public static String encodeByMD5(String var0) {
        if(var0 == null) {
            return null;
        } else {
            try {
                MessageDigest var1;
                (var1 = MessageDigest.getInstance("MD5")).update(var0.getBytes());
                byte[] var5 = var1.digest();
                StringBuffer var2 = new StringBuffer("");

                for(int var3 = 0; var3 < var5.length; ++var3) {
                    int var6;
                    if((var6 = var5[var3]) < 0) {
                        var6 += 256;
                    }

                    if(var6 < 16) {
                        var2.append("0");
                    }

                    var2.append(Integer.toHexString(var6));
                }

                return var2.toString();
            } catch (Exception var4) {
                var4.printStackTrace();
                return "";
            }
        }
    }
}
