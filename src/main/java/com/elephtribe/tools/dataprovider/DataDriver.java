package com.elephtribe.tools.dataprovider;

import com.elephtribe.tools.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * Created by Bytes on 2017/7/26.
 */
public class DataDriver {

    private static final Log log = LogFactory.getLog(DriverDataProvider.class);


    @DataProvider(name="CsvDataProvider")
    public Iterator<?> getDataProvider(Method method) throws IOException
    {
        String file = "";
        if (null != method.getAnnotation(TestData.class)) {
            file = ((TestData)method.getAnnotation(TestData.class)).path() + ((TestData)method.getAnnotation(TestData.class)).fileName();
            return getDataProvider(method.getDeclaringClass(), method, file);
        }
        return getDataProvider(method.getDeclaringClass(), method);
    }

    @DataProvider(name="DefaultCsvProvider")
    public static Iterator<?> getDefaultDataProvider(Method method)
            throws IOException
    {
        Class clazz = method.getDeclaringClass();
        String className = clazz.getName();
        String filePath = className.replace(".", "/");
        filePath = filePath + "." + method.getName() + ".csv";
        return new DriverDataProvider(clazz, method, filePath);
    }

    public Iterator<?> getDataProvider(Class<?> cls, Method method)
            throws IOException
    {
        String className = cls.getSimpleName();
        String dirPlusPrefix = "";
        String[] testresDir = null;
        DataModel dataModel = (DataModel)method.getAnnotation(DataModel.class);
        String fileName = "";
        String filePath = "";
        int i = 0;

        String firstDir = "testdata";

        //目录暂时比较简单，先不做拼接
        dirPlusPrefix = firstDir + "/" + className;

        if ((dataModel == null) || (dataModel.value() == 2))
            fileName = className + "." + method.getName() + ".csv";
        else if (dataModel.value() == 1) {
            fileName = className + ".csv";
        }

        if ((null != testresDir) && (StringUtils.isNotBlank(testresDir[0])))
            filePath = dirPlusPrefix + testresDir[0] + "/" + fileName;
        else {
            filePath = dirPlusPrefix + "/" + fileName;
        }

        System.out.println("测试驱动数据: " + filePath);

        return new DriverDataProvider(cls, method, filePath);
    }

    public Iterator<?> getDataProvider(Class<?> cls, Method method, String filePath)
            throws IOException
    {
        return new DriverDataProvider(cls, method, filePath);
    }
}

