package gst.trainingcourse.mockproject_team01.model;

import java.io.Serializable;
import java.util.Date;

public class LessonSchedule implements Serializable {
    private long id;
    private Date startDate;
    private Date endDate;
    private Subject subject;
    private int day;
    private int lesson;

    public LessonSchedule(long id, Date startDate, Date endDate, Subject subject, int day, int lesson) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subject = subject;
        this.day = day;
        this.lesson = lesson;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getLesson() {
        return lesson;
    }

    public void setLesson(int lesson) {
        this.lesson = lesson;
    }
}
