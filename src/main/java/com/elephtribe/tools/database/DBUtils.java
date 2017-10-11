package com.elephtribe.tools.database;

import au.com.bytecode.opencsv.CSVReader;
import com.elephtribe.tools.StringUtils;
import com.elephtribe.tools.dataprovider.CsvParamHandler;
import com.elephtribe.tools.dataprovider.CsvParser;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Bytes on 2017/7/18.
 */
public class DBUtils {
    private static final Logger logger = Logger.getLogger(DBUtils.class);

    private static ThreadLocal<Integer> argsIndex = new ThreadLocal();

    private static ThreadLocal<Connection> lockDBConnectionHolder = new ThreadLocal();

    private static final CsvParser csvParser = new CsvParser();

    //protected static Logger logger =Logger.getLogger(DBUtils.class);

    public static DataMap getQueryResultMap(String tableName,String sql,String DBName) {

        DataMap map = null;
        DBConn conn = new DBConn();
        ResultSet resultSet = conn.executeQuery(tableName,sql,DBName);
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            if (resultSet.next()) {
                map = new DataMap();
                for (int i = 1; i <= rsmd.getColumnCount(); i++)
                    map.put(StringUtils.toUpperCase(rsmd.getColumnName(i)), resultSet.getObject(i));
            }
        }
        catch (SQLException e) {
         /*   if (logger.isErrorEnabled())
                logger.error("数据库操作出错。", e);*/
        } finally {
            conn.close();
        }

