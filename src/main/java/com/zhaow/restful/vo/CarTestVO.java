package com.zhaow.restful.vo;


import java.io.Serializable;

/**
 * 1
 * @author jinwenbiao
 * @since 2022/3/30 10:52
 */
public class CarTestVO implements Serializable {
	private Integer id;
	private Integer parentId;
	private Integer level;
	private String identifier;
	private String name;
	private String dataType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

}
