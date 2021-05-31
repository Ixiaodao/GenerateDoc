package com.zhaow.restful.dialog;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.zhaow.restful.setting.Settings;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author jiwenbiao
 * @since : 2021/5/14 14:28
 */
public class SampleDialogWrapper extends DialogWrapper {
    private final Project project;
    public JTextField field = null;
    public SampleDialogWrapper(Project project) {
        super(true); // use current window as parent
        setTitle("ContextPathDialog");
        this.project = project;
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());
        field = new JTextField();
        Settings settings = Settings.getInstance(project);
        if (settings != null) {
            field.setText(settings.getContextPath());
        }
        dialogPanel.add(field, BorderLayout.CENTER);

        return dialogPanel;
    }

    @Override
    protected void doOKAction() {
        Settings settings = Settings.getInstance(this.project);
        if (settings != null) {
            settings.setContextPath(field.getText());
        }
        this.close(OK_EXIT_CODE);
    }

    @Override
    public void doCancelAction() {
        this.close(CANCEL_EXIT_CODE);
    }

}
