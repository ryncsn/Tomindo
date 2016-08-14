package com.hackret.tomindo.Items;

/**
 * Created by song on 8/14/16.
 */
public class TaskItem {
    public final String title;
    public final String details;

    public TaskItem(String title, String details) {
        this.title = title;
        this.details = details;
    }

    @Override
    public String toString() {
        return details;
    }
}