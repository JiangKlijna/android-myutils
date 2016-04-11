package com.jiangKlijna.application;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;

import com.jiangKlijna.math.Blowfish;

import java.io.File;
import java.util.HashMap;


public class IntentUtil {
    private final static HashMap<String, String> mimeMap;
    // {后缀名， MIME类型}
    private final static String[][] MIME_MAP = {{".3gp", "video/3gpp"}, {".apk", "application/vnd.android.package-archive"}, {".asf", "video/x-ms-asf"}, {".avi", "video/x-msvideo"}, {".bin", "application/octet-stream"}, {".bmp", "image/bmp"}, {".c", "text/plain"}, {".class", "application/octet-stream"}, {".conf", "text/plain"}, {".cpp", "text/plain"}, {".doc", "application/msword"}, {".docx", "application/msword"}, {".xls", "application/msword"}, {".xlsx", "application/msword"}, {".exe", "application/octet-stream"}, {".gif", "image/gif"}, {".gtar", "application/x-gtar"}, {".gz", "application/x-gzip"}, {".h", "text/plain"}, {".htm", "text/html"}, {".html", "text/html"}, {".jar", "application/java-archive"}, {".java", "text/plain"}, {".jpeg", "image/jpeg"}, {".jpg", "image/jpeg"}, {".js", "application/x-javascript"}, {".com.jiangKlijna.log", "text/plain"}, {".m3u", "audio/x-mpegurl"}, {".m4a", "audio/mp4a-latm"}, {".m4b", "audio/mp4a-latm"}, {".m4p", "audio/mp4a-latm"}, {".m4u", "video/vnd.mpegurl"}, {".m4v", "video/x-m4v"}, {".mov", "video/quicktime"}, {".mp2", "audio/x-mpeg"}, {".mp3", "audio/x-mpeg"}, {".mp4", "video/mp4"}, {".mpc", "application/vnd.mpohun.certificate"}, {".mpe", "video/mpeg"}, {".mpeg", "video/mpeg"}, {".mpg", "video/mpeg"}, {".mpg4", "video/mp4"}, {".mpga", "audio/mpeg"}, {".msg", "application/vnd.ms-outlook"}, {".ogg", "audio/ogg"}, {".pdf", "application/pdf"}, {".png", "image/png"}, {".pps", "application/vnd.ms-powerpoint"}, {".ppt", "application/vnd.ms-powerpoint"}, {".prop", "text/plain"}, {".rar", "application/x-rar-compressed"}, {".rc", "text/plain"}, {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"}, {".sh", "text/plain"}, {".tar", "application/x-tar"}, {".tgz", "application/x-compressed"}, {".txt", "text/plain"}, {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"}, {".wmv", "audio/x-ms-wmv"}, {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"}, {".z", "application/x-compress"}, {".zip", "application/zip"}, {"", "*/*"}};

    static {
        mimeMap = new HashMap<String, String>();
        for (String[] strs : MIME_MAP) {
            mimeMap.put(strs[0], strs[1]);
        }
    }


    public static Intent getCaptureIntent(File srcFile, File targetFile) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(srcFile), "image/*");
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        //intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
        //intent.putExtra("aspectY", 1);// x:y=1:2
        intent.putExtra("output", Uri.fromFile(targetFile));
        intent.putExtra("outputFormat", "JPEG");//返回格式
        return intent;
    }

    /**
     * 跳转到拨号页面
     */
    public static Intent getDialIntent(String number) {
        return new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
    }

    /**
     * 直接拨打电话
     */
    public static Intent getCallIntent(String number) {
        return new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
    }

    public static Intent getPicFromCameraIntent(String tmpPicFilePath) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(tmpPicFilePath);
        Uri u = Uri.fromFile(f);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
        return intent;
    }

    /**
     * @return 从图库选择图片的Intent
     */
    public static Intent getPicIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        return intent;
    }

    public static String getPicPath(Context context, Intent intent) {
        Uri uri = intent.getData();
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(index);
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static Intent openFileIntent(File file) {
        Intent it = new Intent();
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.setAction(Intent.ACTION_VIEW);
        it.setDataAndType(Uri.fromFile(file), getMIMEType(file));
        return it;
    }

    private static String getMIMEType(File file) {
        String type = "*";
        String[] strs = file.getName().replace(".", "-.").split("-");
        String suffix = strs[strs.length - 1].toLowerCase();
        return mimeMap.get(suffix);
    }

    public static Intent openGpsIntent() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }


    public static Intent openPicIntent(String filePath) {
        Intent it = new Intent();
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.setAction(Intent.ACTION_VIEW);
        it.setDataAndType(Uri.fromFile(new File(filePath)), "image/*");
        return it;
    }

    public static Intent openSmsIntent(String phone, String sms) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("sms_body", sms);
        intent.putExtra("address", phone);
        return intent;
    }

    public static Intent picContact() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        return intent;
    }
}
