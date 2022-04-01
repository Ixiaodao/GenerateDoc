package com.ixiaodao.view.config;


import com.intellij.openapi.components.ServiceManager;
import com.ixiaodao.model.SettingsVO;

import javax.swing.*;

/**
 * 1
 * @author jinwenbiao
 * @since 2022/4/1 10:59
 */
public class ConfigView {
	private JPanel panel;
	private JTextField addUrl;

	public ConfigView () {
		SettingsVO config = ServiceManager.getService(SettingsVO.class).getState();
		if (config != null) {
			this.addUrl.setText(config.getAddUrl());
		}
	}

	public JComponent getComponent() {
		return panel;
	}

	public JTextField getAddUrl() {
		return addUrl;
	}

	public void setAddUrl(JTextField addUrl) {
		this.addUrl = addUrl;
	}

}
