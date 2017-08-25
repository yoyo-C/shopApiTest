package com.elephtribe.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Bytes on 2017/7/18.
 */
public class DateUtils {
    public static final long ONE_DAY_MILL_SECONDS = 86400000L;
    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("GMT");
    public static final long MILLIS_PER_SECOND = 1000L;
    public static final long MILLIS_PER_MINUTE = 60000L;
    public static final long MILLIS_PER_HOUR = 3600000L;
    public static final long MILLIS_PER_DAY = 86400000L;
    public static final String monthFormat = "yyyyMM";
    public static final String chineseDtFormat = "yyyy年MM月dd日";
    public static final String noSecondFormat = "yyyy-MM-dd HH:mm";
    public static final String simple = "yyyy-MM-dd HH:mm:ss";
    public static final String webFormat = "yyyy-MM-dd";
    public static final String dtSimpleChinese = "yyyy年MM月dd日";
    public static final String week = "EEEE";
    public static final String shortFormat = "yyyyMMdd";
    public static final String longFormat = "yyyyMMddHHmmss";
    public static final String hmsFormat = "HH:mm:ss";
    public static final String simpleFormat = "yyyy-MM-dd HH:mm";
    public static final String dtLongMill = "yyyyMMddHHmmssS";
    public static final String timeFormat = "HHmmss";

    /**
     * 获取长时间格式：yyyyMMddHHmmss
     * @return 长时间格式字符串
     */
    public static String getLongDateString()
    {
        DateFormat df = getNewDateFormat("yyyyMMddHHmmss");
        return df.format(new Date());
    }

    /**
     * 将日期增加指定天数
     * @param date1
     * @param days
     * @return 更新后的日期
     */

    public static Date addDays(Date date1, long days)
    {
        return addSeconds(date1, days * 86400L);
    }

    /**
     * 将日期增加指定小时数
     * @param date
     * @param hours
     * @return 更新后的时间
     */
    public static Date addHours(Date date, long hours)
    {
        return addMinutes(date, hours * 60L);
    }

    public static Date addMinutes(Date date, long minutes)
    {
        return addSeconds(date, minutes * 60L);
    }

    public static Date addSeconds(Date date1, long secs)
    {
        return new Date(date1.getTime() + secs * 1000L);
    }

    public static final int calculateDecreaseDate(String beforDate, String afterDate)
            throws ParseException
    {
        Date date1 = getFormat("yyyy-MM-dd").parse(beforDate);
        Date date2 = getFormat("yyyy-MM-dd").parse(afterDate);
        long decrease = getDateBetween(date1, date2) / 1000L / 3600L / 24L;
        int dateDiff = (int)decrease;
        return dateDiff;
    }



    public static boolean checkDays(Date start, Date end, int days)
    {
        int g = countDays(start, end);

        return g <= days;
    }

    public static boolean checkTime(String statTime)
    {
        if (statTime.length() > 8) {
            return false;
        }

        String[] timeArray = statTime.split(":");

        if (timeArray.length != 3) {
            return false;
        }

        for (int i = 0; i < timeArray.length; i++) {
            String tmpStr = timeArray[i];
            try
            {
                Integer tmpInt = new Integer(tmpStr);

                if (i == 0) {
                    if ((tmpInt.intValue() > 23) || (tmpInt.intValue() < 0)) {
                        return false;
                    }

                }
                else if ((tmpInt.intValue() > 59) || (tmpInt.intValue() < 0))
                    return false;
            }
            catch (Exception e) {
                return false;
            }
        }

        return true;
    }

    public static String convert(String dateString, DateFormat formatIn, DateFormat formatOut) {
        try {
            Date date = formatIn.parse(dateString);

            return formatOut.format(date);
        } catch (ParseException e) {
            System.out.println("convert() --- orign date error: " + dateString);
        }return "";
    }

    public static String convert2ChineseDtFormat(String dateString)
    {
        DateFormat df1 = getNewDateFormat("yyyyMMdd");
        DateFormat df2 = getNewDateFormat("yyyy年MM月dd日");

        return convert(dateString, df1, df2);
    }

