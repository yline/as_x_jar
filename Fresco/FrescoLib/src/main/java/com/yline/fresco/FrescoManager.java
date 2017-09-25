package com.yline.fresco;

import android.net.Uri;

import com.facebook.common.util.UriUtil;
import com.yline.fresco.view.FrescoView;
import com.yline.fresco.view.FrescoViewSafelyHolder;

/**
 * Fresco调用工具类
 *
 * @author yline 2017/9/23 -- 14:48
 * @version 1.0.0
 */
public class FrescoManager {
    private FrescoManager() {
        // Context context
        // FrescoConfig.initConfig(context, true);
    }

    public static FrescoManager getInstance() {
        return FrescoManagerHolder.getInstance();
    }

    private static class FrescoManagerHolder {
        private static FrescoManager sInstance;

        private static FrescoManager getInstance() {
            if (null == sInstance) {
                sInstance = new FrescoManager();
            }
            return sInstance;
        }
    }

    // 还需要测试，多个文件夹的情况
    public static void setImageResource(FrescoView frescoView, int imageId) {
        Uri imageUri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(imageId)).build();
        frescoView.setImageURI(imageUri);
    }

    public static void setImageUri(FrescoView frescoView, String imageUri) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setImageUri(imageUri);
        safelyHolder.build();
    }

    public static void setImageUri(FrescoView frescoView, String imageUri, int width, int height) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setLayoutParams(width, height);
        safelyHolder.setImageUri(imageUri);
        safelyHolder.build();
    }
}
