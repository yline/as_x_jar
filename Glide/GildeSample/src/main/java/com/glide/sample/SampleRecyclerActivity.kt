package com.glide.sample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.glide.R
import com.glide.sdk.ImageLoader
import com.yline.base.BaseAppCompatActivity
import com.yline.test.UrlConstant
import com.yline.view.recycler.adapter.AbstractRecyclerAdapter
import com.yline.view.recycler.holder.RecyclerViewHolder
import kotlinx.android.synthetic.main.activity_sample_recycler.sample_recycler

/**
 * created on 2020-06-08 -- 20:52
 * @author yline
 */
class SampleRecyclerActivity : BaseAppCompatActivity() {
    companion object {
        fun launch(context: Context?) {
            if (null == context) {
                return
            }

            val intent = Intent()
            intent.setClass(context, SampleRecyclerActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_recycler)

        val recyclerAdapter = SampleRecyclerAdapter()
        sample_recycler.layoutManager = GridLayoutManager(this, 2)
        sample_recycler.adapter = recyclerAdapter

        val dataList = ArrayList<String>()
        for (i in 0..99) {
            dataList.add(UrlConstant.getUrl())
        }

        recyclerAdapter.setDataList(dataList, true)
    }

    inner class SampleRecyclerAdapter : AbstractRecyclerAdapter<String>() {
        override fun getItemRes(): Int {
            return R.layout.item_sample_recycler
        }

        override fun onBindViewHolder(
            holder: RecyclerViewHolder,
            position: Int
        ) {
            val url = get(position)

            val targetView = holder.get<ImageView>(R.id.item_sample_recycler_img)
            ImageLoader.displayImage(targetView, url)

            holder.setText(R.id.item_sample_recycler_text, url)
        }

    }
}