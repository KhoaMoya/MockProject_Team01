package gst.trainingcourse.mockproject_team01.model;

public class SubjectTime {
    private int id;
    private int day;
    private int lesson;

    public SubjectTime(int id, int day, int lesson) {
        this.id = id;
        this.day = day;
        this.lesson = lesson;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
