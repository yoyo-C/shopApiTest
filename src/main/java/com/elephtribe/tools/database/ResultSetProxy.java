package com.elephtribe.tools.database;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;

/**
 * Created by Bytes on 2017/7/18.
 */
public class ResultSetProxy implements InvocationHandler
{
    private ResultSet resultSet;
    private DBConn conn;
    private Object proxy;
    private Class<?> targetClass = ResultSet.class;

    public ResultSetProxy(ResultSet resultSet, DBConn conn) {
        this.resultSet = resultSet;
        this.conn = conn;
        this.proxy = Proxy.newProxyInstance(this.targetClass.getClassLoader(), new Class[] { this.targetClass }, this);
    }

    public ResultSet getProxy()
    {
        return (ResultSet)this.proxy;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        if ("close".equals(method.getName())) {
            if (null != this.conn)
            {
                this.conn.close();
            }
            return null;
        }
        return method.invoke(this.resultSet, args);
    }
}
