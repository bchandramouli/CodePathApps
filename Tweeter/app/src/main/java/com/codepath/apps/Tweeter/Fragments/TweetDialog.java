package com.codepath.apps.Tweeter.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.Tweeter.R;
import com.codepath.apps.Tweeter.TimelineActivity;

/**
 * Created by moulib on 2/22/15.
 */
public class TweetDialog extends DialogFragment {

    private ImageView ivUserProfile;
    private TextView tvUserName;
    private TextView tvUserHandle;
    private EditText etTweetBody;

    private int charCount;

    private OnSaveListener listener;


    //Define the interface now, that the parent activity needs to implement
    public interface OnSaveListener {
        public void onPostTweet(String post, long reply_id);
    }

    public TweetDialog() {
        // Empty constructor needed for dialog fragments
    }

    public static TweetDialog newInstance(int reply_id) {
        TweetDialog twt = new TweetDialog();
        Bundle args = new Bundle();
        // TODO Need args? args.putString("full_image", url);
        args.putLong("reply_id", reply_id);
        twt.setArguments(args);
        return twt;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnSaveListener) {
            this.listener = (OnSaveListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + "must implement TweetDialog.onSaveListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.share_tweet, container);

        ivUserProfile = (ImageView) view.findViewById(R.id.ivUserProfile);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvUserHandle = (TextView) view.findViewById(R.id.tvUserHandle);
        etTweetBody = (EditText) view.findViewById(R.id.etTweetBody);

        // Get rid of the title on the full screen images
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        Button btSave = (Button) view.findViewById(R.id.btSend);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPostTweet(etTweetBody.getText().toString(), getArguments().getLong("reply_id"));
                getDialog().dismiss();
            }
        });

        return view;
    }

}
