package com.xy.commonbase.utils;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Size;

import com.xy.commonbase.R;
import com.xy.commonbase.base.BaseApplication;
import com.xy.commonbase.bean.MyDateBean;
import com.xy.commonbase.http.ApiException;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */

public class TimeUtil {

    public static final long getLongTime(String time) {
        return getLongTime(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static final long getLongTime(String time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //将long转成字符串
    public static final String getStringTime(long time) {
        if (time == 0)
            return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
    }

    public static final String getStringDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    //将long转成字符串
    public static final String getStringTime(long time, String pattern) {
        return new SimpleDateFormat(pattern).format(new Date(time));
    }

    public static final String getFrontTime(String time) {
        long longTime = getLongTime(time);
        //比对当前时间
        //过去了多久
        return getFrontTime(longTime);
    }

    public static final Date parseProjectTime(String time) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static final String getDayOfMonth(String time) {
        Date date = parseProjectTime(time);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    public static final String getTimeOfMonth(String time) {
        Date date = parseProjectTime(time);
        return new SimpleDateFormat("MM-dd HH:mm").format(date);
    }

    public static final String getTimeOfDay(String time) {
        Date date = parseProjectTime(time);
        return new SimpleDateFormat("HH:mm").format(date);
    }

    public static final String getFrontTime(long longTime) {
        long subTime = System.currentTimeMillis() - longTime;
        if (subTime / 1000 < 60) {
            //1min 内
            return subTime / 1000
                    + BaseApplication.getApplication()
                    .getString(R.string.front_second);
            // 1h 内
        } else if (subTime / 1000 / 60 < 60) {
            return subTime / 1000 / 60 + BaseApplication.getApplication()
                    .getString(R.string.front_minute);
            // 1d 内
        } else if (subTime / 1000 / 60 / 60 < 24) {
            return subTime / 1000 / 60 / 60 + BaseApplication.getApplication()
                    .getString(R.string.front_hour);
        }else if (subTime / 1000 / 60 / 60 / 24 < 365){
            return getStringTime(longTime,BaseApplication.getApplication()
                    .getString(R.string.format_day_month));
        } else {
            return getStringTime(longTime, BaseApplication.getApplication()
                    .getString(R.string.format_day_year));
        }

//        if (subTime / 1000 < 60) {
//            //60秒之内
//            return subTime / 1000
//                    + BaseApplication.getApplication()
//                    .getString(R.string.front_second);
//        } else if (subTime / 1000 / 60 < 60) {
//            return subTime / 1000 / 60 + BaseApplication.getApplication()
//                    .getString(R.string.front_minute);
//        } else if (subTime / 1000 / 60 / 60 < 24) {
//            return subTime / 1000 / 60 / 60 + BaseApplication.getApplication()
//                    .getString(R.string.front_hour);
//        } else if (subTime / 1000 / 60 / 60 / 24 < 30) {
//            return subTime / 1000 / 60 / 60 / 24 + BaseApplication.getApplication()
//                    .getString(R.string.front_day);
//        } else if (subTime / 1000 / 60 / 60 / 24 < 365){
//            return getStringTime(longTime,BaseApplication.getApplication().getString(R.string.format_time_month));
//        }else {
//            return getStringTime(longTime,BaseApplication.getApplication()
//                    .getString(R.string.format_day_year));
//        }
    }

    public static String countDownTime(long time) {
        if (time < 60) {
            return BaseApplication.getApplication().getString(R.string.left) + "："
                    + getSecond(time);
        } else if (time / 60 < 60) {
            return BaseApplication.getApplication().getString(R.string.left) + "："
                    + getMinute(time);
        } else if (time / 60 / 60 < 24) {
            return BaseApplication.getApplication().getString(R.string.left) + "："
                    + getHour(time);
        } else if (time / 60 / 60 / 24 < 365) {
            return BaseApplication.getApplication().getString(R.string.left) + "：" + (time / 60 / 60 / 24)
                    + BaseApplication.getApplication().getString(R.string.day) + getHour(time);
        } else {
            return "该团不用拼了";
        }
    }

    private static String getSecond(long time) {
        DecimalFormat format = new DecimalFormat("00");
        return format.format(time);
    }

    private static String getMinute(long time) {
        DecimalFormat format = new DecimalFormat("00");
        return format.format(time / 60) + ":" + format.format(time % 60);
    }

    private static String getHour(long time) {
        DecimalFormat format = new DecimalFormat("00");
        return format.format(time / 60 / 60 % 24) + ":"
                + format.format(time / 60 % 60) + ":"
                + format.format(time % 60);
    }


    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;

    }

    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static float getTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        float time = (hour * 60 * 60) + (minute * 60) + second;
        return time;
    }

    public static List<MyDateBean> getSevenDate(Date curDate) {
        String[] months = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        List<MyDateBean> dateList = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.setTime(curDate);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;

        for (int i = -7, j = 0; i < 8; i++) {
            int day = c.get(Calendar.DAY_OF_MONTH) + i - j;
            if (day < 0) {
                month--;
                j = i;
                if (month < 1) {
                    month = 12;
                    year--;
                }
                c.clear();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month - 1);
                day = MaxDayFromDay_OF_MONTH(year, month) + day;
                c.set(Calendar.DAY_OF_MONTH, day);
            }
            if (day > MaxDayFromDay_OF_MONTH(year, month)) {
                month++;
                j = i;
                if (month > 12) {
                    month = 1;
                    year++;
                }
                c.clear();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month - 1);
                day = c.get(Calendar.DAY_OF_MONTH);
                c.set(Calendar.DAY_OF_MONTH, day);
            }
//            dateList.add(new Date(year,month,day));
            String week = computeWeek(year + "", month + "", day + "");
            String date = year + "-" + formatDayAndMonth(month) + "-" + formatDayAndMonth(day);

            dateList.add(new MyDateBean(formatDayAndMonth(day) + "", week, months[month - 1], date));
        }
        return dateList;
    }

