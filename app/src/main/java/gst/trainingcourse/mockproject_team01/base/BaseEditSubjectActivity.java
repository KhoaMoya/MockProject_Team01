package gst.trainingcourse.mockproject_team01.base;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import gst.trainingcourse.mockproject_team01.R;
import gst.trainingcourse.mockproject_team01.model.tracker.EditAction;
import gst.trainingcourse.mockproject_team01.model.tracker.SubjectTracker;

public abstract class BaseEditSubjectActivity extends AppCompatActivity implements View.OnClickListener {

    protected Button btnOk, btnCancel;
    protected EditText edtSubjectName;
    protected ArrayList<SubjectTracker> subjectList;

    protected abstract void initViews();

    protected void initActions() {
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void onClickOk() {
        validateSubjectName(edtSubjectName.getText().toString());
    }

    protected abstract void getData();

    protected abstract void actionOk(String newName);

    private void onClickCancel() {
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_ok) {
            onClickOk();
        } else if (view.getId() == R.id.btn_cancel) {
            onClickCancel();
        }
    }

    private void validateSubjectName(String name) {
        String newName = name.trim();
        if (newName.isEmpty()) {
            Toast.makeText(this, "New name is empty", Toast.LENGTH_SHORT).show();
        } else if (isExistSubjectName(name)) {
            Toast.makeText(this, "This name already exists", Toast.LENGTH_SHORT).show();
        } else {
            actionOk(newName);
        }
    }

    private boolean isExistSubjectName(String newName) {
        for (SubjectTracker subjectTracker : subjectList) {
            if (subjectTracker.action != EditAction.DELETE && subjectTracker.subject.getName().equals(newName))
                return true;
        }
        return false;
    }
}
