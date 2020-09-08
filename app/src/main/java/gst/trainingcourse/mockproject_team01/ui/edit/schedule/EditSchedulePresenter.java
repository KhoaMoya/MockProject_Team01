package gst.trainingcourse.mockproject_team01.ui.edit.schedule;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import gst.trainingcourse.mockproject_team01.MyApplication;
import gst.trainingcourse.mockproject_team01.adapter.SubjectTableAdapter;
import gst.trainingcourse.mockproject_team01.base.BaseScheduleActivity;
import gst.trainingcourse.mockproject_team01.model.LessonSchedule;
import gst.trainingcourse.mockproject_team01.model.PassObject;
import gst.trainingcourse.mockproject_team01.model.Subject;
import gst.trainingcourse.mockproject_team01.model.WeekSchedule;
import gst.trainingcourse.mockproject_team01.model.tracker.EditAction;
import gst.trainingcourse.mockproject_team01.model.tracker.ScheduleTracker;
import gst.trainingcourse.mockproject_team01.model.tracker.SubjectTracker;
import gst.trainingcourse.mockproject_team01.responsitory.database.AppDatabaseHelper;
import gst.trainingcourse.mockproject_team01.utils.TimeUtils;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditSchedulePresenter implements EditScheduleContract.presenter {

    public ArrayList<ScheduleTracker> scheduleTrackerList;
    public ArrayList<SubjectTracker> subjectTrackerList;
    public boolean isEditingSubjectName = false;
    private CompositeDisposable mDisposable;
    private BaseScheduleActivity mActivity;
    public SubjectTableAdapter subjectTableAdapter;

    public EditSchedulePresenter(BaseScheduleActivity activity) {
        this.mActivity = activity;
        mDisposable = new CompositeDisposable();
        scheduleTrackerList = new ArrayList<>();
        subjectTrackerList = new ArrayList<>();
    }

    @Override
    public void initWeekSchedule(WeekSchedule weekSchedule) {
        mActivity.currentWeekSchedule = weekSchedule;
        for (LessonSchedule sch : weekSchedule.getLessonSchedules()) {
            scheduleTrackerList.add(new ScheduleTracker(EditAction.NONE, sch));
        }
    }

    @Override
    public void loadAllSubjectFromDb() {
        Single.create(new SingleOnSubscribe<ArrayList<Subject>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Subject>> emitter) throws Exception {
                if (!emitter.isDisposed())
                    emitter.onSuccess(AppDatabaseHelper.getInstance(MyApplication.getContext()).getAllSubjects());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<Subject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(ArrayList<Subject> subjects) {
                        updateSubjectTable(subjects);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void updateSubjectTable(ArrayList<Subject> subjects) {
        subjectTrackerList.clear();
        for (Subject sub : subjects) {
            subjectTrackerList.add(new SubjectTracker(EditAction.NONE, sub));
        }
        showSubjectTable();
    }

    @Override
    public void updateScheduleTracker(ScheduleTracker tracker) {
        if (tracker.action == EditAction.ADD) {
            scheduleTrackerList.add(tracker);
        } else if (tracker.action == EditAction.EDIT || tracker.action == EditAction.DELETE) {
            int index = scheduleTrackerList.size() - 1;
            while (index >= 0) {
                ScheduleTracker scheduleTracker = scheduleTrackerList.get(index);
                if (scheduleTracker.schedule.getId() == tracker.schedule.getId() && scheduleTracker.action != EditAction.DELETE) {
                    EditAction action = scheduleTracker.action;
                    if (tracker.action == EditAction.EDIT) {
                        if (action == EditAction.NONE) {
                            scheduleTracker.action = EditAction.EDIT;
                        }
                        scheduleTracker.schedule = tracker.schedule;
                    } else if (tracker.action == EditAction.DELETE) {
                        if (action == EditAction.ADD) {
                            scheduleTrackerList.remove(index);
                        } else {
                            scheduleTrackerList.get(index).action = EditAction.DELETE;
                        }
                    }
                    break;
                }
                index -= 1;
            }
        }
        mActivity.updateWeekSchedule(reloadSchedulesInWeekFromTrackers(mActivity.currentWeekSchedule.getDates()));
    }

    @Override
    public WeekSchedule reloadSchedulesInWeekFromTrackers(Date[] dates) {
        long startTime, endTime;
        WeekSchedule weekSchedule = new WeekSchedule(dates[0], dates[1]);

        long startWeek = dates[0].getTime();
        long endWeek = dates[1].getTime();

        for (int i = 0; i < scheduleTrackerList.size(); i++) {
            if (scheduleTrackerList.get(i).action == EditAction.DELETE) continue;

            LessonSchedule schedule = scheduleTrackerList.get(i).schedule;
            startTime = schedule.getStartDate().getTime();
            endTime = schedule.getEndDate().getTime();
            int day = schedule.getDay();

            if (startTime < endWeek
                    && TimeUtils.dayOf(startTime) <= day
                    && endTime >= startWeek
                    && TimeUtils.dayOf(endTime) >= day) {
                weekSchedule.getLessonSchedules().add(schedule);
            }
        }
        return weekSchedule;
    }

    @Override
    public void setClickableSubjectTable(boolean isClickable) {
        subjectTableAdapter.isCanClickItem = isClickable;
        subjectTableAdapter.isCanDragItem = !isClickable;
    }

    @Override
    public void setDraggableScheduleTable(boolean isDragable) {
        if (isDragable) {
            mActivity.scheduleTable.setAlpha(1f);
            mActivity.txtWeekName.setAlpha(1f);
            mActivity.txtWeekName.setClickable(true);
        } else {
            mActivity.scheduleTable.setAlpha(0.5f);
            mActivity.txtWeekName.setAlpha(0.5f);
            mActivity.txtWeekName.setClickable(false);
        }
        mActivity.scheduleTableAdapter.isCanDragItem = isDragable;
    }

    @Override
    public void editSubject(Subject editedSubject) {
        int index = -1;
        if (editedSubject != null) {
            for (int i = 0; i < subjectTrackerList.size(); i++) {
                if (subjectTrackerList.get(i).subject.getId() == editedSubject.getId()
                        && subjectTrackerList.get(i).action != EditAction.DELETE) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                if (subjectTrackerList.get(index).action == EditAction.NONE) {
                    subjectTrackerList.get(index).action = EditAction.EDIT;
                }
                subjectTrackerList.get(index).subject = editedSubject;

                showSubjectTable();
                updateScheduleTrackersBySubjectTrackers();
                WeekSchedule newWeekSchedule = reloadSchedulesInWeekFromTrackers(mActivity.currentWeekSchedule.getDates());
                mActivity.updateWeekSchedule(newWeekSchedule);
            }
        }
    }

    @Override
    public ArrayList<Subject> getSubjectListToDisplay() {
        ArrayList<Subject> list = new ArrayList<>();
        for (SubjectTracker tracker : subjectTrackerList) {
            if (tracker.action != EditAction.DELETE) list.add(tracker.subject);
        }
        return list;
    }

    @Override
    public void showSubjectTable() {
        subjectTableAdapter.setSubjecList(getSubjectListToDisplay());
    }

    @Override
    public void addSubject(Subject subject) {
        subjectTrackerList.add(new SubjectTracker(EditAction.ADD, subject));
        showSubjectTable();
    }

    @Override
    public void updateScheduleTrackersBySubjectTrackers() {
        for (int i = 0; i < subjectTrackerList.size(); i++) {
            if (subjectTrackerList.get(i).action == EditAction.DELETE) continue;
            long idSubject = subjectTrackerList.get(i).subject.getId();
            for (int j = 0; j < scheduleTrackerList.size(); j++) {
                ScheduleTracker scheduleTracker = scheduleTrackerList.get(j);
                LessonSchedule schedule = scheduleTracker.schedule;
                if (schedule.getSubject().getId() == idSubject && scheduleTracker.action != EditAction.DELETE) {
                    schedule.setSubject(subjectTrackerList.get(i).subject);
                }
            }
        }
    }

    @Override
    public WeekSchedule mapWeekSchedule(WeekSchedule fromDb) {
        ArrayList<LessonSchedule> scheduleList = new ArrayList<>(fromDb.getLessonSchedules());
        for (int i = 0; i < scheduleList.size(); i++) {
            boolean isExist = false;
            long id = scheduleList.get(i).getId();
            for (int j = 0; j < scheduleTrackerList.size(); j++) {
                if (scheduleTrackerList.get(j).schedule.getId() == id) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                scheduleTrackerList.add(new ScheduleTracker(EditAction.NONE, scheduleList.get(i)));
            }
        }
        return reloadSchedulesInWeekFromTrackers(fromDb.getDates());
    }

    @Override
    public void save() {
        if (!isEdited()) return;

        mActivity.showLoading();
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    AppDatabaseHelper database = AppDatabaseHelper.getInstance(mActivity);
                    for (SubjectTracker subjectTracker : subjectTrackerList) {
                        switch (subjectTracker.action) {
                            case EDIT:
                                database.updateSubject(subjectTracker.subject);
                                break;
                            case DELETE:
                                database.deleteSubject(subjectTracker.subject);
                                break;
                            case ADD:
                                database.insertSubject(subjectTracker.subject);
                                break;
                        }
                    }
                    for (ScheduleTracker scheduleTracker : scheduleTrackerList) {
                        switch (scheduleTracker.action) {
                            case EDIT:
                                database.updateSchedule(scheduleTracker.schedule);
                                break;
                            case DELETE:
                                database.deleteSchedule(scheduleTracker.schedule);
                                break;
                            case ADD:
                                database.insertLessionSchedule(scheduleTracker.schedule);
                                break;
                        }
                    }
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onComplete() {
                        mActivity.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(mActivity, "Save fail", Toast.LENGTH_SHORT).show();
                        mActivity.hideLoading();
                    }
                });
    }

    @Override
    public boolean isEdited() {
        for (ScheduleTracker scheduleTracker : scheduleTrackerList) {
            if (scheduleTracker.action != EditAction.NONE) return true;
        }
        for (SubjectTracker subjectTracker : subjectTrackerList) {
            if (subjectTracker.action != EditAction.NONE) return true;
        }
        return false;
    }

    @Override
    public void onNextWeek() {
        Date[] dates = TimeUtils.getPeriodNextWeek(mActivity.currentWeekSchedule.getStartDate());
        updateWeekSchedule(dates);
    }

    @Override
    public void onPrevWeek() {
        Date[] dates = TimeUtils.getPeriodPrevWeek(mActivity.currentWeekSchedule.getStartDate());
        updateWeekSchedule(dates);
    }

    @Override
    public void updateWeekSchedule(final Date[] dates) {
        Single.create(new SingleOnSubscribe<WeekSchedule>() {
            @Override
            public void subscribe(SingleEmitter<WeekSchedule> emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(AppDatabaseHelper.getInstance(mActivity).getWeekSchedule(dates));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<WeekSchedule>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(WeekSchedule newWeekSchedule) {
                        WeekSchedule mapWeekSchedule = mapWeekSchedule(newWeekSchedule);
                        mActivity.updateWeekSchedule(mapWeekSchedule);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void updateWeekSchedule(final int year, final int month, final int day) {
        updateWeekSchedule(TimeUtils.getPeriodWeek(year, month, day));
    }

    @Override
    public void deleteItem(final PassObject<?> passObject) {
        if (passObject.type == PassObject.Type.SUBJECT) {
            Subject subject = (Subject) passObject.data;
            for (int i = 0; i < subjectTrackerList.size(); i++) {
                if (subjectTrackerList.get(i).subject.getId() == subject.getId()) {
                    int index = scheduleTrackerList.size() - 1;
                    while (index >= 0) {
                        ScheduleTracker scheduleTracker = scheduleTrackerList.get(index);
                        if (scheduleTracker.schedule.getSubject().getId() == subject.getId()) {
                            if (scheduleTracker.action == EditAction.ADD) {
                                scheduleTrackerList.remove(index);
                            } else {
                                scheduleTracker.action = EditAction.DELETE;
                            }
                        }
                        index -= 1;
                    }
                    if (subjectTrackerList.get(i).action == EditAction.ADD) {
                        subjectTrackerList.remove(i);
                    } else {
                        subjectTrackerList.get(i).action = EditAction.DELETE;
                    }
                    break;
                }
            }
            showSubjectTable();
        } else if (passObject.type == PassObject.Type.LESSON) {
            LessonSchedule lessonSchedule = (LessonSchedule) passObject.data;
            int index = scheduleTrackerList.size() - 1;
            while (index >= 0) {
                if (scheduleTrackerList.get(index).schedule.getId() == lessonSchedule.getId()) {
                    if (scheduleTrackerList.get(index).action == EditAction.ADD) {
                        scheduleTrackerList.remove(index);
                    } else {
                        scheduleTrackerList.get(index).action = EditAction.DELETE;
                    }
                }
                index -= 1;
            }
        }
        WeekSchedule weekSchedule = reloadSchedulesInWeekFromTrackers(mActivity.currentWeekSchedule.getDates());
        mActivity.updateWeekSchedule(weekSchedule);
    }

    @Override
    public void addDisposable(Disposable disposable) {
        mDisposable.add(disposable);
    }

    @Override
    public void dispose() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable.clear();
        }
    }
}
