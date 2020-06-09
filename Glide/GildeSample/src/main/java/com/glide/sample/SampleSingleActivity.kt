package com.glide.sample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.glide.R
import com.glide.sdk.ImageLoader
import com.yline.base.BaseAppCompatActivity
import com.yline.test.UrlConstant
import kotlinx.android.synthetic.main.activity_sample_single.sample_single_clear
import kotlinx.android.synthetic.main.activity_sample_single.sample_single_img
import kotlinx.android.synthetic.main.activity_sample_single.sample_single_load
import kotlinx.android.synthetic.main.activity_sample_single.sample_single_occupy
import kotlinx.android.synthetic.main.activity_sample_single.sample_single_text

/**
 * created on 2020-06-09 -- 11:33
 * @author yline
 */
class SampleSingleActivity : BaseAppCompatActivity() {
    companion object {
        fun launch(context: Context?) {
            if (null == context) {
                return
            }

            val intent = Intent()
            intent.setClass(context, SampleSingleActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_single)

        initView()
    }

    private fun initView() {
        pickOne()

        initViewClick()
    }

    private fun initViewClick() {
        // 加载
        sample_single_load.setOnClickListener {
            pickOne()
        }

        // 加载，使用占位图
        sample_single_occupy.setOnClickListener {
            val url = UrlConstant.getUrl()
            sample_single_text.text = url

            val placeHolder = ColorDrawable(Color.BLUE)
            val error = ColorDrawable(Color.RED)
            val fallback = ColorDrawable(Color.GRAY)
            ImageLoader.displayImage(sample_single_img, url, placeHolder, error, fallback)
        }

        // 清除
        sample_single_clear.setOnClickListener {
            ImageLoader.clearSingleImage(sample_single_img)
        }
    }

    private fun pickOne() {
        val url = UrlConstant.getUrl()
        sample_single_text.text = url

        ImageLoader.displayImage(sample_single_img, url)
    }
}