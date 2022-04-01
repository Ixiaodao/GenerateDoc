package com.ixiaodao.action;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiType;
import com.ixiaodao.hepler.PsiClassCarTestHelper;
import com.ixiaodao.model.CarTestConfigVO;
import com.ixiaodao.model.CarTestVO;
import com.ixiaodao.ui.CarTestConfig;
import com.ixiaodao.utils.DocCommentUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1
 * @author jinwenbiao
 * @since 2022/3/30 20:20
 */
public class GenerateApiDocAction extends AnAction {

	private static final String reg = "(.*?)\\<(.*)\\>";

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		Project project = e.getProject();
		if (project == null) {
			return;
		}
		//Messages.showWarningDialog("Can't Select Template!", GlobalDict.TITLE_INFO);
		PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);
		if (psiElement instanceof PsiMethod) {
			PsiMethod psiMethod = (PsiMethod) psiElement;
			//
			String desc = DocCommentUtil.getDoc(psiMethod);

			PsiClass containingClass = psiMethod.getContainingClass();
			String serviceName = null;
			if (containingClass != null) {
				// 服务代码
				serviceName = containingClass.getQualifiedName() + "#" + psiMethod.getName();
			}
			PsiParameterList parameterList = psiMethod.getParameterList();
			PsiParameter[] parameters = parameterList.getParameters();
			if (parameters.length > 1) {
				Messages.showWarningDialog("Param个数不能超过1个", "提示");
				return;
			}
			PsiParameter parameter = parameters[0];
			String returnClassName = null;
			String commonClassName = null;
			PsiType returnType = psiMethod.getReturnType();
			if (returnType != null) {
				returnClassName = returnType.getCanonicalText();
			}

			CarTestConfigVO carTestConfigVO = new CarTestConfigVO();
			carTestConfigVO.setName(desc);
			carTestConfigVO.setDesc(desc);
			carTestConfigVO.setServiceName(serviceName);
			carTestConfigVO.setBeanId(serviceName);
			carTestConfigVO.setMethodName(psiMethod.getName());
			carTestConfigVO.setParamClassName(parameter.getType().getCanonicalText());
			carTestConfigVO.setReturnClassName(returnClassName);
			carTestConfigVO.setCommonClassName(commonClassName);




			PsiClass onePsiClassByClassName = PsiClassCarTestHelper.findOnePsiClassByClassName(carTestConfigVO.getParamClassName(), project);

			List<CarTestVO> paramList = null;
			if (onePsiClassByClassName != null) {
				paramList = PsiClassCarTestHelper.create(onePsiClassByClassName).convertClassToCarTestList(project, false);
				if (paramList != null) {
					for (CarTestVO carTestVO : paramList) {
						carTestVO.setType(1);
					}
				}
			}

			List<CarTestVO> returnList = getReturnString(returnType, project);
			if (returnList != null) {
				for (CarTestVO carTestVO : returnList) {
					carTestVO.setType(2);
				}
			}

			new CarTestConfig(project, carTestConfigVO, paramList, returnList).show();
		}
	}

	private List<CarTestVO> getReturnString(PsiType returnType, Project project) {
		if (returnType == null) {
			return null;
		}
		String canonicalText = returnType.getCanonicalText();
		if ("void".equals(canonicalText)) {
			return null;
		}
		final Pattern pattern = Pattern.compile(reg);
		final Matcher matcher = pattern.matcher(canonicalText);
		if (matcher.find()) {
			String group1 = matcher.group(1);
			String group2 = matcher.group(2);
			if (group2.contains("java.util")) {
				final Pattern patternNew = Pattern.compile(reg);
				final Matcher matcherNew = pattern.matcher(canonicalText);
				if (matcherNew.find()) {
					String group = matcherNew.group(2);
					if (!group.startsWith("java.util")) {
						PsiClass onePsiClassByClassName = PsiClassCarTestHelper.findOnePsiClassByClassName(group, project);
						if (onePsiClassByClassName != null) {
							return PsiClassCarTestHelper.create(onePsiClassByClassName).convertClassToCarTestList(project, false);
						}
					} else {

					}
				}
			}
		} else {
			PsiClass onePsiClassByClassName = PsiClassCarTestHelper.findOnePsiClassByClassName(canonicalText, project);
			if (onePsiClassByClassName != null) {
				return PsiClassCarTestHelper.create(onePsiClassByClassName).convertClassToCarTestList(project, false);
			}
		}
		return null;
	}

	@Override
	public void update(@NotNull AnActionEvent e) {
		PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);
		boolean visible = psiElement instanceof PsiMethod;
		e.getPresentation().setVisible(visible);
	}

}