    public static int MaxDayFromDay_OF_MONTH(int year, int month) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0
        int day = time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
        return day;
    }

    public static String formatDayAndMonth(int value) {
        DecimalFormat format = new DecimalFormat("##00");
        return format.format(value);
    }


    public static String computeWeek(long time) {
        Date date = new Date(time);
        String year = TimeUtil.getYear(date) + "";
        String month = TimeUtil.getMonth(date) + "";
        String day = TimeUtil.getDay(date) + "";
        return computeWeek(year, month, day);
    }

    /**
     * 计算星期
     *
     * @param yearStr
     * @param monthStr
     * @param dayStr
     * @return
     */
    //
    public static String computeWeek(String yearStr, String monthStr, String dayStr) {
        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        int year = Integer.parseInt(yearStr);
        int month = Integer.parseInt(monthStr);
        int day = Integer.parseInt(dayStr);
        Log.e("TAG", "------FORT-----》》" + year + "        " + month +/*"      "+week+*/"     " + day);
        String week = null;

        if (month == 1 || month == 2) {

        }

        int c = year / 100;
        int d = day;
        int y = year % 100;
        int m = month;
        if (m == 1 || m == 2) {
            y--;
            m = month + 12;
        }
        // 运用Zeller公式计算星期
        int w = (y + (y / 4) + (c / 4) - 2 * c + (26 * (m + 1) / 10) + d - 1) % 7;
        while (w < 0) {
            w += 7;
        }
//        if (month == 0 || month == 1) {
//            w += 2;
//        }
        if (w >= 7) {
            w = w % 7;
        }
        return weeks[w];

    }

    public static boolean compareSameDay(long o, long y) {
        String u = getStringTime(o, "yyyyMMdd");
        String x = getStringTime(y, "yyyyMMdd");
        if (u.equals(x)) {
            return true;
        }
        return false;
    }


    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */

    public static Date parse(String strDate, String pattern) {

        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */

    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    public static long getLongDate(long time) {
        // 解析给定的值
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);

        // 清空结果单位
        Calendar date = Calendar.getInstance();
        date.clear();

        // 设置结果值
        date.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        return date.getTimeInMillis();
    }

    public static String getTimeOfMonth(long time) {
        String curYear = getStringTime(System.currentTimeMillis(), "yyyy");
        String year = getStringTime(time, "yyyy");
        if (curYear.equals(year)) {
            return getStringTime(time, BaseApplication.getApplication().getString(R.string.format_time_month));
        } else {
            return getStringTime(time, BaseApplication.getApplication().getString(R.string.format_day_year));
        }
    }

    public static String getDayOfMonth(long time) {
        String curYear = getStringTime(System.currentTimeMillis(), "yyyy");
        String year = getStringTime(time, "yyyy");
        if (curYear.equals(year)) {
            return getStringTime(time, BaseApplication.getApplication().getString(R.string.format_day_month));
        } else {
            return getStringTime(time, BaseApplication.getApplication().getString(R.string.format_day_year));
        }
    }

    /**
     * @param startTime 开启时间
     * @param endTime   结束时间
     * @param desc      0： 尚未开始（开始时间错误）
     *                  1： 等待开始（开始时间正确并小于1月显示倒计时）
     *                  2： 等待结束 （结束时间正确 并小于1月显示倒计时）
     *                  3： 等待开始（开始时间正确 大于1月显示具体时间）
     *                  4： 等待结束 (结束时间正确 大于1月显示具体时间）
     *                  5： 已经结束
     * @return 时间
     */
    public static String getCountDownTime(long startTime, long endTime, @Size(6) String... desc) throws ApiException{
        long curTime = System.currentTimeMillis();
        if (startTime > curTime) {
            long time = startTime - curTime;
            if (time < 0) {
                throw new ApiException(15301,desc[0]);
            } else if (time < 60) {
                return String.format(getDesc(desc[1]), getSecond(time));
            } else if (time / 60 < 60) {
                return String.format(getDesc(desc[1]), getMinute(time));
            } else if (time / 60 / 60 < 24) {
                return String.format(getDesc(desc[1]), getHour(time));
            } else if (time / 60 / 60 / 24 < 30) {
                return String.format(getDesc(desc[1]), (time / 60 / 60 / 24)
                        + BaseApplication.getApplication().getString(R.string.day)
                        + getHour(time));
            } else {
                throw new ApiException(15301,String.format(desc[3], getDayOfMonth(startTime)));
            }
        }
        if (curTime >= endTime) {
            throw new ApiException(15301,desc[5]);
        }
        long time = endTime - curTime;
        if (time < 0) {
            return desc[0];
        } else if (time < 60) {
            return String.format(getDesc(desc[2]), getSecond(time));
        } else if (time / 60 < 60) {
            return String.format(getDesc(desc[2]), getMinute(time));
        } else if (time / 60 / 60 < 24) {
            return String.format(getDesc(desc[2]), getHour(time));
        } else if (time / 60 / 60 / 24 < 30) {
            return String.format(getDesc(desc[2]), (time / 60 / 60 / 24)
                    + BaseApplication.getApplication().getString(R.string.day)
                    + getHour(time));
        } else {
            throw new ApiException(15301,String.format(desc[4], getDayOfMonth(startTime)));
        }
    }

    private static String getDesc(String desc) {
        if (desc == null || desc.isEmpty()) {
            return BaseApplication.getApplication().getString(R.string.left);
        } else {
            return desc;
        }
    }

}
