package gst.trainingcourse.mockproject_team01.base;

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

    protected WeekSchedule currentSchedule;
    protected AppDatabaseHelper appDatabase;
    protected ScheduleTableAdapter scheduleTableAdapter;
    protected RecyclerView scheduleTable;
    protected TextView txtWeekName;
    protected Disposable disposable;


    protected void initTableSchedule(boolean isCanDragItem) {
        int rowHeight = scheduleTable.getHeight() / ScheduleTableAdapter.ROW_NUMBER;
        scheduleTableAdapter = new ScheduleTableAdapter(this, rowHeight, isCanDragItem, getSupportFragmentManager());

        scheduleTable.setLayoutManager(scheduleTableAdapter.layoutManager);
        scheduleTable.setAdapter(scheduleTableAdapter);
    }

    protected void showWeekSchedule(WeekSchedule weekSchedule) {
        currentSchedule = weekSchedule;
        scheduleTableAdapter.setScheduleList(weekSchedule);
        txtWeekName.setText(weekSchedule.getName());
    }

    protected Single<WeekSchedule> getWeekSchedule(Date[] dates) {
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

    protected SingleObserver<WeekSchedule> weekScheduleObserver = new SingleObserver<WeekSchedule>() {

        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onSuccess(WeekSchedule weekSchedule) {
            showWeekSchedule(weekSchedule);
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
