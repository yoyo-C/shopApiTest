package com.elephtribe.tools;

import com.elephtribe.tools.database.DBUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by Bytes on 2017/7/18.
 */
public class TestDataUtil {
    /**
     * 通过csv文件导入
     * @param csvFile
     * @param tableName
     * @param args
     */
    public void csvToMysql(String csvFile, String tableName,String[] args,String DBName)
    {
        try
        {
            int argsIndex = 0;
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(csvFile);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String preSql = "INSERT INTO " + tableName;
            String sql = null;
            String line = null;
            if ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ",");
                preSql = preSql + " (";
                while (st.hasMoreTokens()) {
                    preSql = preSql + st.nextToken() + ",";
                }
                preSql = preSql.substring(0, preSql.length() - 1) + ")";
            }
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ",");
                sql = preSql + " VALUES (";
                while (st.hasMoreTokens()) {
                    String currentToken = st.nextToken();
                    if ((currentToken.equals("?")) || (currentToken.equals("'?'"))) {
                        if (argsIndex < args.length)
                            currentToken = currentToken.replaceFirst("\\?", args[(argsIndex++)]);
                        else {
                            throw new Exception("参数args个数不足,请检查");
                        }
                    }
                    sql = sql + currentToken + ",";
                }
                sql = sql.substring(0, sql.length() - 1) + ")";
                int retVal = DBUtils.getUpdateResultMap(tableName,sql, DBName);
                if ((retVal >= 1)) {
                    System.out.println("成功插入数据:" + sql);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void csvToMysql(String csvFile, String tableName,String DBName)
    {
        csvToMysql(csvFile, tableName, new String[0], DBName);
    }


}
