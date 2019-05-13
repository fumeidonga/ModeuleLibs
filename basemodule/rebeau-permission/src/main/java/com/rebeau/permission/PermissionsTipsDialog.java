package com.rebeau.permission;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
/**
 * 权限提示对话框
 */
public class PermissionsTipsDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    public View mViewShade;
    public View submit_line;
    public TextView tips;
    public TextView submit;
    public TextView cancel;
    public FrameLayout view_dialog_framelayout;
    public View view;
    public TextView title_tv;

    private IDialogClickListener mPositiveListener;
    private IDialogClickListener mNegativeListener;
    private PermissionsManager.DialogType dataBean;

    public PermissionsTipsDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        mContext = context;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.dv_permission_submit){
            if(mPositiveListener != null) {
                mPositiveListener.onClick();
            }
            dismiss();
        } else if(v.getId() == R.id.dv_permission_cancel){
            if(mNegativeListener != null) {
                mNegativeListener.onClick();
            }
            dismiss();
        } else if(v.getId() == R.id.dv_permission_view_dialog_red_gift){

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(getContext()).inflate(R.layout.dv_util_permission_tips_dialog_layout, null);
        setContentView(view);
        initView(view);
        setCancelable(false);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x0000000));

    }

    @Override
    public void show() {
        super.show();

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300);
        mViewShade.startAnimation(alphaAnimation);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.dv_util_permission_dialog_show_anim);
        view.startAnimation(animation);
        if(dataBean != null){
            if(dataBean.getHidecancel()) {
                submit_line.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                submit.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.dv_util_permission_selector_round_rb_white_right));
            } else {
                cancel.setVisibility(View.VISIBLE);
            }

            if(dataBean.getType() == PermissionsManager.PERMISSION_OVERLAY){
                tips.setText(getOverStringSource());
            } else if(dataBean.getType() == PermissionsManager.PERMISSION_XIAOMI_WRITE_SETTINGS) {
                tips.setText(getXiaomiSysSettingStringSource());
            } else {
                tips.setText(dataBean.getMessage());
            }

            submit.setText(dataBean.getButtonName());
            if(!TextUtils.isEmpty(dataBean.getTitle())){
                title_tv.setText(dataBean.getTitle());
            }
        }

    }

    private void initView(View view){
        mViewShade = view.findViewById(R.id.dv_permission_view_dialog_red_gift);
        submit_line = view.findViewById(R.id.dv_permission_submit_line);
        tips = view.findViewById(R.id.dv_permission_network_tips_textview);
        submit = view.findViewById(R.id.dv_permission_submit);
        cancel = view.findViewById(R.id.dv_permission_cancel);
        title_tv=view.findViewById(R.id.dv_permission_title_tv);
        view_dialog_framelayout = view.findViewById(R.id.dv_permission_view_dialog_framelayout);
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);


    }

    public void closeDialog(View view) {
        dismiss();
    }

    public static class DialogBuilder {

        private Context context;
        private PermissionsManager.DialogType dataBean;

        private IDialogClickListener positiveListener;
        private IDialogClickListener negativeListener;

        public DialogBuilder(Context context) {
            this.context = context;
        }

        public PermissionsTipsDialog build() {
            PermissionsTipsDialog inviteSignShowDialog = new PermissionsTipsDialog(context);
            inviteSignShowDialog.mPositiveListener = positiveListener;
            inviteSignShowDialog.mNegativeListener = negativeListener;
            inviteSignShowDialog.dataBean = dataBean;
            return inviteSignShowDialog;
        }

        public DialogBuilder dataBean(PermissionsManager.DialogType data) {
            this.dataBean = data;
            return this;
        }

        public DialogBuilder positiveClickListener(IDialogClickListener listener) {
            positiveListener = listener;
            return this;
        }

        public DialogBuilder negativeClickListener(IDialogClickListener listener) {
            negativeListener = listener;
            return this;
        }

    }
    public interface IDialogClickListener {
        void onClick();
    }

    private String getOverStringSource(){
        String manufacturer = Build.MANUFACTURER;
        String source = Html.fromHtml(mContext.getResources().getString(R.string.dv_util_permission_eye_tips_settings_default))
                + getAppName(mContext) + Html.fromHtml(mContext.getResources().getString(R.string.dv_util_permission_eye_tips_settings));
        if (TextUtils.isEmpty(manufacturer)){
            return source;
        }
        if (manufacturer.equals("Xiaomi")){
            return source;
        }else if (manufacturer.equals("360")){
            source = Html.fromHtml(mContext.getResources().getString(R.string.dv_util_permission_eye_tips_settings_360))
                    + getAppName(mContext) + Html.fromHtml(mContext.getResources().getString(R.string.dv_util_permission_eye_tips_settings_360d));
            return source;
        }else if (manufacturer.equals("OPPO")){
            source = Html.fromHtml(mContext.getResources().getString(R.string.dv_util_permission_eye_tips_settings_oppo1))
                    + getAppName(mContext) + Html.fromHtml(mContext.getResources().getString(R.string.dv_util_permission_eye_tips_settings_oppo));
            return source;
        }else {
            return source;
        }
    }

    private String getXiaomiSysSettingStringSource() {
        String source = Html.fromHtml(mContext.getResources().getString(R.string.dv_util_permission_sys_tips_settings_default))
                + getAppName(mContext) + Html.fromHtml(mContext.getResources().getString(R.string.dv_util_permission_sys_tips_settings));

        return source;
    }
    /**
     * 获取应用程序名称
     */
    public synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }


}
