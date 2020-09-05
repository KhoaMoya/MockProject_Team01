package gst.trainingcourse.mockproject_team01;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication context;

    public static MyApplication getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        context = this;
        super.onCreate();
    }
}
