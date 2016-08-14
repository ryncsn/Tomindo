package com.hackret.tomindo.Contents;
import com.hackret.tomindo.Items.TaskItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class TaskLayerContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<TaskItem> ITEMS = new ArrayList<TaskItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, TaskItem> ITEM_MAP = new HashMap<String, TaskItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            createTmpTaskItem();
        }
    }


    public static void createTmpTaskItem() {
        int position = ITEMS.size() + 1;
        System.out.println("Task " + String.valueOf(position));
        TaskItem item = new TaskItem("Task " + String.valueOf(position), "Task Detail");
        ITEMS.add(item);
        ITEM_MAP.put(item.title, item);
    }
}
