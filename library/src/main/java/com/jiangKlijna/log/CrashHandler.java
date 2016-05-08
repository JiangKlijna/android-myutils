package com.jiangKlijna.log;

import android.content.Context;
import android.os.Build;
import android.os.Process;

import com.jiangKlijna.application.AppUtil;
import com.jiangKlijna.io.FileUtil;
import com.jiangKlijna.io.IO;
import com.jiangKlijna.view.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @author jiangKlijna
 */
public class CrashHandler implements UncaughtExceptionHandler {
    public static final String TAG = CrashHandler.class.getName();
    public static final String LOG_SUFFIX = ".log";
    public static final char separator = '\n';

    private static CrashHandler sInstance;

    private final Context mContext;
    private final Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private final String appVerName;
    private final String appVerCode;
    private final String vendor;
    private final String OsVer;
    private final String model;
    private final String mid;

    private String token = "";

    private CrashHandler(Context c) {
        mContext = c.getApplicationContext();
        appVerName = "appVerName:" + AppUtil.getVersionName(mContext);
        appVerCode = "appVerCode:" + AppUtil.getVersionCode(mContext);
        OsVer = "OsVer:" + Build.VERSION.RELEASE;
        vendor = "vendor:" + Build.MANUFACTURER;
        model = "model:" + Build.MODEL;
        mid = "mid:" + AppUtil.getMid(mContext);

        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static synchronized CrashHandler Init(Context c) {
        if (c == null) {
            throw new NullPointerException("Context is null");
        }
        if (sInstance == null) {
            sInstance = new CrashHandler(c);
        }
        return sInstance;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        ToastUtil.showShortToast(mContext, "程序出现异常,即将退出");
        handleException(ex);

        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            Process.killProcess(Process.myPid());// System.exit(1);
        }
    }

    private void handleException(Throwable ex) {
        try {
            IO.copyFile_char(fomatCrashInfo(ex), getCrashFile());
        } catch (Throwable e) {
            L.saveToLog(e);
        }
    }

    //        String crashMD5 = "crashMD5:"+ getMD5Str(dump);
    private String fomatCrashInfo(Throwable ex) {

        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);
        String crashDump = "crashDump:" + "{" + info.toString() + "}";
        printWriter.close();

        String logTime = "logTime:" + L.getYmdHms();
        String exception = "exception:" + ex.toString();

        StringBuilder sb = new StringBuilder();
        sb.append("&start---").append(separator)
                .append(logTime).append(separator)
                .append(appVerName).append(separator)
                .append(appVerCode).append(separator)
                .append(OsVer).append(separator)
                .append(vendor).append(separator)
                .append(model).append(separator)
                .append(mid).append(separator)
                .append(exception).append(separator)
                .append(crashDump).append(separator)
                .append("&end---").append(separator);
        return sb.toString();
    }

    public File getCrashFile() throws IOException {
        File dir = FileUtil.SDCARD_APP_DIR;//mContext.getFilesDir();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File crashFile = new File(dir, "Crash_" + token + "_" + L.getYmdHms() + LOG_SUFFIX);
        if (!crashFile.exists()) {
            crashFile.createNewFile();
        }
        return crashFile;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