    public static String convert2WebFormat(String dateString) {
        DateFormat df1 = getNewDateFormat("yyyyMMdd");
        DateFormat df2 = getNewDateFormat("yyyy-MM-dd");

        return convert(dateString, df1, df2);
    }

    public static String convertFromWebFormat(String dateString) {
        DateFormat df1 = getNewDateFormat("yyyyMMdd");
        DateFormat df2 = getNewDateFormat("yyyy-MM-dd");

        return convert(dateString, df2, df1);
    }

    public static int countDays(Date dateStart, Date dateEnd)
    {
        if ((dateStart == null) || (dateEnd == null)) {
            return -1;
        }

        return (int)((dateEnd.getTime() - dateStart.getTime()) / 86400000L);
    }

    public static long countDays(String startDate, String endDate)
    {
        Date tempDate1 = null;
        Date tempDate2 = null;
        long days = 0L;
        try
        {
            tempDate1 = string2Date(startDate);

            tempDate2 = string2Date(endDate);
            days = (tempDate2.getTime() - tempDate1.getTime()) / 86400000L;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    public static boolean dateLessThanNowAddMin(Date date, long min)
    {
        return addMinutes(date, min).before(new Date());
    }

    public static boolean dateNotLessThan(String date1, String date2, DateFormat format)
    {
        try
        {
            Date d1 = format.parse(date1);
            Date d2 = format.parse(date2);

            return !d1.before(d2);
        }
        catch (ParseException e) {
        }return false;
    }

    public static final Long dateToNumber(Date date)
    {
        if (date == null) {
            return null;
        }

        Calendar c = Calendar.getInstance();

        c.setTime(date);
        String month;
        if (c.get(2) + 1 >= 10)
            month = "" + (c.get(2) + 1);
        else
            month = "0" + (c.get(2) + 1);
        String day;
        if (c.get(5) >= 10)
            day = "" + c.get(5);
        else {
            day = "0" + c.get(5);
        }

        String number = c.get(1) + "" + month + day;

        return new Long(number);
    }

    public static final String dtFromShortToSimpleStr(String strDate)
    {
        if (null != strDate) {
            Date date;
            try {
                date = shortstring2Date(strDate);
            } catch (ParseException e) {
                date = null;
            }
            if (null != date) {
                return dtSimpleFormat(date);
            }
        }
        return "";
    }

    public static final String dtLongMillFormat(Date date)
    {
        if (date == null) {
            return "";
        }

        return getFormat("yyyyMMddHHmmssS").format(date);
    }

    public static final String dtShortSimpleFormat(Date date)
    {
        if (date == null) {
            return "";
        }
        return getFormat("yyyyMMdd").format(date);
    }

    public static final String dtSimpleChineseFormat(Date date)
    {
        if (date == null) {
            return "";
        }

        return getFormat("yyyy年MM月dd日").format(date);
    }

    public static final String dtSimpleChineseFormatStr(String date)
            throws ParseException
    {
        if (date == null) {
            return "";
        }

        return getFormat("yyyy年MM月dd日").format(string2Date(date));
    }

    public static final String dtSimpleFormat(Date date)
    {
        if (date == null) {
            return "";
        }

        return getFormat("yyyy-MM-dd").format(date);
    }

    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(format).format(date);
    }



    public static String formatMonth(Date date) {
        if (date == null) {
            return null;
        }

        return new SimpleDateFormat("yyyyMM").format(date);
    }

    public static String formatTimeRange(Date startDate, Date endDate, String format) {
        if ((endDate == null) || (startDate == null)) {
            return null;
        }

        String rt = null;
        long range = endDate.getTime() - startDate.getTime();
        long day = range / 86400000L;
        long hour = range % 86400000L / 3600000L;
        long minute = range % 3600000L / 60000L;

        if (range < 0L) {
            day = 0L;
            hour = 0L;
            minute = 0L;
        }

        rt = format.replaceAll("dd", String.valueOf(day));
        rt = rt.replaceAll("hh", String.valueOf(hour));
        rt = rt.replaceAll("mm", String.valueOf(minute));

        return rt;
    }

    public static Date getBeforeDate()
    {
        Date date = new Date();

        return new Date(date.getTime() - 86400000L);
    }

    public static String getBeforeDay(Date date)
            throws ParseException
    {
        Calendar cad = Calendar.getInstance();
        cad.setTime(date);
        cad.add(5, -1);
        return dtSimpleFormat(cad.getTime());
    }

    public static String getBeforeDay(String StringDate)
            throws ParseException
    {
        Date tempDate = string2Date(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(5, -1);
        return dtSimpleFormat(cad.getTime());
    }

    public static String getBeforeDayString(int days) {
        Date date = new Date(System.currentTimeMillis() - 86400000L * days);
        DateFormat dateFormat = getNewDateFormat("yyyyMMdd");

        return getDateString(date, dateFormat);
    }



    public static String getChineseDateString(Date date)
    {
        DateFormat dateFormat = getNewDateFormat("yyyy年MM月dd日");

        return getDateString(date, dateFormat);
    }

    public static final long getDateBetween(Date dBefor, Date dAfter)
    {
        long lBefor = 0L;
        long lAfter = 0L;
        long lRtn = 0L;

        lBefor = dBefor.getTime();
        lAfter = dAfter.getTime();

        lRtn = lAfter - lBefor;

        return lRtn;
    }

    public static final int getDateBetweenNow(Date dateBefore)
    {
        if (dateBefore == null) {
            return 0;
        }
        return (int)(getDateBetween(dateBefore, new Date()) / 1000L / 60L);
    }

    public static String getDateString(Date date)
    {
        DateFormat df = getNewDateFormat("yyyyMMdd");

        return df.format(date);
    }

    public static String getDateString(Date date, DateFormat dateFormat) {
        if ((date == null) || (dateFormat == null)) {
            return null;
        }

        return dateFormat.format(date);
    }

    public static Date getDayBegin(Date date)
    {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        df.setLenient(false);

        String dateString = df.format(date);
        try
        {
            return df.parse(dateString); } catch (ParseException e) {
        }
        return date;
    }

    public static int getDayOfWeek(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(7);
    }

    public static final String getDiffDate(Date dt, int idiff)
    {
        Calendar c = Calendar.getInstance();

        c.setTime(dt);
        c.add(5, idiff);
        return dtSimpleFormat(c.getTime());
    }

    public static final String getDiffDate(int diff)
    {
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        c.add(5, diff);
        return dtSimpleFormat(c.getTime());
    }

    public static final String getDiffDate(String srcDate, String format, int diff) {
        DateFormat f = new SimpleDateFormat(format);
        try
        {
            Date source = f.parse(srcDate);
            Calendar c = Calendar.getInstance();

            c.setTime(source);
            c.add(5, diff);
            return f.format(c.getTime()); } catch (Exception e) {
        }
        return srcDate;
    }

    public static final String getDiffDateDtShort(Date dt, int idiff)
    {
        Calendar c = Calendar.getInstance();

        c.setTime(dt);
        c.add(5, idiff);
        return dtShortSimpleFormat(c.getTime());
    }

    public static final String getDiffDateMin(Date dt, int idiff)
    {
        Calendar c = Calendar.getInstance();

        c.setTime(dt);
        c.add(5, idiff);
        return simpleFormat(c.getTime());
    }

    public static final Date getDiffDateTime(int diff) {
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        c.add(5, diff);
        return c.getTime();
    }

    public static final String getDiffDateTime(int diff, int hours)
    {
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        c.add(5, diff);
        c.add(10, hours);
        return dtSimpleFormat(c.getTime());
    }

    public static long getDiffDays(Date one, Date two)
    {
        Calendar sysDate = new GregorianCalendar();

        sysDate.setTime(one);

        Calendar failDate = new GregorianCalendar();

        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 86400000L;
    }

    public static long getDiffMinutes(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();

        sysDate.setTime(one);

        Calendar failDate = new GregorianCalendar();

        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 60000L;
    }

    public static final String getDiffMon(Date dt, int idiff)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(2, idiff);
        return dtSimpleFormat(c.getTime());
    }

    public static long getDiffSeconds(Date one, Date two)
    {
        Calendar sysDate = new GregorianCalendar();

        sysDate.setTime(one);

        Calendar failDate = new GregorianCalendar();

        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 1000L;
    }

    public static String getDiffStringDate(Date dt, int diff)
    {
        Calendar ca = Calendar.getInstance();

        if (dt == null)
            ca.setTime(new Date());
        else {
            ca.setTime(dt);
        }

        ca.add(5, diff);
        return dtSimpleFormat(ca.getTime());
    }

    public static String getEmailDate(Date today)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

        String todayStr = sdf.format(today);
        return todayStr;
    }

