package com.rebeau.technology.threeparty;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.fragment.BaseAppFragment;
import com.rebeau.commons.fragment.BaseLazyLoadFragment;
import com.rebeau.technology.R;
import com.rebeau.views.loading.RBLoadStatusView;

import java.util.ArrayList;

import butterknife.ButterKnife;

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

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
        }
    };

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
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }
}
