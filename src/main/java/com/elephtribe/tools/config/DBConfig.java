package com.elephtribe.tools.config;

/**
 * Created by Bytes on 2017/7/18.
 */
public class DBConfig {
    private String driverClass = "com.mysql.jdbc.Driver";
    private String connectionUrl;
    private String username;
    private String password;
    private String schema;
    private CONN_TYPE connectionType;

    public String getConnectionUrl()
    {
        return this.connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClass() {
        return this.driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public CONN_TYPE getConnectionType() {
        return this.connectionType;
    }

    public void setConnectionType(CONN_TYPE connectionType) {
        this.connectionType = connectionType;
    }

    public String toString()
    {
        return "DBConfig [connectionUrl=" + this.connectionUrl + ", username=" + this.username + ", password=" + this.password + ", schema=" + this.schema + "]";
    }

    public enum CONN_TYPE
    {
        OB_STD
    }
}
