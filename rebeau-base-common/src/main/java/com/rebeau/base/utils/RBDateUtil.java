/*********************************************************************/
/*  文件名  RBDateUtil 　                                              */
/*  程序名  日期工具类                                                */
/*  改版记录   2014/08/25  新建                           谷建德      */
/*         Copyright 2014 DLIN. All Rights Reserved.                 */
/*********************************************************************/

package com.rebeau.base.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("SimpleDateFormat")
public class RBDateUtil {

	/**
	 * LOG用TAG参数
	 */
	private final static String TAG = RBDateUtil.class.getSimpleName();
	public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_YYYY_MM_DD ="yyyy/MM/dd";
	public static final String FORMAT_WITH_Z = "yyyy-MM-dd HH:mm:ss Z";
	/**
	 * 默认日期格式
	 */
	public static final SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 存放数字与汉字对应的map
	 */
	private static Map<String, String> valueMap;
	/**
	 * 默认日期时间格式
	 */
	private static final SimpleDateFormat format2YMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final SimpleDateFormat format2YMDHM = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private static final SimpleDateFormat format2MDHM = new SimpleDateFormat("M月d日 HH:mm");

	public static final SimpleDateFormat formatMD = new SimpleDateFormat("MM-dd");

	private RBDateUtil() {
	}

	/**
	 * 获取当前的时间 格式化：YYYY-MM-DD
	 *
	 * @return 获取当前的格式化后的时间
	 */
	public static String getFileFolderNameByTime() {
		return formatYMD.format(new Date());
	}

	/**
	 * 获取当前的时间
	 * 
	 * @return 当前的时间YYY-MM-DD HH-MM-SS
	 */
	public static String getDate() {

		return format2YMDHMS.format(new Date());
	}

	public static String getMonthDayHourMinute() {

		return format2MDHM.format(new Date());
	}

	public static String str2Time(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}

