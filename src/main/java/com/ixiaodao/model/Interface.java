package com.ixiaodao.model;


import java.io.Serializable;
import java.util.List;

public class Interface implements Serializable {

	private static final long serialVersionUID = 6148678628333598449L;

    private String name;
    
    private String jira_id;

    private String description;

    private Integer interfaceType;

    private Integer requestType;

    private String requestUrl;

    private String serviceCode;

    private String serviceid;

    private String method;

    private String requestClassName;

    private String responseClassName;

    private List<CarTestVO> requestParam;
    
    private List<CarTestVO> responseParam;

    private String responseCommonClass;//公共响应结果类名

    private Integer projectId;

	private String projectName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJira_id() {
		return jira_id;
	}

	public void setJira_id(String jira_id) {
		this.jira_id = jira_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(Integer interfaceType) {
		this.interfaceType = interfaceType;
	}

	public Integer getRequestType() {
		return requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceid() {
		return serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRequestClassName() {
		return requestClassName;
	}

	public void setRequestClassName(String requestClassName) {
		this.requestClassName = requestClassName;
	}

	public String getResponseClassName() {
		return responseClassName;
	}

	public void setResponseClassName(String responseClassName) {
		this.responseClassName = responseClassName;
	}

	public List<CarTestVO> getRequestParam() {
		return requestParam;
	}

	public void setRequestParam(List<CarTestVO> requestParam) {
		this.requestParam = requestParam;
	}

	public List<CarTestVO> getResponseParam() {
		return responseParam;
	}

	public void setResponseParam(List<CarTestVO> responseParam) {
		this.responseParam = responseParam;
	}

	public String getResponseCommonClass() {
		return responseCommonClass;
	}

	public void setResponseCommonClass(String responseCommonClass) {
		this.responseCommonClass = responseCommonClass;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}