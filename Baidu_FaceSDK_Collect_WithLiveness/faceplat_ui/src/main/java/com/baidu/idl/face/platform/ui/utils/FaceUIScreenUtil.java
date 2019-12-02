package com.baidu.idl.face.platform.ui.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * 功能一、获得屏幕相关的辅助类
 * 功能二、单位转换
 */
public class FaceUIScreenUtil {
    public FaceUIScreenUtil() {
        /* 实例化失败 */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp to px
     *
     * @param context 上下文
     * @param dpValue dp
     * @return px
     */
    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue,
                context.getResources().getDisplayMetrics());
    }

    /**
     * sp to px
     *
     * @param context 上下文
     * @param spValue sp
     * @return px
     */
    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue,
                context.getResources().getDisplayMetrics());
    }

    /**
     * px to dp
     *
     * @param context 上下文
     * @param pxValue px
     * @return dp
     */
    public static float px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxValue / scale);
    }

    /**
     * px to sp
     *
     * @param context 上下文
     * @param pxValue px
     * @return sp
     */
    public static float px2sp(Context context, float pxValue) {
        return (pxValue / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 获得屏幕宽度
     *
     * @param context 上下文
     * @return such as 720 if success
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (null != wm) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context 上下文
     * @return such as 1184 if success
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (null != wm) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.heightPixels;
    }
}
