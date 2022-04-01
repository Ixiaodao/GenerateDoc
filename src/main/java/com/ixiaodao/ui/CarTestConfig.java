package com.ixiaodao.ui;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.ixiaodao.model.CarTestConfigVO;
import com.ixiaodao.model.CarTestVO;
import com.ixiaodao.model.Interface;
import com.ixiaodao.utils.HttpClient;
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
    private List<CarTestVO> paramList;
    private List<CarTestVO> returnList;

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
        this.paramList = paramList;
        this.returnList = returnList;
    }

    private void initEvent() {}

    @Override
    protected void doOKAction() {
        onOK();
        super.doOKAction();
    }

    /**
     * 确认按钮回调事件
     */
    private void onOK() {
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

        HttpClient.sendPost("http://localhost:8080/interface/audo/autoAdd", anInterface);

    }

    /**
     * 初始化方法
     */
    private void initPanel() {}

}
