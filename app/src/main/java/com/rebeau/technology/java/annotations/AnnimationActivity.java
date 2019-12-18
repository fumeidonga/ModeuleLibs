package com.rebeau.technology.java.annotations;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.activity.BaseFragmentActivity;
import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;
import com.rebeau.technology.demo.TestSerializable;
import com.rebeau.views.loading.RBLoadStatusView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnnimationActivity extends BaseFragmentActivity {

    @BindView(R.id.array_map)
    public Button array_map;
    @BindView(R.id.button17)
    public Button button17;
    @BindView(R.id.button18)
    public Button button18;
    @BindView(R.id.button19)
    public Button button19;
    @BindView(R.id.button20)
    public Button button20;
    @BindView(R.id.button21)
    public Button button21;

    @Override
    protected View createSuccessView() {
        RBLogUtil.dt();
        View view = LayoutInflater.from(this).inflate(R.layout.activity_error, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RBLogUtil.dt();
    }

    @Override
    protected void onResume() {
        super.onResume();

        RBLogUtil.dt();
    }

    @Override
    protected void onPause() {
        super.onPause();
        RBLogUtil.dt();
    }

    @Override
    protected void onStop() {
        super.onStop();
        RBLogUtil.dt();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RBLogUtil.dt();
    }

    private void initView() {
        array_map.setText("read me");
        button17.setText("");
        button18.setText("");
        button19.setText("");
        button20.setText("");
        button21.setText("");
    }

    @Override
    protected void onLoadData() {
        notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
    }

    @Override
    protected String getTitleBarName() {
        return "注解";
    }

    @OnClick(R.id.array_map)
    public void array_map(){
        MarkdownUtils.setData(this, "java/注解.md");
    }

    @OnClick(R.id.button17)
    public void button17(){
    }

    @OnClick(R.id.button18)
    public void array_map18(){

    }

    @OnClick(R.id.button19)
    public void array_map19(){
        MarkdownUtils.setData(this, "android/问题集锦.md");
    }

    @OnClick(R.id.button20)
    public void array_map20(){

    }
}
