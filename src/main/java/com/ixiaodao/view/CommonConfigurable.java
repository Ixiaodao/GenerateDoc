package com.ixiaodao.view;


import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.ixiaodao.model.SettingsVO;
import com.ixiaodao.view.config.ConfigView;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

/**
 * 1
 * @author jinwenbiao
 * @since 2022/4/1 10:57
 */
public class CommonConfigurable implements Configurable {

	ConfigView configView = new ConfigView();

	@Nls(capitalization = Nls.Capitalization.Title)
	@Override
	public String getDisplayName() {
		return "GenerateDoc";
	}

	@Override
	public @Nullable JComponent createComponent() {
		return configView.getComponent();
	}

	@Override
	public boolean isModified() {
		SettingsVO config = ServiceManager.getService(SettingsVO.class).getState();

		if (config == null) {
			return false;
		}
		if (!Objects.equals(config.getAddUrl(), configView.getAddUrl().getText())) {
			return true;
		}
		return false;
	}

	@Override
	public void apply() throws ConfigurationException {
		SettingsVO config = ServiceManager.getService(SettingsVO.class).getState();
		if (config != null) {
			config.setAddUrl(configView.getAddUrl().getText());
		}
	}

}
