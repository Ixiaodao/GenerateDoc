package com.ixiaodao.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.ixiaodao.hepler.PsiClassCarTestHelper;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.StringSelection;

public class ConvertClassToCarTestJSONCompressedAction extends AbstractBaseAction {
    @Override
    public void actionPerformed(AnActionEvent e) {

        PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);
        PsiClass psiClass = getPsiClass(psiElement);

        if(psiClass == null) return;

        String json = PsiClassCarTestHelper.create(psiClass).convertClassToCarTestJSON(myProject(e), false);
        CopyPasteManager.getInstance().setContents(new StringSelection(json));
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);
        boolean flag = psiElement instanceof PsiClass;
        e.getPresentation().setVisible(flag);
    }

}
