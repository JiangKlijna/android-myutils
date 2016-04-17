package com.JUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jiangKlijna.adapter.XAdapter;

/**
 * Created by jiangKlijna on 16-4-11.
 */
public class ActMain extends Activity implements AdapterView.OnItemClickListener {

    private ListView lv;
    private XAdapter<String> adapter;
    private static final String[] title = new String[]{"XAdapter", "ObjectKey"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(lv = new ListView(this));
        init();
    }

    private void init() {
        adapter = new XAdapter<String>(this) {
            @Override
            protected View initData(int position, View convertView, ViewGroup parent) {
                XAdapter.ViewHolder holder = XAdapter.getHolder(getContext(), convertView, TextView.class, position);
//                XAdapter.ViewHolder holder = XAdapter.getHolder(getContext(), convertView, parent, R.layout.text_item, position);
                convertView = holder.getConvertView();
                ((TextView) convertView).setText(title[position]);
                return convertView;
            }
        };
        lv.setAdapter(adapter);
        adapter.setArray(title);
        lv.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }
}
