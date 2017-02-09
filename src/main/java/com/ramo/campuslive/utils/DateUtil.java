package com.ramo.campuslive.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ramo on 2016/6/20.
 */
public class DateUtil {
    private Calendar c;
    private int day;

    public DateUtil(Date d) {
        c = Calendar.getInstance();
        c.setTime(d);
    }

    public String getYear() {
        int year = c.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    public String getTime() {

        int MINUTE = c.get(Calendar.MINUTE);
        int SECOND = c.get(Calendar.SECOND);
        int HOUR = c.get(Calendar.HOUR);
        StringBuffer buffer = new StringBuffer();
        buffer.append(HOUR);
        buffer.append(":");
        buffer.append(MINUTE);
        buffer.append(":");
        buffer.append(SECOND);
        return buffer.toString();
    }

    public String getDay() {
        int MONTH = c.get(Calendar.MONTH);
        int DAY_OF_MONTH = c.get(Calendar.DAY_OF_MONTH);
        return MONTH + "." + DAY_OF_MONTH;
    }

    public static int compare_date(Date dt1, Date dt2) {

        try {
            if (dt1.getTime() > dt2.getTime()) {
                // System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                //  System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}
