/*********************************************************************/
/*  文件名  RBStringUtil.java    　                                   */
/*  程序名  字符串工具类                     						 */
/*  版本履历   2014/09/25  新建                  陈豪    			 */
/* 		       2014/09/26  UI1.0变更             陈豪  				 */
/*         Copyright 2014 DILIN. All Rights Reserved.                */
/*********************************************************************/
package com.rebeau.base.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 字符串工具类
 * 
 */
public class RBStringUtil {
	/**
	 * 随机实例
	 */
	private static final Random DEFULT_RANDOM = new Random();

	/**
	 * 判断字符串不为空并且不为提示字符
	 * 
	 * @param value
	 *            字符串
	 * @param hint
	 *            提示字符
	 * @return true:不为空且不为提示字符，false:为空或为提示字符
	 */
	public static boolean validateString(String value, String hint) {
		if (!isEmpty(value) && !value.equals(hint)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串不为空并且不为提示字符
	 * 
	 * @param value
	 *            字符串
	 * @param hint
	 *            提示字符
	 * @return true:不为空且不为提示字符，false:为空或为提示字符
	 */
	public static boolean validateString(Object value, String hint) {
		if (!isEmpty(value) && !value.equals(hint)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param value
	 *            字符串
	 * @return true:为空,false:不为空
	 */
	public static boolean isEmpty(Object value) {
		if (value == null || value.toString().trim().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 去除制表符、空格、回车、换行
	 * 
	 * @param str
	 *            原字符串
	 * @return 去除制表符后的字符串
	 */
	public static String filterBlankTag(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 按Byte位数截取字符串
	 * 
	 * @param str
	 *            字符串
	 * @param byteLen
	 *            byte位数
	 * @param paddingSuffix
	 *            超长补位符，一般为省略号
	 * @return 截取后的字符串
	 */
	public static String subStrB(String str, int byteLen, String paddingSuffix) {
		if (str == null) {
			return str;
		}
		int suffixLen = paddingSuffix.getBytes().length;

		StringBuffer sbuffer = new StringBuffer();
		char[] chr = str.trim().toCharArray();
		int len = 0;
		for (int i = 0; i < chr.length; i++) {
			if (chr[i] >= 0xa1) {
				len += 2;
			} else {
				len++;
			}
		}

		if (len <= byteLen) {
			return str;
		}

		len = 0;
		for (int i = 0; i < chr.length; i++) {

			if (chr[i] >= 0xa1) {
				len += 2;
				if (len + suffixLen > byteLen) {
					break;
				} else {
					sbuffer.append(chr[i]);
				}
			} else {
				len++;
				if (len + suffixLen > byteLen) {
					break;
				} else {
					sbuffer.append(chr[i]);
				}
			}
		}
		sbuffer.append(paddingSuffix);
		return sbuffer.toString();
	}

	/**
	 * 判断字符串是否正确格式的手机号码
	 * 
	 * @param mobiles
	 *            手机号码
	 * @return true:格式正确,false:格式错误
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isMobile(String mobiles) {
		Pattern p = Pattern.compile("^[\\+]?\\d*((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	/**
	 * 判断字符串是否正确的邮箱
	 * 
	 * @param mail 邮箱字符串
	 * @return true:格式正确,false:格式错误
	 */
	public static boolean isMail(String mail){
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(mail);
		return matcher.matches();
	}

	/**
	 * 将num中的手机号码给屏蔽掉
	 * 
	 * @param sParam
	 * @return
	 */
	public static String checkNum(String sParam) {
		
		if(sParam == null ||sParam.length()<=0){
			return "";
		}
		Pattern pattern = Pattern.compile("(1|861)(3|5|8)\\d{9}$*");
		Matcher matcher = pattern.matcher(sParam);
		StringBuffer bf = new StringBuffer();
		while (matcher.find()) {
			bf.append(matcher.group()).append(",");
		}
		int len = bf.length();
		if (len > 0) {
			bf.deleteCharAt(len - 1);
		}
		if(bf.length()>0){
			String phoneArray[] = bf.toString().split(",");
			for(int i=0;i<phoneArray.length;i++){
				sParam = sParam.replace(phoneArray[i], formatPhone(phoneArray[i]));
			}
		}
		
		return sParam;
	}
	
	public static String  formatPhone(String phone){
		if(phone.length()==11){
			return phone.substring(0, 3)+"****"+phone.substring(8,11);
		}else{
			return "";
		}
	}
		
		/*if (num == null || num.length() == 0) {
			return "";
		}
		
		String numsString = num;
		Pattern pattern = Pattern.compile("(?<!\\d)(?:(?:1[0-9]\\d{9})|(?:861[358]\\d{9}))(?!\\d)");
		Matcher matcher = pattern.matcher(num);
		StringBuffer bf = new StringBuffer(64);
		while (matcher.find()) {
			bf.append(matcher.group()).append(",");
		}

		int len = bf.length();
		if (len > 0) {
			bf.deleteCharAt(len - 1);
		}
		String[] ss = bf.toString().split(",");
		String[] ss1 = bf.toString().split(",");
		System.out.println(ss.length);
		for (int i = 0; i < ss.length; i++) {
			ss[i] = blackContentNumber(ss[i]);
			numsString.replace(ss1[i], ss[i]);
			numsString = StrReplace(numsString, ss1[i], ss[i]);
		}
		return numsString;*/
		// return bf.toString();
	

	/**
	 * 屏蔽手机号
	 */
	public static String blackContentNumber(String content) {
		if (content == null || content.length() == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Pattern p_phone = Pattern.compile("(1[0-9]{10})");/* 把1开头11位及以上的数字都匹配出来 */
		Matcher m_phone = p_phone.matcher(content);
		while (m_phone.find()) {
			if (m_phone.group(1).length() == 11) {/* 只把1开头且正好11位的数字替换掉 */
				// Log.e(TAG, content.indexOf(m_phone.group(1)) + " : " +
				// m_phone.group(1));
				String numberblock = content.substring(content.indexOf(m_phone.group(1)),
						content.indexOf(m_phone.group(1)) + 3)
						+ "******"
						+ content.substring(content.indexOf(m_phone.group(1)) + 9,
								content.indexOf(m_phone.group(1)) + 11);
				content = content.replaceFirst(m_phone.group(1), numberblock);
			}

		}
		return content;
	}

	/**
	 * rStr 源字符串 rFix 要查找替换的字符串 rRep 替换成的字符串
	 * 
	 * @param rStr
	 * @param rFix
	 * @param rRep
	 * @return
	 */
	public static String StrReplace(String rStr, String rFix, String rRep) {
		int l = 0;
		String gRtnStr = rStr;
		do {
			l = rStr.indexOf(rFix, l);
			if (l == -1)
				break;
			gRtnStr = rStr.substring(0, l) + rRep + rStr.substring(l + rFix.length());
			l += rRep.length();
			rStr = gRtnStr;
		} while (true);
		return gRtnStr.substring(0, gRtnStr.length());
	}

	/**
	 * 判断包含文字的字符串是否正确格式的手机号码
	 * 
	 * @param chatMsg
	 *            可能包含文字的字符串
	 * @return true:格式正确,false:格式错误
	 */
	public static String isMobileNumber(String chatMsg) {
		// 更改上面的手机正则时勿忘更改此处正则表达式
		Pattern pattern = Pattern
				.compile("(?<!\\d)(((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[6-8])|(18[0-9]))\\d{8})(?!\\d)");
		Matcher matcher = pattern.matcher(chatMsg);

		while (matcher.find()) {
			String number = matcher.group();
			chatMsg = chatMsg.replace(number, number.subSequence(0, 3) + "***" + number.substring(10));
		}
		return chatMsg;
	}

	/**
	 * 生成随机验证码
	 * 
	 * @param len
	 *            验证码位数
	 * @return 随机验证码
	 */
	public static String makeRandom(int len) {

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < len; i++) {
			buffer.append(DEFULT_RANDOM.nextInt(10));
		}
		return buffer.toString();
	}

	/**
	 * 文字变色工具方法
	 * 
	 * @param str
	 *            变色文字
	 * @param color
	 *            颜色
	 * @return 变色后的文字
	 */
	public static SpannableStringBuilder changeColor(String str, String key, int color) {
		if (str == null) {
			return new SpannableStringBuilder("");
		}
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		int pos = str.indexOf(key);
		while (pos != -1) {
			style.setSpan(new ForegroundColorSpan(color), pos, pos + key.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			pos = str.indexOf(key, pos + key.length());
		}
		return style;
	}

	/**
	 * 搜索文字变色效果
	 * 
	 * @param str
	 *            内容
	 * @param key
	 *            变色的关键字
	 * @return 变色后的字符串
	 */
	public static SpannableStringBuilder changeTextColor(String str) {
		if (str == null) {
			return new SpannableStringBuilder("");
		}
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		char[] cs = str.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			style.setSpan(new ForegroundColorSpan(Color.RED), i, (i + 1), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		return style;
	}

	/**
	 * 搜索文字变色效果
	 * 
	 * @param str
	 *            内容
	 * @param key
	 *            变色的关键字
	 * @return 变色后的字符串
	 */
	public static SpannableStringBuilder changeColor(String str, String key) {
		if (str == null) {
			return new SpannableStringBuilder("");
		}
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		int pos = str.indexOf(key);
		while (pos != -1) {
			style.setSpan(new ForegroundColorSpan(Color.RED), pos, pos + key.length(),
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			pos = str.indexOf(key, pos + key.length());
		}
		return style;
	}

	/**
	 * 搜索文字变色效果
	 * 
	 * @param str
	 *            内容
	 * @param key
	 *            变色的关键字
	 * @return 变色后的字符串
	 */
	public static SpannableStringBuilder changeColor(String str, ArrayList<String> keys) {
		if (str == null) {
			return new SpannableStringBuilder("");
		}
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		String strtem = str;
		for (String key : keys) {
			int pos = strtem.toUpperCase().indexOf(key.toUpperCase());
			while (pos != -1) {
				style.setSpan(new ForegroundColorSpan(Color.RED), pos, pos + key.length(),
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				pos = str.indexOf(key, pos + key.length());
			}
		}
		return style;
	}

	/**
	 * 判断以个对象的值是否为null
	 * 
	 * @param object
	 * @return 值为null 则返回" " 否则返回原值
	 */
	public static String getValueFrom(String object) {
		if (object == null)
			return "";
		else
			return object;
	}

	/**
	 * 比对字符串是否为全数字
	 * 
	 * @param str
	 *            需要比对的字符
	 * @return true:格式正确,false:格式错误
	 */
	public static boolean isNumeric(String str) {
		if (!isEmpty(str)){
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher isNum = pattern.matcher(str);
			if (!isNum.matches()) {
				return false;
			}
			return true;
		}else {
			return false;
		}

	}

	/**
	 * 去除BigDecimal 小数后面的0
	 * 
	 * @return
	 */
	public static String modifleNumber(BigDecimal num) {
		DecimalFormat df = new DecimalFormat("0.##");
		if (num != null) {
			return df.format(num);
		}
		return null;
	}

	/**
	 * 判断字符串不为数字和字母
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNumAlpha(String s) {
		if (isNumber(s) || isAlpha(s)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为数字
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isNumber(String number) {
		if (number == null)
			return false;
		return number.matches("[+-]?[1-9]+[0-9]*(\\.[0-9]+)?");
	}

	/**
	 * 判断是否为字母
	 * 
	 * @param alpha
	 * @return
	 */
	public static boolean isAlpha(String alpha) {
		if (alpha == null)
			return false;
		return alpha.matches("[a-zA-Z]+");
	}

	/**
	 * 判断是否为汉字
	 * 
	 * @param chineseContent
	 * @return
	 */
	public static boolean isChinese(String chineseContent) {
		if (chineseContent == null)
			return false;
		return chineseContent.matches("[\u4e00-\u9fa5]");
	}

	/**
	 * 获取字符串中的数字
	 * 
	 * @param str
	 * @return
	 */

	public static String getNumber(String str) {
		String str2 = "";
		if (str != null && !"".equals(str)) {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
					str2 += str.charAt(i);
				}
			}
		}
		return str2;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean contains(String a, String b) {
		if(a == null || b == null || a == "" || b == "")
			return false;
		return a.contains(b);
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(String a, String b) {
		if(a == null || b == null)
			return false;
		return a.equals(b);
	}
}
