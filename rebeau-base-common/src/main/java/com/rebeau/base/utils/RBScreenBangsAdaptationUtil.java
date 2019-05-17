package com.rebeau.base.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.rebeau.base.utils.rom.RomUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 有关刘海屏的适配
 * 1、使用单例模式，在第一次初始化时，就获取是否是某一个手机品牌的全面屏手机，只获取一次。
 * 2、对单独品牌的全面屏独立标识，在不同的适配方法里，可以对不同类型的手机，独立适配。
 *
 */
public class RBScreenBangsAdaptationUtil {

    /**
     * 刘海屏截图顶部章节名长度,默认为10
     */
    private static final int BANGS_STRING_LENGTH = 10;
    /**
     * 刘海屏截图顶部章节名长度，小米8为7
     */
    private static final int BANGS_STRING_LENGTH_XIAOMI = 7;

    /**
     * 同上的重载方法
     *
     * @param mWindow
     */
    private static void xiaomiFullScreen(Window mWindow) {
        int flag = 0x00000100 | 0x00000200 | 0x00000400;
        try {
            Method method = Window.class.getMethod("addExtraFlags", int.class);
            method.invoke(mWindow, flag);
        } catch (Exception e) {
//            LogCat.i( "XiaoMi addExtraFlags not found.");
        }
    }

