package com.hackret.tomindo.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hackret.tomindo.Helper.TaskItemDbHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by song on 8/14/16.
 * Just use simple SQLite wrapper, yet.
 */
public class TaskItem {

    //ID in SQLite DB
    private long id;
    public String title;
    public String details;

    //Handler for DB operation
    public static TaskItemDbHelper mDbHandler;

    public boolean isNew() {
        return this.id == (TaskItemDbHelper.RESERVED_ID);
    }

    public static void setDbHandler(TaskItemDbHelper mDbHandler) {
        TaskItem.mDbHandler = mDbHandler;
    }

    public static TaskItemDbHelper getmDbHandler() {
        return mDbHandler;
    }

    public TaskItem(String title, String details) {
        this.id = TaskItemDbHelper.RESERVED_ID;
        this.title = title;
        this.details = details;
    }

    private TaskItem(long id, String title, String details) {
        this.id = id;
        this.title = title;
        this.details = details;
    }

    public TaskItem(TaskItem another, boolean clone) {
        this.title = another.title;
        this.details = another.details;
        if (clone == false) {
            this.id = another.id;
        }
    }

    public TaskItem(TaskItem another) {
        this(another, true);
    }

    public TaskItem fill(TaskItem another) {
        this.title = another.title;
        this.details = another.details;
        return this;
    }

    public static List<TaskItem> all() {
        SQLiteDatabase db = mDbHandler.getReadableDatabase();

        String[] projection = {
                TaskItemDbHelper.TaskItemEntry._ID,
                TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_TITLE,
                TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_DETAIL,
        };

        String sortOrder =
                TaskItemDbHelper.TaskItemEntry._ID + " DESC";

        Cursor c = db.query(
                TaskItemDbHelper.TaskItemEntry.TABLE_NAME,  // The table to query
                projection,                                 // The columns to return
                null,                                         // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sortOrder                                   // The sort order
        );

        List<TaskItem> list = new LinkedList<>();

        c.moveToFirst();

        try {

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                TaskItem item = new TaskItem(
                        c.getLong(c.getColumnIndex(TaskItemDbHelper.TaskItemEntry._ID)),
                        c.getString(c.getColumnIndex(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_TITLE)),
                        c.getString(c.getColumnIndex(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_DETAIL))
                );
                list.add(item);
            }
        } finally {
            c.close();
        }
        return list;
    }

    public void save() {
        if (this.isNew()) {
            System.out.println("Save new item");
            SQLiteDatabase db = mDbHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_TITLE, title);
            values.put(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_DETAIL, details);

            this.id = db.insert(
                    TaskItemDbHelper.TaskItemEntry.TABLE_NAME,
                    null,
                    values
            );
            db.close();
        } else {
            System.out.print("Save old item");
            SQLiteDatabase db = mDbHandler.getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_TITLE, title);
            values.put(TaskItemDbHelper.TaskItemEntry.COLUMN_NAME_DETAIL, details);

            String selection = TaskItemDbHelper.TaskItemEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(this.id)};

            db.update(
                    TaskItemDbHelper.TaskItemEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            db.close();
        }
    }

    public void delete() {
        if (this.isNew()) {
            System.out.print("Save new item");
            return;
        }
        SQLiteDatabase db = mDbHandler.getWritableDatabase();
        String selection = TaskItemDbHelper.TaskItemEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(this.id)};
        System.out.print("Save old item");
        db.delete(TaskItemDbHelper.TaskItemEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    @Override
    public String toString() {
        return title + ":" + details;
    }

}