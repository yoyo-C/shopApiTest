package com.elephtribe.tools.database;

/**
 * Created by Bytes on 2017/7/18.
 */
public class DataExp {
    private String tableName;
    private String colsName;
    private String flag;
    private String exp;
    private String comments;

    public String getTableName()
    {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColsName() {
        return this.colsName;
    }

    public void setColsName(String colsName) {
        this.colsName = colsName;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getExp() {
        return this.exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
