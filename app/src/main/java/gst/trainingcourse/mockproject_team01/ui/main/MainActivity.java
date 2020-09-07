package gst.trainingcourse.mockproject_team01.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.Date;

import gst.trainingcourse.mockproject_team01.R;
import gst.trainingcourse.mockproject_team01.base.BaseScheduleActivity;
import gst.trainingcourse.mockproject_team01.ui.edit.schedule.EditScheduleActivity;
import gst.trainingcourse.mockproject_team01.utils.TimeUtils;

public class MainActivity extends BaseScheduleActivity implements View.OnClickListener{

    public final static int EDIT_SCHEDULE_REQUEST_CODE = 8;
    public final static String EDIT_SCHECULE_KEY = "edit schedule";
    private LinearLayout btnEditSchedule;
    private LinearLayout btnChangeWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initActions();

        loadData();
    }

    private void initViews(){
        scheduleTable = findViewById(R.id.schedule_table);
        txtWeekName = findViewById(R.id.txt_week_name);
        btnChangeWeek = findViewById(R.id.btn_change_week);
        btnEditSchedule = findViewById(R.id.btn_edit_schedule);
    }

    private void initActions() {
        btnEditSchedule.setOnClickListener(this);
        btnChangeWeek.setOnClickListener(this);
    }

    private void loadCurrentWeekSchedule() {
        Date[] dates = TimeUtils.getPeriodWeek(null);
        getWeekSchedule(dates).subscribe(weekScheduleObserver);
    }

    private void loadData() {
        scheduleTable.post(new Runnable() {
            @Override
            public void run() {
                initTableSchedule(false);
                loadCurrentWeekSchedule();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_edit_schedule) {
            Intent intent = new Intent(MainActivity.this, EditScheduleActivity.class);
            intent.putExtra(EDIT_SCHECULE_KEY, currentSchedule);
            startActivityForResult(intent, EDIT_SCHEDULE_REQUEST_CODE);
        } else if (view.getId() == R.id.btn_change_week) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}