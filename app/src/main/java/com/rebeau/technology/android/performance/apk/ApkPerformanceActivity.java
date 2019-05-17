package com.rebeau.technology.android.performance.apk;

import android.app.Activity;
import android.os.Bundle;

import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApkPerformanceActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apk_performance);
        mActivity = this;
        ButterKnife.bind(this);
    }

    Activity mActivity;
    @OnClick(R.id.show_dialog)
    public void button(){
        MarkdownUtils.setData(mActivity, "android/performace/apk_performance_readme.MD");
    }

    @OnClick(R.id.android_performace_tools)
    public void android_profile_cpu(){

    }


    @OnClick(R.id.show_dialog0)
    public void button0(){
        //startActivity(new Intent(mActivity, LayoutActivity.class));
    }

    @OnClick(R.id.show_dialog1)
    public void button1(){

    }
}
