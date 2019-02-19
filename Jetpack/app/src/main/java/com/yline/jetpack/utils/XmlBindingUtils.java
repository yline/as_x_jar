package com.yline.jetpack.utils;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;

import com.yline.jetpack.binding.custom.model.CustomModel;
import com.yline.view.fresco.FrescoManager;
import com.yline.view.fresco.view.FrescoView;

/**
 * 专门实现 BindingAdapter 的工具类
 *
 * @author yline 2019/2/19 -- 10:21
 */
public class XmlBindingUtils {
    /* ------------------------------- Fresco ------------------------------- */
    @BindingAdapter({"bindingImageUri"})
    public static void setFrescoImageUri(FrescoView frescoView, String imageUri) {
        FrescoManager.setImageUri(frescoView, imageUri);
    }

    @BindingAdapter({"bindingImageUri", "bindingWidth", "bindingHeight"})
    public static void setFrescoImageUri(FrescoView frescoView, String imageUri, int width, int height) {
        FrescoManager.setImageUri(frescoView, imageUri, width, height);
    }

    @BindingAdapter({"bindingImageId"})
    public static void setFrescoImageResource(FrescoView frescoView, int imageId) {
        FrescoManager.setImageResource(frescoView, imageId);
    }

    @BindingAdapter({"bindingImageId", "bindingImageWidth", "bindingImageHeight"})
    public static void setFrescoImageResource(FrescoView frescoView, int imageId, int width, int height) {
        FrescoManager.setImageResource(frescoView, imageId, width, height);
    }

    @BindingAdapter({"bindingPath", "bindingBitmapWidth", "bindingBitmapHeight"})
    public static void setFrescoImageLocal(FrescoView frescoView, String path, int bitmapWidth, int bitmapHeight) {
        FrescoManager.setImageLocal(frescoView, path, bitmapWidth, bitmapHeight);
    }

    /* ------------------------------- 其他工具类 ------------------------------- */

    /**
     * 使用情况：建议不要使用；很危险，基本定位问题时，定位不到
     * 全局公用 方法名可自定义，只需关心参数 User
     */
    @BindingConversion
    public static String convertCustomModel2String(CustomModel customModel) {
        return (customModel.getName() + "-" + customModel.getPhone() + "-" + customModel.getAge());
    }
}
