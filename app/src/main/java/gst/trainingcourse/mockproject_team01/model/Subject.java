package gst.trainingcourse.mockproject_team01.model;

import java.io.Serializable;

public class Subject implements Serializable {
    private long id;
    private String name;

    public Subject(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
