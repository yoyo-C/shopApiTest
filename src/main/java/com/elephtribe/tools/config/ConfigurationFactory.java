package com.elephtribe.tools.config;

import com.elephtribe.tools.StringUtils;
//import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
/**
 * Created by Bytes on 2017/7/18.
 */
public class ConfigurationFactory {
//    protected static org.slf4j.Logger logger = LoggerFactory.getLogger(ConfigurationFactory.class);
    private static Configuration configImpl;
    public static final String CONFIG_BASE_DIR = "config/";
    public static final String CONFIG_FILE_NAME = "config.properties";
    public static final String EXT_CONFIG_KEY = "ext_config_file";

    public static Configuration getConfigration()
    {
        if (configImpl == null) {
            configImpl = new ConfigurationImpl();

            loadFromConfig("config.properties");
        }

        String extConfs = configImpl.getPropertyValue("ext_config_file");
        if (StringUtils.isNotBlank(extConfs)) {
            String[] files = extConfs.split(",");
            for (String confName : files) {
                loadFromConfig("config/" + confName);
            }

        }

        String multiENV = System.getProperty("multi_env", "");
        if (!StringUtils.isEmpty(multiENV)) {
            configImpl.setProperty(PropertyConfig.MULTI_ENV, multiENV);
        }

        return configImpl;
    }

    public static void loadFromConfig(String confName)
    {
//        logger.info("加载配置文件 [" + confName + "]");

        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();

        URL configUrl = currentClassLoader.getResource(confName);

        if (configUrl == null) {
//            logger.error("can not find config [" + confName + "]!");
            return;
        }

        Properties properties = new Properties();
        try {
            properties.load(configUrl.openStream());
        } catch (Exception e) {
//            logger.error("can not find ats config [" + confName + "] details [" + e.getMessage() + "]");

            return;
        }

        Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();
        for (Map.Entry entry : entrySet) {
            Object keyObject = entry.getKey();
            Object valueObject = entry.getValue();
            String envConfigValue = System.getProperty("config." + keyObject.toString());

            if (StringUtils.isNotBlank(envConfigValue))
                configImpl.setProperty(keyObject.toString(), envConfigValue);
            else
                configImpl.setProperty(keyObject.toString(), valueObject.toString());
        }
    }
}
