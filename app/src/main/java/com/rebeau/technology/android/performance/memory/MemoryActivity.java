package com.rebeau.technology.android.performance.memory;

import android.app.Activity;
import android.os.Bundle;

import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;

public class MemoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        MarkdownUtils.setData(this, "android/performace/内存优化.MD");
    }
}
