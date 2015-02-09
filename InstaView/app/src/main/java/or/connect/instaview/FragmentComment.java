package or.connect.instaview;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import org.w3c.dom.Comment;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by moulib on 2/8/15.
 */
public class FragmentComment extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_fragment);

        CommentWrapper commentWrapper = (CommentWrapper)getIntent().getSerializableExtra("comments_wrapper");

        FragmentManager fm = getSupportFragmentManager();
        CommentDialog commentDialog = CommentDialog.newInstance(commentWrapper.getComments());
        commentDialog.show(fm, "fragment_comments");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}