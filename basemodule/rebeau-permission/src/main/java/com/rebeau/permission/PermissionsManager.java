package com.rebeau.permission;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.base.utils.rom.RomUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;

import java.util.List;

/**
 * 权限管理
 */

public class PermissionsManager {
    /**
     * 需要申请的部分权限
     */
    public static final String READ_PHONE_STATE = Permission.READ_PHONE_STATE;
    public static final String WRITE_EXTERNAL_STORAGE = Permission.WRITE_EXTERNAL_STORAGE;
    public static final String READ_EXTERNAL_STORAGE = Permission.READ_EXTERNAL_STORAGE;
    public static final String CAMERA = Permission.CAMERA;

    /**
     * 悬浮窗
     */
    public static final int PERMISSION_OVERLAY = 1;
    /**
     * 去设置
     */
    public static final int PERMISSION_GO_SETTINGS = 2;
    public static final int PERMISSION_WRITE_SETTINGS = 3;
    public static final int PERMISSION_XIAOMI_WRITE_SETTINGS = 4;
    public static final int PERMISSION_NOTIFICATION_SETTINGS = 5;

//    private static class PerMissionsManagerHolder {
//        private static final PermissionsManager instance = new PermissionsManager();
//    }
//
//    public static PermissionsManager getInstance() {
//        return PerMissionsManagerHolder.instance;
//    }

    /**
     * 权限判断
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        return AndPermission.hasPermissions(context, permissions);
    }

    /*public boolean hasPermissions(Fragment fragment, String... permissions){
        return AndPermission.hasPermissions(fragment, permissions);
    }*/

