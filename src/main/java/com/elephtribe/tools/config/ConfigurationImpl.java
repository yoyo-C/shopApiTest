package com.elephtribe.tools.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bytes on 2017/7/18.
 */
public class ConfigurationImpl implements Configuration {
    private Map<String, String> configMap = new HashMap();

    public Map<String, String> getConfig()
    {
        Map map = new HashMap();
        map.putAll(this.configMap);
        return map;
    }

    public void setConfig(Map<String, String> map) {
        this.configMap.putAll(map);
    }

    public String getPropertyValue(String key) {
        return this.configMap.get(key);
    }

    public void setProperty(String key, String value)
    {
        this.configMap.put(key, value);
    }

    public String getPropertyValue(String key, String defaultValue) {
        return this.configMap.get(key) == null ? defaultValue : this.configMap.get(key);
    }
}
