package or.connect.todolist;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by moulib on 1/19/15.
 */
//@Table(name = "Tasks")
public class Task extends Model {

    //@Column(name = "Task Name")
    public String taskName;

    //@Column(name = "Task Priority")
    public int priority;

    public Task() {
        // Needs a constructor with empty args
        super();
    }
    public Task(String taskName, int priority) {
        super();
        this.taskName = taskName;
        this.priority = priority;
    }
}