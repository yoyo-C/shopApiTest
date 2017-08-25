package com.elephtribe.tools.config;

import java.util.Map;

/**
 * Created by Bytes on 2017/7/18.
 */
public interface Configuration {
    Map<String, String> getConfig();

    String getPropertyValue(String paramString);

    String getPropertyValue(String paramString1, String paramString2);

    void setProperty(String paramString1, String paramString2);
}
