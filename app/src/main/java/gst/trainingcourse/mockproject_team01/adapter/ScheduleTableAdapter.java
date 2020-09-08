package gst.trainingcourse.mockproject_team01.adapter;

import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import gst.trainingcourse.mockproject_team01.R;
import gst.trainingcourse.mockproject_team01.base.BaseTableAdapter;
import gst.trainingcourse.mockproject_team01.model.LessonSchedule;
import gst.trainingcourse.mockproject_team01.model.PassObject;
import gst.trainingcourse.mockproject_team01.model.Subject;
import gst.trainingcourse.mockproject_team01.model.WeekSchedule;
import gst.trainingcourse.mockproject_team01.model.tracker.EditAction;
import gst.trainingcourse.mockproject_team01.model.tracker.ScheduleTracker;
import gst.trainingcourse.mockproject_team01.ui.edit.schedule.ChoosePeriodDialogFragment;

public class ScheduleTableAdapter extends BaseTableAdapter<LessonSchedule, ScheduleTableAdapter.LessonItemViewHolder> {

    public static int COLUMN_NUMBER = 7;
    public static int ROW_NUMBER = 7;

    private FragmentManager mFragmentManager;
    public WeekSchedule currentWeekSchedule;
    private ChoosePeriodDialogFragment mDialogChoosePeriod;
    private List<Integer> mHeaderPositions = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 14, 21, 28, 35, 42);
    private List<String> mHeaderLabels = Arrays.asList("", "Monday", "Tueday", "Wednesday", "Thursday", "Friday", "Saturday", "Lesson 1",
            "Lesson 2", "Lesson 3", "Lesson 4", "Lesson 5", "Lesson 6");

    public ScheduleTableAdapter(Context context, int rowHeight, boolean isCanDragItem, FragmentManager fragmentManager) {
        super(context, rowHeight, null);
        this.isCanDragItem = isCanDragItem;
        this.mFragmentManager = fragmentManager;
        layoutManager = new GridLayoutManager(context, COLUMN_NUMBER);
        mDataList = new ArrayList<>();
    }

    public void setWeekSchedule(WeekSchedule weekSchedule) {
        this.currentWeekSchedule = weekSchedule;
        mDataList.clear();
        for (int i = 0; i < (COLUMN_NUMBER * ROW_NUMBER); i++) {
            mDataList.add(null);
        }
        for (LessonSchedule schedule : weekSchedule.getLessonSchedules()) {
            int index = schedule.getLesson() * COLUMN_NUMBER + (schedule.getDay() - 1);
            mDataList.set(index, schedule);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LessonItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.subject_item, parent, false);
        itemView.setMinimumHeight(rowHeight);
        return new LessonItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonItemViewHolder holder, int position) {
        holder.bindData();
    }

    @Override
    public int getItemCount() {
        return ROW_NUMBER * COLUMN_NUMBER;
    }

    public class LessonItemViewHolder extends BaseTableAdapter.BaseItemViewHolder {

        public LessonItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubjectName = itemView.findViewById(R.id.txt_subject_name);
            txtSubjectName.setOnDragListener(new OnDragCellListener());
        }

        public void bindData() {
            int position = getAdapterPosition();
            String name = "";
            if (mHeaderPositions.contains(position)) {
                name = mHeaderLabels.get(mHeaderPositions.indexOf(position));
                txtSubjectName.setTextSize(12);
                if (position != 0) txtSubjectName.setBackgroundResource(R.drawable.bg_header_label);
            } else {
                if (!mDataList.isEmpty()) {
                    txtSubjectName.setTextSize(13);
                    LessonSchedule schedule = mDataList.get(position);
                    data = schedule;
                    if (schedule != null) {
                        name = schedule.getSubject().getName();
                    }
                }
            }
            txtSubjectName.setText(name);
        }

        @Override
        public boolean isDraggable() {
            return data != null && isCanDragItem;
        }

        @Override
        public PassObject<LessonSchedule> getPassObject() {
            return new PassObject<>((LessonSchedule) data, PassObject.Type.LESSON, getAdapterPosition());
        }


        public class OnDragCellListener implements View.OnDragListener {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                int positionDrop = getAdapterPosition();
                PassObject<?> srcPassObject = (PassObject<?>) dragEvent.getLocalState();
                if (mHeaderPositions.contains(positionDrop) || lessonScheduleListener == null)
                    return false;

                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        if (positionDrop != srcPassObject.position)
                            view.setBackgroundResource(R.color.colorGray);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        view.setBackgroundResource(R.drawable.bg_stroker_gray);
                        break;
                    case DragEvent.ACTION_DROP:
                        view.setBackgroundResource(R.drawable.bg_stroker_gray);
                        if (positionDrop == srcPassObject.position) break;

                        PassObject<?> desPassObject = new PassObject<>(data, PassObject.Type.LESSON, getAdapterPosition());

                        mDialogChoosePeriod = new ChoosePeriodDialogFragment(ScheduleTableAdapter.this, srcPassObject, desPassObject);
                        mDialogChoosePeriod.show(mFragmentManager, null);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        view.setBackgroundResource(R.drawable.bg_stroker_gray);
                        break;
                    default:
                        break;
                }
                return true;
            }
        }
    }

    public void handleEditingLessonSchedule(PassObject<?> srcPassObject, PassObject<?> desPassObject, Date[] selectedDates) {
        int positionDrop = desPassObject.position;
        int lesson = positionDrop / COLUMN_NUMBER;
        int day = positionDrop % COLUMN_NUMBER + 1;

        if (srcPassObject.type == PassObject.Type.LESSON) {
            LessonSchedule dropSchedule = (LessonSchedule) srcPassObject.data;
            dropSchedule.setDay(day);
            dropSchedule.setLesson(lesson);
            dropSchedule.setStartDate(selectedDates[0]);
            dropSchedule.setEndDate(selectedDates[1]);

            if (positionDrop != srcPassObject.position) {
                if (desPassObject.data != null) {
                    lessonScheduleListener.updateScheduleTracker(new ScheduleTracker(EditAction.DELETE, (LessonSchedule) desPassObject.data));
                }
                lessonScheduleListener.updateScheduleTracker(new ScheduleTracker(EditAction.EDIT, dropSchedule));
            }
        } else {
            long scheduleId = System.currentTimeMillis();
            Subject subject = (Subject) srcPassObject.data;
            LessonSchedule dropSchedule = new LessonSchedule(scheduleId, selectedDates[0], selectedDates[1], subject, day, lesson);

            if (desPassObject.data != null) {
                lessonScheduleListener.updateScheduleTracker(new ScheduleTracker(EditAction.DELETE, (LessonSchedule) desPassObject.data));
            }
            lessonScheduleListener.updateScheduleTracker(new ScheduleTracker(EditAction.ADD, dropSchedule));
        }
    }

}
