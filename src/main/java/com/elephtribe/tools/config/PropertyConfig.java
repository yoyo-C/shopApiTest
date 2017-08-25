package com.elephtribe.tools.config;

import com.elephtribe.tools.StringUtils;

import java.util.Map;

/**
 * Created by Bytes on 2017/7/18.
 */
public class PropertyConfig {
    public static final String CONFIG_BASE_DIR = "config/";
    public static final String DB_CONF_FILE = "dbconf_file";
    public static final String WS_INVOKE = "ws_invoke";
    public static final String TEST_XMODE = "test_xmode";
    public static final String SMOKE_TEST_FLAG = "smoke_test";
    public static final String SWITCH_ENV = "switch_env";
    public static final String DB_CONF_DIR = "config/dbConf/";


    public static Configuration testConfigs = null;

    public static Map<String, String> jarVersionConfig = null;

    public static String DB_TABLENAME_SPIT_REGEX = ",";

    public static int confNumber = -1;

    public static int jarNumber = -1;

    public static String DB_MODE = "devdb";
    public static final String TR_MODE = "tr_mode";
    public static final String DATASOURCE_KEY = "ds";
    public static final String UNITYDB_KEY = "is_unitydb";
    public static final String SCHEMA_REPLACEMENT = "{schema}";
    public static final String TABLENAME_REPLACEMENT = "{tablename}";
    private static String dbconfFile;
    public static String MULTI_ENV = "multiENV";

    public static synchronized void initConfigs()
    {
        if (null == testConfigs) {
            testConfigs = ConfigurationFactory.getConfigration();
        }

        if (null != testConfigs) {
            dbconfFile = testConfigs.getPropertyValue("envconf_file");

            if (StringUtils.isNotBlank(dbconfFile)) {
                DB_MODE = getDbMode(dbconfFile);

                boolean switchEnv = openSwitchEnv();
                if (switchEnv) {
                    dbconfFile = "config/" + dbconfFile.trim();
                    ConfigurationFactory.loadFromConfig(dbconfFile);
                }
                else {
                    dbconfFile = "config/" + dbconfFile.trim();
                    ConfigurationFactory.loadFromConfig(dbconfFile);
                }
            } else {
                dbconfFile = "config.properties";
                //如果db没有配置sqlite，无需环境配置文件
                String dbtype = PropertyConfig.getDBType();
//                if (dbtype.isEmpty()){
//                    logger.info("配置项没有 [envconf_file]！");
//                }
            }

            confNumber = 1;

            while (testConfigs.getPropertyValue("ext" + confNumber + "_db_tablename") != null) {
                confNumber += 1;
            }

            if (testConfigs.getPropertyValue("jar_version") != null) {
                ConfigurationFactory.loadFromConfig("config/jarversion.conf");
            }

            testConfigs.setProperty("jarCaseControl", "true");
            testConfigs.setProperty("filePath", "");
        }
    }

    private static String getDbMode(String dbconfFile) {
        String[] strs = dbconfFile.split(".conf");
        String dbMode = strs[0].trim();

        String sofaTestMode = System.getProperty("sofatest.db_mode", "");
        if (!StringUtils.isEmpty(sofaTestMode)) {
            dbMode = sofaTestMode;
        }
//        logger.info("配置初始化： DB_MODE = " + dbMode);
        return dbMode;
    }


    public static boolean openSwitchEnv()
    {
        if (null == testConfigs) {
            initConfigs();
        }
        return !((null != testConfigs) && (testConfigs.getPropertyValue("switch_env") != null) && ("false".equals(testConfigs.getPropertyValue("switch_env").trim())));
    }



    public static boolean isTestModel()
    {
        if (null == testConfigs) {
            initConfigs();
        }
        return (null != testConfigs) && (testConfigs.getPropertyValue("use_testdb") != null) && ("true".equals(testConfigs.getPropertyValue("use_testdb").trim()));
    }

    public static boolean isSyncTestNGReport() {
        if (null == testConfigs) {
            initConfigs();
        }
        return (null != testConfigs) && (testConfigs.getPropertyValue("sync_report") != null) && ("true".equals(testConfigs.getPropertyValue("sync_report")));
    }

