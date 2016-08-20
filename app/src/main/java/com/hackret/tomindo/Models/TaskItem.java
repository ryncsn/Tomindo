package com.hackret.tomindo.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 8/14/16.
 */
public class TaskItem {
    private static int id_count = 0;
    public final int id;
    public String title;
    public String details;
    public boolean editing;

    private static final int COUNT = 25;

    public static List<TaskItem> ITEMS = new ArrayList<TaskItem>();

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            TaskItem item = new TaskItem("Task " + String.valueOf(i), "Task Detail");
            ITEMS.add(item);
        }
    }

    public TaskItem(String title, String details) {
        this.id = id_count++; //TODO: use db
        this.title = title;
        this.details = details;
        this.editing = false;
    }

    public TaskItem(String title, String details, boolean editing) {
        this(title, details);
        this.editing = editing;
    }

    public static List<TaskItem> all() {
        return ITEMS;
    }

    public void save() {
        //TODO
        return;
    }

    @Override
    public String toString() {
        return details;
    }
}