package com.ixiaodao.utils;


/**
 * 1
 * @author jinwenbiao
 * @since 2022/4/1 16:49
 */
public class MyStringUtil {
	public static String toLowerCaseFirstOne(String s){
		if (s == null || s.length() == 0) {
			return null;
		}
		if(Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}
}
