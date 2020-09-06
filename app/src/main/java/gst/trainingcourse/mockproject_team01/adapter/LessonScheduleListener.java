package gst.trainingcourse.mockproject_team01.adapter;

import gst.trainingcourse.mockproject_team01.model.LessonSchedule;
import gst.trainingcourse.mockproject_team01.model.ScheduleAction;

public interface LessonScheduleListener {
    void updateLessonSchedule(ScheduleAction action, LessonSchedule schedule);
}
