package com.jiangKlijna.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.jiangKlijna.io.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * img/bitmap 工具
 * Author: jiangKlijna
 */
public class ImgUtil {
    private ImgUtil() {
    }

    //bitmap  to  uri
    public static Uri Bitmap2Uri(Context con, Bitmap bitmap) {
        return Uri.parse(MediaStore.Images.Media.insertImage(con.getContentResolver(), bitmap, null, null));
    }

    //uri  to  bitmap
    public static Bitmap Uri2Bitmap(Context con, Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(con.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmap(ImageView iv) {
        return ((BitmapDrawable) iv.getDrawable()).getBitmap();
    }

    /**
     * 按照规定形状图裁剪图片
     * 两张图合并后,把原图的(形状图非矢量)部分裁剪掉
     *
     * @param maskBitmap 形状图
     * @param picBitmap  原图
     * @return 合成之后的bitmap
     */
    public static Bitmap compose(Bitmap maskBitmap, final Bitmap picBitmap) {
        if (maskBitmap == null || picBitmap == null) {
            return null;
        }
        //前置的原图，并将其缩放到跟蒙板大小一直
//        picBitmap = Bitmap.createScaledBitmap(picBitmap, maskBitmap.getWidth(), maskBitmap.getHeight(), false);
        maskBitmap = Bitmap.createScaledBitmap(maskBitmap, picBitmap.getWidth(), picBitmap.getHeight(), false);

        int w = maskBitmap.getWidth();
        int h = maskBitmap.getHeight();
        Bitmap resultBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //前置相片添加蒙板效果
        int[] picPixels = new int[w * h];
        int[] maskPixels = new int[w * h];
        picBitmap.getPixels(picPixels, 0, w, 0, 0, w, h);
        maskBitmap.getPixels(maskPixels, 0, w, 0, 0, w, h);
        for (int i = 0; i < maskPixels.length; i++) {
            if (maskPixels[i] == 0xff000000) {
                picPixels[i] = 0;
            } else if (maskPixels[i] == 0) {
                //do nothing
            } else {
                //把mask的a通道应用与picBitmap
                maskPixels[i] &= 0xff000000;
                maskPixels[i] = 0xff000000 - maskPixels[i];
                picPixels[i] &= 0x00ffffff;
                picPixels[i] |= maskPixels[i];
            }
        }
        //生成前置图片添加蒙板后的bitmap:resultBitmap
        resultBitmap.setPixels(picPixels, 0, w, 0, 0, w, h);
        return resultBitmap;
    }

    public static Drawable getDrawableByRes(Resources res, int resid) {
        return new BitmapDrawable(res, BitmapFactory.decodeResource(res, resid));
    }

    /**
     * @pixels The array to receive the bitmap's colors
     * @offset The first index to write into pixels[]
     * @stride The number of entries in pixels[] to skip between rows (must be >= bitmap's width). Can be negative.
     * @x The x coordinate of the first pixel to read from the bitmap
     * @y The y coordinate of the first pixel to read from the bitmap
     * @width The number of pixels to read from each row
     * @height The number of rows to read
     */
    public static int[] getPixels(final Bitmap bitmap) {
        int w = bitmap.getWidth(), h = bitmap.getHeight();
        int[] pixels = new int[w * h];
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h);
        return pixels;
    }

    public static File getNewImgFile() {
        File f = new File(FileUtil.getSdcardFileDir("jiangKlijna"), System.currentTimeMillis() + ".png");
        try {
            if (!f.exists())
                f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }


    /**
     * @param view
     * @return 将一个view转换为bitmap
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    /**
     * @param view
     * @param rect
     * @return 把view按照rect裁剪
     */
    public static Bitmap convertViewToBitmap(View view, RectF rect) {
        view.buildDrawingCache();
        Bitmap reBitmap = view.getDrawingCache();
//        Canvas canvas = new Canvas(reBitmap);
//        canvas.setBitmap(reBitmap);
//        canvas.clipRect(rectF);
        Matrix matrix = new Matrix();
        matrix.postScale(1, 1);
        Bitmap bitmap = Bitmap.createBitmap(reBitmap, (int) (rect.left), (int) (rect.top), (int) (rect.right - rect.left), (int) (rect.bottom - rect.top), matrix, false);
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * @param fileAbsolutePath img文件路径
     * @param screenp          bitmap的最大显示长宽
     * @return
     */
    public static Bitmap getImgMarkBitmap(String fileAbsolutePath, Point screenp) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileAbsolutePath, options);
        options.inJustDecodeBounds = false;
        if (options.outWidth > screenp.x || options.outHeight > screenp.y) {
            float scale = Math.max(options.outWidth / screenp.x, options.outHeight / screenp.y);
            options.inSampleSize = (int) Math.ceil(scale);
        }
        return BitmapFactory.decodeFile(fileAbsolutePath, options);
    }

    /**
     * @param p 屏幕长宽点
     * @return 获得一个屏幕中心裁剪框
     */
    public static RectF getCropRect(Point p) {
        float hblank = p.x * 0.15f;
        float vblank = (p.y - p.x) / 2;
        return new RectF(hblank, vblank, p.x - hblank, vblank + 0.7f * p.x);
    }

    /**
     * 把bitmap保存成一个图片文件
     *
     * @param b
     */
    public static File getFileByBitMap(Bitmap b) {
        if (b == null) {
            return null;
        }
        try {
            File f = getNewImgFile();
            FileOutputStream out = new FileOutputStream(f);
            b.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            return f;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap captureView(View view, int width, int height) {
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bm));
        return bm;
    }

    // 获取指定Activity的截屏，保存到png文件
    public static File takeScreenShot(View view) {
        // View是你需要截图的View
//        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache();
        Bitmap bitmap = null;
        try {
            bitmap = captureView(view, view.getWidth(), view.getHeight());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        File f = getNewImgFile();
        String fname = f.getAbsolutePath();
        if (bitmap != null) {
            try {
                FileOutputStream out = new FileOutputStream(fname);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        view.destroyDrawingCache();
        return f;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp_px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px_dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
