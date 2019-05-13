package com.rebeau.base.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.view.WindowManager;

public class RBDevicesUtil {

    /**
     * 获取系统亮度
     *
     * @param activity
     * @return
     */
    public static int getScreenBrightness(Context activity) {
        int value = 0;
        if (activity == null) {
            return value;
        }
        ContentResolver cr = activity.getContentResolver();
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {

        }
        return value;
    }

    /**
     * 根据参数设置系统亮度
     *
     * @param activity
     * @param percent
     */
    public static void setScreenBrightness(Activity activity, int percent) {
        if (activity == null) {
            return;
        }
        final WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        if (percent < 5) {
            percent = 5;
        } else if (percent > 255) {
            percent = 255;
        }

        attrs.screenBrightness = percent / 255.0f;
        activity.getWindow().setAttributes(attrs);
    }
}
