package gst.trainingcourse.mockproject_team01.model.tracker;

import java.io.Serializable;

import gst.trainingcourse.mockproject_team01.model.Subject;

public class SubjectTracker implements Serializable {
    public EditAction action;
    public Subject subject;

    public SubjectTracker(EditAction action, Subject subject) {
        this.action = action;
        this.subject = subject;
    }
}
