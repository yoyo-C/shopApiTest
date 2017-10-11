package com.elephtribe.tools;

import com.elephtribe.tools.database.DBUtils;
import com.elephtribe.tools.database.DataMap;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created by Bytes on 2017/7/18.
 */
public class ElephDBUtils {
    /** 数据库连接 */
    private static Connection sdgDBConnection        = null;

    /** 预查询 */
    private static PreparedStatement sdgDBPreparedStatement = null;


    /** 用于传入自定义参数的当前位置记录，使用到自定义不确定参数时必需初始化为0 */
    private static int               argsIndex              = 0;

    /** 标志锁状态 */
    private static boolean           lockFlag               = false;

    /**
     * 按条件的交集查询count
     *
     * @param tableName 数据表名称
     * @param conditions 查询条件
     * @return 记录数量
     */
    public static int selectCountDB(String tableName, String[] conditions,String DBName) {

        if (null == conditions) {
            System.out.println("查询 " + tableName + " 数据记录，参数错误");
            return -1;
        }

        String tempConditonString = "";
        String selectSql = "SELECT count(*) n from " + tableName;
        if (0 != conditions.length) {
            tempConditonString = genConditionSql(conditions);
            selectSql = selectSql + " WHERE " + tempConditonString;
            System.out.println(selectSql);
        }
        DataMap dataMap = DBUtils.getQueryResultMap(tableName,selectSql,DBName);

        int count = dataMap.getIntValue("n");

        return count;
    }


    /**
     * 按条件的交集查询count
     *
     * @param tableName 数据表名称
     * @param condition 查询条件
     * @return 数据记录
     */
    public static int selectCountDB(String tableName, String condition,String DBName) {
        String[] conditions = { condition };
        return selectCountDB(tableName, conditions,DBName);
    }

    /**
     * 按单一条件查询表的某一属性值
     *
     * @param tableName 数据表名称
     * @param key 查询字段
     * @param condition 查询条件
     * @return 字段内容
     */
    public static String selectStrDB(String tableName, String key, String condition, String DBName) {

        if (StringUtils.isBlank(condition)) {
            System.out.println("查询 " + tableName + " 数据记录，条件为空");
            return null;
        }

        String selectSql = "SELECT " + key + " from " + tableName + " WHERE " + condition;

        return DBUtils.getStringValue(tableName, selectSql, DBName);
    }


    /**
     * 按条件的交集查询表的某一属性值
     *
     * @param tableName 数据表名称
     * @param key 查询字段
     * @param conditions 查询条件
     * @param subCondition 查询子条件
     * @return 字段内容
     */
    public static String selectStrDB(String tableName, String key, String[] conditions,
                                     String subCondition,String DBName) {

        if (null == conditions || 0 == conditions.length) {
            System.out.println("查询 " + tableName + " 数据记录，参数错误");
            return null;
        }

        if (StringUtils.isBlank(subCondition)) {
            subCondition = "";
        }

        String tempConditonString = genConditionSql(conditions);

        String selectSql = "SELECT " + key + " from " + tableName + " WHERE " + tempConditonString
                + subCondition;

        return DBUtils.getStringValue(tableName, selectSql, DBName);
    }

    /**
     * 按条件的交集查询表的某一属性值的集合
     *
     * @param tableName 数据表名称
     * @param key 查询字段
     * @param conditions 查询条件
     * @param subCondition 查询子条件
     * @return 字段内容集合
     */
    public static List<String> selectStrDBList(String tableName, String key, String[] conditions,
                                               String subCondition,String DBName) {

        if (null == conditions || 0 == conditions.length) {
            System.out.println("查询 " + tableName + " 数据记录，参数错误");
            return null;
        }

        if (StringUtils.isBlank(subCondition)) {
            subCondition = "";
        }

        String tempConditonString = genConditionSql(conditions);

        String selectSql = "SELECT " + key + " from " + tableName + " WHERE " + tempConditonString
                + subCondition;

        return DBUtils.getStringValueList(tableName,selectSql, DBName);
    }


    /**
     * 按单一条件查询记录
     *
     * @param tableName 数据表名称
     * @param condition 查询条件
     * @param subCondition 查询子条件
     * @return 数据记录
     */
    public static DataMap selectMapDB(String tableName, String condition, String subCondition,String DBName) {
        String[] conditions = { condition };
        return selectMapDB(tableName, conditions, subCondition, DBName);
    }

