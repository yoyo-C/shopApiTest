package com.elephtribe.tools.dataprovider;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * Created by Bytes on 2017/7/26.
 */

public class DriverDataProvider implements Iterator<Object[]> {

    CSVReader reader = null;
    private Class<?>[] parameterTypes;
    private Converter[] parameterConverters;
    public int sum = 0;

    private String[] last;

    public DriverDataProvider(Class<?> cls, Method method, String csvFilePath)
    {
        InputStream is = null;
        String absoluteProviderFilePath = null;

        absoluteProviderFilePath = cls.getClassLoader().getResource(csvFilePath).getPath();
        is = cls.getClassLoader().getResourceAsStream(csvFilePath);

        RuntimeContextHolder.setDataProviderPath(absoluteProviderFilePath);
        try {
            InputStreamReader isr = new InputStreamReader(is);
            this.reader = new CSVReader(isr, ',', '"', 1);
            this.parameterTypes = method.getParameterTypes();
            int len = this.parameterTypes.length;
            this.parameterConverters = new Converter[len];
            for (int i = 0; i < len; i++)
                this.parameterConverters[i] = ConvertUtils.lookup(this.parameterTypes[i]);

        }
        catch (RuntimeException e) {
            System.out.println(cls.getName() + "." + method.getName() + " TestData is not exist!");
        }
    }

    public boolean hasNext()
    {
        try
        {
            if (this.reader == null) {
                return false;
            }
            this.last = this.reader.readNext();
        }
        catch (IOException e)
        {
            System.out.println("Read row data error!");
        }
        return this.last != null;
    }

    private String[] getNextLine()
    {
        if (this.last == null) {
            try {
                this.last = this.reader.readNext();
            } catch (IOException ioe) {
                System.out.println("get next line error!");
                throw new RuntimeException(ioe);
            }
        }
        return this.last;
    }

    public Object[] next()
    {
        String[] next;
        if (this.last != null)
            next = this.last;
        else {
            next = getNextLine();
        }
        this.last = null;
        Object[] args = parseLine(next);
        return args;
    }

    private Object[] parseLine(String[] svals)
    {
        if (svals.length != this.parameterTypes.length) {
            System.err.println("驱动数据个数 [" + svals.length + "] 与参数个数 [" + this.parameterTypes.length + "] 不相等 , " + svals[0]);

            return null;
        }

        int len = svals.length;
        if (len > 0) {
            RuntimeContextHolder.setCaseId(svals[0]);
        }
        Object[] result = new Object[len];
        System.out.println("=============== START [" + svals[0] + "] ===============");
        System.out.println("======> 测试数据 用例ID [" + svals[0] + "] <======");

        for (int i = 0; i < len; i++)
        {
            String curSval = svals[i];


            result[i] = this.parameterConverters[i].convert(this.parameterTypes[i], curSval);
            System.out.println(result[i]);
        }
        return result;
    }

    public void remove()
    {
    }

    public CSVReader getReader()
    {
        return this.reader;
    }



}