    public static final DateFormat getFormat(String format)
    {
        return new SimpleDateFormat(format);
    }

    public static Map<String, String> getLastWeek(String StringDate, int interval)
            throws ParseException
    {
        Map lastWeek = new HashMap();
        Date tempDate = shortstring2Date(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);

        int dayOfMonth = cad.getActualMaximum(5);

        cad.add(5, dayOfMonth - 1);
        lastWeek.put("endDate", shortDate(cad.getTime()));
        cad.add(5, interval);
        lastWeek.put("startDate", shortDate(cad.getTime()));

        return lastWeek;
    }

    public static String getLongDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        return getDateString(date, dateFormat);
    }

    public static DateFormat getNewDateFormat(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);

        df.setLenient(false);
        return df;
    }

    public static String getNewFormatDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return getDateString(date, dateFormat);
    }

    public static String getNextDay(Date date)
            throws ParseException
    {
        Calendar cad = Calendar.getInstance();
        cad.setTime(date);
        cad.add(5, 1);
        return dtSimpleFormat(cad.getTime());
    }

    public static String getNextDay(String StringDate)
            throws ParseException
    {
        Date tempDate = string2Date(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(5, 1);
        return dtSimpleFormat(cad.getTime());
    }

    public static Date getNextDayDtShort(String StringDate)
            throws ParseException
    {
        Date tempDate = shortstring2Date(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(5, 1);
        return cad.getTime();
    }

    public static String getNextDayDtShortToShort(String StringDate)
            throws ParseException
    {
        Date tempDate = shortstring2Date(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(5, 1);
        return dtShortSimpleFormat(cad.getTime());
    }

    public static String getNextMon(String StringDate)
            throws ParseException
    {
        Date tempDate = shortstring2Date(StringDate);
        Calendar cad = Calendar.getInstance();

        cad.setTime(tempDate);
        cad.add(2, 1);
        return shortDate(cad.getTime());
    }

    public static final String getNowDateForPageSelectAhead()
    {
        Calendar cal = Calendar.getInstance();
        if (cal.get(12) < 30)
            cal.set(12, 0);
        else {
            cal.set(12, 30);
        }
        return simpleDate(cal.getTime());
    }

    public static final String getNowDateForPageSelectBehind()
    {
        Calendar cal = Calendar.getInstance();
        if (cal.get(12) < 30) {
            cal.set(12, 30);
        } else {
            cal.set(11, cal.get(11) + 1);
            cal.set(12, 0);
        }
        return simpleDate(cal.getTime());
    }



    public static String getShortFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        Date dt = new Date();

        cal.setTime(dt);
        cal.set(5, 1);

        DateFormat df = getNewDateFormat("yyyyMMdd");

        return df.format(cal.getTime());
    }

    public static String getSmsDate(Date today)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH:mm");

        String todayStr = sdf.format(today);
        return todayStr;
    }

    public static String getTimeString(Date date) {
        DateFormat dateFormat = getNewDateFormat("HH:mm:ss");

        return getDateString(date, dateFormat);
    }

    public static final String getTimeWithSSS()
    {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        return sdFormat.format(date);
    }

    public static String getTodayString() {
        DateFormat dateFormat = getNewDateFormat("yyyyMMdd");

        return getDateString(new Date(), dateFormat);
    }



    public static String getWebDateString(Date date) {
        DateFormat dateFormat = getNewDateFormat("yyyy-MM-dd");

        return getDateString(date, dateFormat);
    }

    public static String getWebFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        Date dt = new Date();

        cal.setTime(dt);
        cal.set(5, 1);

        DateFormat df = getNewDateFormat("yyyy-MM-dd");

        return df.format(cal.getTime());
    }

    public static String getWebNextDayString() {
        Calendar cad = Calendar.getInstance();
        cad.setTime(new Date());
        cad.add(5, 1);
        return dtSimpleFormat(cad.getTime());
    }

    public static String getWebTodayString() {
        DateFormat df = getNewDateFormat("yyyy-MM-dd");

        return df.format(new Date());
    }

    public static final String getWeekDay(Date date)
    {
        return getFormat("EEEE").format(date);
    }



    public static final String hmsFormat(Date date)
    {
        if (date == null) {
            return "";
        }

        return getFormat("HH:mm:ss").format(date);
    }

    public static final Date increaseDate(Date aDate, int days)
    {
        Calendar cal = Calendar.getInstance();

        cal.setTime(aDate);
        cal.add(5, days);
        return cal.getTime();
    }

    public static boolean isBeforeNow(Date date) {
        if (date == null)
            return false;
        return date.compareTo(new Date()) < 0;
    }

    public static final boolean isDefaultWorkingDay(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(7);
        return (week != 7) && (week != 1);
    }

    public static final boolean isLeapYear(int year)
    {
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }



    public static boolean isValidLongDateFormat(String strDate)
    {
        if (strDate.length() != "yyyyMMddHHmmss".length()) {
            return false;
        }
        try
        {
            Long.parseLong(strDate);
        } catch (Exception NumberFormatException) {
            return false;
        }

        DateFormat df = getNewDateFormat("yyyyMMddHHmmss");
        try
        {
            df.parse(strDate);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static boolean isValidLongDateFormat(String strDate, String delimiter)
    {
        String temp = strDate.replaceAll(delimiter, "");

        return isValidLongDateFormat(temp);
    }



    public static boolean isValidShortDateFormat(String strDate) {
        if (strDate.length() != "yyyyMMdd".length()) {
            return false;
        }
        try
        {
            Integer.parseInt(strDate);
        } catch (Exception NumberFormatException) {
            return false;
        }

        DateFormat df = getNewDateFormat("yyyyMMdd");
        try
        {
            df.parse(strDate);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static boolean isValidShortDateFormat(String strDate, String delimiter) {
        String temp = strDate.replaceAll(delimiter, "");

        return isValidShortDateFormat(temp);
    }

    public static final String longDate(Date Date)
    {
        if (Date == null) {
            return null;
        }

        return getFormat("yyyyMMddHHmmss").format(Date);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++)
            System.out.println(getFormat("yyyyMMddHHmmssS").format(new Date()));
    }

    public static Date now()
    {
        return new Date();
    }

    public static Date parseDateLongFormat(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date d = null;

        if ((sDate != null) && (sDate.length() == "yyyyMMddHHmmss".length())) {
            try {
                d = dateFormat.parse(sDate);
            } catch (ParseException ex) {
                return null;
            }
        }

        return d;
    }

    public static Date parseDateNewFormat(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        if ((sDate != null) && (sDate.length() == "yyyy-MM-dd HH:mm:ss".length())) {
            try {
                d = dateFormat.parse(sDate);
            } catch (ParseException ex) {
                return null;
            }
        }
        return d;
    }





    public static Date parseDateNoTimeWithDelimit(String sDate, String delimit) throws ParseException
    {
        sDate = sDate.replaceAll(delimit, "");

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        if ((sDate == null) || (sDate.length() != "yyyyMMdd".length())) {
            throw new ParseException("length not match", 0);
        }

        return dateFormat.parse(sDate);
    }



    public static final String shortDate(Date Date)
    {
        if (Date == null) {
            return null;
        }

        return getFormat("yyyyMMdd").format(Date);
    }

    public static final Date shortstring2Date(String stringDate)
            throws ParseException
    {
        if (stringDate == null) {
            return null;
        }

        return getFormat("yyyyMMdd").parse(stringDate);
    }

    public static final String shortString2SimpleString(String shortString)
    {
        if (shortString == null)
            return null;
        try
        {
            return getFormat("yyyy-MM-dd").format(shortstring2Date(shortString)); } catch (Exception e) {
        }
        return null;
    }

    public static final String shortStringToString(String stringDate) throws ParseException
    {
        if (stringDate == null) {
            return null;
        }
        return shortDate(strToDtSimpleFormat(stringDate));
    }

    public static final String simpleDate(Date date)
    {
        if (date == null) {
            return "";
        }

        return getFormat("yyyy-MM-dd HH:mm").format(date);
    }

    public static final String simpleFormat(Date date)
    {
        if (date == null) {
            return "";
        }
        return getFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static final Date simpleFormatDate(String dateString)
            throws ParseException
    {
        if (dateString == null) {
            return null;
        }
        return getFormat("yyyy-MM-dd HH:mm").parse(dateString);
    }

    public static final Date string2Date(String stringDate)
            throws ParseException
    {
        if (stringDate == null) {
            return null;
        }

        return getFormat("yyyy-MM-dd").parse(stringDate);
    }

    public static Date string2Date(String str, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try
        {
            return sdf.parse(str); } catch (ParseException e) {
        }
        return null;
    }

    public static final Long string2DateLong(String stringDate)
            throws ParseException
    {
        Date d = string2Date(stringDate);

        if (d == null) {
            return null;
        }

        return new Long(d.getTime());
    }

    public static final Date string2DateTime(String stringDate)
            throws ParseException
    {
        if (stringDate == null) {
            return null;
        }

        return getFormat("yyyy-MM-dd HH:mm:ss").parse(stringDate);
    }

    public static final Date string2DateTimeBy23(String stringDate)
            throws ParseException
    {
        if (stringDate == null) {
            return null;
        }
        if (stringDate.length() == 11)
            stringDate = stringDate + "23:59:59";
        else if (stringDate.length() == 13)
            stringDate = stringDate + ":59:59";
        else if (stringDate.length() == 16)
            stringDate = stringDate + ":59";
        else if (stringDate.length() == 10) {
            stringDate = stringDate + " 23:59:59";
        }
        return getFormat("yyyy-MM-dd HH:mm:ss").parse(stringDate);
    }

    public static final Date string2DateTimeByAutoZero(String stringDate)
            throws ParseException
    {
        if (stringDate == null) {
            return null;
        }
        if (stringDate.length() == 11)
            stringDate = stringDate + "00:00:00";
        else if (stringDate.length() == 13)
            stringDate = stringDate + ":00:00";
        else if (stringDate.length() == 16)
            stringDate = stringDate + ":00";
        else if (stringDate.length() == 10) {
            stringDate = stringDate + " 00:00:00";
        }
        return getFormat("yyyy-MM-dd HH:mm:ss").parse(stringDate);
    }


    public static final String StringToStringDate(String stringDate)
    {
        if (stringDate == null) {
            return null;
        }

        if (stringDate.length() != 8) {
            return null;
        }

        return stringDate.substring(0, 4) + stringDate.substring(4, 6) + stringDate.substring(6, 8);
    }

    public static final Date strToDate(String strDate)
    {
        if (strToSimpleFormat(strDate) != null) {
            return strToSimpleFormat(strDate);
        }
        return strToDtSimpleFormat(strDate);
    }

    public static final Date strToDtSimpleFormat(String strDate)
    {
        if (strDate == null) {
            return null;
        }
        try
        {
            return getFormat("yyyy-MM-dd").parse(strDate);
        }
        catch (Exception e) {
        }
        return null;
    }

    public static final Date strToSimpleFormat(String strDate)
    {
        if (strDate == null) {
            return null;
        }
        try
        {
            return getFormat("yyyy-MM-dd HH:mm").parse(strDate);
        }
        catch (Exception e)
        {
        }
        return null;
    }

    public static boolean webDateNotLessThan(String date1, String date2) {
        DateFormat df = getNewDateFormat("yyyy-MM-dd");

        return dateNotLessThan(date1, date2, df);
    }
}