        return map;
    }



    public static List<DataMap> getQueryMultiResultMap(String tableName, String sql,String DBName)
    {
        List dataList = new ArrayList();
        DataMap map = null;
        DBConn conn = new DBConn();
        ResultSet resultSet = conn.executeQuery(tableName,sql,DBName);
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            while (resultSet.next()) {
                map = new DataMap();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    map.put(StringUtils.toUpperCase(rsmd.getColumnName(i)), resultSet.getObject(i));
                }
                dataList.add(map);
            }
        } catch (SQLException e) {
           /* if (logger.isErrorEnabled())
                logger.error("数据库操作出错。", e);*/
        } finally {
            conn.close();
        }

        return dataList;
    }

    @Deprecated
    public static ResultSet getQueryResult(String sql, String tableName,String DBName)
    {
        // logger.warn("建议使用 getQueryMultiResultMap 方法代替");
        DBConn conn = new DBConn();
        ResultSet resultSet = conn.executeQuery(tableName,sql,DBName);
        ResultSet proxy = new ResultSetProxy(resultSet, conn).getProxy();
        return proxy;
    }

    @Deprecated
    public static ResultSet getQueryResult(String sql, String tableName, String dbConfigKey,String DBName)
    {
        // logger.warn("建议使用 getQueryMultiResultMap 方法代替");
        DBConn conn = new DBConn();
        ResultSet resultSet = conn.executeQuery(tableName,sql,DBName);
        ResultSet proxy = new ResultSetProxy(resultSet, conn).getProxy();
        return proxy;
    }



    public static int getUpdateResultMap(String tableName,String sql,String DBName)
    {
        DBConn conn = new DBConn();
        int resultSet = 0;
        try {
            resultSet = conn.executeUpdate(tableName,sql,DBName);
        } catch (Exception e) {
//            if (logger.isErrorEnabled())
                logger.error("数据库操作出错。", e);
            return -1;
        } finally {
            conn.close();
        }

        return resultSet;
    }


    public static String getStringValue(String tableName,String sql,String DBName)
    {
        String value = null;

        DBConn conn = new DBConn();
        ResultSet resultSet = conn.executeQuery(tableName,sql,DBName);

        try {
            if (!resultSet.next())
                return null;
            value = resultSet.getString(1);
        } catch (SQLException e) {
           /* if (logger.isErrorEnabled())
                logger.error("数据库操作出错。", e);*/
        } finally {
            conn.close();
        }

        return value;
    }


    public static List<String> getStringValueList(String tableName,String sql,String DBName)
    {
        List<String> valueList = new ArrayList<String>();

        DBConn conn = new DBConn();
        ResultSet resultSet = conn.executeQuery(tableName,sql,DBName);
        try {
            if (!resultSet.next())
                return null;
            int i= 1;
            String value = null;
            while (resultSet.next()){
                value = resultSet.getString(i);
                valueList.add(value);
                i++;
            }

        } catch (SQLException e) {
           /* if (logger.isErrorEnabled())
                logger.error("数据库操作出错。", e);*/
        } finally {
            conn.close();
        }

        return valueList;
    }

    public static boolean DBCheckMultiColumns(String path, String columns, String[] args,String DBName)
    {
        argsIndex.set(Integer.valueOf(0));
        if (StringUtils.isBlank(path)) {
            return false;
        }
        List columnList = StringUtils.convertColumnRegex2Columns(columns);
        if ((null == columnList) || (0 == columnList.size())) {
            return false;
        }
        int err = 0;
        for (int i = 0; i < columnList.size(); i++) {
            int colNum = ((Integer)columnList.get(i)).intValue();
            if (!DBCheckWithoutConditionImpl(path, colNum, args, DBName)) {
                err++;
            }
        }
        return err <= 0;
    }


    public static void genDBCheckTemplateCsv(String tableName, String path,String DBName)
    {
        if ((StringUtils.isBlank(tableName)) || (StringUtils.isBlank(path))) {
            throw new RuntimeException("tableName or path cannot be null!");
        }
        tableName = tableName.toUpperCase();
        DBConn conn = new DBConn();

        List csvValues = new ArrayList();
        String[] header = { "tableName", "colsName", "comments", "flag", "exp1" };
        csvValues.add(header);
        try {
            String querySql;

            querySql = "select column_name, column_comment from information_schema.columns where table_name='" + tableName + "'";

            int i = 0;
            ResultSet resultSet = conn.executeQuery(tableName,querySql,DBName);
            while (resultSet.next()) {
                String colsName = resultSet.getString(1);
                String comment = resultSet.getString(2);
                String firstColumn = "";
                if (i == 0) {
                    firstColumn = tableName;
                }
                String[] row = { firstColumn, colsName, comment, "Y", "" };
                csvValues.add(row);
                i++;
            }
        } catch (SQLException e) {
           /* if (logger.isErrorEnabled())
                logger.error("数据库操作出错。", e);*/
            throw new RuntimeException(e);
        } finally {
            conn.close();
        }

        File csvFile = csvParser.getCsvFile(String.format(path, tableName));
        try {
            csvParser.writeToCsv(csvFile, csvValues);
        } catch (Exception e1) {
            //logger.error("写入csv文件出错! path=" + path, e1);
            throw new RuntimeException(e1);
        }
    }

    public static boolean DBCheckEmptyColumns(String[] paths, String[] columns,String DBName)
    {
        if ((null == paths) || (null == columns) || (paths.length != columns.length)) {
            //logger.error("参数错误");
            return false;
        }
        int err = 0;
        for (int i = 0; i < paths.length; i++) {
            if (!DBCheckEmptyColumns(paths[i], columns[i], new String[0], DBName)) {
                //logger.warn("DBCheckEmptyColumns返回false [path=" + paths[i] + ", columns=" + columns[i] + "]");

                err++;
            }
        }
        return err <= 0;
    }

    public static boolean DBCheckEmptyColumns(String path, String columns, String[] args,String DBName)
    {
        argsIndex.set(Integer.valueOf(0));
        if (StringUtils.isBlank(path)) {
            return false;
        }
        List columnList = StringUtils.convertColumnRegex2Columns(columns);
        if ((null == columnList) || (0 == columnList.size())) {
            return false;
        }
        int err = 0;
        for (int i = 0; i < columnList.size(); i++) {
            int colNum = ((Integer)columnList.get(i)).intValue();
            if (!DBCheckEmptyColumnImpl(path, colNum, args, DBName)) {
                err++;
            }
        }
        return err <= 0;
    }

    private static boolean DBCheckEmptyColumnImpl(String path, int index, String[] args,String DBName)
    {
        String[] tableNameAndCondition = getTableNameAndConditionWithCsvCFlag(path, index, args);
        if (null == tableNameAndCondition) {
            return false;
        }
        String tableName = tableNameAndCondition[0];
        String condition = tableNameAndCondition[1];
        if ((StringUtils.isBlank(tableName)) || (StringUtils.isBlank(condition))) {
            return false;
        }
        String executeSql = "select * from " + tableName + " where (" + condition + ")";
        return checkDataExist(executeSql, tableName, null,DBName);
    }

    public static boolean checkDataExist(String executeSql, String tableName, String dbConfigKey,String DBName)
    {
        boolean retVal = false;
        try {
            DBConn conn = new DBConn();
            ResultSet actualTable = conn.executeQuery(tableName,executeSql,DBName);
            retVal = !actualTable.next();
            conn.close();
            return retVal;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public static boolean DBCheckWithoutCondition(String[] paths, String[] args,String DBName)
    {
        argsIndex.set(Integer.valueOf(0));
        return DBCheckWithoutCondition(paths, 1, args, DBName);
    }

    public static boolean DBCheckWithoutCondition(String path, String[] args,String DBName)
    {
        argsIndex.set(Integer.valueOf(0));
        return DBCheckWithoutConditionImpl(path, 1, args, DBName);
    }

    public static boolean DBCheckWithoutCondition(String[] paths, int index, String[] args,String DBName)
    {
        argsIndex.set(Integer.valueOf(0));

        int err = 0;
        for (int i = 0; i < paths.length; i++) {
            if ((!DBCheckWithoutConditionImpl(paths[i], index, args, DBName))) {
                //logger.error("CSV文件检验失败 path=[" + paths[i] + "],index=[" + index + "]");
                err++;
            }
        }
        return err <= 0;
    }

    public static boolean DBCheckWithoutCondition(String path, int index, String[] args,String DBName)
    {
        argsIndex.set(Integer.valueOf(0));
        return DBCheckWithoutConditionImpl(path, index, args, DBName);
    }

    private static boolean DBCheckWithoutConditionImpl(String path, int index, String[] args,String DBName)
    {
        String[] tableNameAndCondition = getTableNameAndConditionWithCsvCFlag(path, index, args);

        String tableName = tableNameAndCondition[0];
        String condition = tableNameAndCondition[1];
        if ((StringUtils.isBlank(tableName)) || (StringUtils.isBlank(condition))) {
            return false;
        }
        return DBCheck(new String[] { path }, new String[] { tableName }, new String[] { condition }, index, DBName);
    }

    private static String[] getTableNameAndConditionWithCsvCFlag(String path, int index, String[] args)
    {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        String tableName = "";
        String condition = "";
        InputStream is = null;
        InputStreamReader isr = null;
        CSVReader csvr = null;
        String [] rowdata;
        try
        {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            if (null == is) {
                //logger.error("加载文件失败,检查路径是否正确 [" + path + "]");
            }
            isr = new InputStreamReader(is);
            csvr = new CSVReader(isr);
            List tableList = csvr.readAll();

            int rows = tableList.size();
            if (rows <= 1) {
                return null;
            }
            for (int i = 1; i < rows; i++) {
                rowdata = (String[])tableList.get(i);
                if (1 == i) {
                    tableName = rowdata[0].toUpperCase();
                    if (StringUtils.isBlank(tableName)) {
                        return null;
                    }
                }
                String colName = rowdata[1];
                String flag = rowdata[3];
                String expVal = rowdata[(3 + index)];
                if (flag.equalsIgnoreCase("C")) {
                    if (!StringUtils.isBlank(condition)) {
                        condition = condition + " and ";
                    }
                    expVal = CsvParamHandler.replaceVars(expVal);

                    int curVal = argsIndex.get().intValue();
                    if ((expVal.trim().equals("?")) && (null != args) && (curVal < args.length)) {
                        expVal = args[(curVal++)];
                        argsIndex.set(Integer.valueOf(curVal));
                    }
                    condition = condition + colName + "='" + expVal + "'";
                }
            }

            if (StringUtils.isEmpty(condition))
                return null;
        }
        catch (Exception e)
        {
            return null;
        } finally {
            try {
                if (null != csvr) {
                    csvr.close();
                }
                if (null != isr)
                    isr.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return new String[] { tableName, condition };
    }

    public static boolean DBCheck(String path, String tableName, String condition,String DBName)
    {
        return DBCheck(new String[] { path }, new String[] { tableName }, new String[] { condition }, 1, DBName);
    }

    public static boolean DBCheck(String path, String tableName, String condition, String dbConfigKey,String DBName)
    {
        return DBCheck(new String[] { path }, new String[] { tableName }, new String[] { condition }, 1, new String[] { dbConfigKey }, DBName);
    }

    public static boolean DBCheck(String path, String tableName, String condition, int id,String DBName)
    {
        return DBCheck(new String[] { path }, new String[] { tableName }, new String[] { condition }, id, DBName);
    }

    public static boolean DBCheck(String path, String tableName, String condition, int id, String dbConfigKey,String DBName)
    {
        return DBCheck(new String[] { path }, new String[] { tableName }, new String[] { condition }, id, new String[] { dbConfigKey }, DBName);
    }

    public static boolean DBCheck(String[] path, String[] tableName, String[] condition,String DBName)
    {
        return DBCheck(path, tableName, condition, 1, DBName);
    }

    public static boolean DBCheck(String[] path, String[] tableName, String[] condition, int id,String DBName)
    {
        return DBCheck(path, tableName, condition, id, null, DBName);
    }

    public static boolean DBCheck(String[] path, String[] tableName, String[] condition, int id, String[] dbConfigKey,String DBName)
    {
        boolean isSuccess = true;
        if ((path.length != tableName.length) || (path.length != condition.length) || ((null != dbConfigKey) && (path.length != dbConfigKey.length) ))
        {
            //logger.error("参数错误");
            return false;
        }

        String executeSql = "";
        HashMap resultmap = new HashMap();
        List retMsgList = new ArrayList();
        for (int i = 0; i < path.length; i++)
        {
            executeSql = DataCompare.composeQuerySql(path[i], tableName[i], condition[i]);

            resultmap = DataCompare.compareTableData(path[i], executeSql, tableName[i], id, null == dbConfigKey ? null : dbConfigKey[i], DBName);

            if (!resultmap.isEmpty()) {
                retMsgList.add(resultmap);
                isSuccess = false;
            }
            Collection c = resultmap.keySet();
            Iterator j = c.iterator();
            while (j.hasNext()) {
                Object key = j.next();
                Object value = resultmap.get(key);
               /* if (logger.isErrorEnabled())
                    logger.error(key + "\t" + value);*/
            }
        }


        return isSuccess;
    }


    public static String composeSql(String sql, String condition)
    {
        String excuteSql = sql + " where " + condition;
        return excuteSql;
    }

    public static boolean DBClean(String[] sqls, String[] tables,String DBName)
    {
        if (sqls.length != tables.length)
            return false;
        int error = 0;
        for (int i = 0; i < sqls.length; i++) {
            if (getUpdateResultMap(tables[i],sqls[i],DBName) < 0){
                error++;
            }
        }
        return error <= 0;
    }


    public static String getStringByDate()
    {
        Date dt = new Date();
        return getStringByDate(dt);
    }

    public static String getStringByDate(Date dt)
    {
        if (null != dt) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dtStr = sdf.format(dt);
            return dtStr;
        }
        return "";
    }



    static
    {
        argsIndex.set(Integer.valueOf(0));
    }
}
