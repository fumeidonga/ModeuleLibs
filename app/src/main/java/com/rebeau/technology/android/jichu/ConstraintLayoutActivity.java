package com.rebeau.technology.android.jichu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.rebeau.commons.activity.BaseFragmentActivity;
import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;

import butterknife.ButterKnife;

public class ConstraintLayoutActivity extends BaseFragmentActivity {

    @Override
    protected View createSuccessView() {

        MarkdownUtils.setData(mActivity, "android/constraintlayout.md");
        View view = LayoutInflater.from(getActivity()).inflate(com.rebeau.commons.R.layout.activity_constraint_layout, null);
        ButterKnife.bind(this, view);
        return null;
    }

    @Override
    protected String getTitleBarName() {
        return null;
    }

    @Override
    protected void onLoadData() {

    }
}
