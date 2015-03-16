package com.codepath.apps.Tweeter.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.Tweeter.Abstract.EndlessScrollListener;
import com.codepath.apps.Tweeter.ProfileActivity;
import com.codepath.apps.Tweeter.R;
import com.codepath.apps.Tweeter.TwitterApp;
import com.codepath.apps.Tweeter.TwitterClient;
import com.codepath.apps.Tweeter.models.Tweet;
import com.codepath.apps.Tweeter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by moulib on 3/15/15.
 */
public class UserHeaderFragment extends Fragment{

    private ImageView ivProfile;
    private TextView tvProfileName;
    private TextView tvProfileTagline;
    private TextView tvFollowers;
    private TextView tvFollowing;

    private User user;

    private TwitterClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile, container, false);

        ivProfile = (ImageView) v.findViewById(R.id.ivProfile);
        tvProfileName = (TextView) v.findViewById(R.id.tvProfileName);
        tvProfileTagline = (TextView) v.findViewById(R.id.tvProfileTagline);
        tvFollowers = (TextView) v.findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) v.findViewById(R.id.tvFollowing);

        tvProfileName.setText(user.getName());
        tvProfileTagline.setText(user.getTagline());
        tvFollowers.setText(Integer.toString(user.getFollowers()) + "Followers");
        tvFollowing.setText(Integer.toString(user.getFollowing()) + "Following");

        Picasso.with(getActivity().getApplicationContext()).load(user.getProfile_url()).
                into(ivProfile);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = new TwitterApp().getRestClient();

        client.getUserSettings(new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Parse the JSON User Object for the details.
                User me = new User(response);
                // XXX - TODO - fix this ugliness
                ((ProfileActivity)getActivity()).getSupportActionBar().setTitle("@" + me.getHandle());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //failure case
            }
        });
    }
}
