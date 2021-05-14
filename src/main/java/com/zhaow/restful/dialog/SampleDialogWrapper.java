package com.zhaow.restful.dialog;


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

    public JTextField field = null;
    public SampleDialogWrapper() {
        super(true); // use current window as parent
        init();
        setTitle("ContextPathDialog");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());
        field = new JTextField();
        field.setText(Settings.getInstance().getContextPath());
        dialogPanel.add(field, BorderLayout.CENTER);

        return dialogPanel;
    }

    @Override
    protected void doOKAction() {
        Settings.getInstance().setContextPath(field.getText());
        this.close(OK_EXIT_CODE);
    }

    @Override
    public void doCancelAction() {
        this.close(CANCEL_EXIT_CODE);
    }

}
