package com.rebeau.technology.android.thread;

import android.app.Activity;
import android.os.Bundle;

import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThreadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        mActivity = this;
        ButterKnife.bind(this);
    }

    Activity mActivity;
    @OnClick(R.id.readme)
    public void readme(){
        MarkdownUtils.setData(mActivity, "android/thread/thread.md");
    }

    @OnClick(R.id.android_performace_tools)
    public void android_profile_cpu(){
        MarkdownUtils.setData(this, "android/thread/线程.MD");

    }




}
