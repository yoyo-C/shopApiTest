package com.elephtribe.tools.dataprovider;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.elephtribe.tools.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.util.StringUtil;

import java.io.*;
import java.util.List;

/**
 * Created by Bytes on 2017/7/18.
 */
public class CsvParser {
    private static final Log LOG = LogFactory.getLog(CsvParser.class);
    private static final String DEFAULT_PATH = "/src/test/resources/";

    public File getCsvFile(String csvPath)
    {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/" + csvPath;
        File file = new File(filePath);
        return file;
    }

    public String getCsvFileName(Class<?> objClass, String csvPath)
    {
        String[] paths = csvPath.split("/");
        ArrayUtils.reverse(paths);

        String className = objClass.getSimpleName() + ".csv";

        if (!StringUtils.equals(className, paths[0])) {
            csvPath = StringUtil.replace(csvPath, paths[0], className);
        }

        return csvPath;
    }

    public void writeToCsv(File file, List<String[]> outputValues)
            throws Exception
    {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (Exception e) {
            LOG.error("写入文件【" + file.getName() + "】初始化失败", e);
            throw e;
        }
        try
        {
            OutputStreamWriter osw = null;
            osw = new OutputStreamWriter(outputStream);
            CSVWriter csvWriter = new CSVWriter(osw);
            csvWriter.writeAll(outputValues);
            csvWriter.close();
        } catch (Exception e) {
            LOG.error("通过文件流输出数据失败", e);
            throw e;
        }
    }

    public List readFromCsv(File file)
            throws Exception
    {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (Exception e) {
            LOG.error("读入文件【" + file.getName() + "】初始化失败", e);
            throw e;
        }

        List tableList = null;
        try {
            InputStreamReader isr = new InputStreamReader(inputStream);
            CSVReader csvReader = new CSVReader(isr);
            tableList = csvReader.readAll();
        } catch (Exception e) {
            LOG.error("通过CSV文件流读入数据失败", e);
            throw e;
        }
        return tableList;
    }
}
