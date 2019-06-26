package com.rebeau.technology.android.jichu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.rebeau.commons.activity.BaseFragmentActivity;
import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;
import com.rebeau.views.loading.RBLoadStatusView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConstraintLayoutActivity extends BaseFragmentActivity {

    @Override
    protected View createSuccessView() {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_constraint_layout, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected String getTitleBarName() {
        return "ConstraintLayout";
    }

    @Override
    protected void onLoadData() {

        notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
    }

    @OnClick(R.id.bt_4)
    public void bt_4(){
        MarkdownUtils.setData(mActivity, "android/yuanli/constraintlayout.md");
    }
}
