package gst.trainingcourse.mockproject_team01.model;

public class ScheduleTracker {
    public ScheduleAction action;
    public LessonSchedule schedule;

    public ScheduleTracker(ScheduleAction action, LessonSchedule schedule) {
        this.action = action;
        this.schedule = schedule;
    }
}
