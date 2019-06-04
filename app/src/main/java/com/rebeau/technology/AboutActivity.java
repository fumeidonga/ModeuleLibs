package com.rebeau.technology;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.activity.BaseFragmentActivity;
import com.rebeau.technology.demo.AppAboutFragment;
import com.rebeau.views.loading.RBLoadStatusView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * author: david
 * date: 2019/4/29
 * description: ${Desc} .
 */
public class AboutActivity extends BaseFragmentActivity {

    @BindView(R.id.about_activity_main_view)
    FrameLayout mFrameLayout;

    @Override
    protected View createSuccessView() {
        RBLogUtil.dt();
        View view = LayoutInflater.from(this).inflate(R.layout.activity_about, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RBLogUtil.dt();
    }

    @Override
    protected void onResume() {
        super.onResume();

        RBLogUtil.dt();
    }

    @Override
    protected void onPause() {
        super.onPause();
        RBLogUtil.dt();
    }

    @Override
    protected void onStop() {
        super.onStop();
        RBLogUtil.dt();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RBLogUtil.dt();
    }

    private void initView() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.about_activity_main_view, AppAboutFragment.newInstance(), "aboutactivity")
                .commitAllowingStateLoss();
    }

    @Override
    protected String getTitleBarName() {
        return "关于我们";
    }

    @Override
    protected void onLoadData() {
        notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
    }
}