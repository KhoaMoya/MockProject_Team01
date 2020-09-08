package gst.trainingcourse.mockproject_team01.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import gst.trainingcourse.mockproject_team01.utils.TimeUtils;

public class WeekSchedule implements Serializable {
    private Date startDate;
    private Date endDate;
    private ArrayList<LessonSchedule> lessonSchedules;
    private String name;

    public WeekSchedule(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = TimeUtils.getWeekName(startDate, endDate);
        this.lessonSchedules = new ArrayList<>();
    }

    public WeekSchedule(Date[] dates, ArrayList<LessonSchedule> lessonSchedules) {
        this.startDate = dates[0];
        this.endDate = dates[1];
        this.lessonSchedules = lessonSchedules;
        this.name = TimeUtils.getWeekName(startDate, endDate);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ArrayList<LessonSchedule> getLessonSchedules() {
        return lessonSchedules;
    }

    public void setLessonSchedules(ArrayList<LessonSchedule> lessonSchedules) {
        this.lessonSchedules = lessonSchedules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
