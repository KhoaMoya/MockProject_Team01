package gst.trainingcourse.mockproject_team01.responsitory.database;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import gst.trainingcourse.mockproject_team01.MyApplication;
import gst.trainingcourse.mockproject_team01.model.LessonSchedule;
import gst.trainingcourse.mockproject_team01.model.Subject;
import gst.trainingcourse.mockproject_team01.model.SubjectTime;
import gst.trainingcourse.mockproject_team01.utils.TimeUtils;

import static org.junit.Assert.*;

public class AppDatabaseHelperTest {

    @Test
    public void testDatabase(){
        AppDatabaseHelper databaseHelper = AppDatabaseHelper.getInstance(MyApplication.getContext());
        Subject sub = new Subject(0, "Vật lý");
        SubjectTime time = new SubjectTime(0, 5, 1);
        int subId = databaseHelper.insertSubject(sub);
        int timeId = databaseHelper.insertSubjectTime(time);

        sub.setId(subId);
        time.setId(timeId);

        int scheId = databaseHelper.insertLessionSchedule(new LessonSchedule(0, new Date(), new Date(), sub, time));

        Log.e("Loi", subId + " : " + timeId + " : " + scheId);

        ArrayList<LessonSchedule> schedules = databaseHelper.getSchedule(TimeUtils.getPeriodWeek(null));

        for(LessonSchedule sche : schedules){
            Log.e("Loi", sche.getId() + " : " + sche.getSubject().getName() + " : " + sche.getSubjectTime().getDay() + " - " + sche.getSubjectTime().getLesson());
        }
    }
}