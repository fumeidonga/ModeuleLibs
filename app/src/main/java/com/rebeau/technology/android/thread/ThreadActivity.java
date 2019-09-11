package com.rebeau.technology.android.thread;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;
import com.rebeau.views.viewgroup.AdLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThreadActivity extends Activity {

    @BindView(R.id.layout_frame)
    AdLayout adLayout;

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
        MarkdownUtils.setData(this, "android/thread/线程进程.MD");

    }


    @OnClick(R.id.show_dialog10)
    public void startApp10(){
        Button button = new Button(mActivity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RBLogUtil.dt();
            }
        });
        button.setText("click");
        adLayout.addView(button);
    }



}
