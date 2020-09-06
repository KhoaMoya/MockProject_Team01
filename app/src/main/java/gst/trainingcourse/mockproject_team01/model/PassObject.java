package gst.trainingcourse.mockproject_team01.model;

public class PassObject<T> {

    public enum Type {
        SUBJECT,
        LESSON
    }

    public T data;
    public Type type;
    public int position;

    public PassObject(T data, Type type, int position) {
        this.data = data;
        this.type = type;
        this.position = position;
    }
}
