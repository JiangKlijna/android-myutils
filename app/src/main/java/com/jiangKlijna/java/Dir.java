package com.jiangKlijna.java;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * file 工具
 * Author: com.jiangKlijna
 */
public class Dir {
    public static final File SDCARD_APP_DIR = getSdcardFileDir("jiangKlijna");

    private Dir() {
    }

    /**
     * @return /sdcard/
     */
    public static File getSdcardFileDir() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * @param path 自定义路径
     * @return /sdcard/[path]
     */
    public static File getSdcardFileDir(String path) {
        return getDir(Environment.getExternalStorageDirectory(), path);
    }

    public static File getDir(File updir, String path) {
        File dir = new File(updir, path);
        if (!dir.exists()) dir.mkdir();
        return dir;
    }


    /**
     * @return /sdcard/Android/data/[package_name]/files/
     */
    public static File getExternalFileDir(Context context) {
        return context.getExternalFilesDir("");
    }

    /**
     * @param path 自定义路径
     * @return /sdcard/Android/data/[package_name]/files/[path]
     */
    public static File getExternalFileDir(Context context, String path) {
        File dir = new File(context.getExternalFilesDir(""), path);
        if (!dir.exists()) dir.mkdir();
        return dir;
    }


    /**
     * @return /sdcard/Android/data/[package_name]/cache/
     */
    public static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }


    /**
     * @param path 自定义路径
     * @return /sdcard/Android/data/[package_name]/cache/[path]
     */
    public static File getExternalCacheDir(Context context, String path) {
        File dir = new File(context.getExternalCacheDir(), path);
        if (!dir.exists()) dir.mkdir();
        return dir;
    }

}
