package com.rebeau.technology.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.rebeau.commons.activity.BaseFragmentActivity;
import com.rebeau.technology.R;
import com.rebeau.views.loading.RBLoadStatusView;

import butterknife.ButterKnife;

public class EmptyActivityDemo extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View createSuccessView() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_empty_demo, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected String getTitleBarName() {
        return "没有数据 demo";
    }

    @Override
    protected void onLoadData() {

        notifyLoadStatus(RBLoadStatusView.LOAD_ERROR_DEFAULT);
    }
}
