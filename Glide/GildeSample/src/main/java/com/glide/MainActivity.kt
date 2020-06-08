package com.glide

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.glide.sample.SampleActivity
import com.yline.test.BaseTestActivity
import com.yline.utils.FileSizeUtil

class MainActivity : BaseTestActivity() {
    private var sizeTextView: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun testStart(
        view: View?,
        savedInstanceState: Bundle?
    ) {
        // Glide.init(this, GlideBuilder())

        addButton("Glide Sample") {
            SampleActivity.launch(this)
        }

        sizeTextView = addTextView("缓存大小")
        addButton("查看磁盘大小") {
            val filesDir = getExternalFilesDir("GlidePicture")
            val size = FileSizeUtil.getDirSize(filesDir)

            val sizeMb = size / 1024 / 1024
            sizeTextView!!.text = "$sizeMb Mb"
        }

        addButton("内存 清除") {
            Glide.get(this).clearMemory()
        }

        addButton("磁盘 清除") {
            Glide.get(this).clearDiskCache()
        }
    }

}