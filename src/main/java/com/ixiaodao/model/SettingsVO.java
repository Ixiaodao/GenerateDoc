package com.ixiaodao.model;


import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * 1
 * @author jinwenbiao
 * @since 2022/4/1 11:24
 */
@State(name = "generateDocWb", storages = {@Storage("generateDocWb.xml")})
public class SettingsVO implements PersistentStateComponent<SettingsVO>, Serializable {

	private static final long serialVersionUID = 6247661235910468235L;

	@Nullable
	public static SettingsVO getInstance(Project project) {
		return ServiceManager.getService(project, SettingsVO.class);
	}

	private String addUrl = "http://localhost:8080/interface/auto/autoAdd";

	@Nullable
	@Override
	public SettingsVO getState() {
		return this;
	}

	@Override
	public void loadState(@NotNull SettingsVO state) {
		XmlSerializerUtil.copyBean(state, this);
	}

	public String getAddUrl() {
		return addUrl;
	}

	public static SettingsVO getInstance() {
		return ServiceManager.getService(SettingsVO.class);
	}

	public void setAddUrl(String addUrl) {
		this.addUrl = addUrl;
	}
}
