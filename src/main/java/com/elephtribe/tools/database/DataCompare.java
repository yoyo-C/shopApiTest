package com.elephtribe.tools.database;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import com.elephtribe.tools.DateUtils;
import com.elephtribe.tools.StringUtils;
import com.elephtribe.tools.dataprovider.CsvParamHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bytes on 2017/7/18.
 */
public class DataCompare {
    private static List<?> dataExps = null;
    private static int FLAG_COLUMN = 3;
    private static int COLS_COLUMN = 1;
    private static int COMMENT_COLUMN = 2;
    private static int EXPSTAR_COLUMN = 3;

    public static String composeQuerySql(String path, String tableName, String condition)
    {
        ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
        strat.setType(DataExp.class);
        String[] columns = { "tableName", "colsName", "comments", "flag", "exp" };
        strat.setColumnMapping(columns);
        CsvToBean csv = new CsvToBean();
        String excuteSql = "";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        InputStreamReader isr = new InputStreamReader(is);

        dataExps = csv.parse(strat, isr);
        try
        {
            if (null != isr)
                isr.close();
        }
        catch (Exception e)
        {
        }
        String clos = "";
        for (int i = 1; i < dataExps.size(); i++) {
            if (!"N".equals(((DataExp)dataExps.get(i)).getFlag()))
                clos = clos + "," + ((DataExp)dataExps.get(i)).getColsName();
        }
        clos = clos.replaceFirst(",", "");

        excuteSql = "SELECT " + clos + " FROM " + tableName + " where " + condition;

        return excuteSql;
    }

    public static HashMap<String, String> compareTableData(String path, String executeSql, String tableName, int id, String dbConfigKey,String DBName)
    {
        String act = "";
        String exp = "";
        HashMap message = new HashMap();
        InputStreamReader isr = null;
        CSVReader csvr = null;
        DBConn conn = null;
        try
        {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);

            isr = new InputStreamReader(is);
            csvr = new CSVReader(isr);

            List tableList = csvr.readAll();

            int rows = tableList.size();

            conn = new DBConn();
            ResultSet actualTable = conn.executeQuery(tableName,executeSql, DBName);

            if (actualTable.next()) {
                for (int d = 1; d < rows; d++)
                {
                    String[] rowdata = (String[])tableList.get(d);

                    if ((!"N".equals(rowdata[FLAG_COLUMN])) && (!"C".equals(rowdata[FLAG_COLUMN])))
                    {
                        if (("T".equals(rowdata[FLAG_COLUMN])) || ("Y".equals(rowdata[FLAG_COLUMN]))) {
                            exp = rowdata[(EXPSTAR_COLUMN + id)];
                            exp = CsvParamHandler.replaceVars(exp);
                            Object tmp = actualTable.getObject(rowdata[COLS_COLUMN]);
                            if (tmp != null)
                                act = actualTable.getString(rowdata[COLS_COLUMN]);
                            else
                                act = "null";
                            if (!exp.equals(act)) {
                                message.put(Integer.toString(d), "检查表:" + tableName + " <BR> " + rowdata[COLS_COLUMN] + " " + rowdata[COMMENT_COLUMN] + " <BR> id=[" + id + "] 期望值=" + exp + " 实际值=" + act);
                            }

                        }
                        else if ((StringUtils.isNotBlank(rowdata[FLAG_COLUMN])) && (rowdata[FLAG_COLUMN].startsWith("D")))
                        {
                            String errorSeconds = rowdata[FLAG_COLUMN].substring(1);
                            long errSec = 0L;
                            if ((StringUtils.isNotBlank(errorSeconds)) && (StringUtils.isNumeric(errorSeconds)))
                            {
                                errSec = Long.valueOf(errorSeconds).longValue();
                            }
                            exp = rowdata[(EXPSTAR_COLUMN + id)];
                            exp = CsvParamHandler.replaceVars(exp);

                            if (exp.toLowerCase().equals("now"))
                                exp = DateUtils.getLongDateString(new Date());
                            else if (exp.toLowerCase().equals("today")) {
                                exp = DateUtils.getDateString(new Date());
                            }

                            Date actDate = actualTable.getTimestamp(rowdata[COLS_COLUMN]);
                            String actString = null;

                            if (actDate != null)
                            {
                                switch (exp.length()) {
                                    case 6:
                                        actString = DateUtils.getTimeString(actDate);
                                        break;
                                    case 8:
                                        actString = DateUtils.getDateString(actDate);
                                        break;
                                    default:
                                        actString = DateUtils.getLongDateString(actDate);
                                }
                            }

                            if ((exp.contains(".")) && (StringUtils.isNotBlank(actString)))
                            {
                                char[] actChars = actString.toCharArray();
                                for (int i = 0; (i < exp.length()) && (i < actChars.length); i++) {
                                    if (exp.charAt(i) == '.') {
                                        actChars[i] = '.';
                                    }
                                }
                                actString = new String(actChars);
                            }

                            if ((null == actDate) || (null == actString)) {
                                actString = "null";
                            }
                            if ((StringUtils.isNumeric(exp)) && (StringUtils.isNumeric(actString)) && (exp.length() == 14) && (actString.length() == 14) && (errSec > 0L))
                            {
                                long expTime = DateUtils.parseDateLongFormat(exp).getTime() / 1000L;
                                long actTime = DateUtils.parseDateLongFormat(actString).getTime() / 1000L;
                                if (Math.abs(expTime - actTime) > errSec) {
                                    message.put(Integer.toString(d), "日期判断  检查表:" + tableName + " <BR> " + rowdata[COLS_COLUMN] + " " + rowdata[COMMENT_COLUMN] + " <BR> id=[" + id + "] 期望值=" + exp + "±" + errSec + " 实际值=" + actString);
                                }

                            }
                            else if (!exp.equals(actString)) {
                                message.put(Integer.toString(d), "日期判断  检查表:" + tableName + " <BR> " + rowdata[COLS_COLUMN] + " " + rowdata[COMMENT_COLUMN] + " <BR> id=[" + id + "] 期望值=" + exp + " 实际值=" + actString);
                            }

                        }
                        else
                        {
                            String regEx = rowdata[(EXPSTAR_COLUMN + id)];
                            Pattern p = Pattern.compile(regEx);
                            Object tmp = actualTable.getObject(rowdata[COLS_COLUMN]);
                            if (tmp != null)
                                act = actualTable.getString(rowdata[COLS_COLUMN]);
                            else
                                act = "null";
                            Matcher m = p.matcher(act);
                            if (!m.find()) {
                                message.put(Integer.toString(d), "规则表达式匹配 检查表:" + tableName + " <BR> " + rowdata[COLS_COLUMN] + " " + rowdata[COMMENT_COLUMN] + " <BR> id=[" + id + "] 期望值=" + regEx + " 实际值=" + act);
                            }
                        }

                    }

                }

            }
            else
            {
                message.put("NONE", "检查表：" + tableName + "出错！未查询到数据, sql=[" + executeSql + "]");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.close();
                }
                if (null != csvr) {
                    csvr.close();
                }
                if (null != isr)
                    isr.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return message;
    }
}
