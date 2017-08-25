package com.elephtribe.tools.database;

import com.elephtribe.tools.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by Bytes on 2017/7/18.
 */
public class DataMap extends HashMap<String, Object> {
    private static final long serialVersionUID = 9164967756576939731L;

    public String getStringValue(String key)
    {
        key = convertKey(key);
        String value = null;
        if (containsKey(key)) {
            Object obj = get(key);
            if (null == obj)
                return null;
            if ((obj instanceof BigDecimal))
                value = obj.toString();
            else {
                value = String.valueOf(obj);
            }
        }

        return value;
    }

    public int getIntValue(String key)
    {
        key = convertKey(key);
        String stringValue = getStringValue(key);
        int value = Integer.parseInt(stringValue);

        return value;
    }

    public long getLongValue(String key)
    {
        key = convertKey(key);
        String stringValue = getStringValue(key);
        long value = Long.parseLong(stringValue);

        return value;
    }

    public static String convertKey(String key)
    {
        return StringUtils.isBlank(key) ? null : key.toUpperCase();
    }

    public Object get(String key) {
        String k = convertKey(key);
        return super.get(k);
    }
}
