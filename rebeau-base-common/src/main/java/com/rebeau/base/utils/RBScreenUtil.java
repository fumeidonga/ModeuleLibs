package com.rebeau.base.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Method;


/**
 *
 */
public class RBScreenUtil {

	private Context context;

	private RBScreenUtil() {}

	private RBScreenUtil(Context context) {
		this.context = context.getApplicationContext();
	}

	// 私有内部类，按需加载，用时加载，也就是延迟加载
	private static class Holder {
		private static RBScreenUtil singleton5 = new RBScreenUtil(RBApplicationUtil.getContext());
	}

	/**
	 *
	 * @return
	 */
	public static RBScreenUtil getInstance() {
		return Holder.singleton5;
	}

	/**
	 *
	 * @param f
	 * @return
	 */
	public int dip2px(float f) {
		return (int) (0.5D + (double) (f * getDensity(context)));
	}

	/**
	 *
	 * @param i
	 * @return
	 */
	public int dip2px(int i) {
		return (int) (0.5D + (double) (getDensity(context) * (float) i));
	}

	/**
	 *
	 * @param i
	 * @return
	 */
	public int get480Height(int i) {
		return (i * getScreenWidth()) / 480;
	}

	/**
	 *
	 * @param context
	 * @return
	 */
	public float getDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	/**
	 *
	 * @return
	 */
	public int getScal() {
		return (100 * getScreenWidth()) / 480;
	}

	/**
	 *
	 * @return
	 */
	public int getScreenDensityDpi() {
		return context.getResources().getDisplayMetrics().densityDpi;
	}

	/**
	 *
	 * @return
	 */
	public int getScreenHeight() {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 *
	 * @return
	 */
	public int getScreenWidth() {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 *
	 * @return
	 */
	public float getXdpi() {
		return context.getResources().getDisplayMetrics().xdpi;
	}

	/**
	 *
	 * @return
	 */
	public float getYdpi() {
		return context.getResources().getDisplayMetrics().ydpi;
	}

	/**
	 *
	 * @param f
	 * @return
	 */
	public int px2dip(float f) {
		float f1 = getDensity(context);
		return (int) (((double) f - 0.5D) / (double) f1);
	}

	/**
	 *
	 * @param i
	 * @return
	 */
	public int px2dip(int i) {
		float f = getDensity(context);
		return (int) (((double) i - 0.5D) / (double) f);
	}

	/**
	 * sp转化为像素.
	 */
	public int spToPx(float sp) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (sp * scale + 0.5f);
	}

	/**
	 * 获取如果设计图以360dp宽度的情况下，1dp对于的px值
	 * 用于在不同的
	 */
	public static float getZoomValue(Context context) {
		final int widthPx = getScreenWidth(context);
		final float density = getScreenDensity(context);
		final float nowWidthDp = widthPx/density;
		float zoom = nowWidthDp / 360F;
		return zoom;
	}

	public static float getZoomDp(Context context, float dp) {
		return getZoomValue(context) * dp;
	}

	public static float getZoomSp(Context context, float sp) {
		return getZoomValue(context) * sp;
	}

	/**
	 * 获取设备屏幕的宽度
	 */
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 获取设备屏幕的高度
	 */
	public static int getScreenHeight(Context context) {
		if (context == null) {
			return 1;
		}
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 获取设备的密度
	 */
	public static float getScreenDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	/**
	 * 获取屏幕尺寸与密度.
	 *
	 * @param context the context
	 * @return mDisplayMetrics
	 */
	public static DisplayMetrics getDisplayMetrics(Context context) {
		Resources mResources;
		if (context == null) {
			mResources = Resources.getSystem();

		} else {
			mResources = context.getResources();
		}
		//DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
		//DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
		return mResources.getDisplayMetrics();
	}

	/**
	 * sp转化为像素.
	 */
	public static int spToPx(Context context, float sp) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		final float zoomValue = Math.max(1.0f, getZoomValue(context));
		return (int) (sp * scale * zoomValue + 0.5f);
	}

	/**
	 * 密度转换为像素
	 */
	public static int dpToPx(Context context, float dp) {
		final float scale = getScreenDensity(context);
		final float zoomValue = getZoomValue(context);
		return (int) (dp * scale * zoomValue + 0.5f);
	}

	/**
	 * 像素转换为密度
	 */
	public static int pxToDp(Context context, float px) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/**
	 * px值转换为sp值
	 */
	public static int pxToSp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 设置系统亮度
	 *
	 * @param activity
	 */
	public static void setScreenBrightnessAuto(Activity activity) {
		if (activity == null) {
			return;
		}
		final WindowManager.LayoutParams attrs = activity.getWindow()
				.getAttributes();
		attrs.screenBrightness = -1.0f;
		activity.getWindow().setAttributes(attrs);
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

	/**
	 * 获取系统亮度
	 *
	 * @param activity
	 * @return
	 */
	public static int getScreenBrightness(Activity activity) {
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
	 * 将窗口转为不透明
	 * @param activity
	 */
	public static void convertActivityFromTranslucent(Activity activity) {
		try {
			Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
			method.setAccessible(true);
			method.invoke(activity);
		} catch (Throwable t) {
		}
	}


	/**
	 * 将窗口转为透明
	 * @param activity
	 */
	public static void convertActivityToTranslucent(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			convertActivityToTranslucentAfterL(activity);
		} else {
			convertActivityToTranslucentBeforeL(activity);
		}
	}


	/**
	 * 将窗口转为透明 before Android 5.0
	 * @param activity
	 */
	public static void convertActivityToTranslucentBeforeL(Activity activity) {
		try {
			Class<?>[] classes = Activity.class.getDeclaredClasses();
			Class<?> translucentConversionListenerClazz = null;
			for (Class clazz : classes) {
				if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
					translucentConversionListenerClazz = clazz;
				}
			}
			Method method = Activity.class.getDeclaredMethod("convertToTranslucent",
					translucentConversionListenerClazz);
			method.setAccessible(true);
			method.invoke(activity, new Object[]{
					null
			});
		} catch (Throwable t) {
		}
	}

	/**
	 * 将窗口转为透明  after Android 5.0
	 * Calling the convertToTranslucent method on platforms after Android 5.0
	 */
	private static void convertActivityToTranslucentAfterL(Activity activity) {
		try {
			Method getActivityOptions = Activity.class.getDeclaredMethod("getActivityOptions");
			getActivityOptions.setAccessible(true);
			Object options = getActivityOptions.invoke(activity);

			Class<?>[] classes = Activity.class.getDeclaredClasses();
			Class<?> translucentConversionListenerClazz = null;
			for (Class clazz : classes) {
				if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
					translucentConversionListenerClazz = clazz;
				}
			}
			Method convertToTranslucent = Activity.class.getDeclaredMethod("convertToTranslucent",
					translucentConversionListenerClazz, ActivityOptions.class);
			convertToTranslucent.setAccessible(true);
			convertToTranslucent.invoke(activity, null, options);
		} catch (Throwable t) {
		}
	}
}
