package org.jigang.plat.core.lang.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by BF100177 on 2016/6/17.
 */
public class DateUtil {

    public final static String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";

    public static String parseDateToString(Date date) {
        return new SimpleDateFormat(FORMAT_YYYY_MM_DD_HH_MM_SS).format(date);
    }

    public static Date parse(String date) throws ParseException {
        return new SimpleDateFormat(FORMAT_YYYY_MM_DD_HH_MM_SS).parse(date);
    }

    public static Date parse(String date, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(date);
    }

    public static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 获取今天的日期
     *
     * @return
     */
    public static Date getToday() {
        return getBeginTime(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getCurrentTime() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 增加日期,days可以为负数
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        return addCalendar(date, Calendar.DAY_OF_MONTH, days);
    }

    /**
     * 增加月份,months可以为负数
     *
     * @param date
     * @param months
     * @return
     */
    public static Date addMonths(Date date, int months) {
        return addCalendar(date, Calendar.MONTH, months);
    }

    /**
     * 增加年,years可以为负数
     *
     * @param date
     * @param years
     * @return
     */
    public static Date addYears(Date date, int years) {
        return addCalendar(date, Calendar.YEAR, years);
    }

    /**
     * 增加日期
     * @param date
     * @param calendarDimension Calendar维度,如Calendar.DAY_OF_MONTY表示 天
     * @param value 增加的值
     * @return
     */
    public static Date addCalendar(Date date, int calendarDimension, int value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(calendarDimension, value);
        return cal.getTime();
    }

    /**
     * 获取某天的开始时间
     *
     * @param day
     * @return
     */
    public static Date getBeginTime(Date day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * 获取某天的结束时间
     *
     * @param day
     * @return
     */
    public static Date getEndTime(Date day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * 大于等于
     * @param date1
     * @param date2
     * @return date1 >= date2
     */
    public static boolean greaterEquals(Date date1, Date date2){
        return date1.compareTo(date2) >= 0;
    }

    /**
     * 大于
     * @param date1
     * @param date2
     * @return date1 > date2
     */
    public static boolean greaterThan(Date date1, Date date2){
        return date1.compareTo(date2) > 0;
    }



    /**
     * 判断是否为周六日
     * @param date
     * @return
     */
    public static boolean isWeekend(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }
}
