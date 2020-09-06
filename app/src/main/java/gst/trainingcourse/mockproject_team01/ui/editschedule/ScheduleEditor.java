package gst.trainingcourse.mockproject_team01.ui.editschedule;

import java.util.ArrayList;

import gst.trainingcourse.mockproject_team01.MyApplication;
import gst.trainingcourse.mockproject_team01.model.Subject;
import gst.trainingcourse.mockproject_team01.responsitory.database.AppDatabaseHelper;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ScheduleEditor {

    public static Single<ArrayList<Subject>> getAllSubjectFromDb() {
        return Single.create(new SingleOnSubscribe<ArrayList<Subject>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Subject>> emitter) throws Exception {
                if (!emitter.isDisposed())
                    emitter.onSuccess(AppDatabaseHelper.getInstance(MyApplication.getContext()).getAllSubjects());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
