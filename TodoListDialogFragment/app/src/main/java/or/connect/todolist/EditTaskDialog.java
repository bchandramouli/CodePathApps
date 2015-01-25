package or.connect.todolist;

/**
 * Created by moulib on 1/24/15.
 */

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class EditTaskDialog extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText mEditTask;

    public interface EditTaskDialogListener {
        void onFinishEditDialog(String inputText);
    }

    public EditTaskDialog() {
        // Empty constructor needed for DialogFragment
    }

    public static EditTaskDialog newInstance(String task) {
        EditTaskDialog frag = new EditTaskDialog();
        Bundle args = new Bundle();

        args.putString("Task", task);
        frag.setArguments(args);

        return (frag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_task, container);
        mEditTask = (EditText) view.findViewById(R.id.etEditTask);
        String taskStr = getArguments().getString("Task");
        getDialog().setTitle("Edit Task");

        mEditTask.setText(taskStr);
        mEditTask.setSelection(mEditTask.getText().length());

        mEditTask.setOnEditorActionListener(this);

        // show soft keyboard automatically
        mEditTask.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return (view);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (EditorInfo.IME_ACTION_DONE == actionId) {
            //Return input text to Activity
            EditTaskDialogListener listener = (EditTaskDialogListener) getActivity();
            listener.onFinishEditDialog(mEditTask.getText().toString());
            getDialog().dismiss();
            return (true);
        }
        return (false);
    }
}
