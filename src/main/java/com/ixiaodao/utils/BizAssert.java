package com.ixiaodao.utils;


import org.apache.commons.lang3.StringUtils;

/**
 * 1
 * @author jinwenbiao
 * @since 2022/4/1 14:29
 */
public class BizAssert {

	public static void notNull(Object object, String msg) {
		if (object == null) {
			throw new RuntimeException(msg);
		}
	}

	public static void notEmpty(String s, String msg) {
		if (StringUtils.isEmpty(s)) {
			throw new RuntimeException(msg);
		}
	}

}
