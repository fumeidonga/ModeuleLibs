package com.rebeau.base.utils.rom;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

public class RomUtil {
    private static final String TAG = "RomUtil";
    private static final String TOAST_HINT = "无法跳转至权限设置页面，请手动设置或向我们反馈";

    /**
     *
     */
    public static final String ROM_MIUI = "MIUI";
    /**
     * 华为
     */
    public static final String ROM_EMUI = "EMUI";
    /**
     *
     */
    public static final String ROM_VIVO = "VIVO";
    /**
     *
     */
    public static final String ROM_OPPO = "OPPO";
    /**
     * 魅族
     */
    public static final String ROM_FLYME = "FLYME";
    /**
     *
     */
    public static final String ROM_SMARTISAN = "SMARTISAN";
    /**
     * 360
     */
    public static final String ROM_QIKU = "QIKU";
    /**
     *
     */
    public static final String ROM_LETV = "LETV";
    /**
     *
     */
    public static final String ROM_LENOVO = "LENOVO";
    /**
     *
     */
    public static final String ROM_NUBIA = "NUBIA";
    /**
     *
     */
    public static final String ROM_ZTE = "ZTE";
    /**
     *
     */
    public static final String ROM_COOLPAD = "COOLPAD";
    /**
     *
     */
    public static final String ROM_UNKNOWN = "UNKNOWN";

    private static final String SYSTEM_VERSION_MIUI = "ro.miui.ui.version.name";
    private static final String SYSTEM_VERSION_EMUI = "ro.build.version.emui";
    private static final String SYSTEM_VERSION_VIVO = "ro.vivo.os.version";
    private static final String SYSTEM_VERSION_OPPO = "ro.build.version.opporom";
    private static final String SYSTEM_VERSION_FLYME = "ro.build.display.id";
    private static final String SYSTEM_VERSION_SMARTISAN = "ro.smartisan.version";
    private static final String SYSTEM_VERSION_LETV = "ro.letv.eui";
    private static final String SYSTEM_VERSION_LENOVO = "ro.lenovo.lvp.version";

    public static String getRomName() {
        if (checkIsMiuiRom()) {
            return ROM_MIUI;
        }
        if (checkIsHuaweiRom()) {
            return ROM_EMUI;
        }
        if (isVivoRom()) {
            return ROM_VIVO;
        }
        if (isOppoRom()) {
            return ROM_OPPO;
        }
        if (checkIsMeizuRom()) {
            return ROM_FLYME;
        }
        if (isSmartisanRom()) {
            return ROM_SMARTISAN;
        }
        if (checkIs360Rom()) {
            return ROM_QIKU;
        }
        if (isLetvRom()) {
            return ROM_LETV;
        }
        if (isLenovoRom()) {
            return ROM_LENOVO;
        }
        if (isZTERom()) {
            return ROM_ZTE;
        }
        if (isCoolPadRom()) {
            return ROM_COOLPAD;
        }
        return ROM_UNKNOWN;
    }

    /**
     * 获取 emui 版本号
     *
     * @return
     */
    public static double getEmuiVersion() {
        try {
            String emuiVersion = getSystemProperty("ro.build.version.emui");
            String version = emuiVersion.substring(emuiVersion.indexOf("_") + 1);
            return Double.parseDouble(version);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 4.0;
    }

    /**
     * 获取小米 rom 版本号，获取失败返回 -1
     *
     * @return miui rom version code, if fail , return -1
     */
    public static int getMiuiVersion() {
        String version = getSystemProperty("ro.miui.ui.version.name");
        if (version != null) {
            try {
                return Integer.parseInt(version.substring(1));
            } catch (Exception e) {
                Log.e(TAG, "get miui version code error, version : " + version);
            }
        }
        return -1;
    }

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }

    /**
     * 1
     * check if is miui ROM
     */
    public static boolean checkIsMiuiRom() {
        if (isMiuiRom) {
            return true;
        }
        isMiuiRom = !TextUtils.isEmpty(getSystemProperty(SYSTEM_VERSION_MIUI));

        return isMiuiRom;
    }

    public static boolean isMiuiRom = false;

    /**
     * 2
     * check if is oppo ROM
     */
    public static boolean isOppoRom() {
        return !TextUtils.isEmpty(getSystemProperty(SYSTEM_VERSION_OPPO));
    }

    /**
     * 3
     * check if is vivo ROM
     */
    public static boolean isVivoRom() {
        return !TextUtils.isEmpty(getSystemProperty(SYSTEM_VERSION_VIVO));
    }

