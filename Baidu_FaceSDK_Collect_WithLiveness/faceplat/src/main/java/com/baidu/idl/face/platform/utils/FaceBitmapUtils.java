package com.baidu.idl.face.platform.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 这个类提供一些操作Bitmap的方法
 */
public final class FaceBitmapUtils {

    /**
     * Private constructor to prohibit nonsense instance creation.
     */
    private FaceBitmapUtils() {
    }

    /**
     * decode 传出来的 argb 的bitmap值
     *
     * @param argbBytes 内容
     * @param roundRect 预览图片大小
     * @return null if exception happened
     */
    public static Bitmap createBitmap(int[] argbBytes, Rect roundRect) {
        if (null == argbBytes || null == roundRect) {
            return null;
        }
        try {
            return Bitmap.createBitmap(argbBytes, roundRect.width(), roundRect.height(), Bitmap.Config.ARGB_8888);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 将预览中的流，转换为，能够直接转成bitmap的byte流
     *
     * @return null if exception
     */
    public static byte[] preFrame2RowImage(byte[] data, Camera camera) {
        if (null == data || null == camera) {
            return null;
        }

        Camera.Size previewSize = camera.getParameters().getPreviewSize(); // 获取尺寸,格式转换的时候要用到
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21,
                previewSize.width, previewSize.height,
                null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, baos);// 80--JPG图片的质量[0-100],100最高
        return baos.toByteArray();
    }
}
