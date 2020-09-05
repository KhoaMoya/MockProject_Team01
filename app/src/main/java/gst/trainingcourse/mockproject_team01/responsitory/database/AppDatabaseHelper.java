package gst.trainingcourse.mockproject_team01.responsitory.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

import gst.trainingcourse.mockproject_team01.model.LessonSchedule;
import gst.trainingcourse.mockproject_team01.model.Subject;
import gst.trainingcourse.mockproject_team01.model.SubjectTime;
import gst.trainingcourse.mockproject_team01.model.WeekSchedule;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "ScheduleDatabase";
    public final static int DATABASE_VERSION = 1;

    // table subject
    public final static String SUBJECT_TABLE_NAME = "tblSubject";
    public final static String SUBJECT_ID = "id";
    public final static String SUBJECT_NAME = "name";

    // table subject schedule
    public final static String LESSON_SCHEDULE_TABLE_NAME = "tblSubjectSchedule";
    public final static String LESSON_SCHEDULE_ID = "id";
    public final static String LESSON_SCHEDULE_SUBJECT_ID = "subject_id";
    public final static String LESSON_SCHEDULE_SUBJECT_TIME_ID = "subject_time_id";
    public final static String LESSON_SCHEDULE_START_TIME = "start_date";
    public final static String LESSON_SCHEDULE_END_TIME = "end_date";

    // table subject time
    public final static String SUBJECT_TIME_TABLE_NAME = "tblSubjectTime";
    public final static String SUBJECT_TIME_ID = "id";
    public final static String SUBJECT_TIME_DAY = "day";
    public final static String SUBJECT_TIME_LESSON = "lesson";

    private Context context;
    private static AppDatabaseHelper mInstance;

    private AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static AppDatabaseHelper getInstance(Context context) {
        if (mInstance == null) mInstance = new AppDatabaseHelper(context);
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createSubjectTable = "CREATE TABLE " + SUBJECT_TABLE_NAME + " (" + SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SUBJECT_NAME + " TEXT)";
        String createScheduleTable = "CREATE TABLE " + LESSON_SCHEDULE_TABLE_NAME + " (" + LESSON_SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LESSON_SCHEDULE_SUBJECT_ID + " INTEGER, "
                + LESSON_SCHEDULE_SUBJECT_TIME_ID + " INTEGER, "
                + LESSON_SCHEDULE_START_TIME + " INTEGER, "
                + LESSON_SCHEDULE_END_TIME + " INTEGER)";
        String createSubjectTimeTable = "CREATE TABLE " + SUBJECT_TIME_TABLE_NAME + " (" + SUBJECT_TIME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SUBJECT_TIME_DAY + " INTEGER, "
                + SUBJECT_TIME_LESSON + " INTEGER)";
        sqLiteDatabase.execSQL(createSubjectTable);
        sqLiteDatabase.execSQL(createScheduleTable);
        sqLiteDatabase.execSQL(createSubjectTimeTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropSubjectTable = "DROP TABLE IF EXISTS " + SUBJECT_TABLE_NAME;
        String dropScheduleTable = "DROP TABLE IF EXISTS " + LESSON_SCHEDULE_TABLE_NAME;
        String dropSubjectTimeTable = "DROP TABLE IF EXISTS " + SUBJECT_TIME_TABLE_NAME;

        sqLiteDatabase.execSQL(dropSubjectTable);
        sqLiteDatabase.execSQL(dropScheduleTable);
        sqLiteDatabase.execSQL(dropSubjectTimeTable);
    }

    public WeekSchedule getWeekSchedule(Date[] dates){
        return new WeekSchedule(dates, getSchedule(dates));
    }

    public ArrayList<LessonSchedule> getSchedule(Date[] dates) {
        long startTime = dates[0].getTime();
        long endTime = dates[1].getTime();

        String sql = "SELECT * FROM " + LESSON_SCHEDULE_TABLE_NAME + " where " + LESSON_SCHEDULE_START_TIME + " >= " + startTime
                + " AND " + LESSON_SCHEDULE_END_TIME + " < " + endTime;
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<LessonSchedule> listSchedule = new ArrayList<>();

        int id, subId, subTimeId;
        long sTime, eTime;
        Subject subject;
        SubjectTime subjectTime;

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            do {
                try {
                    id = cursor.getInt(cursor.getColumnIndex(LESSON_SCHEDULE_ID));
                    subId = cursor.getInt(cursor.getColumnIndex(LESSON_SCHEDULE_SUBJECT_ID));
                    subTimeId = cursor.getInt(cursor.getColumnIndex(LESSON_SCHEDULE_SUBJECT_TIME_ID));
                    sTime = cursor.getLong(cursor.getColumnIndex(LESSON_SCHEDULE_START_TIME));
                    eTime = cursor.getLong(cursor.getColumnIndex(LESSON_SCHEDULE_END_TIME));

                    subject = getSuject(subId);
                    subjectTime = getSubjectTime(subTimeId);

                    listSchedule.add(new LessonSchedule(id, new Date(sTime), new Date(eTime), subject, subjectTime));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        return listSchedule;
    }

    public Subject getSuject(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + SUBJECT_TABLE_NAME + " WHERE " + SUBJECT_ID + " = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            String subName = cursor.getString(cursor.getColumnIndex(SUBJECT_NAME));
            return new Subject(id, subName);
        }
        return null;
    }

    public SubjectTime getSubjectTime(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + SUBJECT_TIME_TABLE_NAME + " WHERE " + SUBJECT_TIME_ID + " = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndex(SUBJECT_TIME_DAY));
            int lesson = cursor.getInt(cursor.getColumnIndex(SUBJECT_TIME_LESSON));
            return new SubjectTime(id, day, lesson);
        }
        return null;
    }

    public int insertSubject(Subject subject) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SUBJECT_NAME, subject.getName());
        int id = (int) db.insert(SUBJECT_TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int insertSubjectTime(SubjectTime subjectTime) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SUBJECT_TIME_DAY, subjectTime.getDay());
        values.put(SUBJECT_TIME_LESSON, subjectTime.getLesson());
        int id = (int) db.insert(SUBJECT_TIME_TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int insertLessionSchedule(LessonSchedule schedule) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LESSON_SCHEDULE_SUBJECT_ID, schedule.getSubject().getId());
        values.put(LESSON_SCHEDULE_SUBJECT_TIME_ID, schedule.getSubjectTime().getId());
        values.put(LESSON_SCHEDULE_START_TIME, schedule.getStartDate().getTime());
        values.put(LESSON_SCHEDULE_END_TIME, schedule.getEndDate().getTime());
        int id = (int) db.insert(LESSON_SCHEDULE_TABLE_NAME, null, values);
        db.close();
        return id;
    }

}
