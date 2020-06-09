package com.glide

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.glide.sample.SampleRecyclerActivity
import com.glide.sample.SampleSingleActivity
import com.glide.sdk.ImageLoader
import com.glide.sdk.config.CustomGlideModule
import com.yline.test.BaseTestActivity
import com.yline.utils.FileSizeUtil
import java.util.concurrent.Executors

class MainActivity : BaseTestActivity() {
    private lateinit var sizeTextView: TextView

    private lateinit var stateTextView: TextView

    @SuppressLint("SetTextI18n")
    override fun testStart(
        view: View?,
        savedInstanceState: Bundle?
    ) {
        addButton("Glide SampleSingle") {
            SampleSingleActivity.launch(this)
        }

        addButton("Glide SampleRecycler") {
            SampleRecyclerActivity.launch(this)
        }


        sizeTextView = addTextView("缓存大小")
        addButton("查看磁盘大小") {
            val filesDir = CustomGlideModule.getCacheFile(this)
            val size = FileSizeUtil.getDirSize(filesDir)

            val sizeMb = size / 1024 / 1024
            sizeTextView.text = "$sizeMb Mb"
        }

        addButton("内存 清除") {
            ImageLoader.clearMemoryCache(this)
        }

        stateTextView = addTextView("缓存清理 进度")
        addButton("磁盘 清除") {
            Executors.newSingleThreadExecutor()
                    .submit {
                        val result = ImageLoader.clearDiskCache(this)

                        this.runOnUiThread {
                            stateTextView.text = if (result) "清理成功" else "清理失败"
                        }
                    }
        }
    }

}