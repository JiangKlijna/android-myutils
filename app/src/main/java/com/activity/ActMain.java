package com.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.jiangKlijna.XAdapter;

/**
 * Created by jiangKlijna on 16-4-11.
 */
public class ActMain extends Activity implements AdapterView.OnItemClickListener {

    private ListView lv;
    private XAdapter<String> adapter;
    private static final String[] title = new String[]{"XAdapter", "ObjectKey", "CrashHandler", "dlksajlkjdl", "android-myutil"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(lv = new ListView(this));
        init();
    }

    private void init() {
        adapter = new XAdapter<String>(this) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = XAdapter.getHolder(getContext(), convertView, TextView.class);
//                XAdapter.ViewHolder holder = XAdapter.getHolder(getContext(), convertView, parent, R.layout.text_item, position);
                TextView tv = holder.getConvertView();
                tv.setText(getItem(position));
                tv.setPadding(20, 20, 20, 20);
                tv.setTextColor(Color.rgb(0xab, 0xcd, 0xef));
                tv.setTextSize(20);
                return tv;
            }
        };
        lv.setAdapter(adapter);
        adapter.setData(title);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, adapter.getItem(i), Toast.LENGTH_SHORT).show();
    }
}
