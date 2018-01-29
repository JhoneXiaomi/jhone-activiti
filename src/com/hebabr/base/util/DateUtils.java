package com.hebabr.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {

    private final static String pattern = "yyyy-MM-dd";

    /**
     * 当前时间的String类型
     * 
     * @param format
     * @return
     */
    public static String dateToString(String format) {
        return dateToString(Calendar.getInstance().getTime(), format);
    }

    /**
     * 指定格式将String类型转换为Date类型
     * 
     * @param format
     * @param str
     * @return
     */
    public static Date stringFormatToDate(String format, String str) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到本月的第一天
     * 
     * @return
     */
    public static String getMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return dateToString(calendar.getTime(), pattern);
    }
    
    /**
     * 得到本月的第一天
     * 
     * @return
     */
    public static Date getMonthFirstDayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 得到本月的最后一天
     * 
     * @return
     */
    public static String getMonthLastDay() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateToString(calendar.getTime(), pattern);
    }

    /**
     * 得到指定日期月的第一天
     * 
     * @return
     */
    public static String getMonthFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return dateToString(calendar.getTime(), pattern);
    }

    /**
     * 得到指定日期月的最后一天
     * 
     * @return
     */
    public static String getMonthLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateToString(calendar.getTime(), pattern);
    }

    /**
     * 日期加减秒秒数
     * 
     * @param date
     * @param seconds
     * @return
     */
    public static String getBeforeOrAfterSecond(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return dateToString(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 日期加减几天
     * 
     * @param date
     * @param days
     *            [days正数日期加days天，days负数日期减days天]
     * @return
     */
    public static String getBeforeOrAfterDate(Date date, int days) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + days);
        return df.format(calendar.getTime());
    }
    
    
    public static String getBeforeOrAfterMonth(String dateStr, int months) {
    	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.add(Calendar.MONTH, months);
    	Date anyDate = calendar.getTime();
    	return sdf.format(anyDate);
    }


    /**
     * @param year
     *            int 年份
     * @param month
     *            int 月份
     * @return int 某年某月的最后一天
     */
    public static int getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month + 1);
        // 某年某月的最后一天
        return cal.getActualMaximum(Calendar.DATE);
    }

    public static Date stringToDate(String s) {
        if (s != null && !s.equals("")) {
            return stringToDate(s, pattern);
        } else {
            return null;
        }

    }

    public static Date stringToDate(String s, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将date格式参数转换成String格式。
     * 
     * @param date
     *            指定日期。
     * @return 指定日期字符串(使用默认格式:yyyy-MM-dd)。
     */
    public static String dateToString(Date date) {
        return dateToString(date, pattern);
    }

    public static String dateToString(Date date, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            if (date == null) {
                date = Calendar.getInstance().getTime();
            }
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 把**分**秒的时间换成 正常的Date格式 0 * @return
     */
    public static Date getFullTimeByfm(String fmTime, String ymdDate) {
        if (fmTime == null || "".equals(fmTime) || ymdDate == null || "".equals(ymdDate)) {
            return null;
        }

        String hour = fmTime.split("点")[0];
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        String min = fmTime.split("点")[1].split("分")[0];
        if (min.length() == 1) {
            min = "0" + min;
        }
        String fullDate = ymdDate + " " + hour + ":" + min + ":00";
        return stringFormatToDate("yyyy-MM-dd HH:mm:ss", fullDate);
    }

    public static String getFmByTime(Date fmDate) {
        if (fmDate == null) {
            return null;
        }
        String fmStr = dateToString(fmDate, "HH:mm");
        return fmStr.split(":")[0] + "点" + fmStr.split(":")[1] + "分";

    }

    public static String getByTime(Date date) {
        if (date == null) {
            return null;
        }
        String fmStr = dateToString(date, "HH:mm");
        return fmStr;

    }

    /**
     * 传入指定日期获取上1月的当天
     * 
     * @param date
     * @return
     */
    public static Calendar getDateOfLastMonth(Calendar date) {
        Calendar lastDate = (Calendar) date.clone();
        lastDate.add(Calendar.MONTH, -1);
        return lastDate;
    }

    /**
     * 根据指定日期获取下1月的当天
     * 
     * @param date
     * @return
     */
    public static Calendar getDateOfNextMonth(Calendar date) {
        Calendar lastDate = (Calendar) date.clone();
        lastDate.add(Calendar.MONTH, 1);
        return lastDate;
    }

    /**
     * 根据指定日期获取上1月
     * 
     * @param dateStr
     *            日期值 （默认日期格式 yyy-MM）。
     * @return 日期日历。
     */
    public static Calendar getDateOfLastMonth(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            Date date = sdf.parse(dateStr);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return getDateOfLastMonth(c);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format(yyyy-MM): " + dateStr);
        }
    }

    /**
     * 传入指定日期获取上1月的上1天
     * 
     * @param date
     * @return
     */
    public static Calendar getDateOfNextMonth(String format, String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(dateStr));
            return getDateOfNextMonth(c);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format(yyyy-MM): " + dateStr);
        }
    }

    public static String getAmfmByDateStr(String dateStr) {
        Date date = stringFormatToDate("yyyy-MM-dd HH:mm:ss", dateStr);
        int hour = date.getHours();
        if (hour >= 8 && hour <= 12) {
            return "am";
        } else if (hour >= 14 && hour <= 18) {
            return "pm";
        } else {
            return null;
        }
    }

    /**
     * 获取指定日期月时间集合。
     * 
     * @param dateStr
     *            指定日期。
     * @return 指定日期当月日期集合。
     */
    public static List<Date> gatMonth(Date date) {
        List<Date> list = new ArrayList<Date>();
        // 获得当前时间
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // 设置代表的日期为1号
        c.set(Calendar.DATE, 1);
        // 获得当前月的最大日期数
        int maxDay = c.getActualMaximum(Calendar.DATE);
        // 输出该月中的所有日期
        for (int i = 1; i <= maxDay; i++) {
            list.add(c.getTime());
            c.add(Calendar.DATE, 1);
        }
        return list;
    }

    /**
     * 获取指定日期月时间集合。
     * 
     * @param dateStr
     *            指定字符串日期。
     * @param format
     *            日期格式。
     * @return
     */
    public static List<String> showCalendar(String dateStr, String format) {
        List<String> list = new ArrayList<String>();
        // 获得当前时间
        Calendar c = Calendar.getInstance();
        c.setTime(stringFormatToDate(format, dateStr));
        // 设置代表的日期为1号
        c.set(Calendar.DATE, 1);
        // 获得当前月的最大日期数
        int maxDay = c.getActualMaximum(Calendar.DATE);
        // 输出该月中的所有日期
        for (int i = 1; i <= maxDay; i++) {
            list.add(dateToString(c.getTime(), format));
            c.add(Calendar.DATE, 1);
        }
        return list;
    }

    public static void main(String[] args) {
//        List<String> list = showCalendar("2015-02-27", pattern);
//        for (String str : list) {
//            System.out.println(str);
//        }
    	System.out.println(getBeforeOrAfterMonth("2015-02-27", -1));
    }

    // public static void main(String[] args) {
    // System.out.println(DateUtils.getMonthFirstDay()+"==="+DateUtils.getMonthLastDay());
    // System.out.println(DateUtils.getBeforeOrAfterDate(new Date(), 3));
    // System.out.println(DateUtils.getBeforeOrAfterSecond(new Date(),3));
    // System.out.println(DateUtils.stringFormatToDate("yyyy-MM-dd HH:mm:ss",
    // "2013-01-05 12:34:33"));
    // System.out.println(DateUtils.stringToDate("2013-01-05"));
    // System.out.println(DateUtils.getLastDayOfMonth(2013, 4));
    // System.out.println(getFullTimeByfm("3点15分"));
    // System.out.println(getFmByTime(new Date()));

    // }
}
