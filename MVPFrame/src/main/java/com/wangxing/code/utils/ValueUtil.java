package com.wangxing.code.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.TypedValue;


import com.wangxing.code.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ValueUtil {
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat LONG_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat SHORT_TIME_FORMAT = new SimpleDateFormat("MM月dd日 HH:mm");
    private static final SimpleDateFormat TIME_STAMP = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
    private static final SimpleDateFormat SPECIFIC_DATE = new SimpleDateFormat("yyyyMMdd");

    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            // nothing
        }
        return 0;
    }

    public static long parseLong(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            // nothing
        }
        return 0;
    }

    public static float parseFloat(String str) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            // nothing
        }
        return 0;
    }

    public static boolean checkMoney(String money) {
        return money.matches("\\d*\\.?\\d?\\d?");
    }

    public static String formatDateTime(long timestamp) {
        return DATE_TIME_FORMAT.format(new Date(timestamp));
    }


    public static String formatLongTime(long timestamp) {
        return LONG_TIME_FORMAT.format(new Date(timestamp));
    }

    public static String formatShortTime(long timestamp) {
        return SHORT_TIME_FORMAT.format(new Date(timestamp));
    }

    public static String formatStamp(Date date) {
        return TIME_STAMP.format(date.getTime());
    }

    public static String formatSpecificDate(Date date) {
        return SPECIFIC_DATE.format(date.getTime());
    }

    public static String formatDuration(int duration) {
        return DateUtils.formatElapsedTime(duration);
    }

    public static String formatMoney(Context context, float money) {
        return context.getString(R.string.common_money,
                String.format(Locale.ENGLISH, "%.2f", money));
    }

    public static double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {

        }
        return 0.00;
    }

}
