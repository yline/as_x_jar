package net.sourceforge.simcpux.yline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;
import com.yline.utils.FileUtil;

public class ShareActivity extends BaseTestActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, ShareActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private static final String WEB_URL = "https://www.baidu.com/";
    private static final String TITLE = "五毛钱特效";
    private static final String DESCRIPTION = "道长：芸娘，月白要生了，今年我们就不能来 　　了。 　　毒萝：芸娘，五哥他……我想去他墓前，守着 　　他。 　　军爷：明年开始我就要调往边关了，怕是也不 　　能常来了。 　　喵姐：这一个个的混蛋，亏我还从大漠赶来， 　　他们不来，我们俩喝！";


    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("分享网页[带图] - 朋友", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareWechat.shareWebpage2Friend(ShareActivity.this,
                        WEB_URL, TITLE, DESCRIPTION, "默认图片哦");
            }
        });
        addButton("分享网页[不带图] - 朋友", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareWechat.shareWebpage2Friend(ShareActivity.this,
                        WEB_URL, TITLE, DESCRIPTION, null);
            }
        });
        addButton("分享网页[带图] - 朋友圈", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareWechat.shareWebpage2Circle(ShareActivity.this,
                        WEB_URL, TITLE, DESCRIPTION, "默认图片哦");
            }
        });

        addButton("分享图片 a - 朋友", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareWechat.shareImage2Friend(ShareActivity.this, FileUtil.getPathTop() + "a.jpg");
            }
        });
        addButton("分享图片 b - 朋友", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareWechat.shareImage2Friend(ShareActivity.this, FileUtil.getPathTop() + "b.jpg");
            }
        });
        addButton("分享图片 a - 朋友圈", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareWechat.shareImage2Circle(ShareActivity.this, FileUtil.getPathTop() + "a.jpg");
            }
        });
    }
}
