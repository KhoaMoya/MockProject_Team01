package gst.trainingcourse.mockproject_team01.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

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
    public static String getWeekName(Date startDate, Date endDate){
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

        if(startYear!=endYear){
            name = String.format("Day " + stringStartDate + " - " + "%02d/%02d/%d", endDay-1, endMonth, endYear);
        } else if(startMonth != endMonth){
            name = String.format("Day %02d/%02d-%02d/%02d/%d", startDay, startMonth, endDay-1, endMonth, endYear);
        } else {
            name = String.format("Day %02d-%02d/%02d/%d", startDay, endDay-1, endMonth, endYear);
        }
        return name;
    }
}
