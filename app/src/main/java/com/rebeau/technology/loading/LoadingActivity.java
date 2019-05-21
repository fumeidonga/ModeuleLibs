package com.rebeau.technology.loading;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.base.utils.RBScreenBangsAdaptationUtil;
import com.rebeau.commons.activity.BaseFragmentActivity;
import com.rebeau.permission.PermissionsManager;
import com.rebeau.permission.PermissionsTipsDialog;
import com.rebeau.technology.MainActivity;
import com.rebeau.technology.R;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 *
 * vivo 手机的全屏需要手动在设置中打开，上vivo市场的应用会自动打开
 *
 */
public class LoadingActivity extends BaseFragmentActivity implements PermissionsManager.PermissionListener  {

    LoadHandler mHandler;
    private boolean isPermissionError = false;
    private List<String> permissions;

    public static class LoadHandler extends Handler {

        WeakReference <LoadingActivity> reference;

        public LoadHandler(LoadingActivity reference) {
            this.reference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                if (reference.get() != null) {
                    super.handleMessage(msg);
                    reference.get().showRationaleDialog();
                }
            } else {
                if (reference.get() != null) {
                    reference.get().startActivity(new Intent(reference.get(), MainActivity.class));
                    reference.get().finish();
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        if(!hasPermissions()) {
            PermissionsManager.requestPermissions(this, this, PermissionsManager.READ_PHONE_STATE, PermissionsManager.WRITE_EXTERNAL_STORAGE,
                    PermissionsManager.READ_EXTERNAL_STORAGE);
        } else {
            openApp();
        }

        /**
         * 显示刘海屏
         */
        RBScreenBangsAdaptationUtil.displayScreenBang(this);
    }

    @Override
    protected View createSuccessView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_loading, null);
        //ButterKnife.bind(this, view);
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
    }


    /**
     * 必须权限
     * @return
     */
    private boolean hasPermissions(){
        return PermissionsManager.hasPermissions(this, PermissionsManager.READ_PHONE_STATE, PermissionsManager.WRITE_EXTERNAL_STORAGE,
                PermissionsManager.READ_EXTERNAL_STORAGE);
    }

    /**
     * 权限申请成功
     * @param permissions
     */
    @Override
    public void onPermissionsGranted(List<String> permissions) {
        RBLogUtil.dt();
        isPermissionError = false;
        openApp();
    }

    /**
     * 用户拒绝权限
     * @param permissions
     */
    @Override
    public void onPermissionsDenied(List<String> permissions) {
        RBLogUtil.dt();
        this.permissions = permissions;
        isPermissionError = false;
        mHandler.sendEmptyMessageDelayed(0, 100);
    }

    /**
     * 拒绝权限，不再提示
     * @param permissions
     */
    @Override
    public void onPermissionsError(List<String> permissions) {
        RBLogUtil.dt();
        isPermissionError = true;
    }

    /**
     * 提示对话框
     */
    public void showRationaleDialog() {
        final List<String> permission = permissions;
        String message = PermissionsManager.getDialogTips(this, permission);
        // 显示弹窗.
        PermissionsManager.DialogType dialogType = null;
        RBLogUtil.d("isPermissionError = " + isPermissionError);
        if(isPermissionError) {
            dialogType = new PermissionsManager.DialogType(-1, message, "设置", false, true);
        } else {
            dialogType = new PermissionsManager.DialogType(-1, message, "确定", false, true);
        }
        if (isFinishing() || isDestroyed()) {
            return;
        }

        new PermissionsTipsDialog.DialogBuilder(LoadingActivity.this)
                .dataBean(dialogType)
                .positiveClickListener(new PermissionsTipsDialog.IDialogClickListener() {
                    @Override
                    public void onClick() {
                        //submit
                        RBLogUtil.d("ok");
                        if(isPermissionError) {
                            PermissionsManager.setPermission(new PermissionsManager.PermissionSettingListener() {
                                @Override
                                public void settingsBack(int type) {
                                    if(!hasPermissions()) {
                                        showRationaleDialog();
                                    } else {
                                        isPermissionError = false;
                                        openApp();
                                    }
                                }

                                @Override
                                public void cancelBtnClick() {
                                }
                            }, LoadingActivity.this);
                        } else {
                            PermissionsManager.requestPermissions(LoadingActivity.this, LoadingActivity.this, PermissionsManager.READ_PHONE_STATE, PermissionsManager.WRITE_EXTERNAL_STORAGE,
                                    PermissionsManager.READ_EXTERNAL_STORAGE);
                        }
                    }
                })
                .negativeClickListener(new PermissionsTipsDialog.IDialogClickListener() {
                    @Override
                    public void onClick() {
                        //cancel
                    }
                })
                .build()
                .show();
    }

    private void openApp() {
        mHandler.sendEmptyMessageDelayed(1, 1500);
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
    protected boolean isActivityLoadingEnable() {
        return false;
    }

    @Override
    protected boolean isSlidingPaneBackEnable() {
        return false;
    }
}
