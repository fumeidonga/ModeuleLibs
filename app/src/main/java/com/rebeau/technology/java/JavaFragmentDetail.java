package com.rebeau.technology.java;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.fragment.BaseLazyLoadFragment;
import com.rebeau.technology.R;
import com.rebeau.views.loading.RBLoadStatusView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: david
 * date: 2019/4/30
 * description: ${Desc} .
 */
public class JavaFragmentDetail extends BaseLazyLoadFragment {

    public static JavaFragmentDetail newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        JavaFragmentDetail fragment = new JavaFragmentDetail();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.java_fragment_one_textview)
    TextView mTextView;

    private String mType;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RBLogUtil.dt();
            notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
            mTextView.setText(mType);
        }
    };

    @Override
    protected View createSuccessView(@Nullable ViewGroup container) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.java_fragment_two, container, false);
        ButterKnife.bind(this, view);

        RBLogUtil.dt();
        if (getArguments() != null) {
            mType = getArguments().getString("type");
        }
        return view;
    }


    @Override
    protected boolean haveLazyData() {
        RBLogUtil.dt();
        return isLazyLoad;
    }

    @Override
    protected void onLoadData() {
        RBLogUtil.dt();
        isLazyLoad = true;
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }
}
