package com.rebeau.technology.loading;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.rebeau.commons.activity.BaseFragmentActivity;
import com.rebeau.technology.MainActivity;
import com.rebeau.technology.R;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

public class LoadingActivity extends BaseFragmentActivity {

    LoadHandler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }


    /**
     * no need to init substatusbar
     */
    @Override
    protected void initSubStatusBar() {

    }

    public boolean isFromBackToFront(){
        return false;
    }

    @Override
    protected boolean isShowAdLoading(){
        return false;
    }

    @Override
    protected boolean isActivityTitleBarEnable() {
        return false;
    }

    @Override
    protected boolean isNeedLoadCreateView() {
        return false;
    }

    @Override
    protected View createSuccessView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_loading, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected String getTitleBarName() {
        return null;
    }

    @Override
    protected void onLoadData() {

        if(mHandler == null) {
            mHandler = new LoadHandler(this);
        }
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    public static class LoadHandler extends Handler {

        WeakReference <LoadingActivity> reference;

        public LoadHandler(LoadingActivity reference) {
            this.reference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            reference.get().startActivity(new Intent(reference.get(), MainActivity.class));
            reference.get().finish();
        }
    }
}
