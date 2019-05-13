package com.rebeau.base.utils.rom;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class MeizuUtil {
    private static final String TAG = "MeizuUtil";

    /**
     * 检测 meizu 悬浮窗权限
     */
    public static boolean checkFloatWindowPermission(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            //OP_SYSTEM_ALERT_WINDOW = 24;
            return RomUtil.checkOp(context, 24);
        }
        return true;
    }

    /**
     * 去魅族权限申请页面
     */
    public static void applyPermission(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
        intent.putExtra("packageName", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
