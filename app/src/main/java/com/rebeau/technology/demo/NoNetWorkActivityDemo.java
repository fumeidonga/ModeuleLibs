package com.rebeau.technology.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.rebeau.commons.activity.BaseFragmentActivity;
import com.rebeau.technology.R;
import com.rebeau.views.loading.RBLoadStatusView;

import butterknife.ButterKnife;

public class NoNetWorkActivityDemo extends BaseFragmentActivity {

    @Override
    protected View createSuccessView() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_no_net_work_demo, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected String getTitleBarName() {
        return "没有网络";
    }

    @Override
    protected void onLoadData() {

        notifyLoadStatus(RBLoadStatusView.LOAD_ERROR_NO_NETWORK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_net_work_demo);


        // 设置点击事件
        setEmptyViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyLoadStatus(RBLoadStatusView.LOADING);
                //onLoadData();
            }
        });
    }
}
