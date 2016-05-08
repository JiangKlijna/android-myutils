package com.jiangKlijna.application;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 应用程序工具包
 * Author: jiangKlijna
 */
public class AppUtil {

    /**
     * 判断程序是否处于后台
     *
     * @return true表示程序当前处于后台，false表示程序当前处于前台
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
            }
        }
        return false;
    }

    public static String getMid(Context context) {
        String imei = "", AndroidID = "", serialNo = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
            AndroidID = android.provider.Settings.System.getString(context.getContentResolver(), "android_id");
            serialNo = getDeviceSerialForMid2();
        } catch (Exception e) {
        }
//        String m2 = getMD5Str(imei + AndroidID + serialNo);
        return imei + AndroidID + serialNo;
    }

    private static String getDeviceSerialForMid2() {
        String serial = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception ignored) {
        }
        return serial;
    }

    //版本名
    public static String getVersionName(Context context) {
        try {
            return getPackageInfo(context).versionName;
        } catch (Exception e) {
            return "";
        }
    }

    //版本号
    public static int getVersionCode(Context context) {
        try {
            return getPackageInfo(context).versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

    private static PackageInfo pi;

    private static PackageInfo getPackageInfo(Context context) {
        if (pi != null) {
            return pi;
        }
        try {
            pi = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
        }
        return pi;
    }

    /**
     * 命令行
     *
     * @param exec
     */
    public static int exec(String exec) {
        int status = Integer.MIN_VALUE;
        try {
            Process p = Runtime.getRuntime().exec(exec);
            status = p.exitValue();
            p.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

    /**
     * 是否连接网络
     */
    public static boolean isConnNet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    /**
     * 是否连接Wifi
     */
    public static boolean isConnWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetworkInfo != null && wifiNetworkInfo.isConnected();
    }
}
