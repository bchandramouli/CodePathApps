package or.connect.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class edit_item extends Activity {

    // Add private variable to hold the updated entity and to pass it back to the parent!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String editItem = getIntent().getStringExtra("editItem");

        // Get the update and send it to Main
        EditText etUpdItem = (EditText) findViewById(R.id.etUpdItem);
        //String updText = etUpdItem.getText().toString();
        etUpdItem.setText(editItem);
        etUpdItem.setSelection(etUpdItem.getText().length());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);

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

    public void onSubmit(View v) {
        // Get the update and send it to Main
        EditText etUpdItem = (EditText) findViewById(R.id.etUpdItem);
        String updText = etUpdItem.getText().toString();

        int position = getIntent().getIntExtra("editPos", 0);

        // Prepare data intent, put data and return it...
        Intent data = new Intent();
        data.putExtra("updatedText", etUpdItem.getText().toString());
        data.putExtra("editPos", position);

        setResult(RESULT_OK, data); // set result code and bundle data for response

        // close the activity and return to Main
        this.finish();
    }
}
