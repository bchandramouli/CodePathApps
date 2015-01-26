package or.connect.todolist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by moulib on 1/25/15.
 */
public class TasksCursorAdapter extends CursorAdapter {

    public TasksCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // Override newView method - just inflates and returns
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return (LayoutInflater.from(context).inflate(R.layout.item_task, parent, false));
    }

    // bindView method binds the data to a view => set the text etc.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        /* XXX - to be tested
        if(cursor.getPosition() % 2 == 1) {
            view.setBackgroundColor(context.getResources().getColor(R.color.background_odd));
        }
        else {
            view.setBackgroundColor(context.getResources().getColor(R.color.background_even));
        }
        */

        // Find all the fields
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvPriority = (TextView) view.findViewById(R.id.tvPriority);

        // Get params from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow("Task Name"));
        int priority = cursor.getInt(cursor.getColumnIndexOrThrow("Task Priority"));

        //Populate fields with extracted properties
        tvName.setText(name);
        tvPriority.setText(String.valueOf(priority));
    }
}
