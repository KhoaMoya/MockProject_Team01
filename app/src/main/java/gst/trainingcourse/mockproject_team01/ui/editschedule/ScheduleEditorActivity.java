package gst.trainingcourse.mockproject_team01.ui.editschedule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
import gst.trainingcourse.mockproject_team01.model.Subject;
import gst.trainingcourse.mockproject_team01.model.WeekSchedule;
import gst.trainingcourse.mockproject_team01.ui.editsubject.SubjectEditorActivity;
import gst.trainingcourse.mockproject_team01.ui.main.MainActivity;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ScheduleEditorActivity extends BaseScheduleActivity implements View.OnClickListener
        , OnClickItemListener<Subject>
        , LessonScheduleListener {

    private ImageView imgTrash, btnPrevWeek, btnNextWeek;
    private RecyclerView subjectTable;
    private Button btnEditSubjectName, btnOk, btnCancel;
    private SubjectTableAdapter mSubjectTableAdapter;

    private ArrayList<LessonSchedule> mEditedLessonScheduleList;
    private WeekSchedule mCurrentWeekSchedule;
    private ArrayList<Subject> mCurrentSubjectList;
    private CompositeDisposable mDisposable;

    private boolean isEditingSubjectName = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_editor);

        inits();
        initViews();
        initActions();
        loadDatas();
    }

    private void inits() {
        mDisposable = new CompositeDisposable();
        mEditedLessonScheduleList = new ArrayList<>();
        mCurrentSubjectList = new ArrayList<>();
    }

    private void loadDatas() {
        scheduleTable.post(new Runnable() {
            @Override
            public void run() {
                initTableSchedule(true);
                scheduleTableAdapter.setLessonScheduleListener(ScheduleEditorActivity.this);
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
        WeekSchedule schedule = (WeekSchedule) getIntent().getSerializableExtra(MainActivity.EDIT_SCHECULE_KEY);
        if (schedule != null) {
            showWeekSchedule(schedule);
            mCurrentWeekSchedule = schedule;
            mEditedLessonScheduleList = schedule.getLessonSchedules();
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
            case R.id.btn_ok:
                break;
            case R.id.btn_cancel:
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
    }

    @Override
    public void updateLessonSchedule(ScheduleAction action, LessonSchedule schedule) {
        Log.e("Loi", action.name() + " : " + schedule.getId());

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
        Intent intent = new Intent(this, SubjectEditorActivity.class);
        startActivity(intent);
    }

    private void setEnableButton(boolean isEnable) {
        btnOk.setEnabled(isEnable);
        btnCancel.setEnabled(isEnable);
        btnPrevWeek.setEnabled(isEnable);
        btnNextWeek.setEnabled(isEnable);
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
        btnEditSubjectName = findViewById(R.id.btn_edit_subject);
    }

    @Override
    protected void onDestroy() {
        mDisposable.dispose();
        mDisposable.clear();
        super.onDestroy();
    }
}