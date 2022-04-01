package com.ixiaodao.utils;


import com.intellij.psi.PsiClass;
import com.ixiaodao.model.ReturnClassNameVO;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1
 * @author jinwenbiao
 * @since 2022/4/1 13:56
 */
public class JavaTypeUtil {
	private static final String REG_COUNT1 = "(.*)\\<(.*)\\>";
	private static final String REG_COUNT2 = "(.*)\\<(?:(.*)\\<(.*)\\>)\\>";


	public static boolean isJavaBaseType (PsiClass psiClass) {
		String qualifiedName = psiClass.getQualifiedName();
		return qualifiedName == null
				|| qualifiedName.startsWith("java.lang")
				|| qualifiedName.startsWith("java.math");
	}

	public static boolean isJavaBaseType (String qualifiedName) {
		return qualifiedName == null
				|| qualifiedName.startsWith("java.lang")
				|| qualifiedName.startsWith("java.math");
	}

	public static ReturnClassNameVO getReturnClassName (String returnClassName) {
		int i = StringUtils.countMatches(returnClassName, "<");
		if (i == 0) {
			return new ReturnClassNameVO(null, null, returnClassName);
		}
		if (i == 1) {
			Pattern pattern = Pattern.compile(REG_COUNT1);
			Matcher matcher = pattern.matcher(returnClassName);
			if (matcher.find()) {
				String result = matcher.group(1);
				String className = matcher.group(2);
				if (JavaTypeUtil.isJavaBaseType(result)) {
					return new ReturnClassNameVO(null, null, className);
				}
				return new ReturnClassNameVO(result, null, className);
			}
		}
		if (i == 2) {
			Pattern pattern = Pattern.compile(REG_COUNT2);
			Matcher matcher = pattern.matcher(returnClassName);
			if (matcher.find()) {
				String result = matcher.group(1);
				String listName = matcher.group(2);
				String className = matcher.group(3);
				return new ReturnClassNameVO(result, listName, className);
			}
		}
		return new ReturnClassNameVO();
	}
}
