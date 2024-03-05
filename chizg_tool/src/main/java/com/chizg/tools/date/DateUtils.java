package com.chizg.tools.date;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat YYYY_MM = new SimpleDateFormat("yyyy-MM");
    public static final SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

    public static String getYYYYMMDD(){
        return YYYYMMDD.format(new Date());
    }
}
