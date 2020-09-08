package gst.trainingcourse.mockproject_team01.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import gst.trainingcourse.mockproject_team01.R;
import gst.trainingcourse.mockproject_team01.base.BaseScheduleActivity;
import gst.trainingcourse.mockproject_team01.ui.edit.schedule.EditScheduleActivity;

public class MainActivity extends BaseScheduleActivity implements View.OnClickListener {

    public final static int EDIT_SCHEDULE_REQUEST_CODE = 8;
    public final static String EDIT_SCHECULE_KEY = "edit schedule";

    private LinearLayout btnEditSchedule;
    private LinearLayout btnChangeWeek;

    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initViews();
        initActions();

        loadData();
    }

    private void init() {
        mMainPresenter = new MainPresenter(this);
    }

    private void initViews() {
        scheduleTable = findViewById(R.id.schedule_table);
        txtWeekName = findViewById(R.id.txt_week_name);
        btnChangeWeek = findViewById(R.id.btn_change_week);
        btnEditSchedule = findViewById(R.id.btn_edit_schedule);
        progressBarLoading = findViewById(R.id.pb_loading);
    }

    private void initActions() {
        btnEditSchedule.setOnClickListener(this);
        btnChangeWeek.setOnClickListener(this);
    }

    private void loadData() {
        scheduleTable.post(new Runnable() {
            @Override
            public void run() {
                initTableSchedule(false);
                mMainPresenter.loadCurrentWeekSchedule();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_edit_schedule) {
            openEditScheduleActivity();
        } else if (view.getId() == R.id.btn_change_week) {
            mMainPresenter.showDialogChangeWeek();
        }
    }

    private void openEditScheduleActivity() {
        Intent intent = new Intent(MainActivity.this, EditScheduleActivity.class);
        intent.putExtra(EDIT_SCHECULE_KEY, currentWeekSchedule);
        startActivityForResult(intent, EDIT_SCHEDULE_REQUEST_CODE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mMainPresenter.reloadWeekSchedule();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}