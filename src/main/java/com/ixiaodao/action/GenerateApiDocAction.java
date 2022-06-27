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
import com.ixiaodao.model.ReturnClassNameVO;
import com.ixiaodao.ui.CarTestConfig;
import com.ixiaodao.utils.DocCommentUtil;
import com.ixiaodao.utils.JavaTypeUtil;
import com.ixiaodao.utils.MyStringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import java.util.List;

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
			String beanId = null;
			if (containingClass != null) {
				// 服务代码
				serviceName = containingClass.getQualifiedName();
				String name = containingClass.getName();
				beanId = MyStringUtil.toLowerCaseFirstOne(name);
			}
			PsiParameterList parameterList = psiMethod.getParameterList();
			PsiParameter[] parameters = parameterList.getParameters();
			if (parameters.length > 1) {
				Messages.showErrorDialog("Param个数不能超过1个", "提示");
				return;
			}
			String paramClassName;
			if (parameters.length == 0) {
				paramClassName = null;
			} else {
				paramClassName = parameters[0].getType().getCanonicalText();
			}
			String returnClassName = null;
			PsiType returnType = psiMethod.getReturnType();
			if (returnType != null) {
				returnClassName = returnType.getCanonicalText();
			}

			CarTestConfigVO carTestConfigVO = new CarTestConfigVO();
			carTestConfigVO.setName(desc);
			carTestConfigVO.setDesc(desc);
			carTestConfigVO.setServiceName(serviceName);
			carTestConfigVO.setBeanId(beanId);
			carTestConfigVO.setMethodName(psiMethod.getName());
			carTestConfigVO.setParamClassName(paramClassName);
			carTestConfigVO.setReturnClassName(returnClassName);

			PsiClass onePsiClassByClassName = PsiClassCarTestHelper.findOnePsiClassByClassName(carTestConfigVO.getParamClassName(), project);
			List<CarTestVO> paramList = null;
			if (onePsiClassByClassName != null) {
				paramList = PsiClassCarTestHelper.create(onePsiClassByClassName).convertClassToCarTestList(project, false);
			}

			ReturnClassNameVO returnClassNameVO = getReturnString(returnType, project);
			List<CarTestVO> returnList = null;

			if (returnClassNameVO != null) {
				carTestConfigVO.setCommonClassName(returnClassNameVO.getResultName());
				if ("java.util.List".equals(returnClassNameVO.getResultName())) {
					carTestConfigVO.setCommonClassName(null);
				}
				PsiClass returnClass = PsiClassCarTestHelper.findOnePsiClassByClassName(returnClassNameVO.getClassName(), project);
				if (returnClass != null) {
					List<CarTestVO> carTestVOS = PsiClassCarTestHelper.create(returnClass).convertClassToCarTestList(project, false);
					if (CollectionUtils.isNotEmpty(carTestVOS)) {
						returnList = carTestVOS;
					}
				}
			}
			new CarTestConfig(project, carTestConfigVO, paramList, returnList).show();
		}
	}

	private ReturnClassNameVO getReturnString(PsiType returnType, Project project) {
		if (returnType == null) {
			return null;
		}
		String canonicalText = returnType.getCanonicalText();
		if ("void".equals(canonicalText)) {
			return null;
		}
		return JavaTypeUtil.getReturnClassName(canonicalText);
	}

	@Override
	public void update(@NotNull AnActionEvent e) {
		PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);
		boolean visible = psiElement instanceof PsiMethod;
		e.getPresentation().setVisible(visible);
	}

}
