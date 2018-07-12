package com.freetempo.yamviewer.utils;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class TimeUtil {

    private static final String TIME_FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd";

    public static String getDateStringFromTimeStamp(long time) {
        Calendar calendar = Calendar.getInstance(Locale.TAIWAN);
        calendar.setTimeInMillis(time * 1000);
        return DateFormat.format(TIME_FORMAT_YEAR_MONTH_DAY, calendar).toString();
    }
}