    public static String getTcenterProjectName()
    {
        if (null == testConfigs) {
            initConfigs();
        }
        String retStr = "";
        if (null != testConfigs) {
            retStr = testConfigs.getPropertyValue("tcenter_project");
        }
        if (StringUtils.isBlank(retStr)) {
            retStr = "";
        }
        return retStr;
    }

    public static String getTcenterTestPlanName()
    {
        if (null == testConfigs) {
            initConfigs();
        }
        String retStr = "";
        if (null != testConfigs) {
            retStr = testConfigs.getPropertyValue("tcenter_testplan");
        }
        if (StringUtils.isBlank(retStr)) {
            retStr = "";
        }
        return retStr;
    }

    public static String getTestNGXMLFilePath()
    {
        if (null == testConfigs) {
            initConfigs();
        }
        String retStr = "";
        if (null != testConfigs) {
            retStr = testConfigs.getPropertyValue("testng_xml_file");
        }
        if (StringUtils.isBlank(retStr)) {
            retStr = "target/surefire-reports/testng-results.xml";
        }
        return retStr;
    }


    //获取db类型，默认为mysql，sqlite需要配置
    public static String getDBType() {
        if (null == testConfigs) {
            initConfigs();
        }
        String dbType = testConfigs.getPropertyValue("dbType");
        if (StringUtils.isBlank(dbType)) {
            return null;
        }
        return dbType;
    }

    //sqlite数据直接放在配置文件中，无需单独配置
    public static  String getSqlitePath(){
        if (null == testConfigs) {
            initConfigs();
        }
        String sqlitePath = testConfigs.getPropertyValue("sqlite.path");
        if (StringUtils.isBlank(sqlitePath)) {
            return null;
        }
        return sqlitePath;
    }

    //sqlite数据直接放在配置文件中，无需单独配置
    public static String getSqliteDBName()
    {
        if (null == testConfigs) {
            initConfigs();
        }

        String sqliteName = testConfigs.getPropertyValue("sqlite.name");
        return sqliteName;
    }

    //从db配置文件中获取db配置信息
    public static DBConfig getDBConfig(String DBName)
    {
        DBConfig dbconfig = new DBConfig();

        if (null == testConfigs) {
            initConfigs();
        }
        String url = "";
        String username = "";
        String password = "";
        String schema = "";

        url = testConfigs.getPropertyValue(DBName + "_db_url");
        username = testConfigs.getPropertyValue(DBName + "_db_username");
        password = testConfigs.getPropertyValue(DBName + "_db_password");

        dbconfig.setConnectionUrl(url);
        dbconfig.setUsername(username);
        dbconfig.setPassword(password);
        dbconfig.setSchema(schema);

        return dbconfig;
    }

    //根据表明和dbconfigkey来获取数据库连接，适用于分库分表，保留原方法
    public static DBConfig getDBConfig(String tableName, String dbConfigKey)
    {
        DBConfig dbconfig = new DBConfig();

        if ((StringUtils.isBlank(tableName)) && (StringUtils.isBlank(dbConfigKey)))
        {
            return dbconfig;
        }

        if (null == testConfigs) {
            initConfigs();
        }
        String url = "";
        String username = "";
        String password = "";
        String schema = "";

        if (StringUtils.isNotBlank(tableName)) {
            tableName = tableName.toUpperCase().trim();
        }

        if ((!StringUtils.isBlank(dbConfigKey)) && (!"".equals(dbConfigKey))) {
            dbConfigKey = dbConfigKey.trim();

            if (dbConfigKey.startsWith("ext")) {
                url = testConfigs.getPropertyValue(dbConfigKey + "_db_url");
                username = testConfigs.getPropertyValue(dbConfigKey + "_db_username");
                password = testConfigs.getPropertyValue(dbConfigKey + "_db_password");
                schema = testConfigs.getPropertyValue(dbConfigKey + "_db_schema");
            } else if (dbConfigKey.startsWith("obstd")) {
                url = testConfigs.getPropertyValue(dbConfigKey + "_db_url");
                dbconfig.setConnectionType(DBConfig.CONN_TYPE.OB_STD);
            }

        }
        else if (matchTableWithMultiRegex(tableName, testConfigs.getPropertyValue("db_tablename"), testConfigs.getPropertyValue("db_regex")))
        {
            url = testConfigs.getPropertyValue("db_url");
            username = testConfigs.getPropertyValue("db_username");
            password = testConfigs.getPropertyValue("db_password");
            schema = testConfigs.getPropertyValue("db_schema");
        } else {
            for (int i = 1; i < confNumber; i++) {
                if (matchTableWithMultiRegex(tableName, testConfigs.getPropertyValue("ext" + i + "_db_tablename"), testConfigs.getPropertyValue("ext" + i + "_db_regex")))
                {
                    url = testConfigs.getPropertyValue("ext" + i + "_db_url");
                    username = testConfigs.getPropertyValue("ext" + i + "_db_username");
                    password = testConfigs.getPropertyValue("ext" + i + "_db_password");
                    schema = testConfigs.getPropertyValue("ext" + i + "_db_schema");
                    break;
                }

            }

        }

        dbconfig.setConnectionUrl(url);
        dbconfig.setUsername(username);
        dbconfig.setPassword(password);
        dbconfig.setSchema(schema);

        return dbconfig;
    }

