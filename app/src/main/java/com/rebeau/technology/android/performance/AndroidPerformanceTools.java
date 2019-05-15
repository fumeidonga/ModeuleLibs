package com.rebeau.technology.android.performance;

import android.app.Activity;
import android.os.Bundle;

import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AndroidPerformanceTools extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_performance_tools);
        ButterKnife.bind(this);
    }



    @OnClick(R.id.show_dialog)
    public void show_dialog(){

    }



    @OnClick(R.id.android_performace_tools)
    public void android_profile_cpu(){
        MarkdownUtils.setData(this, "android/performace/android_prifile_cup.MD");
    }



    @OnClick(R.id.show_dialog0)
    public void systrace(){
        MarkdownUtils.setData(this, "android/performace/systrace.MD");
    }
}
