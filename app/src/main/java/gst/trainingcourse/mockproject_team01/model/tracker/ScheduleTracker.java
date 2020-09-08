package gst.trainingcourse.mockproject_team01.model.tracker;

import gst.trainingcourse.mockproject_team01.model.LessonSchedule;

public class ScheduleTracker {
    public EditAction action;
    public LessonSchedule schedule;

    public ScheduleTracker(EditAction action, LessonSchedule schedule) {
        this.action = action;
        this.schedule = schedule;
    }
}
