package gst.trainingcourse.mockproject_team01.base;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gst.trainingcourse.mockproject_team01.adapter.LessonScheduleListener;
import gst.trainingcourse.mockproject_team01.adapter.OnClickItemListener;
import gst.trainingcourse.mockproject_team01.model.PassObject;

public abstract class BaseTableAdapter<O, T extends BaseTableAdapter.BaseItemViewHolder> extends RecyclerView.Adapter<T> {

    protected int rowHeight;
    protected LayoutInflater mInflater;
    protected Context context;
    public GridLayoutManager layoutManager;
    protected OnClickItemListener<O> mClickItemListener;
    protected ArrayList<O> mDataList;
    public boolean isCanDragItem = false;
    protected LessonScheduleListener lessonScheduleListener;

    public BaseTableAdapter(Context context, int rowHeight, OnClickItemListener<O> clickItemListener) {
        this.rowHeight = rowHeight;
        this.context = context;
        this.mClickItemListener = clickItemListener;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public ArrayList<O> getDataList() {
        ArrayList<O> list = new ArrayList<>();
        for (O o : mDataList) if (o != null) list.add(o);
        return list;
    }

    public void setLessonScheduleListener(LessonScheduleListener listener) {
        this.lessonScheduleListener = listener;
    }

    public class BaseItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        protected O data;
        protected TextView txtSubjectName;

        public BaseItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        public PassObject<O> getPassObject() {
            return null;
        }

        @Override
        public boolean onLongClick(View view) {
            if (isDragable()) {
                PassObject<O> passObject = getPassObject();
                ClipData clipData = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                view.startDragAndDrop(clipData, shadowBuilder, passObject, 0);
                return true;
            }
            return false;
        }

        @Override
        public void onClick(View view) {
            if (data != null && mClickItemListener != null && isClickable()) {
                mClickItemListener.onClickItem(data);
            }
        }

        public boolean isClickable() {
            return false;
        }

        public boolean isDragable() {
            return false;
        }
    }
}
