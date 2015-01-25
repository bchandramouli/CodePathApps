package or.connect.todolist;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by moulib on 1/24/15.
 */
public class FragmentEditDialog extends FragmentActivity implements EditTaskDialog.EditTaskDialogListener {

    private String editTask;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_task);

        editTask = getIntent().getStringExtra("editItem");
        position = getIntent().getIntExtra("editPos", 0);

        showEditDialog();

        //Toast.makeText(this, editTask, Toast.LENGTH_SHORT).show();
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EditTaskDialog editTaskDialog = EditTaskDialog.newInstance(editTask);
        editTaskDialog.show(fm, "fragment_edit_task");
    }

    @Override
    public void onFinishEditDialog(String inputText) {

        EditText etUpdTask = (EditText) findViewById(R.id.etEditTask);
        etUpdTask.setText(inputText);
        etUpdTask.setSelection(etUpdTask.getText().length());

        Intent data = new Intent();
        data.putExtra("updatedText", inputText);
        data.putExtra("editPos", position);
        setResult(RESULT_OK, data); // set result code and bundle data for response

        // close the activity and return to Main
        this.finish();
    }
}