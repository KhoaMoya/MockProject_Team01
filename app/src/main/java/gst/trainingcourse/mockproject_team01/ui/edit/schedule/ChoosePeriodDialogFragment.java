package gst.trainingcourse.mockproject_team01.ui.edit.schedule;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

import gst.trainingcourse.mockproject_team01.R;
import gst.trainingcourse.mockproject_team01.adapter.ScheduleTableAdapter;
import gst.trainingcourse.mockproject_team01.model.LessonSchedule;
import gst.trainingcourse.mockproject_team01.model.PassObject;
import gst.trainingcourse.mockproject_team01.utils.TimeUtils;

public class ChoosePeriodDialogFragment extends DialogFragment implements View.OnClickListener {

    private RadioGroup radioGroup;
    private TextView btnOk, btnCancel;
    private EditText txtStartDate, txtEndDate;
    private Date startDate, endDate;
    private LinearLayout chooseDateLayout;

    Calendar calendar = Calendar.getInstance();
    private PassObject<?> mDesPassObject;
    private PassObject<?> mSrcPassObject;
    private ScheduleTableAdapter mAdapter;

    public ChoosePeriodDialogFragment(ScheduleTableAdapter adapter, PassObject<?> srcPassObject, PassObject<?> desPassObject) {
        this.mAdapter = adapter;
        this.mDesPassObject = desPassObject;
        this.mSrcPassObject = srcPassObject;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogfragment_choose_period, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initActions();
        initDatas();
    }

    private void initViews(View view) {
        radioGroup = view.findViewById(R.id.radiogroup);
        btnOk = view.findViewById(R.id.btn_ok);
        btnCancel = view.findViewById(R.id.btn_cancel);
        txtStartDate = view.findViewById(R.id.txt_start_date);
        txtEndDate = view.findViewById(R.id.txt_end_date);
        chooseDateLayout = view.findViewById(R.id.layout_choose_date);
        setCancelable(false);
    }

    private void initActions() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radiobtn_day) {
                    txtEndDate.setEnabled(false);
                    txtStartDate.setEnabled(false);
                    chooseDateLayout.setAlpha(0.3f);
                } else {
                    txtEndDate.setEnabled(true);
                    txtStartDate.setEnabled(true);
                    chooseDateLayout.setAlpha(1f);
                }
            }
        });

        txtStartDate.setOnClickListener(this);
        txtEndDate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    private void initDatas() {
        if (mSrcPassObject.type == PassObject.Type.LESSON) {
            LessonSchedule srcSchedule = (LessonSchedule) mSrcPassObject.data;
            startDate = srcSchedule.getStartDate();
            endDate = srcSchedule.getEndDate();
        } else {
//            startDate = SharedPreferencesHelper.getStartDate();
//            endDate = SharedPreferencesHelper.getEndDate();
            calendar.setTime(mAdapter.currentWeekSchedule.getStartDate());
            calendar.add(Calendar.DAY_OF_MONTH, mDesPassObject.position % ScheduleTableAdapter.COLUMN_NUMBER - 1);
            Date thisDate = calendar.getTime();
            Date[] dates = TimeUtils.getPeriodDay(thisDate);
            startDate = dates[0];
            endDate = dates[1];
        }
        selectRadioButton(startDate, endDate);
    }

    @Override
    public void onClick(View view) {
        int year, month, day;
        switch (view.getId()) {
            case R.id.txt_start_date:
                calendar.setTime(startDate);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                        startDate = calendar.getTime();

                        if (endDate.getTime() < startDate.getTime()) {
                            calendar.set(year, month, day + 1);
                            endDate = calendar.getTime();
                        }
                        displayStartDateAndEndDate();
                    }
                }, year, month, day).show();
                break;
            case R.id.txt_end_date:
                calendar.setTime(endDate);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                        Date tempDate = calendar.getTime();
                        if (tempDate.getTime() <= startDate.getTime()) {
                            Toast.makeText(getContext(), "Error: End date < start date", Toast.LENGTH_SHORT).show();
                        } else {
                            endDate = tempDate;
                            displayStartDateAndEndDate();
                        }
                    }
                }, year, month, day).show();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_ok:
                if (radioGroup.getCheckedRadioButtonId() == R.id.radiobtn_day) {
                    calendar.setTime(mAdapter.currentWeekSchedule.getStartDate());
                    calendar.add(Calendar.DAY_OF_MONTH, mDesPassObject.position % ScheduleTableAdapter.COLUMN_NUMBER - 1);
                    Date thisDate = calendar.getTime();
                    Date[] dates = TimeUtils.getPeriodDay(thisDate);
                    startDate = dates[0];
                    endDate = dates[1];
                }
//                SharedPreferencesHelper.saveStartDate(startDate);
//                SharedPreferencesHelper.saveStartDate(endDate);

                mAdapter.handleEditingLessonSchedule(mSrcPassObject, mDesPassObject, new Date[]{startDate, endDate});
                dismiss();
                break;
            default:
                break;
        }

    }

    private void selectRadioButton(Date startDate, Date endDate) {
        long subMils = endDate.getTime() - startDate.getTime();
        if (subMils <= 86400000L) {
            radioGroup.check(R.id.radiobtn_day);
        } else {
            radioGroup.check(R.id.radiobtn_period);
        }
        displayStartDateAndEndDate();
    }

    private void displayStartDateAndEndDate() {
        txtStartDate.setText(TimeUtils.convertDatetoString(startDate));
        txtEndDate.setText(TimeUtils.convertDatetoString(endDate));
    }
}
