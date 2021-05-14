package com.zhaow.restful.action;


import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.zhaow.restful.dialog.SampleDialogWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * @author jiwenbiao
 * @since : 2021/5/14 14:31
 */
public class ContextPathAction extends AnAction implements DumbAware {
    public ContextPathAction() {
        super("Set ContextPath", "Set project contextPath", AllIcons.General.Settings);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        new SampleDialogWrapper().showAndGet();
    }

}
