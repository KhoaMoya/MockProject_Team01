package gst.trainingcourse.mockproject_team01.adapter.listener;

import android.view.DragEvent;
import android.view.View;

import gst.trainingcourse.mockproject_team01.model.PassObject;

public class RecycleBinDropListener implements View.OnDragListener {

    private OnDeleteItemListener deleteItemListener;

    public RecycleBinDropListener(OnDeleteItemListener deleteItemListener) {
        this.deleteItemListener = deleteItemListener;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_ENTERED:
                onEntered(view);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                onExited(view);
                break;
            case DragEvent.ACTION_DROP:
                onExited(view);
                if (deleteItemListener != null) {
                    deleteItemListener.onDeleteItem((PassObject<?>) dragEvent.getLocalState());
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void onEntered(View view) {
        view.setScaleX(1.5f);
        view.setScaleY(1.5f);
    }

    private void onExited(View view) {
        view.setScaleX(1f);
        view.setScaleY(1f);
    }
}
