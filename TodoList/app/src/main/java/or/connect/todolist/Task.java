package or.connect.todolist;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.squareup.picasso.Cache;

import java.util.List;

/**
 * Created by moulib on 1/19/15.
 */
public class Task extends Model {

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

    // Return the cursor for all tasks
    /*
    public static Cursor fetchTasksCursor() {
        String tableName = Cache.getTableInfo(Task.class).getTableName();
        String resultRecords = new Select(tableName + ".*, " + tableName + ".Id as _id").
                from(Task.class).toSql();
        // Execute query on the ActiveAndroid SQLite DB
        Cursor resultCursor = Cache.openDatabase().rawQuery(resultRecords, null);

        return (resultCursor);
    }
    */


}