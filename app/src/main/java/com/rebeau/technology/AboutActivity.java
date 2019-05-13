package com.rebeau.technology;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

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
        View view = LayoutInflater.from(this).inflate(R.layout.activity_about, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
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