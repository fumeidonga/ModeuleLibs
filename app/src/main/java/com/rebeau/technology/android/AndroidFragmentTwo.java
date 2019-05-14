package com.rebeau.technology.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.fragment.BaseLazyLoadFragment;
import com.rebeau.technology.R;
import com.rebeau.views.loading.RBLoadStatusView;

import butterknife.ButterKnife;

/**
 * author: david
 * date: 2019/4/30
 * description: ${Desc} .
 */
public class AndroidFragmentTwo extends BaseLazyLoadFragment {

    public static AndroidFragmentTwo newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        AndroidFragmentTwo fragment = new AndroidFragmentTwo();
        fragment.setArguments(args);
        RBLogUtil.dt();
        return fragment;
    }

    @Override
    protected View createSuccessView(@Nullable ViewGroup container) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.android_fragment_one, container, false);
        ButterKnife.bind(this, view);

        RBLogUtil.dt();
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
        notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
    }
}
