package gst.trainingcourse.mockproject_team01.ui.main;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.util.Date;

import gst.trainingcourse.mockproject_team01.base.BaseScheduleActivity;
import gst.trainingcourse.mockproject_team01.model.WeekSchedule;
import gst.trainingcourse.mockproject_team01.responsitory.database.AppDatabaseHelper;
import gst.trainingcourse.mockproject_team01.utils.TimeUtils;

public class MainPresenter implements MainContract {
    private BaseScheduleActivity mActivity;

    public MainPresenter(BaseScheduleActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public void loadCurrentWeekSchedule() {
        Date[] dates = TimeUtils.getPeriodWeek(null);
        mActivity.getWeekSchedule(dates).subscribe(mActivity.weekScheduleObserver);
    }

    @Override
    public void reloadWeekSchedule() {
        WeekSchedule weekSchedule = AppDatabaseHelper.getInstance(mActivity).
                getWeekSchedule(mActivity.currentWeekSchedule.getDates());
        mActivity.updateWeekSchedule(weekSchedule);
    }

    public void showDialogChangeWeek() {
        int[] times = TimeUtils.getYearMonthDay(mActivity.currentWeekSchedule.getStartDate());
        new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mActivity.getWeekSchedule(TimeUtils.getPeriodWeek(year, month, day)).subscribe(mActivity.weekScheduleObserver);
            }
        }, times[0], times[1], times[2]).show();
    }
}
