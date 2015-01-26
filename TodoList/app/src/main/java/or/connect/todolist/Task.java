package or.connect.todolist;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.squareup.picasso.Cache;

import java.util.Comparator;
import java.util.List;

/**
 * Created by moulib on 1/19/15.
 */
public class Task extends Model implements Comparable<Task> {

    private int taskId;
    public String taskName;
    public int taskPriority;

    public Task() {
        // Needs a constructor with empty args
        super();
    }
    public Task(String name, int priority) {
        super();
        this.taskName = name;
        this.taskPriority = priority;
    }

    public String getTaskName() {
        return (taskName);
    }

    public void setTaskName(String name) {
        this.taskName = name;
    }

    public int getTaskPriority() {
        return (taskPriority);
    }

    public void setTaskPriority(int priority) {
        this.taskPriority = priority;
    }

    public int getTaskId() {
        return (taskId);
    }

    public void setTaskId(int id) {
        this.taskId = id;
    }

    @Override
    public int compareTo(Task compareTask) {
        return (this.taskPriority - compareTask.taskPriority);
    }
}