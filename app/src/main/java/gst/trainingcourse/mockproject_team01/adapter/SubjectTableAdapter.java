package gst.trainingcourse.mockproject_team01.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import gst.trainingcourse.mockproject_team01.R;
import gst.trainingcourse.mockproject_team01.adapter.listener.OnClickItemListener;
import gst.trainingcourse.mockproject_team01.base.BaseTableAdapter;
import gst.trainingcourse.mockproject_team01.model.PassObject;
import gst.trainingcourse.mockproject_team01.model.Subject;

public class SubjectTableAdapter extends BaseTableAdapter<Subject, SubjectTableAdapter.SubjectViewHolder> {

    public static int COLUMN_NUMBER = 3;
    public static int ROW_NUMBER = 5;

    public boolean isCanClickItem = false;

    public SubjectTableAdapter(Context context, OnClickItemListener<Subject> subjectListener, int rowHeight) {
        super(context, rowHeight, subjectListener);
        this.mClickItemListener = subjectListener;
        this.isCanDragItem = true;
        layoutManager = new GridLayoutManager(context, COLUMN_NUMBER);
        mDataList = new ArrayList<>();
    }

    public void setSubjecList(ArrayList<Subject> list) {
        mDataList.clear();
        for (int i = 0; i < COLUMN_NUMBER * ROW_NUMBER; i++) {
            mDataList.add(null);
        }
        for (int i = 0; i < list.size(); i++) {
            mDataList.set(i, list.get(i));
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.subject_item, parent, false);
        itemView.setMinimumHeight(rowHeight);
        return new SubjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return ROW_NUMBER * COLUMN_NUMBER;
    }

    public class SubjectViewHolder extends BaseTableAdapter.BaseItemViewHolder {

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubjectName = itemView.findViewById(R.id.txt_subject_name);
        }

        public void bindData(int position) {
            if (!mDataList.isEmpty()) {
                Subject subject = mDataList.get(position);
                data = subject;
                if (subject != null) {
                    txtSubjectName.setText(subject.getName());
                }
            }
        }

        @Override
        public boolean isClickable() {
            return isCanClickItem;
        }

        @Override
        public boolean isDraggable() {
            return data != null && isCanDragItem;
        }

        @Override
        public PassObject<Subject> getPassObject() {
            return new PassObject<>((Subject) data, PassObject.Type.SUBJECT, getAdapterPosition());
        }
    }
}