    /**
     * 按条件的交集查询记录
     *
     * @param tableName 数据表名称
     * @param conditions 查询条件
     * @param subCondition 查询子条件
     * @return 数据记录
     */
    public static DataMap selectMapDB(String tableName, String[] conditions, String subCondition,String DBName) {

        if (null == conditions || 0 == conditions.length) {
            System.out.println("查询 " + tableName + " 数据记录，参数错误");
            return null;
        }

        if (StringUtils.isBlank(subCondition)) {
            subCondition = "";
        }

        String tempConditonString = genConditionSql(conditions);

        String selectSql = "SELECT * from " + tableName + " WHERE " + tempConditonString
                + subCondition;

        DataMap map = DBUtils.getQueryResultMap(tableName,selectSql, DBName);

        return map;

    }


    /**
     * 按单个条件修改记录
     *
     * @param tableName
     * @param col
     * @param val
     * @param condition
     * @author fanshao
     */
    public static void upDateDB(String tableName, String col, String val, String condition,String DBName) {

        String[] cols = { col };
        String[] vals = { val };
        String[] conditions = { condition };
        upDateDB(tableName, cols, vals, conditions, DBName);

    }


    /**
     * 按条件的交集修改记录
     *
     * @param tableName
     * @param cols
     * @param values
     * @param conditions
     * @author fanshao
     */
    public static void upDateDB(String tableName, String[] cols, String[] values,
                                String[] conditions,String DBName) {

        if (cols.length != values.length || null == conditions || 0 == conditions.length) {
            System.out.println("修改 " + tableName + " 数据记录，参数错误");
            return;
        }

        String tempSql = genColValSql(cols, values);

        String tempConditonString = genConditionSql(conditions);

        String upDateSql = "UPDATE " + tableName + " SET " + tempSql + " WHERE "
                + tempConditonString;

        upDateSql = upDateSql.replaceAll("'null'", "null");

        DBUtils.getUpdateResultMap(tableName,upDateSql, DBName);

    }

    /**
     * 按单个条件删除记录
     *
     * @param tableName
     * @param condition
     * @author fanshao
     */
    public static void deleteDB(String tableName, String condition,String DBName) {

        String[] conditions = { condition };
        deleteDB(tableName, conditions, DBName);

    }

    /**
     * 按条件的交集删除记录
     *
     * @param tableName
     * @param conditions
     * @author fanshao
     */
    public static void deleteDB(String tableName, String[] conditions,String DBName) {

        if (null == conditions || 0 == conditions.length) {
//            System.out.println("删除 " + tableName + " 数据记录，参数错误");
            throw new Error("删除 " + tableName + " 数据记录，参数错误");
//            return;
        }

        String tempConditonString = genConditionSql(conditions);

        String deleteSql = "DELETE FROM " + tableName + " WHERE " + tempConditonString;

        DBUtils.getUpdateResultMap(tableName,deleteSql, DBName);

    }


    /**
     * 根据传入的keys、vals插入记录
     *
     * @param tableName
     * @param keys
     * @param vals
     * @author fanshao
     */
    public static void insertDB(String tableName, String keys, String vals,String DBName) {

        keys = "(" + keys + ")";
        vals = "(" + vals + ")";

        String insertSql = "INSERT INTO " + tableName + keys + " VALUES " + vals;

        DBUtils.getUpdateResultMap(tableName,insertSql, DBName);
    }

    /**
     * 组装条件
     *
     * @param conditions
     * @return 组装结果
     * @author fanshao
     */
    public static String genConditionSql(String[] conditions) {

        StringBuilder tempConditon = new StringBuilder();

        for (int i = 0; i < conditions.length; i++) {
            tempConditon.append(" AND ").append(conditions[i]);
        }

        return tempConditon.toString().replaceFirst(" AND ", "");

    }

    /**
     * 组装属性&值
     *
     * @param cols
     * @param values
     * @author fanshao
     */
    public static String genColValSql(String[] cols, String[] values) {

        StringBuilder tempSql = new StringBuilder();

        for (int i = 0; i < cols.length; i++) {
            tempSql.append(",").append(cols[i]).append(" = '").append(values[i]).append("'");
        }

        return tempSql.toString().replaceFirst(",", "");

    }

    /**
     * 用顺手了就拿过来咧~~
     *
     * @param path
     * @param condition
     * @param columns
     * @author fanshao
     */
    public static boolean DBCheckMultiColumns(String[] path, String[] condition, String[] columns,String DBName) {

        int i = 0;
        for (String singlePath : path) {

            String cols = columns[i];

            // 要是不按照规范写文件名就悲催了~~~
            String tableName = singlePath.substring(singlePath.indexOf(".") + 1,
                    singlePath.length() - 4);
            if (cols.contains(":")) {

                for (int j = Integer.parseInt(cols.split(":")[0]); j <= Integer.parseInt(cols
                        .split(":")[1]); j++) {

                    if (!DBUtils.DBCheck(singlePath, tableName, condition[i], j, DBName)) {
                        return false;
                    }
                }

            } else {
                if (!DBUtils.DBCheck(singlePath, tableName, condition[i], Integer.parseInt(cols), DBName)) {
                    return false;
                }
            }
            i++;
        }

        return true;

    }


