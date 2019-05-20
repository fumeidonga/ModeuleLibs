package com.rebeau.technology.android.performance.memory;

import android.app.Activity;
import android.os.Bundle;

import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MemoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        mActivity = this;
        ButterKnife.bind(this);
    }

    Activity mActivity;
    @OnClick(R.id.readme)
    public void readme(){
        MarkdownUtils.setData(mActivity, "android/performace/ram/ram_performance_readme.MD");
    }

    @OnClick(R.id.android_performace_tools)
    public void android_profile_cpu(){
        MarkdownUtils.setData(this, "android/performace/ram/内存优化.MD");

    }


    @OnClick(R.id.show_dialog0)
    public void button0(){
        //startActivity(new Intent(mActivity, LayoutActivity.class));
    }

    @OnClick(R.id.show_dialog1)
    public void button1(){

    }
}
