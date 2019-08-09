package com.rebeau.technology.android;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.activity.BaseFragmentActivity;
import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;
import com.rebeau.technology.demo.AppAboutFragment;
import com.rebeau.technology.demo.TestSerializable;
import com.rebeau.views.loading.RBLoadStatusView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ErrorActivity extends BaseFragmentActivity {

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
    }

    @Override
    protected void onLoadData() {
        notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
    }

    @Override
    protected String getTitleBarName() {
        return "开发奇淫技巧";
    }

    @OnClick(R.id.array_map)
    public void array_map(){

        MarkdownUtils.setData(this, "android/two/array_map_error.md");
    }

    @OnClick(R.id.button17)
    public void button17(){
        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("com.chaozh.iReaderFree", "com.chaozh.iReader.ui.activity.WelcomeActivity"));
//        intent.setComponent(new ComponentName("com.kmxs.reader", "com.km.app.home.view.LoadingActivity"));
//        intent.setComponent(new ComponentName("com.sina.weibo", "com.sina.weibo.MainTabActivity"));
        intent.setComponent(new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.SplashActivity"));
        intent.putExtra("anykey", new TestSerializable());
        startActivity(intent);

        /*Intent intent=new Intent();
        //包名 包名+类名（全路径）
        intent.setClassName("com.kmxs.reader", "com.km.app.home.view.LoadingActivity");
        startActivity(intent);*/
    }

    @OnClick(R.id.button18)
    public void array_map18(){
        MarkdownUtils.setData(this, "android/thread/定时任务.md");

    }

    @OnClick(R.id.button19)
    public void array_map19(){
        MarkdownUtils.setData(this, "android/问题集锦.md");
    }

    @OnClick(R.id.button20)
    public void array_map20(){

    }
}
