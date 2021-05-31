package com.zhaow.restful.setting;


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
 * @author jiwenbiao
 * @since : 2021/5/14 14:36
 */
@State(
        name = "RestfulToolkitSettings",
        storages = @Storage(value = "RestfulToolkitContextPathSetting.xml"))
public class Settings implements PersistentStateComponent<Settings>, Serializable {
    private String contextPath;

    @Nullable
    public static Settings getInstance(Project project) {
        return ServiceManager.getService(project, Settings.class);
    }

    @Override
    public @Nullable Settings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull Settings settings) {
        XmlSerializerUtil.copyBean(settings, this);
    }

    public static Settings getInstance() {
        return ServiceManager.getService(Settings.class);
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

}
