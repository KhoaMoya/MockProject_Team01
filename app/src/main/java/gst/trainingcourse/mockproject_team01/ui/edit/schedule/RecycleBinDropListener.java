package gst.trainingcourse.mockproject_team01.ui.edit.schedule;

import android.view.DragEvent;
import android.view.View;

public class RecycleBinDropListener implements View.OnDragListener {
    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                onEntered(view);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                onExited(view);
                break;
            case DragEvent.ACTION_DROP:
                break;
            case DragEvent.ACTION_DRAG_ENDED:
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
