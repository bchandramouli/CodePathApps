package or.connect.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends Activity {

    protected static final int TASK_DEFAULT_PRIORITY = 10;

    List<Task> items;
    TasksAdapter taskAdapter;
    ListView lvItems;

    TasksDatabase db;

    private final int REQUEST_CODE = 1;

    private static final boolean DEBUG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this);
        setContentView(R.layout.activity_main);

        db = new TasksDatabase(this);
        items = db.getAllTasks();

        lvItems = (ListView)findViewById(R.id.lvItems);
        taskAdapter = new TasksAdapter(this, (ArrayList<Task>)items);

        // Attach the adapter to the ListView
        lvItems.setAdapter(taskAdapter);

        if (items.isEmpty()) {
            Toast.makeText(this, "List em and get em done", Toast.LENGTH_SHORT).show();
        }

        setupItemListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupItemListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                                           View view, int position, long id) {

                Task delTask = items.get(position);

                db.deleteTask(delTask);
                items.remove(position);

                taskAdapter.notifyDataSetChanged();

                //get the new cursor... how to???
                // Switch to new cursor and update contents of ListView
                //TasksAdapter.changeCursor(newCursor);

                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {

                // first parameter is the context, second is the class of the activity to launch
                // Intent i = new Intent(MainActivity.this, edit_item.class);
                Intent i = new Intent(MainActivity.this, FragmentEditDialog.class);

                String itemText = items.get(position).taskName;

                i.putExtra("editPos", position);
                i.putExtra("editItem", itemText);

                startActivityForResult(i, REQUEST_CODE); // brings up the edit
            }
        });
        /*
        if (items.isEmpty()) {
            Toast.makeText(this, "Nicely done!", Toast.LENGTH_SHORT).show();
        }
        */
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        EditText etPriority = (EditText) findViewById(R.id.etPriority);
        String itemText = etNewItem.getText().toString();

        int priority = TASK_DEFAULT_PRIORITY;

        try {
            priority = Integer.parseInt(etPriority.getText().toString());
        } catch(NumberFormatException e) {
            Toast.makeText(this, "Default priority [10]", Toast.LENGTH_SHORT).show();
        }

        Task task = new Task(itemText, priority);

        db.addTask(task);
        items.add(task);

        taskAdapter.notifyDataSetChanged();

        etNewItem.setText("");
        etPriority.setText("");
    }


    // Handle the result from edit activity...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            int position = data.getExtras().getInt("editPos");

            Task task = items.get(position);
            task.taskName = data.getExtras().getString("updatedText");

            if (DEBUG) {
                Toast.makeText(this, task.taskName, Toast.LENGTH_SHORT).show();
            }

            db.updateTask(task);

            items.remove(position);
            items.add(position, task);

            taskAdapter.notifyDataSetChanged();
        }
    }

    private void readItems() {

        /* XXX - Cursor stuff... leave it alone for now...
        // Query for items from the database and get a cursor back
        Cursor taskCursor = Task.fetchTasksCursor();

        // Setup cursor adapter using cursor from last step
        taskAdapter = new TasksCursorAdapter(this, taskCursor);
        */
    }
}
