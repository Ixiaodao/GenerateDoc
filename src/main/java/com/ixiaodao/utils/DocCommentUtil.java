package com.ixiaodao.utils;


import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocToken;

/**
 * 1
 * @author jinwenbiao
 * @since 2022/3/31 15:36
 */
public class DocCommentUtil {
	public static String getDoc (PsiMethod psiMethod) {
		PsiDocComment docComment = psiMethod.getDocComment();
		if (docComment == null) {
			return null;
		}
		PsiElement[] descriptionElements = docComment.getDescriptionElements();
		if (descriptionElements == null || descriptionElements.length <= 0) {
			return null;
		}
		String desc = "";
		for (PsiElement descriptionElement : descriptionElements) {
			if (descriptionElement instanceof PsiDocToken) {
				desc = desc + descriptionElement.getText() + "    ";
			}
		}
		return desc;
	}
}
