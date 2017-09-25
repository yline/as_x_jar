package com.yline.fresco.view;

import android.net.Uri;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * 特定用来配置 Fresco参数
 *
 * @author yline 2017/9/23 -- 17:41
 * @version 1.0.0
 */
class FrescoViewHolder {
    private ViewGroup.LayoutParams layoutParams;

    private Uri imageUri; // 网络链接

    protected FrescoView frescoView;

    public FrescoViewHolder(FrescoView view) {
        assert null != view;
        this.frescoView = view;
    }

    public void build() {
        if (null == imageUri) {
            return;
        }

        if (null != layoutParams) {
            frescoView.setLayoutParams(layoutParams);
        }

        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(imageUri);
        ImageRequest imageRequest = imageRequestBuilder.build();

        PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
        controllerBuilder.setOldController(frescoView.getController());
        controllerBuilder.setImageRequest(imageRequest);
        DraweeController controller = controllerBuilder.build();

        // GenericDraweeHierarchy genericDraweeHierarchy = frescoView.getHierarchy();

        frescoView.setController(controller);
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }
}
