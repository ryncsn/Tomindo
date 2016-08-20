package com.hackret.tomindo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hackret.tomindo.Frags.TaskLayer.OnListFragmentInteractionListener;
import com.hackret.tomindo.Models.TaskItem;
import com.hackret.tomindo.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TaskItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TaskLayerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private enum TaskType {NEW_TASK, NORMAL_TASK}

    ;
    private final List<TaskItem> mDataSet;
    private final OnListFragmentInteractionListener mListener;

    public TaskLayerListAdapter(List<TaskItem> items, OnListFragmentInteractionListener listener) {
        mDataSet = items;
        mListener = listener;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
    }

    public void createNewTask(){
        mDataSet.add(new TaskItem("Task Title", "Task Detail", true));
        this.notifyDataSetChanged();
    }
}
