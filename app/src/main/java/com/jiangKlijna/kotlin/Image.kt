package com.jiangKlijna.kotlin

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * img/bitmap 工具
 * Author: com.jiangKlijna
 */
object Image {

    //bitmap  to  uri
    fun Bitmap2Uri(con: Context, bitmap: Bitmap): Uri {
        return Uri.parse(MediaStore.Images.Media.insertImage(con.contentResolver, bitmap, null, null))
    }

    //uri  to  bitmap
    fun Uri2Bitmap(con: Context, uri: Uri): Bitmap? {
        try {
            return MediaStore.Images.Media.getBitmap(con.contentResolver, uri)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

    //bitmap to intarr
    fun bitmap2Ints(bitmap: Bitmap): IntArray {
        val w = bitmap.width
        val h = bitmap.height
        val pixels = IntArray(w * h)
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h)
        return pixels
    }

    // bitmap to byteArr
    fun bitmap2Bytes(bitmap: Bitmap?, format: Bitmap.CompressFormat): ByteArray? {
        if (bitmap == null) return null
        val baos = ByteArrayOutputStream()
        bitmap.compress(format, 100, baos)
        return baos.toByteArray()
    }

    //byteArr to bitmap
    fun bytes2Bitmap(bytes: ByteArray?): Bitmap? {
        return if (bytes == null || bytes.size == 0) null else BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun drawable2Bitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        } else if (drawable is NinePatchDrawable) {
            val bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    if (drawable.getOpacity() != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight())
            drawable.draw(canvas)
            return bitmap
        } else {
            return null
        }
    }

    fun bitmap2Drawable(res: Resources, bitmap: Bitmap?): Drawable? {
        return if (bitmap == null) null else BitmapDrawable(res, bitmap)
    }

    fun drawable2Bytes(drawable: Drawable?, format: Bitmap.CompressFormat): ByteArray? {
        return if (drawable == null) null else bitmap2Bytes(drawable2Bitmap(drawable), format)
    }

    fun bytes2Drawable(res: Resources?, bytes: ByteArray): Drawable? {
        return if (res == null) null else bitmap2Drawable(res, bytes2Bitmap(bytes))
    }

    /**
     * @param view
     * *
     * @return 将一个view转换为bitmap
     */
    fun convertViewToBitmap(view: View): Bitmap {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache()
        return view.drawingCache
    }

    /**
     * @param view
     * *
     * @param rect
     * *
     * @return 把view按照rect裁剪
     */
    fun convertViewToBitmap(view: View, rect: RectF): Bitmap {
        view.buildDrawingCache()
        val reBitmap = view.drawingCache
        //        Canvas canvas = new Canvas(reBitmap);
        //        canvas.setBitmap(reBitmap);
        //        canvas.clipRect(rectF);
        val matrix = Matrix()
        matrix.postScale(1f, 1f)
        val bitmap = Bitmap.createBitmap(reBitmap, rect.left.toInt(), rect.top.toInt(), (rect.right - rect.left).toInt(), (rect.bottom - rect.top).toInt(), matrix, false)
        view.destroyDrawingCache()
        return bitmap
    }

    /**
     * @param fileAbsolutePath img文件路径
     * *
     * @param screenp          bitmap的最大显示长宽
     * *
     * @return
     */
    fun getImgMarkBitmap(fileAbsolutePath: String, screenp: Point): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(fileAbsolutePath, options)
        options.inJustDecodeBounds = false
        if (options.outWidth > screenp.x || options.outHeight > screenp.y) {
            val scale = Math.max(options.outWidth / screenp.x, options.outHeight / screenp.y).toFloat()
            options.inSampleSize = Math.ceil(scale.toDouble()).toInt()
        }
        return BitmapFactory.decodeFile(fileAbsolutePath, options)
    }

    /**
     * @param p 屏幕长宽点
     * *
     * @return 获得一个屏幕中心裁剪框
     */
    fun getCropRect(p: Point): RectF {
        val hblank = p.x * 0.15f
        val vblank = ((p.y - p.x) / 2).toFloat()
        return RectF(hblank, vblank, p.x - hblank, vblank + 0.7f * p.x)
    }

    //把bitmap保存成一个图片文件
    fun bitmap2File(b: Bitmap?): File? {
        if (b == null) return null
        try {
            val f = newImgFile()
            val out = FileOutputStream(f)
            b.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
            return f
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

    fun newImgFile(): File {
        val f = File(Dir.SDCARD_APP_DIR, System.currentTimeMillis().toString() + ".png")
        try {
            if (!f.exists()) f.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return f
    }

    fun getDrawableByRes(res: Resources, resid: Int): Drawable {
        return BitmapDrawable(res, BitmapFactory.decodeResource(res, resid))
    }

    fun imageView2Bitmap(iv: ImageView): Bitmap {
        return (iv.drawable as BitmapDrawable).bitmap
    }

    /**
     * 按照规定形状图裁剪图片
     * 两张图合并后,把原图的(形状图非矢量)部分裁剪掉

     * @param maskBitmap 形状图
     * *
     * @param picBitmap  原图
     * *
     * @return 合成之后的bitmap
     */
    fun compose(maskBitmap: Bitmap?, picBitmap: Bitmap?): Bitmap? {
        var maskBitmap = maskBitmap
        if (maskBitmap == null || picBitmap == null) return null
        //前置的原图，并将其缩放到跟蒙板大小一直
        //        picBitmap = Bitmap.createScaledBitmap(picBitmap, maskBitmap.getWidth(), maskBitmap.getHeight(), false);
        maskBitmap = Bitmap.createScaledBitmap(maskBitmap, picBitmap.width, picBitmap.height, false)

        val w = maskBitmap!!.width
        val h = maskBitmap.height
        val resultBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        //前置相片添加蒙板效果
        val picPixels = IntArray(w * h)
        val maskPixels = IntArray(w * h)
        picBitmap.getPixels(picPixels, 0, w, 0, 0, w, h)
        maskBitmap.getPixels(maskPixels, 0, w, 0, 0, w, h)
        for (i in maskPixels.indices) {
            if (maskPixels[i] == 0xff000000.toInt()) {
                picPixels[i] = 0
            } else if (maskPixels[i] == 0) {
                //do nothing
            } else {
                //把mask的a通道应用与picBitmap
                maskPixels[i] = maskPixels[i] and 0xff000000.toInt()
                maskPixels[i] = 0xff000000.toInt() - maskPixels[i]
                picPixels[i] = picPixels[i] and 0x00ffffff
                picPixels[i] = picPixels[i] or maskPixels[i]
            }
        }
        //生成前置图片添加蒙板后的bitmap:resultBitmap
        resultBitmap.setPixels(picPixels, 0, w, 0, 0, w, h)
        return resultBitmap
    }
}
