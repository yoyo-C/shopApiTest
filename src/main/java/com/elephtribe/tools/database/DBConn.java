package com.elephtribe.tools.database;

import com.elephtribe.tools.config.DBConfig;
import com.elephtribe.tools.config.PropertyConfig;
import org.apache.commons.lang.StringUtils;

import java.sql.*;

/**
 * Created by Bytes on 2017/7/18.
 */
public class DBConn {
    private Statement statement = null;
    private Connection conn=null;
    private ResultSet myResultSet = null;
    /**
     * 查询sql执行
     * @param sql
     * @param tableName 表名
     * @return 查询sql执行结果
     *
     */
    public ResultSet executeQuery(String tableName,String sql,String DBName)
    {
        try
        {
            initConnection(tableName, DBName);
            this.myResultSet = statement.executeQuery(sql);

        } catch (Exception ex) {
            System.err.println("数据库操作出错。sql=[" + sql + "], tableName=[" + tableName + "]");
        }

        return this.myResultSet;
    }


    /**
     * 执行更新sql
     * @param sql
     * @param tableName
     * @return sql执行结果
     */
    public int executeUpdate(String tableName,String sql,String DBName)
    {
        int result = 0;
        try {
            initConnection(tableName,DBName);

            result = this.statement.executeUpdate(sql);

        } catch (Exception ex) {
            System.err.println("数据库操作出错。sql=[" + sql + "], tableName=[" + tableName + "]");

            return -1;
        }
        return result;
    }

    /**
     * 关闭数据库连接
     */
    public void close()
    {
        try
        {
            if (this.myResultSet != null) {
                this.myResultSet.close();
            }
            if (this.conn != null){
                this.conn.close();
            }
            if (this.statement != null) {
                this.statement.close();
            }

        }
        catch (SQLException e) {
            System.err.println("关闭数据库连接出错。");
        }
    }


    /**
     *  初始化数据源连，目前自己创建jdbc连接，后续会迁移到配置文件中
     *  @param tableName 表名：根据表名来确认连接的数据库
     */
    private void initConnection(String tableName, String DBName) throws Exception {

        String dbType = PropertyConfig.getDBType();

        try{
            if (!StringUtils.isBlank(dbType)&& dbType.equals("sqlite")){
                //sqlite从工程配置文件读取
                String dbPath = PropertyConfig.getSqlitePath();
                String dbName = PropertyConfig.getSqliteDBName();
                Class.forName("org.sqlite.JDBC");
                if (null == dbPath) {
                    dbPath = Class.class.getClass().getResource("/").getPath();
                } else {
                    if (!dbPath.endsWith("/")) {
                        dbPath = dbPath + "/";
                    }
                    if (!dbPath.startsWith("/") && !dbPath.matches("^[a-hA-H]:\\S*")) {
                        dbPath = Class.class.getClass().getResource("/").getPath() + dbPath;
                    }
                }
                // 创建一个数据库连接
                this.conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath + dbName);
                this.statement=this.conn.createStatement();
            }else if (dbType.isEmpty()|| dbType.equals("mysql")){

                //mysql从db配种中读取db配置数据
                DBConfig dbconf = PropertyConfig.getDBConfig(DBName);
                String user =dbconf.getUsername();
                String password = dbconf.getPassword();
                String[] urlArr = dbconf.getConnectionUrl().split(",");


                Class.forName("com.mysql.jdbc.Driver");
                //遍历配置的数据库，校验当前操作的db是否存在
                for (String url:urlArr){
                    this.conn=DriverManager.getConnection(url,user,password);
                    this.statement=this.conn.createStatement();
                    ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null );

                    if (rs.next()) {
                        break;
                    }
                }

            }

        }catch (ClassNotFoundException e1){
            System.out.println("数据库驱动不存在！"+e1.toString());
        }catch (SQLException e2){
            System.out.println("数据库存在异常,请检查数据库配置！"+e2.toString());
        }
    }
}
