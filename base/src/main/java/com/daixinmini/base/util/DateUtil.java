package com.daixinmini.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static final Long ONE_HOUR_MS = 3600000L;

    public static Timestamp now() {
        return current();
    }

    public static Timestamp current() {
        return new Timestamp(new Date().getTime());
    }

    public static Timestamp yyyyMMddHH(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }

        try {
            return Timestamp.valueOf(str + ":00:00");
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static String yyyyMMddHHmmssSSS(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(time);
    }

    public static Timestamp yyyyMMddHHmmssSSS(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }

        try {
            return Timestamp.valueOf(str);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static String yyyyMMddHHmmss(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
    }

    public static Timestamp yyyyMMddHHmmss(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }

        try {
            return Timestamp.valueOf(str);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static String yyyyMMddHHmmssSimple(Date time) {
        if (time == null) {
            return null;
        }
        return new SimpleDateFormat("yyyyMMddHHmmss").format(time);
    }

    public static Timestamp yyyyMMddHHmmssSimple(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }

        try {
            return new Timestamp(new SimpleDateFormat("yyyyMMddHHmmSSS").parse(str).getTime());
        } catch (Exception e) {
            return null;
        }
    }

    public static String yyyyMMddHHmm(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(time);
    }

    public static Timestamp yyyyMMddHHmm(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }

        try {
            return Timestamp.valueOf(str + ":00");
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static String yyyyMM(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM").format(time);
    }

    public static String yyyyMMInChinese(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy年MM月").format(time);
    }

    public static Timestamp yyyyBegin(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }
        try {
            return Timestamp.valueOf(str + "01-01 00:00:00");
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static Timestamp yyyyMMBegin(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }

        try {
            return Timestamp.valueOf(str + "-01 00:00:00");
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static Timestamp yyyyMMEnd(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }

        try {
            String[] split = str.split("-");
            int day = getLastDayOfMonth(BasicUtil.integer(split[0]), BasicUtil.integer(split[1]));
            return Timestamp.valueOf(str + "-" + day + " 00:00:00");
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static String yyyyMMdd(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }

    public static String yyyyMMddInChinese(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy年MM月dd日").format(time);
    }

    public static String MMdd(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("MM-dd").format(time);
    }

    public static Timestamp yyyyMMdd(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }

        try {
            return Timestamp.valueOf(str + " 00:00:00");
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * 返回所在周的第几天(1-7分别对应周一到周日)
     *
     * @param time
     * @return
     */
    public static String dayOfWeekInChinese(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        // 值为1-7，当为0时，设置为7
        int i = (cal.get(Calendar.DAY_OF_WEEK) - 1) == 0 ? 7 : (cal.get(Calendar.DAY_OF_WEEK) - 1);
        String weekStr = null;
        switch (i) {
            case 1:
                weekStr = "星期一";
                break;
            case 2:
                weekStr = "星期二";
                break;
            case 3:
                weekStr = "星期三";
                break;
            case 4:
                weekStr = "星期四";
                break;
            case 5:
                weekStr = "星期五";
                break;
            case 6:
                weekStr = "星期六";
                break;
            case 7:
                weekStr = "星期日";
                break;
        }
        return weekStr;
    }

    public static String yyyy(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy").format(time);
    }

    public static String yy(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("yy").format(time);
    }

    public static String MM(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("MM").format(time);
    }

    public static String dd(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("dd").format(time);
    }

    public static String HH(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("HH").format(time);
    }

    public static String mm(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("mm").format(time);
    }

    public static String mmInChinese(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("mm分钟").format(time);
    }

    public static String HHmm(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("HH:mm").format(time);
    }

    public static String HHmmInChinese(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("HH小时mm分钟").format(time);
    }

    public static String MMddInChinese(Date time) {
        if (time == null) {
            return "";
        }
        return new SimpleDateFormat("MM月dd日").format(time);
    }

    public static int year(Date time) {
        return Integer.valueOf(new SimpleDateFormat("yyyy").format(time));
    }

    public static int month(Date time) {
        return Integer.valueOf(new SimpleDateFormat("MM").format(time));
    }

    public static int day(Date time) {
        return Integer.valueOf(new SimpleDateFormat("dd").format(time));
    }

    public static int hour(Date time) {
        return Integer.valueOf(new SimpleDateFormat("HH").format(time));
    }

    public static int min(Date time) {
        return Integer.valueOf(new SimpleDateFormat("mm").format(time));
    }

    public static Timestamp toDateAuto(String arg) {
        if (arg == null) {
            return null;
        }

        try {
            int length = arg.length();
            if (length == 4) {
                return Timestamp.valueOf(arg + "-01-01 " + "00:00:00");
            } else if (length == 7) {
                return Timestamp.valueOf(arg + "-01 " + "00:00:00");
            } else if (length == 10) {
                return Timestamp.valueOf(arg + " 00:00:00");
            } else if (length == 13) {
                return Timestamp.valueOf(arg + ":00:00");
            } else if (length == 16) {
                return Timestamp.valueOf(arg + ":00");
            } else if (length >= 19) {
                return Timestamp.valueOf(arg);
            }
        } catch (Throwable e) {
            String pattern = "Exception while format {0} to Date";
            String msg = MessageFormat.format(pattern, arg);
            logger.error(msg, e);
        }
        return null;
    }

    public static int getLastDayOfMonth(int year, int month) {
        if (month == Calendar.JANUARY || month == Calendar.MARCH || month == Calendar.MAY || month == Calendar.JULY
                || month == Calendar.AUGUST || month == Calendar.OCTOBER || month == Calendar.DECEMBER) {
            return 31;
        } else if (month == Calendar.APRIL || month == Calendar.JUNE || month == Calendar.SEPTEMBER
                || month == Calendar.NOVEMBER) {
            return 30;
        } else {
            if (IsLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
    }

    private static boolean IsLeapYear(int year) {
        boolean dividedExactlyBy4 = year % 4 == 0;
        boolean dividedExactlyBy100 = year % 100 == 0;
        boolean dividedExactlyBy400 = year % 400 == 0;

        if (dividedExactlyBy400 || (dividedExactlyBy4 && !dividedExactlyBy100)) {
            return true;
        }
        return false;
    }

    public static Timestamp plusYear(Date date, int n) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, n);
        return new Timestamp(cal.getTimeInMillis());
    }

    public static Timestamp plusMonth(Date date, int n) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return new Timestamp(cal.getTimeInMillis());
    }

    public static Timestamp plusDay(Date date, int n) {
        if (date == null) {
            return null;
        }
        long fTime = date.getTime() + (long) n * 24 * 3600000;
        return new Timestamp(fTime);
    }

    public static Timestamp plusHour(Date date, int n) {
        if (date == null) {
            return null;
        }
        long fTime = date.getTime() + (long) n * 3600000;
        return new Timestamp(fTime);
    }

    public static Timestamp plusMin(Date date, int minute) {
        if (date == null) {
            return null;
        }
        Long fTime = date.getTime() + (long) 1 * minute * 60000;
        return new Timestamp(fTime);
    }

    public static Timestamp plusSecond(Date date, int second) {
        if (date == null) {
            return null;
        }
        Long fTime = date.getTime() + (long) 1 * second * 1000;
        return new Timestamp(fTime);
    }

    /**
     * 得到零点
     */
    public static Timestamp zero(Date datetime) {
        if (datetime == null) {
            return null;
        }
        return toDateAuto(yyyyMMdd(datetime));
    }

    public static boolean isSameYear(Date a, Date b) {
        int aYear = year(a);
        int bYear = year(b);
        return aYear == bYear;
    }

    public static boolean isSameMonth(Date a, Date b) {
        int aMonth = month(a);
        int bMonth = month(b);
        return aMonth == bMonth;
    }

    public static boolean isSameDay(Date a, Date b) {
        int aDay = day(a);
        int bDay = day(b);
        return aDay == bDay;
    }

    public static boolean isSameHour(Date a, Date b) {
        int aHour = hour(a);
        int bHour = hour(b);
        return aHour == bHour;
    }

    /**
     * 计算间隔天数
     */
    public static int internalDays(Date a, Date b) {
        if (a == null || b == null) {
            return -1;
        }
        return Long.valueOf((a.getTime() - b.getTime()) / (24 * 3600000)).intValue();
    }

    /**
     * 计算间隔小时
     */
    public static int internalHours(Date a, Date b) {
        if (a == null || b == null) {
            return -1;
        }
        return Long.valueOf((a.getTime() - b.getTime()) / 3600000).intValue();
    }

    /**
     * 计算间隔分钟
     */
    public static int internalMins(Date a, Date b) {
        if (a == null || b == null) {
            return -1;
        }
        return Long.valueOf((a.getTime() - b.getTime()) / 60000).intValue();
    }

    /**
     * 计算间隔秒
     */
    public static int internalSeconds(Date a, Date b) {
        if (a == null || b == null) {
            return -1;
        }
        return Long.valueOf((a.getTime() - b.getTime()) / 1000).intValue();
    }

    /**
     * 周一
     */
    public static Timestamp monday(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(time);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * 周日
     */
    public static Timestamp sunday(Date time) {
        Timestamp monday = monday(time);
        return plusDay(monday, 6);
    }

    /**
     * 年的第一天
     *
     * @return yyyy-01-01 HH:mm:ss
     */
    public static Timestamp firstDayOfYear(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * 年的最后一天
     *
     * @return yyyy-12-31 HH:mm:ss
     */
    public static Timestamp lastDayOfYear(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * 去年的第一天
     *
     * @return yyyy-01-01 HH:mm:ss
     */
    public static Timestamp firstDayOfPrevYear(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.add(Calendar.YEAR, -1);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * 月的第一天
     *
     * @return yyyy-MM-01 HH:mm:ss
     */
    public static Timestamp firstDayOfMonth(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * 月的最后一天
     *
     * @return yyyy-MM-28/29/30/31 HH:mm:ss
     */
    public static Timestamp lastDayOfMonth(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * 周的第一天，周一
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static Timestamp firstDayOfWeek(Date time) {
        return monday(time);
    }

    /**
     * 周的最后一天，周日
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static Timestamp lastDayOfWeek(Date time) {
        return sunday(time);
    }


    public static Timestamp firstDayOfQuarter(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        if (currentMonth <= 3) {
            cal.set(Calendar.MONTH, 0);
        } else if (currentMonth <= 6) {
            cal.set(Calendar.MONTH, 3);
        } else if (currentMonth <= 9) {
            cal.set(Calendar.MONTH, 6);
        } else if (currentMonth <= 12) {
            cal.set(Calendar.MONTH, 9);
        }
        cal.set(Calendar.DAY_OF_MONTH, 1);

        return new Timestamp(cal.getTimeInMillis());
    }


    public static Timestamp lastDayOfQuarter(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        if (currentMonth <= 3) {
            cal.set(Calendar.MONTH, 2);
            cal.set(Calendar.DATE, 31);
        } else if (currentMonth <= 6) {
            cal.set(Calendar.MONTH, 5);
            cal.set(Calendar.DATE, 30);
        } else if (currentMonth <= 9) {
            cal.set(Calendar.MONTH, 8);
            cal.set(Calendar.DATE, 30);
        } else if (currentMonth <= 12) {
            cal.set(Calendar.MONTH, 11);
            cal.set(Calendar.DATE, 31);
        }
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * 等于
     *
     * @param hhmm1
     * @param hhmm2
     * @return
     */
    public static boolean eqHHmm(String hhmm1, String hhmm2) {
        return hhmm1.equals(hhmm2);
    }

    /**
     * 大于
     *
     * @param hhmm1
     * @param hhmm2
     * @return
     */
    public static boolean gtHHmm(String hhmm1, String hhmm2) {
        int hh1 = Integer.valueOf(hhmm1.substring(0, 2));
        int mm1 = Integer.valueOf(hhmm1.substring(3, 5));

        int hh2 = Integer.valueOf(hhmm2.substring(0, 2));
        int mm2 = Integer.valueOf(hhmm2.substring(3, 5));

        if (hh1 > hh2 || (hh1 == hh2 && mm1 > mm2)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 大于等于
     */
    public static boolean geHHmm(String hhmm1, String hhmm2) {
        return eqHHmm(hhmm1, hhmm2) || gtHHmm(hhmm1, hhmm2);
    }

    /**
     * 小于
     */
    public static boolean ltHHmm(String hhmm1, String hhmm2) {
        int hh1 = Integer.valueOf(hhmm1.substring(0, 2));
        int mm1 = Integer.valueOf(hhmm1.substring(3, 5));

        int hh2 = Integer.valueOf(hhmm2.substring(0, 2));
        int mm2 = Integer.valueOf(hhmm2.substring(3, 5));

        if (hh1 < hh2 || (hh1 == hh2 && mm1 < mm2)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 小于
     */
    public static boolean leHHmm(String hhmm1, String hhmm2) {
        return eqHHmm(hhmm1, hhmm2) || ltHHmm(hhmm1, hhmm2);
    }

    /**
     * 根据开始时间算时长
     *
     * @param startDate 出生日期
     * @param endDate   计算比较日期
     * @return " 1年2月3天"
     */
    public static String getDayGapDesc(Date startDate, Date endDate) {
        if (startDate == null) {
            return "";
        }

        Calendar accordingCalendar = Calendar.getInstance();
        if (endDate != null) {
            accordingCalendar.setTimeInMillis(endDate.getTime());
        }

        long startDateTimeInMillis = startDate.getTime();
        if (startDateTimeInMillis > accordingCalendar.getTimeInMillis()) {
            return "";
        }

        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.setTimeInMillis(startDateTimeInMillis);

        int startYear = startDateCalendar.get(Calendar.YEAR);
        int startMonth = startDateCalendar.get(Calendar.MONTH);
        int startDay = startDateCalendar.get(Calendar.DAY_OF_MONTH);

        int accordingYear = accordingCalendar.get(Calendar.YEAR);
        int accordingMonth = accordingCalendar.get(Calendar.MONTH);
        int accordingDay = accordingCalendar.get(Calendar.DAY_OF_MONTH);

        int day = accordingDay + (accordingDay < startDay ? getLastDayOfMonth(startYear, startMonth) : 0) - startDay;
        int month = accordingMonth + (accordingMonth < startMonth ? 12 : 0) - startMonth;
        int year = accordingYear - startYear;
        if (accordingMonth < startMonth || (accordingMonth == startMonth && accordingDay < startDay)) {
            year--;
        }

        String val = "";

        if (day > 0) {
            val = day + "天";
        }

        if (month > 0) {
            val = month + "月" + val;
        }

        if (year > 0) {
            val = year + "年" + val;
        }

        val = val + getTimeGapDesc(startDate, accordingCalendar.getTime());

        return val;
    }

    /**
     * 根据开始时间算间隔时分秒
     *
     * @param startDate 出生日期
     * @param endDate   计算比较日期
     * @return " 1小时2分3秒"
     */
    public static String getTimeGapDesc(Date startDate, Date endDate) {
        if (startDate == null) {
            return "";
        }

        Calendar accordingCalendar = Calendar.getInstance();
        if (endDate != null) {
            accordingCalendar.setTimeInMillis(endDate.getTime());
        }

        long startDateTimeInMillis = startDate.getTime();
        if (startDateTimeInMillis > accordingCalendar.getTimeInMillis()) {
            return "";
        }

        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.setTimeInMillis(startDateTimeInMillis);

        int accordingDay = accordingCalendar.get(Calendar.DAY_OF_MONTH);

        int startYear = startDateCalendar.get(Calendar.YEAR);
        int startMonth = startDateCalendar.get(Calendar.MONTH);
        int startDay = startDateCalendar.get(Calendar.DAY_OF_MONTH);

        int day = accordingDay + (accordingDay < startDay ? getLastDayOfMonth(startYear, startMonth) : 0) - startDay;

        long startTime = startDate.getTime();

        long accordingTime = endDate.getTime();

        int secs = (int) Math.round(((accordingTime - startTime) % (60 * 1000)) / 1000);

        int mins = (int) Math.ceil((accordingTime - startTime) / (60 * 1000));

        int hous = (int) Math.ceil((accordingTime - startTime) / (60 * 60 * 1000));

        String val = "";

        if (day <= 0) {
            if (secs > 0) {
                val = secs + "秒";
            }

            if (mins > 0) {
                val = mins + "分" + val;
            }

            if (hous > 0) {
                val = hous + "小时" + val;
            }
        }

        return val;
    }

    /**
     * @param datetime
     * @return
     * @Title getDayBegin
     * @Description 获取该日期00:00时间
     */
    public static Timestamp getDayBegin(Timestamp datetime) {
        if (datetime == null) {
            return null;
        }

        Timestamp dayBegin = toDateAuto(yyyyMMdd(datetime) + " 00:00:00.000");

        return dayBegin;
    }

    /**
     * @param datetime
     * @return
     * @Title getDayEnd
     * @Description 获取该日期23:59时间
     */
    public static Timestamp getDayEnd(Timestamp datetime) {
        if (datetime == null) {
            return null;
        }

        Timestamp dayEnd = DateUtil.toDateAuto(DateUtil.yyyyMMdd(datetime) + " 23:59:59.999");

        return dayEnd;
    }

    public static int getAge(Date birthday) {
        if (birthday == null) {
            return 0;
        }
        int age = 0;
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(DateUtil.now());
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthday);
        Integer year = nowCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
        if (year > 0) {
            age = year;
            Integer month = nowCalendar.get(Calendar.MONTH) - birthCalendar.get(Calendar.MONTH);
            Integer day = nowCalendar.get(Calendar.DAY_OF_MONTH) - birthCalendar.get(Calendar.DAY_OF_MONTH);
            if (month < 0 || (month == 0 && day < 0)) {
                age = year - 1;
            }
        }
        return age;
    }

    public static int getAgeMonth(Date birthday) {
        if (birthday == null) {
            return 0;
        }
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(DateUtil.now());
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthday);

        Integer month = nowCalendar.get(Calendar.MONTH) - birthCalendar.get(Calendar.MONTH);

        Integer day = nowCalendar.get(Calendar.DAY_OF_MONTH) - birthCalendar.get(Calendar.DAY_OF_MONTH);

        if (month > 0) {
            if (day < 0) {
                month = month - 1;
            }
            return month;
        } else if (month < 0) {
            month = 12 + month;
            if (day < 0) {
                month = month - 1;
            }
            return month;
        } else {

            int ageMonth = 0;
            if (day < 0) {
                ageMonth = 11;
            }
            return ageMonth;
        }
    }

    public static int getAgeDay(Date birthday) {
        if (birthday == null) {
            return 0;
        }
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(DateUtil.now());
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthday);
        int nowDay = nowCalendar.get(Calendar.DAY_OF_MONTH);
        int birDay = birthCalendar.get(Calendar.DAY_OF_MONTH);
        if (nowDay >= birDay) {
            return nowDay - birDay;
        } else {
            int year = birthCalendar.get(Calendar.YEAR);
            int month = birthCalendar.get(Calendar.MONTH);
            int lastDayOfMonthBir = getLastDayOfMonth(year, month + 1);
            return lastDayOfMonthBir - birDay + nowDay;
        }
    }

    // public static String getAgeDesc(Date birthday) {
    //     return getAgeDesc(birthday, true);
    // }
    //
    // public static String getAgeDesc(Date birthday, boolean showMonth) {
    //     if (birthday == null) {
    //         return "";
    //     }
    //     StringBuilder sb = new StringBuilder();
    //     sb.append(getAge(birthday));
    //     sb.append("岁");
    //     if (showMonth) {
    //         int month = getAgeMonth(birthday);
    //         if (month != 0) {
    //             sb.append(month).append("月");
    //         }
    //     }
    //     return sb.toString();
    // }

    public static String getBirthday(BigDecimal birthYear, BigDecimal birthMonth, BigDecimal birthDay) {
        return DateUtil.yyyyMMdd(
                getBirthday(BasicUtil.integer(birthYear), BasicUtil.integer(birthMonth, 1), BasicUtil.integer(birthDay, 1)));
    }

    public static Date getBirthday(int birthYear, int birthMonth, int birthDay) {
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.set(birthYear, birthMonth - 1, birthDay);
        return yyyyMMdd(yyyyMMdd(birthCalendar.getTime()));
    }

    public static Timestamp calcLastMenstrualPeriod(int weeks, int days) {
        return plusDay(now(), (weeks * 7 + days) * -1);
    }

    public static Map<String, BigDecimal> getGentational(Date date) {
        Map<String, BigDecimal> map = new HashMap<>();
        if (date == null) {
            return map;
        }
        long time = (now().getTime() - date.getTime()) / (24 * 3600000);
        if (time < 0) {
            return map;
        }
        long weeks = time / 7;
        long days = time % 7;

        map.put("week", BasicUtil.decimal(weeks));
        map.put("day", BasicUtil.decimal(days));

        return map;
    }

    public static Timestamp getBirthdayFromAge(Integer age) {
        if (age == null) {
            return null;
        }

        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(DateUtil.now());
        int year = nowCalendar.get(Calendar.YEAR);

        Date date = getBirthday(year - age, 1, 1);
        return new Timestamp(date.getTime());
    }

    public static Timestamp getBirthdayFromAge(Integer y, Integer m) {
        if (y == null) {
            y = 0;
        }
        if (m == null) {
            m = 0;
        }
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(DateUtil.now());
        int year = nowCalendar.get(Calendar.YEAR);
        int month = nowCalendar.get(Calendar.MONTH);
        Date date = getBirthday(year - y, month - m, 1);
        return new Timestamp(date.getTime());
    }

    public static String formatDate(Date time, String format) {
        if (time == null || BasicUtil.isEmpty(format)) {
            return "";
        }
        return new SimpleDateFormat(format).format(time);
    }
}
