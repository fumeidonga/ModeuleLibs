package com.rebeau.base.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.rebeau.base.R;

import java.lang.reflect.Field;

/**
 * 自定义状态栏工具类
 *
 */
public class RBStatusBarUtil {

	/* 系统状态栏及导航栏相关api */
	/**
	 * 获取状态栏高度
	 */
	public static int getStatusBarHeight(Context activity) {
		Class<?> c;
		Object obj;
		Field field;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = activity.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return sbar;
	}


	public static void initSubStatusBar(Activity activity) {
		initSubStatusBar(activity, true);
	}

	/**
	 * 初始化二级页面状态栏
	 * @param activity
	 */
	public static void initSubStatusBar(Activity activity, boolean light) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			// 暂不支持4.4以下系统
			return;
		}

		Window window = activity.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			//5.0 以上处理
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

			window.setStatusBarColor(Color.TRANSPARENT);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && light) {
			// 6.0 以上处理为：白底加黑字(默认)，带高斯模糊的页面（详情页，个人主页等）透明底加白字，要显示的时候再设置为白底加黑字

			View decorView = activity.getWindow().getDecorView();
			if (decorView != null) {
				int vis = decorView.getSystemUiVisibility();
				vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

				decorView.setSystemUiVisibility(vis);
			}
		}
	}

	public static void initSubStatusBar(Activity activity, View view, int color) {
		initSubStatusBar(activity, view, color, activity.getResources().getColor(R.color.rb_ui_status_bar_background));
	}

	/**
	 * 初始化状态栏
	 *
	 * @param activity
	 * @param view
	 * @param color    设置状态栏背景颜色
	 */
	public static void initSubStatusBar(Activity activity, View view, int color, int defaultColor) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			// 暂不支持4.4以下系统
			return;
		}

		ViewGroup.LayoutParams params = view.getLayoutParams();
		params.height = getStatusBarHeight(activity);
		view.setLayoutParams(params);
		view.setVisibility(View.VISIBLE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// 1. 默认为白底加黑字
			// 2. 带高斯模糊的页面（详情页，个人主页等）透明底加白字
			view.setBackgroundColor(color);
		} else {
			// 1. 默认为黑底加白字
			// 2. 阅读器相关页面为深灰色加白字
			view.setBackgroundColor(defaultColor);
		}
	}

	/**
	 * 填充状态栏
	 *
	 * @param activity
	 * @param view
	 */
	public static int initMainStatusBar(Activity activity, View view) {
		// support 4.4+;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			return 0;
		}

		int height = getStatusBarHeight(activity);
		if (height <= 0) {
			return 0;
		}

		ViewGroup.LayoutParams params = view.getLayoutParams();
		params.height = getStatusBarHeight(activity);
		view.setLayoutParams(params);
		view.setVisibility(View.VISIBLE);

		return height;
	}

	/**
	 * 设置状态栏字体颜色
	 *
	 * @param activity
	 * @param light
	 */
	public static void setStatusBarColor(Activity activity, boolean light) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			//6.0 系统以上才支持
			View decorView = activity.getWindow().getDecorView();
			if (decorView != null) {
				int vis = decorView.getSystemUiVisibility();
				int result = vis & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
				if (light) {
					// 已经是高亮的情况下，不进行设置
					if (result != 0) {
						return;
					}
					vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
				} else {
					// 已经不是高亮的情况下，不进行设置
					if (result == 0) {
						return;
					}
					vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
				}
				decorView.setSystemUiVisibility(vis);
			}
		}
	}

	/**
	 * 设置状态（白底黑字/黑底白字）
	 *
	 * @param activity
	 * @param show
	 */
	public static void setStatusBarBackground(Activity activity, View view, boolean show) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			// 不支持4.4以下系统
			return;
		}
		if (show) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				view.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
			} else {
				view.setBackgroundColor(activity.getResources().getColor(R.color.rb_ui_status_bar_background));
			}
		} else {
			view.setBackgroundColor(Color.TRANSPARENT);
		}
	}
}
