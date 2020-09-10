package gst.trainingcourse.mockproject_team01.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class TimeUtilsTest {

    long dateTime = 1599709044058L;
    long startDateWeekTime = 1599411600000L;
    long endDateWeekTime = 1600016400000L;
    long startDateTime = 1599670800000L;
    long endDateTime = 1599757200000L;

    long startDatePrevWeekTime = 1598806800000L;
    long endDatePrevWeekTime = 1599411600000L;

    long startDateNextWeekTime = 1600016400000L;
    long endDateNextWeekTime = 1600621200000L;

    String weekName = "Day 07-13/09/2020";
    String dateName = "10/09/2020";

    Date startDateWeek, endDateWeek, date;

    @Before
    public void initsDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTime);
        date = calendar.getTime();

        calendar.setTimeInMillis(startDateWeekTime);
        startDateWeek = calendar.getTime();

        calendar.setTimeInMillis(endDateWeekTime);
        endDateWeek = calendar.getTime();
    }

    @Test
    public void getPeriodWeek() {
        Date[] actuals = TimeUtils.getPeriodWeek(date);
        long[] times = new long[]{actuals[0].getTime(), actuals[1].getTime()};
        Assert.assertArrayEquals(new long[]{startDateWeekTime, endDateWeekTime}, times);
    }

    @Test
    public void getPeriodPrevWeek() {
        Date[] dates = TimeUtils.getPeriodPrevWeek(date);
        Assert.assertArrayEquals(new long[]{startDatePrevWeekTime, endDatePrevWeekTime}, new long[]{dates[0].getTime(), dates[1].getTime()});
    }

    @Test
    public void getPeriodNextWeek() {
        Date[] dates = TimeUtils.getPeriodNextWeek(date);
        Assert.assertArrayEquals(new long[]{startDateNextWeekTime, endDateNextWeekTime}, new long[]{dates[0].getTime(), dates[1].getTime()});
    }

    @Test
    public void getWeekName() {
        String actual = TimeUtils.getWeekName(startDateWeek, endDateWeek);
        Assert.assertEquals(weekName, actual);
    }

    @Test
    public void getPeriodDay() {
        Date[] dates = TimeUtils.getPeriodDay(date);
        Assert.assertArrayEquals(new long[]{startDateTime, endDateTime}, new long[]{dates[0].getTime(), dates[1].getTime()});
    }

    @Test
    public void convertDatetoString() {
        String actual = TimeUtils.convertDatetoString(date);
        Assert.assertEquals(dateName, actual);
    }

    @Test
    public void getYearMonthDay() {
        int[] actuals = TimeUtils.getYearMonthDay(date);
        Assert.assertArrayEquals(new int[]{2020, 8, 10}, actuals);
    }

    @Test
    public void dayOf() {
        int actual = TimeUtils.dayOf(1599708766510L);
        Assert.assertEquals(5, actual);
    }
}