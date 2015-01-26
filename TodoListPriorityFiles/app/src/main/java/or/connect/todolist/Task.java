package or.connect.todolist;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by moulib on 1/19/15.
 */
@Table(name = "Tasks")
public class Task extends Model implements Comparable<Task> {

    @Column(name = "Task Name")
    public String taskName;

    @Column(name = "Task Priority")
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

    @Override
    public int compareTo(Task compareTsk) {
        return (this.taskPriority - compareTsk.taskPriority);
    }
}