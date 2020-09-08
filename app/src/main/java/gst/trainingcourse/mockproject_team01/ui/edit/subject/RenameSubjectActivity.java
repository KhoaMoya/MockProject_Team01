package gst.trainingcourse.mockproject_team01.ui.edit.subject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import gst.trainingcourse.mockproject_team01.R;
import gst.trainingcourse.mockproject_team01.base.BaseEditSubjectActivity;
import gst.trainingcourse.mockproject_team01.model.Subject;
import gst.trainingcourse.mockproject_team01.model.tracker.SubjectTracker;
import gst.trainingcourse.mockproject_team01.ui.edit.schedule.EditScheduleActivity;

public class RenameSubjectActivity extends BaseEditSubjectActivity {

    private TextView txtOldName;

    private Subject editSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_subject);

        initViews();
        initActions();
        getData();
    }

    @Override
    public void getData() {
        subjectList = (ArrayList<SubjectTracker>) getIntent().getSerializableExtra(EditScheduleActivity.SUBJECT_LIST_KEY);
        if (subjectList == null) finish();

        editSubject = (Subject) getIntent().getSerializableExtra(EditScheduleActivity.RENAME_SUBJECT_KEY);
        if (editSubject == null) finish();
        else {
            txtOldName.setText(editSubject.getName());
        }
    }

    @Override
    protected void initViews() {
        txtOldName = findViewById(R.id.txt_old_name);
        edtSubjectName = findViewById(R.id.edt_new_name);
        btnOk = findViewById(R.id.btn_ok);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    @Override
    protected void actionOk(String newName) {
        editSubject.setName(newName);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EditScheduleActivity.RENAME_SUBJECT_KEY, editSubject);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}