    /**
     * 4
     * meizu
     *
     * @return
     */
    public static boolean checkIsMeizuRom() {
        //return Build.MANUFACTURER.contains("Meizu");
        String meizuFlymeOSFlag = getSystemProperty(SYSTEM_VERSION_FLYME);
        if (TextUtils.isEmpty(meizuFlymeOSFlag)) {
            return false;
        } else if (meizuFlymeOSFlag.contains("flyme") || meizuFlymeOSFlag.toLowerCase().contains(ROM_FLYME)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 5
     * 360
     *
     * @return
     */
    public static boolean checkIs360Rom() {
        //fix issue https://github.com/zhaozepeng/FloatWindowPermission/issues/9
        return Build.MANUFACTURER.contains(ROM_QIKU)
                || Build.MANUFACTURER.contains("360");
    }

    /**
     * 6
     * letv
     *
     * @return
     */
    public static boolean isLetvRom() {
        return !TextUtils.isEmpty(getSystemProperty(SYSTEM_VERSION_LETV));
    }

    /**
     * 7
     * huawei
     *
     * @return
     */
    public static boolean checkIsHuaweiRom() {
        return Build.MANUFACTURER.contains("HUAWEI") ||
                !TextUtils.isEmpty(getSystemProperty(SYSTEM_VERSION_EMUI));
    }

    /**
     * 8
     * zte
     *
     * @return
     */
    public static boolean isZTERom() {
        String manufacturer = Build.MANUFACTURER;
        String fingerPrint = Build.FINGERPRINT;
        return (!TextUtils.isEmpty(manufacturer) && (fingerPrint.toLowerCase().contains(ROM_NUBIA) || fingerPrint.toLowerCase().contains(ROM_ZTE)))
                || (!TextUtils.isEmpty(fingerPrint) && (fingerPrint.toLowerCase().contains(ROM_NUBIA) || fingerPrint.toLowerCase().contains(ROM_ZTE)));
    }

    /**
     * 9
     * lenovo
     *
     * @return
     */
    public static boolean isLenovoRom() {
        return !TextUtils.isEmpty(getSystemProperty(SYSTEM_VERSION_LENOVO));
    }

    /**
     * 10
     *
     * @return
     */
    public static boolean isCoolPadRom() {
        String model = Build.MODEL;
        String fingerPrint = Build.FINGERPRINT;
        return (!TextUtils.isEmpty(model) && model.toLowerCase().contains(ROM_COOLPAD))
                || (!TextUtils.isEmpty(fingerPrint) && fingerPrint.toLowerCase().contains(ROM_COOLPAD));
    }

    /**
     * 11
     * smart
     *
     * @return
     */
    public static boolean isSmartisanRom() {
        return !TextUtils.isEmpty(getSystemProperty(SYSTEM_VERSION_SMARTISAN));
    }


    /**
     * 判断是否是国产手机
     *
     * @return
     */
    public static boolean isDomesticSpecialRom() {
        return RomUtil.checkIsMiuiRom()
                || RomUtil.checkIsHuaweiRom()
                || RomUtil.checkIsMeizuRom()
                || RomUtil.checkIs360Rom()
                || RomUtil.isOppoRom()
                || RomUtil.isVivoRom()
                || RomUtil.isLetvRom()
                || RomUtil.isZTERom()
                || RomUtil.isLenovoRom()
                || RomUtil.isCoolPadRom();
    }


    /**
     * 通过反射来获取权限
     * 方法 1
     *
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean checkOtherOps(Context context) {
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = 24;
            arrayOfObject1[1] = Binder.getCallingUid();
            arrayOfObject1[2] = context.getPackageName();
            int m = (Integer) method.invoke(object, arrayOfObject1);
            //4.4至6.0之间的非国产手机，例如samsung，sony一般都可以直接添加悬浮窗
            return m == AppOpsManager.MODE_ALLOWED || !RomUtil.isDomesticSpecialRom();
        } catch (Exception ignore) {
        }
        return false;
    }

    /**
     * 通过反射来获取权限
     * 方法 2
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class clazz = AppOpsManager.class;
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        } else {
            Log.e(TAG, "Below API 19 cannot invoke!");
        }
        return false;
    }

    /**
     * @param intent
     * @param context
     * @return
     */
    public static boolean startActivitySafely(Intent intent, Context context) {
        if (isIntentAvailable(intent, context)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断Intent是否存在
     *
     * @param intent
     * @param context
     * @return
     */
    private static boolean isIntentAvailable(Intent intent, Context context) {
        return intent != null && context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    public static void showAlertToast(Context context) {
        Toast.makeText(context, TOAST_HINT, Toast.LENGTH_LONG).show();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }
}
