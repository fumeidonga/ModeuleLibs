package com.rebeau.technology.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.fragment.BaseAppFragment;
import com.rebeau.technology.R;
import com.rebeau.technology.android.rxjava.RxjavaActivity;
import com.rebeau.views.loading.RBLoadStatusView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: david
 * date: 2019/4/30
 * description: ${Desc} .
 */
public class AppAboutFragment extends BaseAppFragment {

    public static AppAboutFragment newInstance() {
        Bundle args = new Bundle();
        AppAboutFragment fragment = new AppAboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AppAboutFragment newInstance(ArrayList<Object> obj, String type) {
        Bundle args = new Bundle();
        args.putSerializable("obj", obj);
        args.putString("type", type);
        AppAboutFragment fragment = new AppAboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            //ddd = getArguments().getString("", "");

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        RBLogUtil.dt();
    }

    @Override
    public void onStop() {
        super.onStop();
        RBLogUtil.dt();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RBLogUtil.dt();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RBLogUtil.dt();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        RBLogUtil.dt(hidden);
    }

    @Override
    protected View createSuccessView(@Nullable ViewGroup container) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_about_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onLoadData() {
        notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
    }

    @OnClick(R.id.iv_app_about_logo)
    public void go(){
        startActivity(new Intent(mActivity, RxjavaActivity.class));
    }
}