    private static boolean matchTableWithMultiRegex(String tableName, String tableNameConfig, String regex)
    {
        if (StringUtils.isBlank(regex)) {
            return matchTable(tableName, tableNameConfig, regex);
        }
        String[] multiRegex = regex.split(DB_TABLENAME_SPIT_REGEX);
        if (0 == multiRegex.length) {
            return matchTable(tableName, tableNameConfig, regex);
        }
        for (int i = 0; i < multiRegex.length; i++) {
            if (matchTable(tableName, tableNameConfig, multiRegex[i])) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(",".split(",").length);
    }

    private static boolean matchTable(String tableName, String tableNameConfig, String regex)
    {
        if ((StringUtils.isBlank(tableNameConfig)) || (StringUtils.isBlank(tableName))) {
            return false;
        }
        String[] tables = tableNameConfig.toUpperCase().split(DB_TABLENAME_SPIT_REGEX);
        boolean regexMatch = (StringUtils.isNotBlank(regex)) && (regex.contains("{tablename}"));
        for (String table : tables) {
            table = table.trim();
            if (regexMatch)
            {
                String schema = calcSchema(table);
                String name = calcTableName(table);
                String calcRegex = regex.replace("{schema}", schema).replace("{tablename}", name);

                if (tableName.matches(calcRegex)) {
                    return true;
                }

            }
            else if (table.equals(tableName)) {
                return true;
            }
        }

        return false;
    }

    private static String calcSchema(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return "";
        }
        int pos = tableName.indexOf(46);
        if (pos <= 0) {
            return "";
        }
        return tableName.substring(0, pos);
    }

    private static String calcTableName(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return "";
        }
        int pos = tableName.indexOf(46);
        if (pos < 0) {
            return tableName;
        }
        return tableName.substring(pos + 1);
    }

//    public static DBConfig getDBConfig(String tableName)
//    {
//        return getDBConfig(tableName, null);
//    }

    public static String getDBConfigFile()
    {
        if (null == testConfigs) {
            initConfigs();
        }
        String dbConfFile = testConfigs.getPropertyValue("dbconf_file");
        if (StringUtils.isBlank(dbConfFile)) {
            return null;
        }
        return dbConfFile;
    }

    public static String getJarVersion()
    {
        if (null == testConfigs) {
            initConfigs();
        }
        String jarVersion = "";
        if (null != testConfigs) {
            jarVersion = testConfigs.getPropertyValue(testConfigs.getPropertyValue("jar_version"));
        }
        if (StringUtils.isBlank(jarVersion)) {
            jarVersion = "";
        }
        return jarVersion;
    }

    public static String getConfig(String key)
    {
        if (null == testConfigs) {
            initConfigs();
        }
        String value = testConfigs.getPropertyValue(key);
        return value;
    }

    public static void setConfig(String key, String value)
    {
        if (null == testConfigs) {
            initConfigs();
        }
        testConfigs.setProperty(key, value);
    }

    public static String getDbconfFile()
    {
        if (null == testConfigs) {
            initConfigs();
        }
        return dbconfFile;
    }

    public static void setMULTI_ENV(String multiEnv)
    {
        if (null == testConfigs) {
            initConfigs();
        }
        testConfigs.setProperty(MULTI_ENV, multiEnv);
    }

    public static String getEnvParam(){
        if (null == testConfigs) {
            initConfigs();
        }

        String envprefix = testConfigs.getPropertyValue("server.prefix");

        return  envprefix;
    }
}