		try {
			Date date = formatYMD.parse(str);
			return String.valueOf(date.getTime());
		} catch (Exception e) {
			return str;
		}
	}

	public static String time2str(long time) {
		if (time <= 0) {
			return null;
		}

		Calendar c = Calendar.getInstance();
		Date date = new Date(time);
		c.setTime(date);
		return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 字符串转成日期
	 * 
	 * @param str
	 *            日期字符串（yyyy-MM-dd）
	 * @return 日期
	 * @throws ParseException
	 *             格式错误
	 */
	public static Date strToDate(String str) {
		if (str == null || str.trim().length() == 0) {
			return null;
		}
		Date date = null;
		try {
			date = formatYMD.parse(str);
		} catch (ParseException e) {
			System.out.println(e.toString());
		}
		return date;
	}

	/**
	 * date转年月日
	 *
	 * @param date
	 *            Date
	 * @return 年月日的字符串
	 */
	public static String dateToStr(Date date) {
		if (date == null) {
			return null;
		}
		return formatYMD.format(date);
	}

	/**
	 * date转月日
	 *
	 * @param date
	 *            Date
	 * @return 年月日的字符串
	 */
	public static String dateToStrMD(Date date) {
		if (date == null) {
			return null;
		}
		return formatMD.format(date);
	}

	/**
	 * date转年月日 时分秒
	 * 
	 * @param date
	 *            Date
	 * @return 年月日时分秒的字符串
	 */
	public static String dateToStrHMS(Date date) {// yyyy-MM-dd HH:mm:ss
		if (date == null) {
			return null;
		}
		String s = format2YMDHMS.format(date);
		// // 。0的问题
		if (s.length() > 19)
			s = s.substring(0, 19);
		return s;
	}

	/**
	 * 字符串(yyyy-MM-dd HH:mm)格式成Date
	 * 
	 * @param str
	 *            原始日期
	 * @return 格式化后的日期
	 * @throws Exception
	 *             日期格式异常
	 */
	public static Date strToDateYMDHM(String str) throws ParseException {
		if (str == null) {
			return null;
		}
		Date date = format2YMDHM.parse(str);
		return date;
	}

	/**
	 * 计算年龄
	 * 
	 * @param d1
	 *            出生日期
	 * @return
	 */
	public static int compareDate(Date d1) {
		Date date = new Date();
		return date.getYear() - d1.getYear();
	}

	public static boolean equals(Date d1, Date d2) {
		if (d1 == null && d2 == null) {
			return true;
		}
		if (d1 == null && d2 != null) {
			return false;
		}
		if (d1 != null && d2 == null) {
			return false;
		}
		return d1.equals(d2);
	}

	/**
	 * 将日期转为汉字
	 * 
	 * @param d
	 *            需要转换的日期
	 * @return 转好的汉字日期
	 */
	public static String timeFormat(Date d) {
		String time;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		time = sdf.format(d);
		valueMap = new HashMap<String, String>();
		valueMap.put("1", "一");
		valueMap.put("2", "二");
		valueMap.put("3", "三");
		valueMap.put("4", "四");
		valueMap.put("5", "五");
		valueMap.put("6", "六");
		valueMap.put("7", "七");
		valueMap.put("8", "八");
		valueMap.put("9", "九");
		valueMap.put("0", "零");

		String[] timeArray = time.split("-");
		time = praiseTimeArray(timeArray);

		return time;

	};

	/**
	 * 将time的数组转为String的大写+年月日返回
	 * 
	 * @param timeArray
	 *            时间的数组
	 * @return 数字和年月日大写的String
	 */
	private static String praiseTimeArray(String[] timeArray) {
		String numberString = "";
		String mTime = "年月日";
		for (int j = 0; j < timeArray.length; j++) {
			for (int i = 0; i < timeArray[j].length(); i++) {
				// 如果是月、日，并且首位是0，则不显示
				if (j == 1 || j == 2) {
					if (timeArray[j].charAt(0) == '0') {
						numberString = numberString + valueMap.get(String.valueOf(timeArray[j].charAt(1)));
					} else if (timeArray[j].charAt(0) == '1') {
						if (timeArray[j].charAt(1) != '0')
							numberString = numberString + "十" + valueMap.get(String.valueOf(timeArray[j].charAt(1)));
						else
							numberString = numberString + "十";
					} else if (timeArray[j].charAt(0) == '2') {
						if (timeArray[j].charAt(1) != '0')
							numberString = numberString + "二十" + valueMap.get(String.valueOf(timeArray[j].charAt(1)));
						else
							numberString = numberString + "二十";
					} else if (timeArray[j].charAt(0) == '3')
						if (timeArray[j].charAt(1) != '0')
							numberString = numberString + "三十" + valueMap.get(String.valueOf(timeArray[j].charAt(1)));
						else
							numberString = numberString + "三十";
					break;
				}
				numberString = numberString + valueMap.get(String.valueOf(timeArray[j].charAt(i)));
			}
			numberString += mTime.charAt(j);
		}
		return numberString;

	}

	public static Date str2Date(String str) {
		return str2Date(str, null);
	}

	public static Date str2Date(String str, String format) {
		if (str == null || str.length() == 0) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(str);

		} catch (Exception e) {
			RBLogUtil.et("error");
		}
		return date;
	}

	public static Calendar str2Calendar(String str) {
		return str2Calendar(str, null);
	}

	public static Calendar str2Calendar(String str, String format) {
		Date date = str2Date(str, format);
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}

	public static String date2Str(Calendar c) {// yyyy-MM-dd HH:mm:ss
		return date2Str(c, null);
	}

	public static String date2Str(Calendar c, String format) {
		if (c == null) {
			return null;
		}
		return date2Str(c.getTime(), format);
	}

	public static String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
		return date2Str(d, null);
	}

	/**
	 * 日期转字符串
	 */
	public static String date2Str(Date d, String format) {
		if (d == null) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String s = sdf.format(d);
		return s;
	}

	public static String getCur2DateStr(Context context) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日";
	}

	public static String getCurDateStr() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + "-"
				+ c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
	}

	public static String getCurDateStr(String format) {
		Calendar c = Calendar.getInstance();
		c.isLenient();
		return date2Str(c, format);
	}

	public static String getMillon(long time) {
		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);
	}

	public static String getDay(long time) {
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}

	public static String getSMillon(long time) {
		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(time);
	}

	public static boolean isDateString(String dateStr) {

		boolean isDate = true;
		Date date = null;
		try {
			date = str2Date(dateStr, FORMAT_WITH_Z);
		} catch (Exception e) {
			RBLogUtil.et("error");
			isDate = false;
		}
		if (date == null) {
			isDate = false;
		}
		return isDate;

	}
	
	/**
	 * 比较当前时间是不是大于24小时
	 * @param lastDateStr 上次保存的时间
	 * @return
	 */
	
	public static boolean compareDate24(String lastDateStr){
		if(RBStringUtil.isEmpty(lastDateStr)){
			return true;
		}else{
			long now = System.currentTimeMillis();
			
			Date d = str2Date(lastDateStr, FORMAT_WITH_Z);
			long last = d.getTime();
			int hour =(int)(now-last)/(1000*60*60);
			if(hour>=24){
				return true;
			}else{
				return false;
			}
		}
	}
}
