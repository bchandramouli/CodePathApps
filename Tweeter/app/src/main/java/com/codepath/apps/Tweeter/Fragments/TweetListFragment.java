package com.codepath.apps.Tweeter.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.Tweeter.Abstract.EndlessScrollListener;
import com.codepath.apps.Tweeter.Adapters.TweetArrayAdapter;
import com.codepath.apps.Tweeter.R;
import com.codepath.apps.Tweeter.TwitterApp;
import com.codepath.apps.Tweeter.models.QueryCtrs;
import com.codepath.apps.Tweeter.models.Tweet;
import com.codepath.apps.Tweeter.models.User;

import java.util.ArrayList;

/**
 * Created by moulib on 3/11/15.
 */
public class TweetListFragment extends Fragment {

    // Inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        return v;
    }

    // Creation lifecycle event
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
}
