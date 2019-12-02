package com.baidu.idl.face.platform.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

/**
 * 使用系统默认的弹框
 * （默认的弹框，强制使用者必须使用全球化，因此只提供传入字符串资源）
 * 统一封装，方便后期修改
 *
 * @author yline@kjtpay.com  2018/11/14 -- 17:23
 */
public class KjtDialog {
    private AlertDialog mDialog;

    public static void showDialog(Context context, String title, String content, final DialogInterface.OnClickListener positiveListener) {
        final KjtDialog kjtDialog = new KjtDialog(context, "确定", positiveListener, "取消", null);
        kjtDialog.setTitle(title);
        kjtDialog.setMessage(content);
        kjtDialog.show();
    }

    private KjtDialog(Context context, String positiveText, DialogInterface.OnClickListener positiveListener,
                      String negativeText, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(positiveText) && null != positiveListener) {
            builder.setPositiveButton(positiveText, positiveListener);
        }
        if (!TextUtils.isEmpty(negativeText) && null != negativeListener) {
            builder.setNegativeButton(negativeText, negativeListener);
        }
        mDialog = builder.create();
    }

    public void setTitle(String title) {
        if (null != mDialog) {
            mDialog.setTitle(title);
        }
    }

    public void setMessage(String msg) {
        if (null != mDialog) {
            mDialog.setMessage(msg);
        }
    }

    public void show() {
        try {
            if (null != mDialog && !mDialog.isShowing()) {
                mDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 点击返回键，是否关闭dialog
     */
    public void setCancelable(boolean flag) {
        if (null != mDialog) {
            mDialog.setCancelable(flag);
        }
    }

    /**
     * 点击外部，是否关闭dialog
     */
    public void setCanceledOnTouchOutside(boolean cancel) {
        if (null != mDialog) {
            mDialog.setCanceledOnTouchOutside(cancel);
        }
    }
}
