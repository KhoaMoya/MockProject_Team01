package gst.trainingcourse.mockproject_team01.responsitory.preference;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import gst.trainingcourse.mockproject_team01.MyApplication;

public class SharedPreferencesHelper {

    public final static String START_DATE_KEY = "start date";
    public final static String END_DATE_KEY = "end date";
    public final static String PREFERENCES_NAME = "SubjectSchedulePreferences";

    private static SharedPreferences mSharedPreferences;

    private static void init() {
        if (mSharedPreferences == null)
            mSharedPreferences = MyApplication.getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static Date getStartDate() {
        init();
        long startTime = mSharedPreferences.getLong(START_DATE_KEY, 0);
        return startTime != 0 ? new Date(startTime) : new Date();
    }

    public static Date getEndDate() {
        init();
        long endTime = mSharedPreferences.getLong(END_DATE_KEY, 0);
        return endTime != 0 ? new Date(endTime) : new Date();
    }

    public static void saveStartDate(Date startDate) {
        init();
        mSharedPreferences.edit().putLong(START_DATE_KEY, startDate.getTime()).apply();
    }

    public static void saveEndDate(Date endDate) {
        init();
        mSharedPreferences.edit().putLong(END_DATE_KEY, endDate.getTime()).apply();
    }
}
