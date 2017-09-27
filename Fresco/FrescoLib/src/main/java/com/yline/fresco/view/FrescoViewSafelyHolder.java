package com.yline.fresco.view;

import android.net.Uri;
import android.text.TextUtils;
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

    public void setImageUri(String uriString) {
        if (TextUtils.isEmpty(uriString)){
            return;
        }

        Uri imageUri = Uri.parse(uriString);
        super.setImageUri(imageUri);
    }

    public void setImageUriLower(String uriLowerString) {
        if (TextUtils.isEmpty(uriLowerString)){
            return;
        }

        Uri imageUriLower = Uri.parse(uriLowerString);
        super.setImageUriLower(imageUriLower);
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
}
