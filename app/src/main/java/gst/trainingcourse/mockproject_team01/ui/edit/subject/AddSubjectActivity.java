package gst.trainingcourse.mockproject_team01.ui.edit.subject;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import gst.trainingcourse.mockproject_team01.R;
import gst.trainingcourse.mockproject_team01.base.BaseEditSubjectActivity;
import gst.trainingcourse.mockproject_team01.model.Subject;
import gst.trainingcourse.mockproject_team01.model.tracker.SubjectTracker;
import gst.trainingcourse.mockproject_team01.ui.edit.schedule.EditScheduleActivity;

public class AddSubjectActivity extends BaseEditSubjectActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        initViews();
        initActions();

        getData();
    }

    @Override
    public void getData() {
        subjectList = (ArrayList<SubjectTracker>) getIntent().getSerializableExtra(EditScheduleActivity.SUBJECT_LIST_KEY);
        if (subjectList == null) finish();
    }

    public void initViews() {
        edtSubjectName = findViewById(R.id.edt_subject_name);
        btnOk = findViewById(R.id.btn_ok);
        btnCancel = findViewById(R.id.btn_cancel);
        imgVoiceInput = findViewById(R.id.img_voice_input);
    }

    @Override
    protected void actionOk(String newName) {
        Subject newSubject = new Subject(System.currentTimeMillis(), newName);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EditScheduleActivity.ADD_SUBJECT_KEY, newSubject);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}