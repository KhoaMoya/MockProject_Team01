package gst.trainingcourse.mockproject_team01.base;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

import gst.trainingcourse.mockproject_team01.R;
import gst.trainingcourse.mockproject_team01.model.tracker.EditAction;
import gst.trainingcourse.mockproject_team01.model.tracker.SubjectTracker;

public abstract class BaseEditSubjectActivity extends AppCompatActivity implements View.OnClickListener {

    protected int VOICE_INPUT_REQUEST_CODE = 23;
    protected Button btnOk, btnCancel;
    protected EditText edtSubjectName;
    protected ImageView imgVoiceInput;
    protected ArrayList<SubjectTracker> subjectList;

    protected abstract void initViews();

    protected void initActions() {
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        imgVoiceInput.setOnClickListener(this);
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
        } else if (view.getId() == R.id.img_voice_input) {
            showVoidInputDialog();
        }
    }

    private void showVoidInputDialog() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech a new lesson name");
        try {
            startActivityForResult(intent, VOICE_INPUT_REQUEST_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Sorry your device not supported", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_INPUT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && result.get(0) != null) {
                edtSubjectName.setText("");
                edtSubjectName.append(result.get(0));
            }
        }
    }
}
