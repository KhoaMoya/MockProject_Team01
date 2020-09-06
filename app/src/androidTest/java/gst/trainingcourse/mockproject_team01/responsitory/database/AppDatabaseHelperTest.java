package gst.trainingcourse.mockproject_team01.responsitory.database;

import org.junit.Test;

import java.util.Date;

import gst.trainingcourse.mockproject_team01.MyApplication;
import gst.trainingcourse.mockproject_team01.model.Subject;

public class AppDatabaseHelperTest {

    @Test
    public void testDatabase(){
        AppDatabaseHelper databaseHelper = AppDatabaseHelper.getInstance(MyApplication.getContext());
        Subject sub = new Subject(new Date().getTime(), "Vật lý");
        databaseHelper.insertSubject(sub);
//        int timeId = databaseHelper.insertSubjectTime(time);
//
//        sub.setId(subId);
//        time.setId(timeId);
//
//        int scheId = databaseHelper.insertLessionSchedule(new LessonSchedule(0, new Date(), new Date(), sub, time));
//
//        Log.e("Loi", subId + " : " + timeId + " : " + scheId);
//
//        ArrayList<LessonSchedule> schedules = databaseHelper.getSchedule(TimeUtils.getPeriodWeek(null));
//
//        for(LessonSchedule sche : schedules){
//            Log.e("Loi", sche.getId() + " : " + sche.getSubject().getName() + " : " + sche.getSubjectTime().getDay() + " - " + sche.getSubjectTime().getLesson());
//        }
    }
}