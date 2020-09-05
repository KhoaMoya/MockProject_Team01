package gst.trainingcourse.mockproject_team01.model;

import java.util.Date;

public class LessonSchedule {
    private int id;
    private Date startDate;
    private Date endDate;
    private Subject subject;
    private SubjectTime subjectTime;

    public LessonSchedule(int id, Date startDate, Date endDate, Subject subject, SubjectTime subjectTime) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subject = subject;
        this.subjectTime = subjectTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public SubjectTime getSubjectTime() {
        return subjectTime;
    }

    public void setSubjectTime(SubjectTime subjectTime) {
        this.subjectTime = subjectTime;
    }
}
