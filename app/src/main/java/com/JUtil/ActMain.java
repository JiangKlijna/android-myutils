package com.JUtil;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.jiangKlijna.object.ObjectKey;

/**
 * Created by jiangKlijna on 16-4-11.
 */
public class ActMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("jiangKlijna");
        tv.setTextSize(50);
        setContentView(tv);
        ObjectKey.test();
    }

}
