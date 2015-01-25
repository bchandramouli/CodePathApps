package or.connect.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.List;


public class MainActivity extends Activity {
    ArrayList<String> items;
    ArrayAdapter<String> itemAdapter;
    ListView lvItems;

    private final int REQUEST_CODE = 1;

    private static final boolean DEBUG = false;

    private void readItems() {
        /* Using Files... */
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }

        /*
         * Using ActiveAndroid...
         *
         * items = new ArrayList<String>(); //new Select().from(Task.class));
         */
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this);
        setContentView(R.layout.activity_main);
        //items = new ArrayList<String>();
        readItems();
        lvItems = (ListView)findViewById(R.id.lvItems);
        itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemAdapter);
        writeItems();
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
                items.remove(position);
                itemAdapter.notifyDataSetChanged();
                writeItems();
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

                i.putExtra("editPos", position);
                i.putExtra("editItem", items.get(position));

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
        String itemText = etNewItem.getText().toString();
        itemAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }


    // Handle the result from edit activity...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String updItem = data.getExtras().getString("updatedText");
            int position = data.getExtras().getInt("editPos");

            if (DEBUG) {
                Toast.makeText(this, updItem, Toast.LENGTH_SHORT).show();
            }
            items.remove(position);
            items.add(position, updItem);
            itemAdapter.notifyDataSetChanged();
            writeItems();
        }
    }
}
