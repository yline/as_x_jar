package com.yline.fresco.view;

import android.net.Uri;
import android.view.ViewGroup;

/**
 * 入口检查
 *
 * @author yline 2017/9/23 -- 17:44
 * @version 1.0.0
 */
public final class FrescoViewSafelyHolder extends FrescoViewHolder {

    public FrescoViewSafelyHolder(FrescoView frescoView) {
        super(frescoView);
    }

    public void setImageUri(String imageUriString) {
        Uri imageUri = Uri.parse(imageUriString);
        super.setImageUri(imageUri);
    }

    public void setLayoutParams(int viewWidth, int viewHeight) {
        ViewGroup.LayoutParams layoutParams = frescoView.getLayoutParams();
        if (null != layoutParams) {
            layoutParams.width = viewWidth;
            layoutParams.height = viewHeight;
        } else {
            layoutParams = new ViewGroup.LayoutParams(viewWidth, viewHeight);
        }
        super.setLayoutParams(layoutParams);
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
    }
}
