package com.hackret.tomindo.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.hackret.tomindo.Models.TaskItem;

/**
 * Created by song on 8/22/16.
 */
public class TaskItemDbHelper extends SQLiteOpenHelper {

    //TODO: Generalized db for different Models.
    //TODO: Life cycle of DB.
    public static final int RESERVED_ID = 0;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Tomindo_Tasks.db";

    public static abstract class TaskItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "tomindo_task";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_DETAIL = "detail";
        public static final String COLUMN_NAME_PARENT = "parent";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTERGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TaskItemEntry.TABLE_NAME + " (" +
                    TaskItemEntry._ID + " INTEGER PRIMARY KEY," +
                    TaskItemEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    TaskItemEntry.COLUMN_NAME_DETAIL + TEXT_TYPE + COMMA_SEP +
                    TaskItemEntry.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
                    TaskItemEntry.COLUMN_NAME_STATUS + INTERGER_TYPE + COMMA_SEP +
                    TaskItemEntry.COLUMN_NAME_PARENT + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskItemEntry.TABLE_NAME;

    public TaskItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {;
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //TODO actually upgrade the db instead of just delete it.
        if(oldVersion == newVersion) {
            return;
        }
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO actually upgrade the db instead of just delete it.
        onUpgrade(db, oldVersion, newVersion);
    }

}
