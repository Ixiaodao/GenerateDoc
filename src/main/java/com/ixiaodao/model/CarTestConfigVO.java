package com.ixiaodao.model;


/**
 * 1
 * @author jinwenbiao
 * @since 2022/3/31 15:33
 */
public class CarTestConfigVO {
	private String jiraId;
	private String name;
	private String desc;
	private String serviceName;
	private String beanId;
	private String methodName;
	private String paramClassName;
	private String returnClassName;
	private String commonClassName;
	private String projectName;

	public String getJiraId() {
		return jiraId;
	}

	public void setJiraId(String jiraId) {
		this.jiraId = jiraId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getBeanId() {
		return beanId;
	}

	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getParamClassName() {
		return paramClassName;
	}

	public void setParamClassName(String paramClassName) {
		this.paramClassName = paramClassName;
	}

	public String getReturnClassName() {
		return returnClassName;
	}

	public void setReturnClassName(String returnClassName) {
		this.returnClassName = returnClassName;
	}

	public String getCommonClassName() {
		return commonClassName;
	}

	public void setCommonClassName(String commonClassName) {
		this.commonClassName = commonClassName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