    /**
     * Request permissions.
     * 申请权限
     */
    public static void requestPermissions(final PermissionListener permissionListener, final Context context, String... permissions) {
        AndPermission.with(context)
                .runtime()
                .permission(permissions)
//                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        if (permissionListener != null) {
                            permissionListener.onPermissionsGranted(permissions);
                        }
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        if (permissionListener != null) {
                            permissionListener.onPermissionsDenied(permissions);
                        }
                        if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                            if (permissionListener != null) {
                                permissionListener.onPermissionsError(permissions);
                            }
                            //showSettingDialog(context, permissions);
                        }
                    }
                })
                .start();
    }

    /**
     * Set permissions. go settings
     * 打开设置
     */
    public static void setPermission(final PermissionSettingListener permissionListener, final Context context) {
        RBLogUtil.dt();
        AndPermission.with(context)
                .runtime()
                .setting()
                .onComeback(new Setting.Action() {
                    @Override
                    public void onAction() {
                        RBLogUtil.dt();
                        if (permissionListener != null) {
                            permissionListener.settingsBack(0);
                        }
                    }
                })
                .start();
    }

    private static void setOverLayoutpermission(final PermissionSettingListener permissionListener, final Context context) {

        AndPermission.with(context)
                .overlay()
                .onGranted(new Action<Void>() {
                    @Override
                    public void onAction(Void data) {
                        RBLogUtil.dt();
                        if (permissionListener != null) {
                            permissionListener.settingsBack(0);
                        }
                    }
                })
                .onDenied(new Action<Void>() {
                    @Override
                    public void onAction(Void data) {
                        RBLogUtil.dt();
                        if (permissionListener != null) {
                            permissionListener.settingsBack(-1);
                        }
                    }
                })
                .start();
    }


    /**
     * 判断用户是否允许通知栏权限
     *
     * @param context
     */
    public static boolean getNotificationpermission(Context context){
        boolean b = NotificationManagerCompat.from(context).areNotificationsEnabled();
        try {
            if(b){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationChannel channel = manager.getNotificationChannel("upush_default");
                    if (channel != null && channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                        b=false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }

    public static void requestNotificationpermission(Context context, boolean isDirectOpen) {
        boolean b = NotificationManagerCompat.from(context).areNotificationsEnabled();

        try {
            if (!b || isDirectOpen) {
                //暂时 小米通知栏8.0权限设置页是个空页面 魅族5.0崩溃
                Intent intent = new Intent();
                if(RomUtil.checkIsMiuiRom()||RomUtil.checkIsMeizuRom()){
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                }else
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra("app_package", context.getPackageName());
                    intent.putExtra("app_uid", context.getApplicationInfo().uid);
                } else {
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                }
                context.startActivity(intent);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //8.0以上要判断下是否允许友盟推送显示  小米同上
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationChannel channel = manager.getNotificationChannel("upush_default");
                    if (channel != null && channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                        Intent intent = new Intent();
                        if(RomUtil.checkIsMiuiRom()){
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + context.getPackageName()));
                        }else{
                            intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                            intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                        }
                        context.startActivity(intent);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            if(intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        }

    }

    /**
     * 跳转到通知权限设置页面
     * @param context
     */
    public static void openNotificationPermissionSetting(Context context) {
        requestNotificationpermission(context, true);
    }

    /**
     * 获取权限提示语
     *
     * @param context
     * @param permission
     * @return
     */
    public static String getDialogTips(Context context, final List<String> permission) {
        List<String> permissionNames = Permission.transformText(context, permission);
        String message = context.getString(R.string.dv_util_permission_message_rationale, TextUtils.join("\n", permissionNames));
        return message;
    }

    public static String getDialogTips(Context context, final String... permission) {
        List<String> permissionNames = Permission.transformText(context, permission);
        String message = context.getString(R.string.dv_util_permission_message_rationale, TextUtils.join("\n", permissionNames));
        return message;
    }

    /**
     * 弹出权限提示对话框
     *
     * @param context
     * @param dialogType
     * @param permissionType
     */
    public static void showTipsDialog(final Activity context, DialogType dialogType, final int permissionType) {
        showTipsDialog(context, dialogType, permissionType, null);
    }

    /**
     * 弹出提示对话框
     */
    public static void showTipsDialog(final Activity context, DialogType dialogType, final int permissionType, final PermissionSettingListener permissionListener) {

        if (context.isFinishing()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (context.isDestroyed()) {
                return;
            }
        }
        new PermissionsTipsDialog.DialogBuilder(context)
                .dataBean(dialogType)
                .positiveClickListener(new PermissionsTipsDialog.IDialogClickListener() {
                    @Override
                    public void onClick() {
                        //submit
                        if (permissionType == PERMISSION_GO_SETTINGS) {
                            setPermission(permissionListener, context);
                        } else if (permissionType == PERMISSION_WRITE_SETTINGS) {

                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                                    Uri.parse("package:" + context.getPackageName()));
                            ComponentName componentName = intent.resolveActivity(context.getPackageManager());
                            if (componentName != null) {
                                context.startActivity(intent);
                            }
                        } else if (permissionType == PERMISSION_OVERLAY) {
                            /*if("Redmi Note 4X".equals(Build.MODEL) && RomUtil.checkIsMiuiRom()) {

                                setOverLayoutpermission(permissionListener, context);
                                //FloatPerMissionUtil.getInstance().applyPermission(MainActivity.this);
                            } else {
                                //跳转到设置
                                setPermission(permissionListener, context);
                            }*/
                            //这里去设置悬浮窗的权限， 很多手机是有问题的，还是要特殊处理
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                                setPermission(permissionListener, context);
                            } else {
                                setOverLayoutpermission(permissionListener, context);
                            }
                        } else if(permissionType==PERMISSION_NOTIFICATION_SETTINGS) {
                            requestNotificationpermission(context, false);
                            if (permissionListener != null) {
                                permissionListener.settingsBack(permissionType);
                            }
                        }

                    }
                })
                .negativeClickListener(new PermissionsTipsDialog.IDialogClickListener() {
                    @Override
                    public void onClick() {
                        //cancel
                        if (permissionListener != null) {
                            permissionListener.cancelBtnClick();
                        }
                    }
                })
                .build()
                .show();
    }

    public static class DialogType {
        /**
         * 类型
         */
        int type;
        String message;
        String buttonName;
        boolean nightmode;
        boolean hidecancel;
        String title;

        public DialogType(int type, String message, String buttonName, boolean nightmode, boolean hidecancel) {
            this.type = type;
            this.message = message;
            this.buttonName = buttonName;
            this.nightmode = nightmode;
            this.hidecancel = hidecancel;
        }

        public void setTitle(String title){
            this.title=title;
        }

        public String getTitle(){
            return title;
        }

        public boolean getNightmode() {
            return nightmode;
        }

        public int getType() {
            return type;
        }

        public boolean getHidecancel() {
            return hidecancel;
        }

        public String getMessage() {
            return message;
        }

        public String getButtonName() {
            return buttonName;
        }
    }


    /**
     * The user comes back from the settings page
     */
    public interface PermissionSettingListener {

        /**
         * back from settings
         * default is 0
         */
        void settingsBack(int type);

        /**
         * 取消按钮的点击回调
         */
        void cancelBtnClick();

    }

    public interface PermissionListener {

        /**
         * call when permissions are granted
         *
         * @param permissions
         */
        void onPermissionsGranted(List<String> permissions);

        /**
         * call when one or some permissions are denied
         *
         * @param permissions
         */
        void onPermissionsDenied(List<String> permissions);

        /**
         * get a permissions error: almost params are wrong
         *
         * @param permissions
         */
        void onPermissionsError(List<String> permissions);

    }


}
