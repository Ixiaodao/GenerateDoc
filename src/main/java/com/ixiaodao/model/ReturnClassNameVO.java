package com.ixiaodao.model;


/**
 * 1
 * @author jinwenbiao
 * @since 2022/4/1 15:25
 */
public class ReturnClassNameVO {
	private String resultName;
	private String listName;
	private String className;
	public ReturnClassNameVO () {

	}
	public ReturnClassNameVO (String resultName, String listName, String className) {
		this.resultName = resultName;
		this.listName = listName;
		this.className = className;
	}

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
