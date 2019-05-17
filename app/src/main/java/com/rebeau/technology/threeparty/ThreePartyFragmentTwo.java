package com.rebeau.technology.threeparty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.fragment.BaseLazyLoadFragment;
import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;
import com.rebeau.views.loading.RBLoadStatusView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: david
 * date: 2019/4/30
 * description: ${Desc} .
 */
public class ThreePartyFragmentTwo extends BaseLazyLoadFragment {

    public static ThreePartyFragmentTwo newInstance() {
        Bundle args = new Bundle();
        ThreePartyFragmentTwo fragment = new ThreePartyFragmentTwo();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View createSuccessView(@Nullable ViewGroup container) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.three_party_fragment_two, container, false);
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

    @OnClick(R.id.badtoke)
    public void badtoke(){
        MarkdownUtils.setData(mActivity, "android/bug/badtoken.md");
    }

    @OnClick(R.id.timeout)
    public void timeout(){
        MarkdownUtils.setData(mActivity, "android/bug/timeout.md");

    }
}
