package com.jiangKlijna.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Toast 工具
 * Author: jiangKlijna
 */
public class ToastUtil {
    private static Context context;
    private static Toast toast;
    private static TextView tvContent;

    public static void init(Context c) {
        context = c.getApplicationContext();
    }

    public static Toast getToast(View view) {
        Toast toast = new Toast(view.getContext());
        toast.setView(view);
        return toast;
    }

    public static void setToast(View view) {
        toast.setView(view);
    }

    public static void showToast(Context context, Object obj, int time) {
        if (null == context || obj == null) {
            return;
        }
        if (toast == null) {
            toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            tvContent = new TextView(context);
            tvContent.setTextColor(Color.WHITE);
            tvContent.setGravity(Gravity.CENTER);
            tvContent.setBackgroundColor(Color.argb(200, 0, 0, 0));
            tvContent.setPadding(20, 10, 20, 10);
            toast.setView(tvContent);
        }
        toast.setDuration(time);
        tvContent.setText(obj.toString());
        toast.show();
    }

    public static void showLongToast(Object obj) {
        if (null == context) {
            throw new RuntimeException("init(Context c) must be called before this methord");
        }
        showToast(context, obj, Toast.LENGTH_LONG);
    }

    public static void showLongToast(Context context, Object obj) {
        showToast(context, obj, Toast.LENGTH_LONG);
    }

    public static void showShortToast(Object obj) {
        if (null == context) {
            throw new RuntimeException("init(Context c) must be called before this methord");
        }
        showToast(context, obj, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(Context context, Object obj) {
        showToast(context, obj, Toast.LENGTH_SHORT);
    }

}
