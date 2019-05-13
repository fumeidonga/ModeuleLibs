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

package com.rebeau.share.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Log工具，类似android.util.Log。 tag自动产生，格式:
 * customTagPrefix:className.methodName(L:lineNumber),
 * customTagPrefix为空时只输出：className.methodName(L:lineNumber)。
 */
public class DVLogUtils {

	public static String customTagPrefix = "";
	public final static String TAG = "hrl";

	private DVLogUtils() {
		throw new UnsupportedOperationException("DVLogUtils can't instantiate me...");
	}

	public static boolean allowD = true;

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
	 * @param caller
	 * @return
	 */
	private static String generateTag(StackTraceElement[] caller) {
		String tag = " ";
		String formatString = "%s.%s(L:%d)";
		StringBuilder tempString = new StringBuilder();
		//for (int i = 4; i <= caller.length - 1; i++) {
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

	public static void d() {
		if (!allowD)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);
		Log.d(TAG, tag);
	}

	public static void d(Object content) {
		if (!allowD)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		Log.d(TAG, tag + content);
	}

	public static StackTraceElement getCallerStackTraceElement() {
		return Thread.currentThread().getStackTrace()[4];
	}

}
