package com.hackret.tomindo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hackret.tomindo.Frags.TaskLayer.OnListFragmentInteractionListener;
import com.hackret.tomindo.Helper.TaskItemTouchHelper;
import com.hackret.tomindo.Models.TaskItem;
import com.hackret.tomindo.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TaskItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TaskLayerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements TaskItemTouchHelper.TaskItemLayerAdapter {

    private enum TaskType {NEW_TASK, NORMAL_TASK}

    private final List<TaskItem> mDataSet;
    private final OnListFragmentInteractionListener mListener;

    public TaskLayerListAdapter(List<TaskItem> items, OnListFragmentInteractionListener listener) {
        mDataSet = items;
        mListener = listener;
    }

    @Override
    public void onTaskArchive(int pos) {
        //TODO Archive instead of just remove
        mDataSet.remove(pos);
        this.notifyItemRemoved(pos);
    }

    @Override
    public void onTaskDone(int pos) {
        //TODO Done instead of just remove
        mDataSet.remove(pos);
        this.notifyItemRemoved(pos);
    }

    @Override
    public void onTaskMove(int from, int to) {
        TaskItem tmp = mDataSet.get(from);
        mDataSet.remove(from);
        mDataSet.add(to, tmp);
        this.notifyItemMoved(from, to);
    }

    public void onTaskCreate(int position) {
        mDataSet.add(position, new TaskItem("Task Title", "Task Detail", true));
        this.notifyItemInserted(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TaskType.NORMAL_TASK.ordinal()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_layer_item, parent, false);
            return new TaskViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_layer_item_edit, parent, false);
            return new EditViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).editing ?
                TaskType.NEW_TASK.ordinal() : TaskType.NORMAL_TASK.ordinal();
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public TaskItem mItem;

        public TaskViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.task_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public class EditViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final EditText mContentEdit;
        public TaskItem mItem;

        public EditViewHolder(View view) {
            super(view);
            mView = view;
            mContentEdit = (EditText) view.findViewById(R.id.task_item_edit_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentEdit.getText() + "'";
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof EditViewHolder) {
            final EditViewHolder task_holder = (EditViewHolder) holder;
            InputMethodManager imm = (InputMethodManager)
                    task_holder.mContentEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(task_holder.mContentEdit, InputMethodManager.SHOW_IMPLICIT);
        }
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TaskLayerListAdapter that = this;
        if (holder instanceof TaskViewHolder) {
            final TaskViewHolder task_holder = (TaskViewHolder) holder;
            task_holder.mItem = mDataSet.get(position);
            task_holder.mContentView.setText(mDataSet.get(position).details);

            task_holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(task_holder.mItem);
                    }
                }
            });
        }

        if (holder instanceof EditViewHolder) {
            final EditViewHolder task_holder = (EditViewHolder) holder;
            task_holder.mItem = mDataSet.get(position);
            task_holder.mContentEdit.setText(mDataSet.get(position).details);
            task_holder.mContentEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        task_holder.mItem.title = task_holder.mContentEdit.getText().toString();
                        task_holder.mItem.details = task_holder.mContentEdit.getText().toString();
                        task_holder.mItem.save();
                        task_holder.mItem.editing = false;
                    }
                }
            });
            task_holder.mContentEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        task_holder.mContentEdit.clearFocus();
                    }
                    return false;
                }
            });
            task_holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(task_holder.mItem);
                        that.notifyDataSetChanged();
                    }
                }
            });
            task_holder.mContentEdit.requestFocus();
        }
    }

}
