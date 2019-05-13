/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rebeau.base.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.orhanobut.logger.Printer;
import com.rebeau.base.BuildConfig;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * https://github.com/orhanobut/logger
 *
 * Log工具，类似android.util.Log。 tag自动产生，格式:
 * customTagPrefix:className.methodName(L:lineNumber),
 * customTagPrefix为空时只输出：className.methodName(L:lineNumber)。
 */
public class RBLogUtil {

	public static String customTagPrefix = "";
	public final static String TAG = "david";
	// 是否需要打印bug，可以在application的onCreate函数里面初始化
	public static boolean isDebug = true;

	private RBLogUtil() {
		throw new UnsupportedOperationException("RBLogUtil can't instantiate me...");
	}

	public static boolean allowD = true;
	public static boolean allowE = true;
	public static boolean allowI = true;
	public static boolean allowV = true;
	public static boolean allowW = true;
	public static boolean savelog = true;

	/**
	 * 文字最大限制
	 */
	private static final int MAX_SIZE = 2000;

	//----------------------------------logger begin-----------------------------------------

	public static void defaultLog(){
		Logger.clearLogAdapters();

		PrettyFormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
				.showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
				.methodCount(0)         // 决定打印多少行（每一行代表一个方法）默认：2
				.tag("david")   // LOG TAG默认是PRETTYLOGGER
				.methodOffset(0)        // Default 0
				.logStrategy(new RBLogUtil.LogCatStrategy())
				.build();

		Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy){
			@Override
			public boolean isLoggable(int priority, String tag) {
				return BuildConfig.DEBUG;
			}
		});

		/**
		 * 使用LogCat.e时的配置，会打印线程信息，调用的Method方法堆栈信息
		 */
		PrettyFormatStrategy errorStrategy = PrettyFormatStrategy.newBuilder()
				.showThreadInfo(true)
				.logStrategy(new LogCatStrategy())
				.methodCount(4)
				.tag("ERROR_LOG")
				.build();

		Logger.addLogAdapter(new AndroidLogAdapter(errorStrategy) {
			@Override
			public boolean isLoggable(int priority, String tag) {
				if (priority == Logger.ERROR) {
					return BuildConfig.DEBUG;
				}
				return false;
			}
		});
	}


	public static void saveLog(){

		Logger.clearLogAdapters();
		PrettyFormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
				.showThreadInfo(false)
				.methodCount(0)
				.tag("My custom tag")   // LOG TAG默认是PRETTYLOGGER
				.methodOffset(0)     // (Optional) Skips some method invokes in stack trace. Default 0
				.logStrategy(new RBLogUtil.LogCatStrategy())
				.build();

		Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy){
			@Override
			public boolean isLoggable(int priority, String tag) {
				return BuildConfig.DEBUG;
			}
		});

		Logger.addLogAdapter(new DiskLogAdapter());

	}


	/**
	 * 使用自定义标签
	 * RBLogUtil.t("tag").e("Custom tag for only one use");
	 * RBLogUtil.t("tag").e("message %s ", "Custom tag for only one use");
	 *
	 * E/7PRETTY_LOGGER-tag: ┌─────────────────────────────────
	 * E/0PRETTY_LOGGER-tag: │ Custom tag for only one use
	 * E/5PRETTY_LOGGER-tag: └─────────────────────────────────
	 */
	public static Printer t(String tag) {
		return Logger.t(tag);
	}

	/**
	 * General log function that accepts all configurations as parameter
	 */
	public static void log(int priority, String tag, String message, Throwable throwable) {
		Logger.log(priority, tag, message, throwable);
	}

	/**
	 * RBLogUtil.d("no thread info and method info");
	 *  ┌────────────────────────────
	 *  │ no thread info and method info
	 *  └────────────────────────────
	 * RBLogUtil.d("message is -> %s ", "no thread info and method info");
	 *  ┌────────────────────────────
	 *  │ message is -> no thread info and method info
	 *  └────────────────────────────
	 *
	 * @param message
	 * @param args
	 */
	public static void d(String message, Object... args) {
		Logger.d(message, args);
	}

	public static void d(Object object) {
		Logger.d(object);
	}

	public static void e(String message, Object... args) {
		Logger.e(null, message, args);
	}

	public static void e(Throwable throwable, String message, Object... args) {
		Logger.e(throwable, message, args);
	}

	/**
	 * RBLogUtil.i("no thread info and method info");
	 *  ┌────────────────────────────
	 *  │ no thread info and method info
	 *  └────────────────────────────
	 * RBLogUtil.i("message is -> %s ", "no thread info and method info");
	 *  ┌────────────────────────────
	 *  │ message is -> no thread info and method info
	 *  └────────────────────────────
	 *
	 * @param message
	 * @param args
	 */
	public static void i(String message, Object... args) {
		Logger.i(message, args);
	}

	public static void v(String message, Object... args) {
		Logger.v(message, args);
	}

	public static void w(String message, Object... args) {
		Logger.w(message, args);
	}

	/**
	 * Formats the given xml content and print it
	 */
	public static void xml(String xml) {
		Logger.xml(xml);
	}

	/**
	 * ┌────────────────────────────
	 * │ {
	 * │   "key": 3,
	 * │   "value": "something"
	 * │ }
	 * └────────────────────────────
	 * @param json
	 */
	public static void json(String json){
		Logger.json(json);
	}


	/**
	 * RBLogUtil.map(map);
	 *┌────────────────────────────
	 *│ {key=value, key1=value2}
	 *└────────────────────────────
	 */
	public static void map(Object map){
		Logger.d(map);
	}

	/**
	 * message使用java格式化字符
	 * %s 字符串类型
	 * %c 字符类型 等
	 *
	 * RBLogUtil.map("map B =  %s", map);
	 *┌────────────────────────────
	 *│ map B = {key=value, key1=value2}
	 *└────────────────────────────
	 */
	public static void map(String message, Object map){
		Logger.d(message, map);
		//Logger.e(message, map);
	}

	/**
	 * RBLogUtil.list(Arrays.asList("foo", "bar"));
	 *┌────────────────────────────
	 *│ [foo, bar]
	 *└────────────────────────────
	 */
	public static void list(Object list) {
		Logger.d(list);
	}

	/**
	 * message使用java格式化字符
	 * %s 字符串类型
	 * %c 字符类型 等
	 *
	 * RBLogUtil.list("list A =  %s", Arrays.asList("foo", "bar"));
	 *┌────────────────────────────
	 *│ list A = [foo, bar]
	 *└────────────────────────────
	 */
	public static void list(String message, Object list) {
		Logger.d(message, list);
		//Logger.e(message, list);
	}


	//----------------------------------logger end-----------------------------------------

	//---------------------------------------------------------------------------

	public static void resetTime() {
		if (timeMillions == 0L) {
			timeMillions = System.currentTimeMillis();
		}
	}

	public static void dt() {
		if (!isDebug)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		Log.d(TAG, tag + " , 耗时：" + getTimeUse() + " ms " + Thread.currentThread().getName());
		savelog2File(tag + " , 耗时：" + getTimeUse() + " ms " + Thread.currentThread().getName());
	}

	/**
	 * 获取方法调用堆栈
	 *
	 * @see #getStackTraceElement()
	 */
	public static void dt1() {
		if (!isDebug)
			return;
		StackTraceElement[] caller = getStackTraceElement();
		String tag = generateTag(caller);
		Log.d(TAG, tag + " , 耗时：" + getTimeUse() + " ms " + Thread.currentThread().getName());
		savelog2File(tag + " , 耗时：" + getTimeUse() + " ms " + Thread.currentThread().getName());
	}

	public static void dt(Object content) {
		if (!isDebug)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		Log.d(TAG, tag + content + " , 耗时：" + getTimeUse() + " ms " + Thread.currentThread().getName());
		savelog2File(tag + content + " , 耗时：" + getTimeUse() + " ms " + Thread.currentThread().getName());
	}

	public static void et(Object content) {
		if (!allowE)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		Log.e(TAG, tag + content + " , 耗时：" + getTimeUse() + " ms " + Thread.currentThread().getName());
		savelog2File(tag + content + " , 耗时：" + getTimeUse() + " ms " + Thread.currentThread().getName());
	}

	public static StackTraceElement getCallerStackTraceElement() {
		return Thread.currentThread().getStackTrace()[4];
	}

	/**
	 * @return StackTraceElement[]
	 */
	public static StackTraceElement[] getStackTraceElement() {
		return Thread.currentThread().getStackTrace();
	}

	/**
	 * 保存信息到文件中
	 */
	// 用于格式化日期,作为日志文件名的一部分
	@SuppressLint("SimpleDateFormat")
	private static DateFormat formatter = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
	private static void savelog2File(String info) {

		if(!savelog) return;
		try {
			String time = formatter.format(new Date());
			//打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + "logger";
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				String fileName = path + File.separatorChar +  "log.log";
				FileWriter writer = new FileWriter(fileName, true);
				writer.write(time + " " + info + "\n");
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static long timeMillions = 0L;

	private static long getTimeUse() {

		long t = 0;
		if (timeMillions == 0L) {
			timeMillions = System.currentTimeMillis();
		}
		t = System.currentTimeMillis() - timeMillions;
		timeMillions = System.currentTimeMillis();
		return t;
	}

	private static String generateTag(StackTraceElement caller) {
		String tag = "%s.%s(L:%d)";
		String callerClazzName = caller.getClassName();
		callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
		tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
		tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
		return tag + " ---> ";
	}

	/**
	 * 获取调用当前方法的前5步
	 *
	 * @param caller
	 * @return
	 */
	private static String generateTag(StackTraceElement[] caller) {
		String tag = " ";
		String formatString = "%s.%s(L:%d)";
		StringBuilder tempString = new StringBuilder();
		for (int i = 4; i <= 8; i++) {
			StackTraceElement callers = caller[i];
			String callerClazzName = callers.getClassName();
			callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
			tag = String.format(formatString, callerClazzName, callers.getMethodName(), callers.getLineNumber());
			tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
			tempString.append("   " + tag + " \n ");
		}
		return " ----------> \n " + tempString.toString() + " ----------> ";
	}

	/**
	 * 输出日志
	 *
	 * @param tag tag
	 * @param msg msg
	 */
	public static void printLog(String tag, String msg) {
		// 获取当前log的长度
		int msgSize = msg.length();
		if (msgSize <= MAX_SIZE) {
			Log.e(tag, msg);
			return;
		}
		//如果超出最大值,看需要输出几次
		int count = msgSize / MAX_SIZE;
		int remainder = msgSize % MAX_SIZE;
		if (remainder != 0) {
			count++;
		}
		//遍历输出
		for (int i = 0; i < count; i++) {
			int start = i * MAX_SIZE;
			int end = (i + 1) * MAX_SIZE;
			end = Math.min(end, msgSize);
			Log.e(tag, msg.substring(start, end));
		}

		savelog2File(msg + Thread.currentThread().getName());
	}

	//---------------------------------------------------------------------------


	/**
	 * 解决AndroidStudio Log窗口可能出现的换行错乱问题
	 */
	public static class LogCatStrategy implements LogStrategy {

		private int last;

		@Override
		public void log(int priority, String tag, String message) {
			Log.println(priority, randomKey() + tag, message);
		}

		private String randomKey() {
			int random = new Random().nextInt(10);
			if (random == last) {
				random = (random + 1) % 10;
			}
			last = random;
			return String.valueOf(random);
		}
	}
}
