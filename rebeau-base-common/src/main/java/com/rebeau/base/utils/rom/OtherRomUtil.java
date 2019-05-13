package com.rebeau.base.utils.rom;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

public class OtherRomUtil {

    public static void applyOppoPermission(Context context) {
        Intent intent = new Intent();
        intent.putExtra("packageName", context.getPackageName());
        // OPPO A53|5.1.1|2.1
        intent.setAction("com.oppo.safe");
        intent.setClassName("com.oppo.safe", "com.oppo.safe.permission.PermissionAppListActivity");
        if (!RomUtil.startActivitySafely(intent, context)) {
            //R7
            intent.setAction("com.oppo.safe");
            intent.setClassName("com.oppo.safe", "com.oppo.safe.permission.floatwindow.FloatWindowListActivity");
            if (!RomUtil.startActivitySafely(intent, context)) {
                intent.setAction("com.oppo.safe");
                intent.setClassName("com.oppo.safe", "com.oppo.safe.SecureSafeMainActivity");
                if (!RomUtil.startActivitySafely(intent, context)) {
                    // OPPO R7s|4.4.4|2.1
                    intent.setAction("com.color.safecenter");
                    intent.setClassName("com.color.safecenter", "com.color.safecenter.permission.floatwindow.FloatWindowListActivity");
                    if (!RomUtil.startActivitySafely(intent, context)) {
                        //R9s
                        // com.coloros.safecenter/.MainActivity
                        //com.coloros.privacypermissionsentry.PermissionTopActivity 权限列表
                        intent.setAction("com.coloros.safecenter");
                        intent.setClassName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity");
                        if (!RomUtil.startActivitySafely(intent, context)) {
                            RomUtil.showAlertToast(context);
                        }
                    }
                }
            }
        }
    }


    public static void applyCoolpadPermission(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.yulong.android.seccenter", "com.yulong.android.seccenter.dataprotection.ui.AppListActivity");
        if (!RomUtil.startActivitySafely(intent, context)) {
            RomUtil.showAlertToast(context);
        }
    }

    public static void applyLenovoPermission(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.lenovo.safecenter", "com.lenovo.safecenter.MainTab.LeSafeMainActivity");
        if (!RomUtil.startActivitySafely(intent, context)) {
            RomUtil.showAlertToast(context);
        }
    }

    public static void applyZTEPermission(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.zte.heartyservice.intent.action.startActivity.PERMISSION_SCANNER");
        if (!RomUtil.startActivitySafely(intent, context)) {
            RomUtil.showAlertToast(context);
        }
    }

    public static void applyLetvPermission(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AppActivity");
        if (!RomUtil.startActivitySafely(intent, context)) {
            RomUtil.showAlertToast(context);
        }
    }

    public static void applyVivoPermission(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.FloatWinPerManager");
        if (!RomUtil.startActivitySafely(intent, context)) {
            // 到 i管家 首页
            Intent intent1 = new Intent("com.iqoo.secure");
            intent1.setClassName("com.iqoo.secure", "com.iqoo.secure.MainActivity");
            if (!RomUtil.startActivitySafely(intent1, context)) {
                RomUtil.showAlertToast(context);
            }
        }
    }

    public static void applySmartisanPermission(Context context) {
        Intent intent = new Intent("com.smartisanos.security.action.SWITCHED_PERMISSIONS_NEW");
        intent.setClassName("com.smartisanos.security", "com.smartisanos.security.SwitchedPermissions");
        intent.putExtra("index", 17); //有版本差异,不一定定位正确
        if (!RomUtil.startActivitySafely(intent, context)) {
            intent = new Intent("com.smartisanos.security.action.SWITCHED_PERMISSIONS");
            intent.setClassName("com.smartisanos.security", "com.smartisanos.security.SwitchedPermissions");
            intent.putExtra("permission", new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW});
            if (!RomUtil.startActivitySafely(intent, context)) {
                RomUtil.showAlertToast(context);
            }
        }
    }
}
