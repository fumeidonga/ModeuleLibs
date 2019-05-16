package com.rebeau.technology.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rebeau.commons.fragment.BaseAppFragment;
import com.rebeau.technology.R;
import com.rebeau.views.loading.RBLoadStatusView;

import java.util.ArrayList;

import butterknife.ButterKnife;

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
    protected View createSuccessView(@Nullable ViewGroup container) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_about_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onLoadData() {
        notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
    }
}
