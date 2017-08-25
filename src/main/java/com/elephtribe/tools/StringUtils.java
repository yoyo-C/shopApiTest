package com.elephtribe.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Bytes on 2017/7/18.
 */
public class StringUtils {
    public static final String EMPTY_STRING = "";


    public static String replaceSubString(String originalValue, String replacement, int start, int end)
    {
        if ((isBlank(originalValue)) || (start < 0) || (start > originalValue.length()) || (end < 0) || (end > originalValue.length()) || (end <= start))
        {
            return originalValue;
        }
        String preStr = originalValue.substring(0, start);
        String lastStr = originalValue.substring(end, originalValue.length());
        String retStr = preStr + replacement + lastStr;
        return retStr;
    }

    public static String amountFormat(String amount)
    {
        if ((null == amount) || (amount.equals("0")))
            return "0";
        return amount + "00";
    }

    public static String stringFormat(String str) {
        if ((str.equals("null")) || (str.equals("")))
            str = "0";
        return str;
    }

    public static String stringNullFormat(String str)
    {
        if ((str.equals("null")) || (str.equals("")))
            str = null;
        return str;
    }

    public static String amountSysFormat(String amount)
    {
        if ((null == amount) || (amount.equals("0")))
            return "0.00";
        if (amount.length() >= 3) {
            String point = amount.substring(amount.length() - 2, amount.length());
            String integer = amount.substring(0, amount.length() - 2);
            amount = integer + "." + point;
        } else {
            amount = "0." + amount;
        }
        return amount;
    }



    public static boolean isBlank(String str)
    {
        int strLen;
        if ((str == null) || ((strLen = str.length()) == 0))
            return true;
        strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str)
    {
        return !isBlank(str);
    }

    public static boolean isBlankOrNull(String str)
    {
        int strLen;
        if ((str == null) || (str.equals("null")) || ((strLen = str.length()) == 0))
            return true;
        strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String stringFirstCharToUpperCase(String str)
    {
        if ((null == str) || (str.length() < 1)) {
            return str;
        }
        String strt1 = str.substring(0, 1).toUpperCase();
        String strt2 = str.substring(1);
        str = strt1 + strt2;
        return str;
    }

    public static String stringFirstCharToLowerCase(String str)
    {
        if ((null == str) || (str.length() < 1)) {
            return str;
        }
        String strt1 = str.substring(0, 1).toLowerCase();
        String strt2 = str.substring(1);
        str = strt1 + strt2;
        return str;
    }

    public static List<Integer> convertColumnRegex2Columns(String columnRegex)
    {
        List listColumns = new ArrayList();
        if ((isBlank(columnRegex)) || (!columnRegex.matches("^((\\d+)|(\\d+):(\\d+))(,((\\d+)|(\\d+):(\\d+)))*$")))
        {
            return listColumns;
        }
        String[] columnStrings = columnRegex.split(",");
        for (int i = 0; i < columnStrings.length; i++) {
            String[] columns = columnStrings[i].split(":");
            if (1 == columns.length) {
                listColumns.add(Integer.valueOf(columns[0]));
            } else if (2 == columns.length) {
                int from = Integer.valueOf(columns[0]).intValue();
                int temp = Integer.valueOf(columns[1]).intValue();
                int to = -1;
                if (from > temp) {
                    to = from;
                    from = temp;
                } else {
                    to = temp;
                }
                for (int j = from; j <= to; j++)
                    listColumns.add(Integer.valueOf(j));
            }
            else {
                return new ArrayList();
            }
        }
        Collections.sort(listColumns);
        return listColumns;
    }

    public static String getTradeNo(String url)
    {
        if (null == url) {
            return "";
        }
        String[] tr = url.split("=");
        String tradeNo = tr[(tr.length - 1)];

        return tradeNo;
    }

    public static boolean isNull(String str)
    {
        return str == null;
    }

    public static String getRandomId()
    {
        return DateUtils.getLongDateString() + "-" + new Random().nextInt(1000);
    }

    public static List<Map<String, String>> getParamList(String path) {
        List list = new ArrayList();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        InputStream is2 = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));

        String line = "";
        String[] keys = null;
        try
        {
            int count = 0;
            while (isNotBlank(br2.readLine())) {
                count++;
            }
            count -= 1;
            Map[] maps = new HashMap[count];
            String line1 = br.readLine();
            if (isNotBlank(line1))
                keys = line1.split(",");
            int t = 0;
            while ((line = br.readLine()) != null) {
                Map map = new HashMap();
                String[] strs = line.split(",");
                if (strs.length == keys.length) {
                    for (int i = 0; i < keys.length; i++) {
                        map.put(keys[i], strs[i]);
                    }
                }
                maps[t] = map;
                list.add(maps[t]);
                t++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean isNumeric(String str)
    {
        if (str == null) {
            return false;
        }

        int length = str.length();

        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean compareLarger(String arg1, String arg2)
    {
        return Integer.parseInt(arg1) > Integer.parseInt(arg2);
    }

    public static boolean equals(String str1, String str2)
    {
        if (str1 == null) {
            return str2 == null;
        }

        return str1.equals(str2);
    }

    public static boolean isNotEmpty(String str)
    {
        return (str != null) && (str.length() > 0);
    }

    public static boolean isEmpty(String str)
    {
        return (str == null) || (str.length() == 0);
    }

    public static String substringAfter(String str, String separator)
    {
        if ((str == null) || (str.length() == 0)) {
            return str;
        }

        if (separator == null) {
            return "";
        }

        int pos = str.indexOf(separator);

        if (pos == -1) {
            return "";
        }

        return str.substring(pos + separator.length());
    }

    public static boolean equalsIgnoreCase(String str1, String str2)
    {
        if (str1 == null) {
            return str2 == null;
        }

        return str1.equalsIgnoreCase(str2);
    }

    public static String toUpperCase(String str)
    {
        if (str == null) {
            return null;
        }

        return str.toUpperCase();
    }

    public static String toLowerCase(String str)
    {
        if (str == null) {
            return null;
        }

        return str.toLowerCase();
    }

    /**
     * 判断字符串是否为空或者null
     *
     */
    public static Integer  objectToInt(Object ob){

        if(null != ob){
            return Integer.valueOf(String.valueOf(ob));

        }
        else
            return null;


    }
    private static class IntegerCompare
            implements Comparator<Object> {
        public int compare(Object o1, Object o2)
        {
            Integer int1 = (Integer)o1;
            Integer int2 = (Integer)o2;
            return int1.intValue() - int2.intValue();
        }
    }


    public static boolean isNotContainsBlank(String str) {
        if(isBlank(str)) {
            return false;
        } else if(!isNotBothSidesBlank(str)) {
            return false;
        } else {
            String str1 = str.replaceAll(" ", "");
            return str1.equals(str);
        }
    }

    public static boolean isNotBothSidesBlank(String str) {
        if(isBlank(str)) {
            return false;
        } else {
            String str1 = str.trim();
            return str1.equals(str);
        }
    }
}
