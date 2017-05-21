package com.jiangKlijna.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.jiangKlijna.R
import com.jiangKlijna.kotlin.XAdapter

/**
 * Created by jiangKlijna on 16-4-11.
 */
class ActKotlin : Activity(), AdapterView.OnItemClickListener {

    companion object {
        private val arr: Array<String> = arrayOf("XAdapter", "ObjectKey", "CrashHandler", "dlksajlkjdl", "android-myutil")
    }

    private var lv: ListView? = null
    private var adapter: XAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)
        init()
    }

    private fun init() {
        (findViewById(R.id.main_btn) as Button).let {
            it.text = "kotlin to java"
            it.setOnClickListener {
                finish()
                startActivity(Intent(this@ActKotlin, ActJava::class.java))
            }
        }
        adapter = object : XAdapter<String>(this@ActKotlin) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val holder = XAdapter.getHolder(context, convertView, TextView::class.java)
                //                XAdapter.ViewHolder holder = XAdapter.getHolder(getContext(), convertView, parent, R.layout.text_item, position);
                val tv = holder.getConvertView<TextView>()
                tv.text = getItem(position)
                tv.setPadding(20, 20, 20, 20)
                tv.setTextColor(Color.rgb(0xab, 0xcd, 0xef))
                tv.textSize = 20f
                return tv
            }
        }
        (findViewById(R.id.main_lv) as ListView).let {
            lv = it
            it.adapter = adapter
            adapter!!.setData(arr)
            it.onItemClickListener = this@ActKotlin
        }

    }

    override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
        Toast.makeText(this, adapter!!.getItem(i), Toast.LENGTH_SHORT).show()
    }

}
