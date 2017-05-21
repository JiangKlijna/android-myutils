package com.jiangKlijna.kotlin

import android.util.Log

import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar

import com.jiangKlijna.kotlin.IO
/**
 * log日志打印/保存
 * Author: com.jiangKlijna
 */
object L {
    private val ymd = SimpleDateFormat("yyyy-MM-dd")
    private val hms = SimpleDateFormat("hh-mm-ss")

    val TAG = L::class.java.name

    fun e(str: String) {
        e(TAG, str)
    }

    fun e(tag: String, str: String) {
        Log.e(tag, str)
    }

    fun e(e: Throwable) {
        e(TAG, e)
    }

    fun e(tag: String, e: Throwable) {
        for (stackTraceElement in e.stackTrace) {
            Log.e(tag, stackTraceElement.toString())
        }
    }

    fun i(str: String) {
        i(TAG, str)
    }

    fun i(tag: String, str: String) {
        Log.i(tag, str)
    }

    fun w(str: String) {
        w(TAG, str)
    }

    fun w(tag: String, str: String) {
        Log.w(tag, str)
    }

    fun d(str: String) {
        d(TAG, str)
    }

    fun d(tag: String, str: String) {
        Log.d(tag, str)
    }

    fun v(str: String) {
        v(TAG, str)
    }

    fun v(tag: String, str: String) {
        Log.v(tag, str)
    }

    fun getYmd(): String {
        return ymd.format(Calendar.getInstance().time)
    }

    fun getHms(): String {
        return hms.format(Calendar.getInstance().time)
    }

    val ymdHms: String
        get() = getYmd() + " " + getHms()

    fun saveToLog(e: Throwable) {
        saveToLog(File(Dir.SDCARD_APP_DIR, getYmd() + ".log"), e)
    }

    fun saveToLog(str: CharSequence) {
        saveToLog(File(Dir.SDCARD_APP_DIR, getYmd() + ".log"), str)
    }

    fun saveToLog(logFile: File, str: CharSequence) {
        try {
            IO.str(StringBuffer().append('\n').append(getHms()).append("\t:\n").append(str).toString(), FileWriter(logFile, true))
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun saveToLog(logFile: File, ex: Throwable) {
        try {
            IO.str(StringBuffer().append('\n').append(getHms()).append(":\n").append(getExceptionInfo(ex)).toString(), FileWriter(logFile, true))
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun getExceptionInfo(e: Throwable?): CharSequence {
        if (e == null) {
            return ""
        }
        val buf = StringBuffer()
        buf.append('\n').append(e)
        for (element in e.stackTrace) {
            buf.append("\tat ").append(element.toString()).append('\n')
        }
        buf.append("Caused by: " + e.cause)
                .append(getExceptionInfo(e.cause))
                .append('\n')
        return buf
    }
}
