package com.rebeau.technology.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.fragment.BaseLazyLoadFragment;
import com.rebeau.technology.R;
import com.rebeau.technology.android.yuanli.KaifaGaoshoukeActivity;
import com.rebeau.views.loading.RBLoadStatusView;

import butterknife.ButterKnife;
import butterknife.OnClick;

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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.android_fragment_two, container, false);
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


    @OnClick(R.id.show_dialog0)
    public void button0(){
        startActivity(new Intent(mActivity, KaifaGaoshoukeActivity.class));
    }


    @OnClick(R.id.show_dialog8)
    public void startApp8(){
//        https://github.com/android-notes/Cockroach
    }

    @OnClick(R.id.show_dialog9)
    public void startApp(){

        startActivity(new Intent(mActivity, ErrorActivity.class));
    }
}
