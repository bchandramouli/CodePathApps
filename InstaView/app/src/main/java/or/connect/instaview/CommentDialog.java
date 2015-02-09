package or.connect.instaview;

import android.graphics.Rect;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.w3c.dom.Comment;

import java.util.ArrayList;

/**
 * Created by moulib on 2/8/15.
 */
public class CommentDialog extends DialogFragment {


    public CommentDialog() {
        // Empty constructor needed for dialog fragments
    }

    public static CommentDialog newInstance(ArrayList<PhotoComment> comments) {
        CommentDialog cmnt = new CommentDialog();
        Bundle args = new Bundle();
        args.putSerializable("comments_data", comments);
        cmnt.setArguments(args);
        return cmnt;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.comment_fragment, container);
        ListView lvComments = (ListView) view.findViewById(R.id.lvComments);
        ArrayList<PhotoComment> commentList =
                (ArrayList<PhotoComment>)getArguments().getSerializable("comments_data");

        // Create the adapter and link it to the source
        CommentsAdapter aComments = new CommentsAdapter(this.getActivity(), commentList);

        getDialog().setTitle("Comment List");

        // bind it to the custom adapter
        lvComments.setAdapter(aComments);
        lvComments.requestFocus();
        aComments.notifyDataSetChanged();

        /*
        View mDialogView = this.getView();
        mDialogView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // create a rect for storing the window rect
                Rect r = new Rect(0, 0, 0, 0);

                // retrieve the windows rect
                getActivity().getWindow().getDecorView().getHitRect(r);

                // check if the event position is inside the window rect
                boolean intersects = r.contains((int) event.getX(), (int) event.getY());

                // if the event is not inside then we can close the activity
                if (!intersects) {
                    // close the activity
                    dismiss();
                    // notify that we consumed this event
                    return true;
                }

                return false;
            }
        });
        */

        if (this.getDialog() != null) {
            this.getDialog().setCancelable(true);
            this.getDialog().setCanceledOnTouchOutside(true);
        }

        return view;
    }
}
