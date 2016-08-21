package com.hackret.tomindo.Helper;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by song on 8/21/16.
 */
public class TaskItemTouchHelper extends android.support.v7.widget.helper.ItemTouchHelper {
    /**
     * TODO: Override draw() of this subclass for swipe to mark.
     *
     * Creates an ItemTouchHelper that will work with the given Callback.
     * <p/>
     * You can attach ItemTouchHelper to a RecyclerView via
     * {@link #attachToRecyclerView(RecyclerView)}. Upon attaching, it will add an item decoration,
     * an onItemTouchListener and a Child attach / detach listener to the RecyclerView.
     *
     * @param callback The Callback which controls the behavior of this touch helper.
     */
    public TaskItemTouchHelper(Callback callback) {
        super(callback);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //TODO add sweep to add "取消线"
        super.onDraw(c, parent, state);
    }

    public interface TaskItemLayerAdapter {
        public void onTaskArchive(int pos);

        public void onTaskDone(int pos);

        public void onTaskMove(int from, int to);
    }

    public static class TaskItemCallback extends ItemTouchHelper.Callback{

        private final TaskItemLayerAdapter mAdapter;

        public TaskItemCallback(TaskItemLayerAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            mAdapter.onTaskMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mAdapter.onTaskArchive(viewHolder.getAdapterPosition());
        }

    }
}
