package com.rebeau.technology.threeparty;

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
import butterknife.OnClick;

/**
 * author: david
 * date: 2019/4/30
 * description: ${Desc} .
 */
public class ThreePartyFragmentOne extends BaseLazyLoadFragment {

    public static ThreePartyFragmentOne newInstance() {
        Bundle args = new Bundle();
        ThreePartyFragmentOne fragment = new ThreePartyFragmentOne();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View createSuccessView(@Nullable ViewGroup container) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.three_party_fragment_one, container, false);
        ButterKnife.bind(this, view);
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

    @OnClick(R.id.leakcanary)
    public void leakcanary(){
        //MarkdownUtils.setData(mActivity, "android/performace/view的绘制原理.MD");
    }

    @OnClick(R.id.lint)
    public void lint(){
    }

    @OnClick(R.id.show_dialog)
    public void okhttp(){
    }

    @OnClick(R.id.android_performace_tools)
    public void daggers(){
    }

    @OnClick(R.id.show_dialog0)
    public void retrofits(){
    }

    @OnClick(R.id.show_dialog1)
    public void glide(){
    }

    @OnClick(R.id.show_dialog2)
    public void fresco(){
    }

    @OnClick(R.id.show_dialog3)
    public void butterknifes(){
    }
}
