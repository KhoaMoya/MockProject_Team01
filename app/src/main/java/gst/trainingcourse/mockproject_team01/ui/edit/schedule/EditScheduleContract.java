package gst.trainingcourse.mockproject_team01.ui.edit.schedule;

import java.util.ArrayList;
import java.util.Date;

import gst.trainingcourse.mockproject_team01.model.PassObject;
import gst.trainingcourse.mockproject_team01.model.Subject;
import gst.trainingcourse.mockproject_team01.model.WeekSchedule;
import gst.trainingcourse.mockproject_team01.model.tracker.ScheduleTracker;
import io.reactivex.disposables.Disposable;

interface EditScheduleContract {

    interface presenter {
        void loadAllSubjectFromDb();

        void updateScheduleTracker(ScheduleTracker tracker);

        WeekSchedule reloadSchedulesInWeekFromTrackers(Date[] dates);

        void updateScheduleTrackersBySubjectTrackers();

        void initWeekSchedule(WeekSchedule weekSchedule);

        void updateWeekSchedule(int year, int month, int day);

        void updateWeekSchedule(Date[] dates);

        WeekSchedule mapWeekSchedule(WeekSchedule fromDb);

        void updateSubjectTable(ArrayList<Subject> subjects);

        void setClickableSubjectTable(boolean isClickable);

        void editSubject(Subject editedSubject);

        void showSubjectTable();

        void addSubject(Subject newSubject);

        void addDisposable(Disposable disposable);

        void dispose();

        void save();

        void onNextWeek();

        void onPrevWeek();

        boolean isEdited();

        ArrayList<Subject> getSubjectListToDisplay();

        void deleteItem(PassObject<?> passObject);

        void setDraggableScheduleTable(boolean isDragable);

    }
}