    /**
     * OPPO手机是否有刘海屏
     * from 官方文档
     *
     * @param context
     * @return
     */
    private static boolean oppoHasBangs(Context context) {
        boolean ret = false;
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism");
        } catch (Exception e) {
//            LogCat.e(e.getLocalizedMessage());
        }
        return ret;
    }

    /**
     * VIVO手机是否有刘海屏
     * from 官方文档
     * 0x00000020 是否有凹槽;
     * 0x00000008 是否有圆角。
     *
     * @param context
     * @return
     */
    private static boolean vivoHasBangs(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class vivoClass = cl.loadClass("android.util.FtFeature");
            if (vivoClass != null) {
                Method method = vivoClass.getMethod("isFeatureSupport", int.class);
                if (method != null) {
                    ret = (boolean) method.invoke(vivoClass, 0x00000020);
                }
            }
        } catch (Exception e) {
//            LogCat.e(e.getLocalizedMessage());
        }
        return ret;
    }

    /**
     * 华为手机是否有刘海屏
     * from 官方文档
     *
     * @param context
     * @return
     */
    private static boolean huaweiHasBangs(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (Exception e) {
//            LogCat.e(e.getLocalizedMessage());
        }
        return ret;
    }

    /**
     * 是否是小米刘海屏
     * from 官方文档
     * 注意：
     * SystemProperties 是系统隐藏API，要使用此API，需要在gradle里设置
     *
     * @return
     */
    private static boolean xiaomiHasBangs() {
        try {
            @SuppressLint("PrivateApi")
            Class spClass = Class.forName("android.os.SystemProperties");
            Method getMethod = spClass.getMethod("getInt", String.class, int.class);
            int hasNotch = (int) getMethod.invoke(null, "ro.miui.notch", 0);
            return hasNotch == 1;
        } catch (Exception ignore) {
        }
        return false;
    }

    /**
     * 是否是联想刘海屏
     *
     * @return
     */
    private static boolean lenovoHasBangs(Context context) {
        try {
            int notchConfigId = context.getResources().getIdentifier("config_screen_has_notch", "bool", "android");
            boolean hasNotch = context.getResources().getBoolean(notchConfigId);
            return hasNotch;
        } catch (Exception ignore) {
        }
        return false;
    }

    private static int[] getNotchSizeInHuawei(Context context) {
        int[] notchSizeInHuawei = new int[] {0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            notchSizeInHuawei = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (Exception e) {

        }
        return notchSizeInHuawei;
    }

    private static int getNotchWidthInXiaomi(Context context) {
        int resourceId = context.getResources().getIdentifier("notch_width", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return -1;
    }

    private static int getNotchHeightInXiaomi(Context context) {
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return RBStatusBarUtil.getStatusBarHeight(context);
    }

    private static int getNotchHeightInLenovo(Context context) {
        try {
            int notchHeightId = context.getResources().getIdentifier("notch_h", "integer", "android");
            int notchHeight = context.getResources().getInteger(notchHeightId);
            return notchHeight;
        } catch (Exception e) {
        }
        return 0;
    }

    private static int getNotchWidthInVivo(Context context) {
        return RBScreenUtil.dpToPx(context, 100);
    }

    private static int getNotchHeightInVivo(Context context) {
        return RBScreenUtil.dpToPx(context, 27);
    }

    private static int getNotchHeightInOppo(Context context) {
        //貌似是固定80px?
        return 80;
    }

    /**
     * 考虑到9.0及以上的机型，可以在设置中改变刘海屏的显示方式，但是改变之后并不一定会重启应用，
     * 在google手机上测试，只是会重启当前的Activity，因此，如果需要使用刘海屏的一些信息，需要先注册
     *
     * 建议在Activity的onCreate中，setContentView()之后就调用该方法
     *
     *
     * @param activity
     */
    public static void register(Activity activity) {
        if (Build.VERSION.SDK_INT >= 28) {
            registerOverAndroidP(activity);
        } else {
            registerBelowAndroidP(activity);
        }
    }

    /**
     * 是否存在刘海屏
     * 注意，该函数调用之前需要调用register
     * {@link RBScreenBangsAdaptationUtil#register}
     * @return
     */
    public static boolean isScreenBang() {
        return ScreenBangHolder.getInstance().isScreenBang();
    }

    /**
     * 获取刘海屏区域的高度
     * 注意，该函数调用之前需要调用register
     * {@link RBScreenBangsAdaptationUtil#register}
     * @return
     */
    public static int getScreenBangHeight() {
        return ScreenBangHolder.getInstance().getScreenBangHeight();
    }

    /**
     * android 9.0及以上的适配
     * @param activity
     */
    private static void registerOverAndroidP(Activity activity) {
        final Window window = activity.getWindow();
        window.getDecorView().post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                WindowInsets windowInsets = window.getDecorView().getRootWindowInsets();
                int bangHeight = 0;
                if (windowInsets != null) {
                    try {
                        Class[] parameter = null;
                        Object[] objects = null;
                        Class windowInsetsClass = windowInsets.getClass();
                        Method windowInsetsGetDisplayCutoutMethod = windowInsetsClass.getMethod("getDisplayCutout", parameter);
                        Object displayCutout = windowInsetsGetDisplayCutoutMethod.invoke(windowInsets, objects);
                        if (displayCutout != null) {
                            Class displayCutoutClass = displayCutout.getClass();
                            Method displayCutoutGetBoundingRectsMethod = displayCutoutClass.getMethod("getBoundingRects", parameter);
                            Object listRect = displayCutoutGetBoundingRectsMethod.invoke(displayCutout, objects);
                            if (listRect != null) {
                                List<Rect> rectList = (List<Rect>) listRect;
                                for (Rect rect : rectList) {
                                    if (rect.top == 0) {
                                        int height = rect.height();
                                        if (height > bangHeight) {
                                            bangHeight = height;
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        bangHeight = 0;
                        RBLogUtil.et("error");
                    }
                }
                ScreenBangHolder.getInstance().setIsScreenBang(bangHeight > 0);
                ScreenBangHolder.getInstance().setScreenBangHeight(bangHeight);
            }
        });
    }

    /**
     * android 9.0以下适配
     * 只适配主流的机型(华为，oppo，vivo，小米)
     * @param activity
     */
    private static void registerBelowAndroidP(Activity activity) {
        if (ScreenBangHolder.getInstance().isSelected()) {
            // 如果是已经刷选过了，就不再去判断了
            return;
        }
        int screenBangFlag = 0;
        int screenBangHeight = 0;
        if (RomUtil.isOppoRom()) {
            if (oppoHasBangs(activity)) {
                screenBangHeight = getNotchHeightInOppo(activity);
                screenBangFlag = ScreenBangHolder.OPPO;
            }
        } else if (RomUtil.isVivoRom()) {
            if (vivoHasBangs(activity)) {
                screenBangHeight = getNotchHeightInVivo(activity);
                screenBangFlag = ScreenBangHolder.VIVO;
            }
        } else if (RomUtil.checkIsHuaweiRom()) {
            if (huaweiHasBangs(activity)) {
                screenBangHeight = getNotchSizeInHuawei(activity)[1];
                screenBangFlag = ScreenBangHolder.HUAWEI;
            }
        } else if (RomUtil.checkIsMiuiRom()) {
            if (xiaomiHasBangs()) {
                screenBangHeight = getNotchHeightInXiaomi(activity);
                screenBangFlag = ScreenBangHolder.XIAOMI;
            }
        } else if (RomUtil.isLenovoRom()) {
            if (lenovoHasBangs(activity)) {
                screenBangHeight = getNotchHeightInLenovo(activity);
                screenBangFlag = ScreenBangHolder.LENOVO;
            }
        }

        ScreenBangHolder.getInstance().setIsScreenBang(screenBangFlag != 0);
        ScreenBangHolder.getInstance().setScreenBangHeight(screenBangHeight);
        ScreenBangHolder.getInstance().setBrandFlag(screenBangFlag);
        ScreenBangHolder.getInstance().setSelected();
    }

    /**
     * 显示刘海屏
     * @param activity
     */
    public static void displayScreenBang(Activity activity) {
        Window window = activity.getWindow();
        displayScreenBang(window, 1);
    }

    public static void displayScreenBang(Dialog dialog) {
        displayScreenBang(dialog.getWindow(), 1);
    }

    public static void disableScreenBang(Activity activity) {
        Window window = activity.getWindow();
        displayScreenBang(window, 0);
    }

    public static void disableScreenBang(Dialog dialog) {
        displayScreenBang(dialog.getWindow(), 0);
    }

    /**
     * 显示刘海屏
     * @param window
     */
    private static void displayScreenBang(Window window, int value) {
        if (Build.VERSION.SDK_INT >= 28) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            WindowManager.LayoutParams lp = window.getAttributes();
            Class windowManagerLayoutParamsClass = lp.getClass();
            try {
                Field field = windowManagerLayoutParamsClass.getField("layoutInDisplayCutoutMode");
                field.setAccessible(true);
                field.setInt(lp, value);
            } catch (Exception e) {
                RBLogUtil.et("error");
            }
            window.setAttributes(lp);
        } else {
            if (ScreenBangHolder.getInstance().isSelected()) {
                if (ScreenBangHolder.getInstance().isScreenBang() && ScreenBangHolder.getInstance().isXiaomiBrand()) {
                    xiaomiFullScreen(window);
                }
            } else {
                if (RomUtil.checkIsMiuiRom()) {
                    if (xiaomiHasBangs()) {
                        xiaomiFullScreen(window);
                    }
                }
            }
        }
    }

    /** 装载刘海屏信息的容器 **/
    public static class ScreenBangHolder {
        static final int XIAOMI = 1;
        static final int HUAWEI = 1 << 1;
        static final int VIVO = 1 << 2;
        static final int OPPO = 1 << 3;
        static final int LENOVO = 1 << 4;

        static ScreenBangHolder sINSTANCE;
        /** 刘海屏区域的高度 **/
        private int mScreenBangHeight;
        /** 是否为刘海屏 **/
        private boolean mIsScreenBang;
        /** 是否已经进行过了刘海屏判断 **/
        private boolean mSelected;
        /** 记录品牌 **/
        private int mBrandFlag;

        private ScreenBangHolder() {}

        public static ScreenBangHolder getInstance() {
            if (sINSTANCE == null) {
                synchronized (ScreenBangHolder.class) {
                    if (sINSTANCE == null) {
                        sINSTANCE = new ScreenBangHolder();
                    }
                }
            }
            return sINSTANCE;
        }

        public int getScreenBangHeight() {
            return mScreenBangHeight;
        }

        public void setScreenBangHeight(int mScreenBangHeight) {
            this.mScreenBangHeight = mScreenBangHeight;
        }

        public boolean isScreenBang() {
            return mIsScreenBang;
        }

        public void setIsScreenBang(boolean mIsScreenBang) {
            this.mIsScreenBang = mIsScreenBang;
        }

        public void setSelected() {
            this.mSelected = true;
        }

        public boolean isSelected() {
            return mSelected;
        }

        public void setBrandFlag(int mask) {
            this.mBrandFlag = mask;
        }

        public boolean isXiaomiBrand() {
            return (mBrandFlag & XIAOMI) != 0;
        }
    }
}
