package gst.trainingcourse.mockproject_team01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gst.trainingcourse.mockproject_team01.R;
import gst.trainingcourse.mockproject_team01.model.LessonSchedule;

public class ScheduleTableAdapter extends RecyclerView.Adapter<ScheduleTableAdapter.SimpleTextViewHolder> {

    public final static int COLUMN_NUMBER = 7;
    public final static int ROW_NUMBER = 7;

    private Context context;
    private int itemHeight;
    private ArrayList<LessonSchedule> mLessonScheduleList;
    private LayoutInflater mInflater;
    private List<Integer> mHeaderPositions = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 14, 21, 28, 35, 42);
    private List<String> mHeaderLabels = Arrays.asList("", "Monday", "Tueday", "Wednesday", "Thursday", "Friday", "Saturday", "Lesson 1",
            "Lesson 2", "Lesson 3", "Lesson 4", "Lesson 5", "Lesson 6");

    public ScheduleTableAdapter(Context context, int itemHeight) {
        this.context = context;
        this.itemHeight = itemHeight;
        this.mInflater = LayoutInflater.from(context);
        mLessonScheduleList = new ArrayList<>();
    }

    public void setScheduleList(ArrayList<LessonSchedule> list) {
        mLessonScheduleList.clear();
        for (int i = 0; i < (COLUMN_NUMBER * ROW_NUMBER); i++) {
            mLessonScheduleList.add(null);
        }
        for(LessonSchedule schedule : list){
            int index = schedule.getSubjectTime().getLesson() * COLUMN_NUMBER + (schedule.getSubjectTime().getDay()-1);
            mLessonScheduleList.set(index, schedule);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SimpleTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.subject_item, parent, false);
        itemView.setMinimumHeight(itemHeight);
        return new SimpleTextViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleTextViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return COLUMN_NUMBER * ROW_NUMBER;
    }

    public class SimpleTextViewHolder extends RecyclerView.ViewHolder {

        TextView txtSubjectName;

        public SimpleTextViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubjectName = itemView.findViewById(R.id.txt_subject_name);
        }

        public void bindData(int position) {
            String name = "";
            if (mHeaderPositions.contains(position)) {
                name = mHeaderLabels.get(mHeaderPositions.indexOf(position));
                if (position != 0) txtSubjectName.setBackgroundResource(R.drawable.bg_header_label);
            } else {
                if(!mLessonScheduleList.isEmpty()) {
                    LessonSchedule schedule = mLessonScheduleList.get(position);
                    if (schedule != null) name = schedule.getSubject().getName();
                }
            }
            txtSubjectName.setText(name);
        }
    }
}
