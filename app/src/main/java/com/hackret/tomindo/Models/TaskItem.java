package com.hackret.tomindo.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hackret.tomindo.Helper.TaskItemDbHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by song on 8/14/16.
 * Just use simple SQLite wrapper, yet.
 */
public abstract class TaskItem {
    private long id; //ID in SQLite DB
    public String title;
    public String details;
    public String type;
    public boolean editing;
    protected Integer mStatus;

    //Handler for DB operation
    public static TaskItemDbHelper mDbHandler;

    public static void setDbHandler(TaskItemDbHelper mDbHandler) {
        TaskItem.mDbHandler = mDbHandler;
    }

    public static TaskItemDbHelper getDbHandler() {
        return mDbHandler;
    }

    public static class MileStone extends TaskItem {
        public static final String TYPE = "milestone";
        public MileStone(String title, String details) {
            super(title, details);
            this.type = TYPE;
        }

        public MileStone(TaskItem another, boolean clone) {
            super(another, clone);
            this.type = TYPE;
        }

        public MileStone(TaskItem another) {
            super(another);
            this.type = TYPE;
        }

        public MileStone(long id, String title, String details) {
            super(id, title, details);
            this.type = TYPE;
        }

        @Override
        public boolean isDone() {
            return this.mStatus != 0;
        }

        public void onDone(){
            this.mStatus += 1;
        }
    }

    public static class Counter extends TaskItem {
        public static final String TYPE = "counter";
        public Counter(String title, String details) {
            super(title, details);
            this.type = TYPE;
        }

        public Counter(TaskItem another, boolean clone) {
            super(another, clone);
            this.type = TYPE;
        }

        public Counter(TaskItem another) {
            super(another);
            this.type = TYPE;
        }

        public Counter(long id, String title, String details) {
            super(id, title, details);
            this.type = TYPE;
        }

        @Override
        public boolean isDone() {
            return false;
        }

        public void onDone(){
            this.mStatus = 1;
        }
    }

    public boolean isEditing() {
        return this.editing;
    }
    public boolean isNew() {
        return this.id == (TaskItemDbHelper.RESERVED_ID);
    }

    public TaskItem(String title, String details) {
        this.editing = true;
        this.id = TaskItemDbHelper.RESERVED_ID;
        this.title = title;
        this.details = details;
    }

    protected TaskItem(long id, String title, String details) {
        this.editing = false;
        this.id = id;
        this.title = title;
        this.details = details;
    }

    public TaskItem(TaskItem another, boolean clone) {
        this.editing = false;
        this.title = another.title;
        this.details = another.details;
        if (clone == false) {
            this.id = another.id;
        }
    }

    public TaskItem(TaskItem another) {
        this(another, true);
    }

    public abstract boolean isDone();

    public void swapOrder(TaskItem another) {
        long tmp = this.id;
        this.id = another.id;
        another.id = tmp;
        this.save();
        another.save();
    }

    public static List<TaskItem> all() {
        SQLiteDatabase db = mDbHandler.getReadableDatabase();

        String[] projection = {
                TaskItemDbHelper.TaskItemEntry._ID,
                TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_TITLE,
                TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_DETAIL,
                TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_STATUS,
                TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_TYPE,
        };

        String sortOrder =
                TaskItemDbHelper.TaskItemEntry._ID + " DESC";

        Cursor c = db.query(
                TaskItemDbHelper.TaskItemEntry.TABLE_NAME,
                projection,
                null, null,
                null, null,
                sortOrder
        );

        List<TaskItem> list = new LinkedList<>();
        c.moveToFirst();
        try {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                TaskItem item;
                String task_type = c.getString(c.getColumnIndex(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_TYPE));
                if (task_type.equals(MileStone.TYPE)) {
                    item = new MileStone(
                            c.getLong(c.getColumnIndex(TaskItemDbHelper.TaskItemEntry._ID)),
                            c.getString(c.getColumnIndex(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_TITLE)),
                            c.getString(c.getColumnIndex(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_DETAIL))
                    );
                }
                else if (task_type.equals(Counter.TYPE)) {
                    item = new Counter(
                            c.getLong(c.getColumnIndex(TaskItemDbHelper.TaskItemEntry._ID)),
                            c.getString(c.getColumnIndex(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_TITLE)),
                            c.getString(c.getColumnIndex(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_DETAIL))
                    );
                }
                else {
                    Log.e("Tomindo: TaskItem:" ,"Unsupported Task Type: " + task_type);
                    continue;
                }
                list.add(item);
            }
        } finally {
            c.close();
        }
        return list;
    }

    public void save() {
        if (this.isNew()) {
            SQLiteDatabase db = mDbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_TITLE, title);
            values.put(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_DETAIL, details);
            values.put(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_STATUS, mStatus);
            values.put(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_TYPE, type);

            this.id = db.insert(
                    TaskItemDbHelper.TaskItemEntry.TABLE_NAME,
                    null,
                    values
            );
            db.close();
        } else {
            SQLiteDatabase db = mDbHandler.getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_TITLE, title);
            values.put(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_DETAIL, details);
            values.put(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_STATUS, mStatus);
            values.put(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_TYPE, type);

            String selection = TaskItemDbHelper.TaskItemEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(this.id)};

            db.update(
                    TaskItemDbHelper.TaskItemEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            db.close();
        }
        this.editing = false;
    }

    public void delete() {
        if (this.isNew()) {
            return;
        }
        SQLiteDatabase db = mDbHandler.getWritableDatabase();
        String selection = TaskItemDbHelper.TaskItemEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(this.id)};
        db.delete(TaskItemDbHelper.TaskItemEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    @Override
    public String toString() {
        return title + ":" + details;
    }
}