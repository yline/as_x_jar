package com.glide.sample

import android.os.Bundle
import com.glide.R
import com.yline.base.BaseAppCompatActivity
import android.content.Intent
import android.app.Activity
import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.yline.test.UrlConstant
import com.yline.view.recycler.adapter.AbstractRecyclerAdapter
import com.yline.view.recycler.holder.RecyclerViewHolder
import kotlinx.android.synthetic.main.activity_sample.sample_recycler

/**
 * created on 2020-06-08 -- 20:52
 * @author yline
 */
class SampleActivity : BaseAppCompatActivity() {
    companion object {
        fun launch(context: Context?) {
            if (null == context) {
                return
            }

            val intent = Intent()
            intent.setClass(context, SampleActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

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
            return R.layout.item_sample
        }

        override fun onBindViewHolder(
            holder: RecyclerViewHolder,
            position: Int
        ) {
            val url = get(position)

            val targetView = holder.get<ImageView>(R.id.item_sample_img)
            Glide.with(targetView.context)
                    .load(url)
                    .into(targetView)

            holder.setText(R.id.item_sample_text, url)
        }

    }
}