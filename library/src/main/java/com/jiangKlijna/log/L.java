package com.jiangKlijna.log;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * log日志打印/保存
 * Author: jiangKlijna
 */
public class L {
    public static final String TAG = L.class.getName();

    public static void e(String str) {
        e(TAG, str);
    }

    public static void e(String tag, String str) {
        Log.e(tag, str);
    }

    public static void e(Throwable e) {
        e(TAG, e);
    }

    public static void e(String tag, Throwable e) {
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            Log.e(tag, stackTraceElement.toString());
        }
    }

    public static void i(String str) {
        i(TAG, str);
    }

    public static void i(String tag, String str) {
        Log.i(tag, str);
    }

    public static void w(String str) {
        w(TAG, str);
    }

    public static void w(String tag, String str) {
        Log.w(tag, str);
    }

    public static void saveToLog(String str) {
        File dir = new File(Environment.getExternalStorageDirectory(), "haoma");
        saveToLog(dir, str);
    }

    public static void saveToLog(Throwable e) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "haoma");
            dir.mkdirs();
            File logFile = new File(dir, new SimpleDateFormat("MM-dd").format(Calendar.getInstance().getTime()) + ".com.jiangKlijna.log");
            FileOutputStream fos = new FileOutputStream(logFile, true);
            PrintWriter errWrite = new PrintWriter(fos);
            errWrite.print(new SimpleDateFormat("hh:mm:ss").format(Calendar.getInstance().getTime()));
            errWrite.print("\n\n" + e);
            StackTraceElement[] stack = e.getStackTrace();
            if (stack != null) {
                for (StackTraceElement stackTraceElement : stack) {
                    errWrite.append("\tat ");
                    errWrite.append(stackTraceElement.toString());
                    errWrite.append("\n");
                }
            }
            Throwable cause = e.getCause();
            if (cause != null) {
                errWrite.append("Caused by: " + cause.toString());
            }
            errWrite.flush();
            errWrite.close();
        } catch (FileNotFoundException e1) {
            e.printStackTrace();
        }
    }

    public static void saveToLog(File dir, CharSequence str) {
        try {
            dir.mkdirs();
            File logFile = new File(dir, new SimpleDateFormat("MM-dd").format(Calendar.getInstance().getTime()) + ".com.jiangKlijna.log");
            FileOutputStream fos = new FileOutputStream(logFile, true);
            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(new SimpleDateFormat("hh:mm:ss").format(Calendar.getInstance().getTime()));
            printWriter.print('\n');
            printWriter.print(str);
            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveToLog(String str, File logFile) {
        try {
            File dir = logFile.getParentFile();
            dir.mkdirs();
            FileOutputStream fos = new FileOutputStream(logFile, true);
            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(str);
            printWriter.print('\n');
            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
