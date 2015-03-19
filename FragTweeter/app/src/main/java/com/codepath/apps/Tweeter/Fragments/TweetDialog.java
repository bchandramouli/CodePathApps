package com.codepath.apps.Tweeter.Fragments;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.Tweeter.R;
import com.codepath.apps.Tweeter.models.Tweet;
import com.codepath.apps.Tweeter.models.User;
import com.squareup.picasso.Picasso;

/**
 * Created by moulib on 2/22/15.
 */
public class TweetDialog extends DialogFragment {

    private ImageView ivUserProfile;
    private TextView tvUserName;
    private TextView tvUserHandle;
    private EditText etTweetBody;
    private TextView tvCounter;

    private OnSaveListener listener;

    //Define the interface now, that the parent activity needs to implement
    public interface OnSaveListener {
        public void onPostTweet(String post, long reply_id);
    }

    public TweetDialog() {
        // Empty constructor needed for dialog fragments
    }

    public static TweetDialog newInstance(Tweet tweet, User user) {
        TweetDialog twt = new TweetDialog();
        Bundle args = new Bundle();
        args.putParcelable("tweet", tweet);
        args.putParcelable("user", user);
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

        final Tweet tweet = getArguments().getParcelable("tweet");
        User user = getArguments().getParcelable("user");

        ivUserProfile = (ImageView) view.findViewById(R.id.ivUserProfile);
        tvCounter = (TextView) view.findViewById(R.id.tvCounter);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvUserHandle = (TextView) view.findViewById(R.id.tvUserHandle);
        etTweetBody = (EditText) view.findViewById(R.id.etTweetBody);

        if (tweet != null) {
            String body = "@" + tweet.getUser().getHandle() + " ";
            etTweetBody.setText(body);
            etTweetBody.setSelection(body.length());
        }

        // Add a text changed listener on the tweet edit text!
        etTweetBody.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // this will show characters remaining
                Integer remCount = 140 - s.toString().length();
                tvCounter.setText(remCount.toString());
            }
        });

        etTweetBody.requestFocus();

        tvUserName.setText(user.getName());
        tvUserHandle.setText(user.getHandle());

        Picasso.with(getActivity().getApplicationContext()).load(user.getProfile_url()).
                        into(ivUserProfile);

        // Get rid of the title on the full screen images
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        Button btSave = (Button) view.findViewById(R.id.btSend);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = (tweet != null) ? tweet.getUId() : 0;
                listener.onPostTweet(etTweetBody.getText().toString(), id);
                getDialog().dismiss();
            }
        });

        return view;
    }

}
