package com.elephtribe.tools.dataprovider;

import com.elephtribe.tools.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bytes on 2017/7/18.
 */
public class CsvParamHandler {
    private static Map<String, String> dbCheckVarMap = new HashMap();

    public static String getValue(String varName) {
        return null == dbCheckVarMap.get(varName) ? "" : dbCheckVarMap.get(varName);
    }

    public static void putValue(String key, String value) {
        dbCheckVarMap.put(key, value);
    }

    public static String replaceVars(String originalValue)
    {
        if ((StringUtils.isNotBlank(originalValue)) && (originalValue.contains("$")))
        {
            Pattern varPattern = Pattern.compile("(^|[^\\$])(\\$[a-zA-Z_]\\w*)");

            Matcher varMatcher = varPattern.matcher(originalValue);

            int start = -1;
            int end = -1;
            int group = 2;
            while (varMatcher.find()) {
                String varName = varMatcher.group(group);
                start = varMatcher.start(group);
                end = varMatcher.end(group);
                String varValue = getValue(varName);
                originalValue = StringUtils.replaceSubString(originalValue, varValue, start, end);
                varMatcher = varPattern.matcher(originalValue);
            }
            originalValue = originalValue.replaceAll("\\$\\$", "\\$");
        }
        return originalValue;
    }
}
