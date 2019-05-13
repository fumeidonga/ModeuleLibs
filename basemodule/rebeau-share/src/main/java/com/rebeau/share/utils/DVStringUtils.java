/*********************************************************************/
/*  文件名  DVStringUtils.java    　                                   */
/*  程序名  字符串工具类                     						 */
/*  版本履历   2014/09/25  新建                  陈豪    			 */
/* 		       2014/09/26  UI1.0变更             陈豪  				 */
/*         Copyright 2014 DILIN. All Rights Reserved.                */
/*********************************************************************/
package com.rebeau.share.utils;

/**
 * 
 * 字符串工具类
 * 
 */
public class DVStringUtils {
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
}
