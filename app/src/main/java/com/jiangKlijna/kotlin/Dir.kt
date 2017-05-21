package com.jiangKlijna.kotlin

import android.content.Context
import android.os.Environment

import java.io.File

/**
 * file 工具
 * Author: com.jiangKlijna
 */
object Dir {
    val SDCARD_APP_DIR = getSdcardFileDir("jiangKlijna")

    /**
     * @return /sdcard/
     */
    val sdcardFileDir: File
        get() = Environment.getExternalStorageDirectory()

    /**
     * @param path 自定义路径
     * *
     * @return /sdcard/[path]
     */
    fun getSdcardFileDir(path: String): File {
        return getDir(Environment.getExternalStorageDirectory(), path)
    }

    fun getDir(updir: File, path: String): File {
        val dir = File(updir, path)
        if (!dir.exists()) dir.mkdir()
        return dir
    }


    /**
     * @return /sdcard/Android/data/[package_name]/files/
     */
    fun getExternalFileDir(context: Context): File {
        return context.getExternalFilesDir("")
    }

    /**
     * @param path 自定义路径
     * *
     * @return /sdcard/Android/data/[package_name]/files/[path]
     */
    fun getExternalFileDir(context: Context, path: String): File {
        val dir = File(context.getExternalFilesDir(""), path)
        if (!dir.exists()) dir.mkdir()
        return dir
    }


    /**
     * @return /sdcard/Android/data/[package_name]/cache/
     */
    fun getExternalCacheDir(context: Context): File {
        return context.externalCacheDir
    }


    /**
     * @param path 自定义路径
     * *
     * @return /sdcard/Android/data/[package_name]/cache/[path]
     */
    fun getExternalCacheDir(context: Context, path: String): File {
        val dir = File(context.externalCacheDir, path)
        if (!dir.exists()) dir.mkdir()
        return dir
    }

}
