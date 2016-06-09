package com.JUtil.customview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiangKlijna.adapter.XAdapter;


/**
 * Created by leil7 on 2016/6/8.
 */
public class ActCustomView extends Activity {

    private static final String[] ss = new String[]{"Bundle", "View", "ViewGroup", "PLAListView", "BaseActivity", "R", "ContentView", "ViewInject", "ViewGroup", "ListView", "TextView", "Toast", "mSwipeRefreshLayout"};

    private MyListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(listView = new MyListView(this));
        XAdapter<String> adapter = new XAdapter<String>(ss, getApplicationContext()) {
            @Override
            protected View initData(int position, View convertView, ViewGroup parent) {
                XAdapter.ViewHolder holder = XAdapter.getHolder(getContext(), convertView, TextView.class, position);
                TextView tv = (TextView) (convertView = holder.getConvertView());
                tv.setText(getItem(position));
                tv.setTextColor(Color.BLUE);
                tv.setTextSize(50f);
                return convertView;
            }
        };
        listView.setAdapter(adapter);
    }
}
