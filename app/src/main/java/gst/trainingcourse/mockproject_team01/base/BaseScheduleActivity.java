package gst.trainingcourse.mockproject_team01.base;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

import gst.trainingcourse.mockproject_team01.adapter.ScheduleTableAdapter;
import gst.trainingcourse.mockproject_team01.model.WeekSchedule;
import gst.trainingcourse.mockproject_team01.responsitory.database.AppDatabaseHelper;
import gst.trainingcourse.mockproject_team01.utils.TimeUtils;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseScheduleActivity extends AppCompatActivity {

    public WeekSchedule currentWeekSchedule;
    protected AppDatabaseHelper appDatabase;
    public ScheduleTableAdapter scheduleTableAdapter;
    public RecyclerView scheduleTable;
    public TextView txtWeekName;
    protected Disposable disposable;
    protected ProgressBar progressBarLoading;

    protected void initTableSchedule(boolean isCanDragItem) {
        int rowHeight = scheduleTable.getHeight() / ScheduleTableAdapter.ROW_NUMBER;
        scheduleTableAdapter = new ScheduleTableAdapter(this, rowHeight, isCanDragItem, getSupportFragmentManager());

        scheduleTable.setLayoutManager(scheduleTableAdapter.layoutManager);
        scheduleTable.setAdapter(scheduleTableAdapter);
    }


    public Single<WeekSchedule> getWeekSchedule(Date[] dates) {
        final Date[] periodWeek;
        if (dates.length == 1) {
            periodWeek = TimeUtils.getPeriodWeek(dates[0]);
        } else {
            periodWeek = dates;
        }
        return Single.create(new SingleOnSubscribe<WeekSchedule>() {
            @Override
            public void subscribe(SingleEmitter<WeekSchedule> emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    appDatabase = AppDatabaseHelper.getInstance(BaseScheduleActivity.this);
                    emitter.onSuccess(appDatabase.getWeekSchedule(periodWeek));
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public void updateWeekSchedule(WeekSchedule weekSchedule) {
        currentWeekSchedule = weekSchedule;
        scheduleTableAdapter.setWeekSchedule(weekSchedule);
        txtWeekName.setText(weekSchedule.getName());
    }

    public void showLoading() {
        if (progressBarLoading != null) progressBarLoading.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        if (progressBarLoading != null) progressBarLoading.setVisibility(View.GONE);
    }

    public SingleObserver<WeekSchedule> weekScheduleObserver = new SingleObserver<WeekSchedule>() {

        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onSuccess(WeekSchedule weekSchedule) {
            updateWeekSchedule(weekSchedule);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
    };

    @Override
    protected void onDestroy() {
        if (disposable != null) disposable.dispose();
        super.onDestroy();
    }
}
