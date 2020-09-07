package gst.trainingcourse.mockproject_team01.ui.edit.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gst.trainingcourse.mockproject_team01.R;
import gst.trainingcourse.mockproject_team01.adapter.LessonScheduleListener;
import gst.trainingcourse.mockproject_team01.adapter.OnClickItemListener;
import gst.trainingcourse.mockproject_team01.adapter.SubjectTableAdapter;
import gst.trainingcourse.mockproject_team01.base.BaseScheduleActivity;
import gst.trainingcourse.mockproject_team01.model.LessonSchedule;
import gst.trainingcourse.mockproject_team01.model.ScheduleAction;
import gst.trainingcourse.mockproject_team01.model.ScheduleTracker;
import gst.trainingcourse.mockproject_team01.model.Subject;
import gst.trainingcourse.mockproject_team01.model.WeekSchedule;
import gst.trainingcourse.mockproject_team01.ui.edit.subject.AddSubjectActivity;
import gst.trainingcourse.mockproject_team01.ui.edit.subject.RenameSubjectActivity;
import gst.trainingcourse.mockproject_team01.ui.main.MainActivity;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class EditScheduleActivity extends BaseScheduleActivity implements View.OnClickListener
        , OnClickItemListener<Subject>
        , LessonScheduleListener {

    public final static String RENAME_SUBJECT_KEY = "edit subject key";
    public final static int EDIT_SUBJECT_REQUEST_CODE = 10;
    public final static String ADD_SUBJECT_KEY = "add subject key";
    public final static int ADD_SUBJECT_REQUEST_CODE = 12;

    private ImageView imgTrash, btnPrevWeek, btnNextWeek;
    private RecyclerView subjectTable;
    private Button btnEditSubjectName, btnOk, btnCancel, btnAddSubject;
    private SubjectTableAdapter mSubjectTableAdapter;

    private ArrayList<ScheduleTracker> mScheduleTrackerList;
    private WeekSchedule mCurrentWeekSchedule;
    private ArrayList<Subject> mCurrentSubjectList;
    private CompositeDisposable mDisposable;
    private ArrayList<Integer> mEditedSubjectIndexList;

    private boolean isEditingSubjectName = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        inits();
        initViews();
        initActions();
        loadDatas();
    }

    private void inits() {
        mDisposable = new CompositeDisposable();
        mScheduleTrackerList = new ArrayList<>();
        mCurrentSubjectList = new ArrayList<>();
        mEditedSubjectIndexList = new ArrayList<>();
    }

    private void loadDatas() {
        scheduleTable.post(new Runnable() {
            @Override
            public void run() {
                initTableSchedule(true);
                scheduleTableAdapter.setLessonScheduleListener(EditScheduleActivity.this);
                getScheduleFromIntent();
            }
        });

        subjectTable.post(new Runnable() {
            @Override
            public void run() {
                initSubjectTable();
                loadAllSubjectsFromDb();
            }
        });
    }

    private void initSubjectTable() {
        int rowHeight = subjectTable.getHeight() / SubjectTableAdapter.ROW_NUMBER;
        mSubjectTableAdapter = new SubjectTableAdapter(this, this, rowHeight);

        subjectTable.setLayoutManager(mSubjectTableAdapter.layoutManager);
        subjectTable.setAdapter(mSubjectTableAdapter);
    }

    private void getScheduleFromIntent() {
        WeekSchedule weekSchedule = (WeekSchedule) getIntent().getSerializableExtra(MainActivity.EDIT_SCHECULE_KEY);
        if (weekSchedule != null) {
            showWeekSchedule(weekSchedule);
            mCurrentWeekSchedule = weekSchedule;
            for (LessonSchedule sch : weekSchedule.getLessonSchedules()) {
                mScheduleTrackerList.add(new ScheduleTracker(ScheduleAction.NONE, sch));
            }
        } else {
            finish();
        }
    }

    private void loadAllSubjectsFromDb() {
        ScheduleEditor.getAllSubjectFromDb()
                .subscribe(new SingleObserver<ArrayList<Subject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(ArrayList<Subject> subjects) {
                        showSubjectTable(subjects);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_edit_subject:
                onClickBtnEditLesson();
                break;
            case R.id.btn_add_subject:
                Intent addSubjectIntent = new Intent(EditScheduleActivity.this, AddSubjectActivity.class);
                startActivityForResult(addSubjectIntent, ADD_SUBJECT_REQUEST_CODE);
                break;
            case R.id.btn_ok:
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    private void onClickBtnEditLesson() {
        if (!isEditingSubjectName) {
            enableEditingState();
        } else {
            disableEditingState();
        }
        isEditingSubjectName = !isEditingSubjectName;
    }

    private void showSubjectTable(ArrayList<Subject> subjects) {
        mCurrentSubjectList = subjects;
        mSubjectTableAdapter.setSubjecList(subjects);
    }

    private void initActions() {
        btnEditSubjectName.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnAddSubject.setOnClickListener(this);
    }

    @Override
    public void updateLessonSchedule(ScheduleTracker tracker) {
        if (tracker.action == ScheduleAction.ADD) {
            mScheduleTrackerList.add(tracker);
        } else if (tracker.action == ScheduleAction.EDIT || tracker.action == ScheduleAction.DELETE) {
            for (int i = 0; i < mScheduleTrackerList.size(); i++) {
                if (mScheduleTrackerList.get(i).schedule.getId() == tracker.schedule.getId()) {
                    mScheduleTrackerList.set(i, tracker);
                    break;
                }
            }
        }
        reloadLessonSchedulesInWeekFromTrackerList();
        scheduleTableAdapter.setScheduleList(mCurrentWeekSchedule);
    }

    private void reloadLessonSchedulesInWeekFromTrackerList() {
        long startTime, endTime;
        mCurrentWeekSchedule.getLessonSchedules().clear();

        long startWeek = mCurrentWeekSchedule.getStartDate().getTime();
        long endWeek = mCurrentWeekSchedule.getEndDate().getTime();

        for (int i = 0; i < mScheduleTrackerList.size(); i++) {
            if (mScheduleTrackerList.get(i).action == ScheduleAction.DELETE) continue;

            LessonSchedule schedule = mScheduleTrackerList.get(i).schedule;
            startTime = schedule.getStartDate().getTime();
            endTime = schedule.getEndDate().getTime();

            if (startTime < endWeek && endTime >= startWeek) {
                mCurrentWeekSchedule.getLessonSchedules().add(schedule);
            }
        }
    }

    private void enableEditingState() {
        setEnableButton(false);
        btnEditSubjectName.setText("Cancel editing");
        btnEditSubjectName.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        mSubjectTableAdapter.isCanClickItem = true;
    }

    private void disableEditingState() {
        setEnableButton(true);
        btnEditSubjectName.setText("Edit lesson");
        btnEditSubjectName.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        mSubjectTableAdapter.isCanClickItem = false;
    }

    @Override
    public void onClickItem(Subject subject) {
        Intent intent = new Intent(this, RenameSubjectActivity.class);
        intent.putExtra(RENAME_SUBJECT_KEY, subject);
        startActivityForResult(intent, EDIT_SUBJECT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == EDIT_SUBJECT_REQUEST_CODE) {
                Subject resultSubject = (Subject) data.getSerializableExtra(RENAME_SUBJECT_KEY);
                int index = -1;
                if (resultSubject != null) {
                    for (int i = 0; i < mCurrentSubjectList.size(); i++) {
                        if (mCurrentSubjectList.get(i).getId() == resultSubject.getId()) {
                            index = i;
                            break;
                        }
                    }
                    if (index != -1) {
                        mEditedSubjectIndexList.add(index);
                        mCurrentSubjectList.set(index, resultSubject);

                        updateLessonScheduleListBySubjectList();
                    }
                }
            } else if (requestCode == ADD_SUBJECT_REQUEST_CODE) {
                Subject newSubject = (Subject) data.getSerializableExtra(ADD_SUBJECT_KEY);
                mCurrentSubjectList.add(newSubject);
            }
        }
    }

    private void updateLessonScheduleListBySubjectList() {
        for (int i = 0; i < mEditedSubjectIndexList.size(); i++) {
            for (int j = 0; j < mScheduleTrackerList.size(); j++) {
                long idEditedSubject = mCurrentSubjectList.get(i).getId();
                if (mScheduleTrackerList.get(j).schedule.getSubject().getId() == idEditedSubject) {
                    mScheduleTrackerList.get(j).schedule.setSubject(mCurrentSubjectList.get(i));
                    reloadLessonSchedulesInWeekFromTrackerList();
                    scheduleTableAdapter.setScheduleList(mCurrentWeekSchedule);
                    break;
                }
            }
        }
    }

    private void setEnableButton(boolean isEnable) {
        btnOk.setEnabled(isEnable);
        btnCancel.setEnabled(isEnable);
        btnPrevWeek.setEnabled(isEnable);
        btnNextWeek.setEnabled(isEnable);
        btnAddSubject.setEnabled(isEnable);
    }

    private void initViews() {
        scheduleTable = findViewById(R.id.tbl_schedule);
        subjectTable = findViewById(R.id.tbl_lesson);
        txtWeekName = findViewById(R.id.txt_week_name);
        btnNextWeek = findViewById(R.id.img_next_week);
        btnPrevWeek = findViewById(R.id.img_prev_week);
        imgTrash = findViewById(R.id.img_trash);
        btnOk = findViewById(R.id.btn_ok);
        btnCancel = findViewById(R.id.btn_cancel);
        btnAddSubject = findViewById(R.id.btn_add_subject);
        btnEditSubjectName = findViewById(R.id.btn_edit_subject);
    }

    @Override
    protected void onDestroy() {
        mDisposable.dispose();
        mDisposable.clear();
        super.onDestroy();
    }
}