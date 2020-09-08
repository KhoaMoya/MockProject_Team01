package gst.trainingcourse.mockproject_team01.ui.edit.schedule;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import gst.trainingcourse.mockproject_team01.model.Subject;
import gst.trainingcourse.mockproject_team01.model.WeekSchedule;
import gst.trainingcourse.mockproject_team01.model.tracker.ScheduleTracker;
import gst.trainingcourse.mockproject_team01.ui.edit.subject.AddSubjectActivity;
import gst.trainingcourse.mockproject_team01.ui.edit.subject.RenameSubjectActivity;
import gst.trainingcourse.mockproject_team01.ui.main.MainActivity;
import gst.trainingcourse.mockproject_team01.utils.TimeUtils;
import io.reactivex.SingleObserver;
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

    private EditSchedulePresenter mEditSchedulePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        init();
        initViews();
        initActions();
        loadDatas();
    }

    private void init() {
        mEditSchedulePresenter = new EditSchedulePresenter(this);
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
        mEditSchedulePresenter.subjectTableAdapter = new SubjectTableAdapter(this, this, rowHeight);

        subjectTable.setLayoutManager(mEditSchedulePresenter.subjectTableAdapter.layoutManager);
        subjectTable.setAdapter(mEditSchedulePresenter.subjectTableAdapter);
    }

    private void getScheduleFromIntent() {
        WeekSchedule weekSchedule = (WeekSchedule) getIntent().getSerializableExtra(MainActivity.EDIT_SCHECULE_KEY);
        if (weekSchedule != null) {
            showWeekSchedule(weekSchedule);
            mEditSchedulePresenter.initWeekSchedule(weekSchedule);
        } else {
            finish();
        }
    }

    private void loadAllSubjectsFromDb() {
        mEditSchedulePresenter.getAllSubjectFromDb()
                .subscribe(new SingleObserver<ArrayList<Subject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mEditSchedulePresenter.addDisposable(d);
                    }

                    @Override
                    public void onSuccess(ArrayList<Subject> subjects) {
                        mEditSchedulePresenter.updateSubjectTable(subjects);
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
            case R.id.txt_week_name:
                showChangeWeekDialog();
                break;
            case R.id.img_next_week:
                mEditSchedulePresenter.onNextWeek();
                break;
            case R.id.img_prev_week:
                mEditSchedulePresenter.onPrevWeek();
                break;
            case R.id.btn_ok:
                mEditSchedulePresenter.save();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    private void showChangeWeekDialog() {
        int[] times = TimeUtils.getYearMonthDay(mEditSchedulePresenter.currentWeekSchedule.getStartDate());
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mEditSchedulePresenter.updateWeekSchedule(year, month, day);
            }
        }, times[0], times[1], times[2]).show();
    }

    private void onClickBtnEditLesson() {
        if (!mEditSchedulePresenter.isEditingSubjectName) {
            enableEditingState();
        } else {
            disableEditingState();
        }
        mEditSchedulePresenter.isEditingSubjectName = !mEditSchedulePresenter.isEditingSubjectName;
    }

    private void initActions() {
        btnEditSubjectName.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnAddSubject.setOnClickListener(this);
        txtWeekName.setOnClickListener(this);
        btnNextWeek.setOnClickListener(this);
        btnPrevWeek.setOnClickListener(this);

        imgTrash.setOnDragListener(new RecycleBinDropListener());
    }

    @Override
    public void updateScheduleTracker(ScheduleTracker tracker) {
        mEditSchedulePresenter.updateScheduleTracker(tracker);
    }

    private void enableEditingState() {
        setEnableButton(false);
        btnEditSubjectName.setText("Cancel editing");
        btnEditSubjectName.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        mEditSchedulePresenter.setClickableSubjectTable(true);
    }

    private void disableEditingState() {
        setEnableButton(true);
        btnEditSubjectName.setText("Edit lesson");
        btnEditSubjectName.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        mEditSchedulePresenter.setClickableSubjectTable(false);
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
                mEditSchedulePresenter.editSubject(resultSubject);
            } else if (requestCode == ADD_SUBJECT_REQUEST_CODE) {
                Subject newSubject = (Subject) data.getSerializableExtra(ADD_SUBJECT_KEY);
                mEditSchedulePresenter.addSubject(newSubject);
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
        progressBarLoading = findViewById(R.id.pb_loading);
    }


    @Override
    protected void onDestroy() {
        mEditSchedulePresenter.dispose();
        super.onDestroy();
    }

//     TODO drag để xóa
//     TODO Thêm vào lớp BaseScheduleActivity hàm setResult()
//     TODO ẩn itemview khi drag
//     TODO disable click khi ở chế độ edit lesson name
}