    /**
     * 支持多列的DBCheckWithoutCondition
     * @param path
     * @param columns 如：1:3,4:5  1,3,5  2:3,5,1:3
     * @param args
     * @return 校验结果
     */
    public static boolean DBCheckWithoutConditionByMultiColums(String path, String columns,String DBName,
                                                               String... args) {

        argsIndex = 0;

        if (StringUtils.isBlank(path)) {
            return false;
        }
        List<Integer> columnList = StringUtils.convertColumnRegex2Columns(columns);
        if (null == columnList || 0 == columnList.size()) {
            return false;
        }
        int err = 0;
        for (int i = 0; i < columnList.size(); i++) {
            int colNum = columnList.get(i).intValue();
            if (!DBCheckWithoutCondition(path, colNum, DBName,args)) {
                err++;
                System.out.println("第 " + colNum + "列对比错误");
            }
        }
        return err <= 0;
    }

    /**
     * 用于检查数据是否被删除
     *
     * @param sql sql
     * @return boolean
     * @author fanshao
     */
    public static boolean DBCheckisNull(String sql,String DBName) {

        String tableName = getTableNameFromSql(sql);
        return DBUtils.getQueryMultiResultMap(tableName, sql, DBName).size() == 0;

    }

    /**
     * 可不输表名
     *
     * @param sqls sql
     * @author fanshao
     */
    public static void getUpdateResultMap(String[] sqls,String DBName) {
        // 给哥自己解析去~~-
        for (String sql : sqls) {
            String tableName = getTableNameFromSql(sql);
            DBUtils.getUpdateResultMap(tableName,sql, DBName);
        }

    }

    /**
     * 根据sql自己解析表名
     *
     * @param sql
     * @return 表名
     * @author fanshao
     */
    public static String getTableNameFromSql(String sql) {

        String tableName = null;
        String[] single = sql.toUpperCase().split(" ");
        if (single[0].equals("SELECT")) {
            tableName = single[3];
        } else if (single[0].equals("DELETE") || single[0].equals("INSERT")) {
            tableName = single[2];
        } else if (single[0].equals("UPDATE")) {
            tableName = single[1];
        }

        return tableName;
    }

    /**
     * 每次要new很麻烦，直接搞个静态的；
     *
     * @author fanshao
     */
    public static TestDataUtil testData = new TestDataUtil();

    /**
     * @param tableName 表名
     * @param csvFile   csv文件
     * @param args      参数
     * @author fanshao
     */
    public static void csvToMysql(String tableName, String csvFile,String DBName,String... args) {
        testData.csvToMysql(csvFile, tableName,args, DBName);
    }

    /**
     * @param tableName 表名
     * @param csvFile   csv文件
     * @author fanshao
     */
    public static void csvToMysql(String tableName, String csvFile,String DBName) {
        testData.csvToMysql(csvFile, tableName, DBName);
    }




    /**
     * 通过单一CSV文件校验数据，查询条件由CSV文件中"flag=C"的字段组成，默认检验文件第1列
     *
     * @param path 路径
     * @param index
     *            指定列
     * @param args
     *            自定义参数传入，如果CSV文件的flag='C'处为"?"，则使用args内容逐个替换
     * @return 数据检查状态
     */
    public static boolean DBCheckWithoutCondition(String path, int index, String DBName,String... args) {
        return DBUtils.DBCheckWithoutCondition(path, index, args, DBName);
    }

    /**
     * 更新attributes字段专用方法，具体字段名字需要传进来
     * @param key 修改目标attributes的key，如果不存在，则插入一个
     * @param value 要修改成的值
     * @param table 修改的表
     * @param condition 目标数据的条件
     */
    public static boolean updateAttributes(String key,String value,String table,String condition,String attributsName,String DBName){
        String attributes="";
        attributes =  ElephDBUtils.selectStrDB(table, attributsName, condition, DBName);

        if (StringUtils.isBlank(attributes)){
            System.out.println("查询字段内容不存在！");
            return false;
        }
        try{
            JSONObject json = new JSONObject(attributes);
            json.put(key, value);
            String newAttribute = json.toString();
            ElephDBUtils.upDateDB(table,attributsName,newAttribute,condition, DBName);

        }catch (Exception e){
            System.out.println("查询字段格式错误！");
            return  false;
        }

        return true;

    }

}
