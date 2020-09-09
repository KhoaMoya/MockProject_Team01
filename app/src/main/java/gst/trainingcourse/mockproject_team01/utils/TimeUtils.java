package gst.trainingcourse.mockproject_team01.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static String datePattern = "dd/MM/yyyy";
    public static SimpleDateFormat dateFormat;

    public static Date[] getPeriodWeek(Date date) {
        Date[] result = new Date[2];
        Calendar calendar = Calendar.getInstance();
        if (date != null) calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        result[0] = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 7);
        result[1] = calendar.getTime();

        return result;
    }

    public static Date[] getPeriodPrevWeek(Date date) {
        Date[] result = new Date[2];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        result[0] = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 7);
        result[1] = calendar.getTime();

        return result;
    }

    public static Date[] getPeriodNextWeek(Date date) {
        Date[] result = new Date[2];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        result[0] = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 7);
        result[1] = calendar.getTime();

        return result;
    }

    @SuppressLint("DefaultLocale")
    public static String getWeekName(Date startDate, Date endDate) {
        String name = "";
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String stringStartDate = df.format(startDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        int startMonth = calendar.get(Calendar.MONTH) + 1;
        int startYear = calendar.get(Calendar.YEAR);

        calendar.setTime(endDate);
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);
        int endMonth = calendar.get(Calendar.MONTH) + 1;
        int endYear = calendar.get(Calendar.YEAR);

        if (startYear != endYear) {
            name = String.format("Day " + stringStartDate + " - " + "%02d/%02d/%d", endDay - 1, endMonth, endYear);
        } else if (startMonth != endMonth) {
            name = String.format("Day %02d/%02d-%02d/%02d/%d", startDay, startMonth, endDay - 1, endMonth, endYear);
        } else {
            name = String.format("Day %02d-%02d/%02d/%d", startDay, endDay - 1, endMonth, endYear);
        }
        return name;
    }

    public static Date[] getPeriodDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();
        return new Date[]{startDate, endDate};
    }

    public static String convertDatetoString(Date date) {
        if (dateFormat == null) dateFormat = new SimpleDateFormat(datePattern, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static int[] getYearMonthDay(Date date) {
        int[] times = new int[3];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        times[0] = calendar.get(Calendar.YEAR);
        times[1] = calendar.get(Calendar.MONTH);
        times[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return times;
    }

    public static Date[] getPeriodWeek(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return getPeriodWeek(calendar.getTime());
    }

    public static int dayOf(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 1) day = 8;
        return day;
    }

    public static boolean isInWeek(Date[] weekDates, long[] scheduleTimes, int day) {
        long startWeek = weekDates[0].getTime();
        long endWeek = weekDates[1].getTime();
        long startSchedule = scheduleTimes[0];
        long endSchedule = scheduleTimes[1];

        if (startSchedule >= startWeek && endSchedule <= endWeek) {
            return dayOf(startSchedule) <= day && dayOf(endSchedule) >= day;
        } else {
            if (startSchedule <= startWeek && endSchedule >= endWeek) {
                return true;
            } else if (endSchedule > startWeek && startSchedule < startWeek) {
                return dayOf(endSchedule) >= day;
            } else if (startSchedule < endWeek && endSchedule >= endWeek) {
                return dayOf(startSchedule) <= day;
            }
            return false;
        }
    }

}
