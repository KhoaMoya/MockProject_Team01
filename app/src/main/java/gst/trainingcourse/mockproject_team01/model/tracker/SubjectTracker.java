package gst.trainingcourse.mockproject_team01.model.tracker;

import gst.trainingcourse.mockproject_team01.model.Subject;

public class SubjectTracker {
    public EditAction action;
    public Subject subject;

    public SubjectTracker(EditAction action, Subject subject) {
        this.action = action;
        this.subject = subject;
    }
}
