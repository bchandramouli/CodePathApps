package or.connect.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by moulib on 1/19/15.
 */
public class TasksAdapter extends ArrayAdapter<Task> {

    /* Apply the viewhOlder Pattern and cache views for faster loading */
    private static class ViewHolder {
        TextView taskName;
        TextView priority;
    }

    public TasksAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Task task = getItem(position);

        ViewHolder viewHolder; // view lookup cache -> stored in tag!

        // Check if an existing view is being reused, otherwise inflate the view
        //if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_task, parent, false);
            viewHolder.taskName = (TextView)convertView.findViewById(R.id.tvName);
            viewHolder.priority = (TextView)convertView.findViewById(R.id.tvPriority);
            convertView.setTag(viewHolder);
        //} else {
        //    viewHolder = (ViewHolder)convertView.getTag();
        //}

        // Populate the data into the template view using the data object
        viewHolder.taskName.setText(task.taskName);
        viewHolder.priority.setText(Integer.toString(task.taskPriority));

        // Return the completed view to render on screen
        return convertView;
    }
}
