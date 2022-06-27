package com.ixiaodao.ui;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.ixiaodao.model.CarTestConfigVO;
import com.ixiaodao.model.CarTestVO;
import com.ixiaodao.model.Interface;
import com.ixiaodao.model.SettingsVO;
import com.ixiaodao.utils.BizAssert;
import com.ixiaodao.utils.HttpClient;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * 选择保存路径
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class CarTestConfig extends DialogWrapper {
    /**
     * 主面板
     */
    private JPanel contentPane;
    private JTextField jiraId;
    private JTextField name;
    private JTextField desc;
    private JTextField serviceName;
    private JTextField beanId;
    private JTextField methodName;
    private JTextField paramClassName;
    private JTextField returnClassName;
    private JTextField commonClassName;
    private JTextField projectName;
    private JRadioButton dubboRadioButton;
    private JRadioButton prismRadioButton;
    private List<CarTestVO> paramList;
    private List<CarTestVO> returnList;
    private Project project;
    private String tempServiceName;

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return this.contentPane;
    }

    /**
     * 构造方法
     */
    public CarTestConfig(Project project, CarTestConfigVO config, List<CarTestVO> paramList, List<CarTestVO> returnList) {
        super(project);
        init();
        setTitle("配置信息");
        jiraId.setText(config.getJiraId());
        name.setText(config.getName());
        desc.setText(config.getDesc());
        serviceName.setText(config.getServiceName());
        beanId.setText(config.getBeanId());
        methodName.setText(config.getMethodName());
        paramClassName.setText(config.getParamClassName());
        returnClassName.setText(config.getReturnClassName());
        commonClassName.setText(config.getCommonClassName());
        projectName.setText(project.getName());

        this.project = project;
        this.paramList = paramList;
        this.returnList = returnList;

        tempServiceName = serviceName.getText() + "#" + methodName.getText();

        serviceName.setText(tempServiceName);

        dubboRadioButton.setSelected(true);

        dubboRadioButton.addActionListener(e -> {
            JRadioButton button = (JRadioButton)e.getSource();
            if (button.isSelected()) {
                serviceName.setText(tempServiceName);
                prismRadioButton.setSelected(false);
            } else {
                serviceName.setText(project.getName() + "." + beanId.getText() + "." + methodName.getText());
                prismRadioButton.setSelected(true);
            }
        });
        prismRadioButton.addActionListener(e -> {
            JRadioButton button = (JRadioButton)e.getSource();
            if (button.isSelected()) {
                serviceName.setText(project.getName() + "." + beanId.getText() + "." + methodName.getText());
                dubboRadioButton.setSelected(false);
            } else {
                serviceName.setText(tempServiceName);
                dubboRadioButton.setSelected(true);
            }
        });

    }

    @Override
    protected void doOKAction() {
        try {
            onOK();
        } catch (Exception e) {
            Messages.showErrorDialog(e.getMessage(), "提示");
            return;
        }
        Messages.showInfoMessage("成功", "提示");
        super.doOKAction();
    }

    /**
     * 确认按钮回调事件
     */
    private void onOK() {
        validateParam();
        Interface anInterface = new Interface();
        anInterface.setName(name.getText());
        anInterface.setJira_id(jiraId.getText());
        anInterface.setDescription(desc.getText());
        anInterface.setServiceCode(serviceName.getText());
        anInterface.setServiceid(beanId.getText());
        anInterface.setMethod(methodName.getText());
        anInterface.setRequestClassName(paramClassName.getText());
        anInterface.setResponseClassName(returnClassName.getText());
        anInterface.setRequestParam(paramList);
        anInterface.setResponseParam(returnList);
        anInterface.setResponseCommonClass(commonClassName.getText());
        anInterface.setInterfaceType(2);
        anInterface.setProjectId(121);
        anInterface.setProjectName(projectName.getText());

        String addUrl = SettingsVO.getInstance().getAddUrl();
        if (StringUtils.isEmpty(addUrl)) {
            throw new RuntimeException("创建文档url为空");
        }
        HttpClient.sendPost(addUrl, anInterface);
    }

    private void validateParam() {
        BizAssert.notEmpty(projectName.getText(), "项目名称不能为空");
        BizAssert.notEmpty(name.getText(), "接口名称不能为空");

        if (paramList != null) {
            for (CarTestVO carTestVO : paramList) {
                BizAssert.notEmpty(carTestVO.getIdentifier(), "入参变量名不能为空");
                BizAssert.notEmpty(carTestVO.getName(), "入参" + carTestVO.getIdentifier() + "注释不能为空");
                BizAssert.notNull(carTestVO.getDataType(), "入参字段类型不能为空");
                BizAssert.notNull(carTestVO.getParentId(), "入参上级不能为空");
                BizAssert.notNull(carTestVO.getLevel(), "入参级别不能为空");
                carTestVO.setType(1);
            }
        }
        if (returnList != null) {
            for (CarTestVO carTestVO : returnList) {
                BizAssert.notEmpty(carTestVO.getIdentifier(), "出参变量名不能为空");
                BizAssert.notEmpty(carTestVO.getName(), "出参" + carTestVO.getIdentifier() + "注释不能为空");
                BizAssert.notNull(carTestVO.getDataType(), "出参字段类型不能为空");
                BizAssert.notNull(carTestVO.getParentId(), "出参上级不能为空");
                BizAssert.notNull(carTestVO.getLevel(), "出参级别不能为空");
                carTestVO.setType(2);
            }
        }


    }

    /**
     * 初始化方法
     */
    private void initPanel() {}

}
