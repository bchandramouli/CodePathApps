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
import com.codepath.apps.Tweeter.TimelineActivity;
import com.codepath.apps.Tweeter.TwitterApp;
import com.codepath.apps.Tweeter.models.QueryCtrs;
import com.codepath.apps.Tweeter.models.Tweet;
import com.codepath.apps.Tweeter.models.User;

import java.util.ArrayList;

/**
 * Created by moulib on 3/11/15.
 */
public class TweetListFragment extends Fragment {

    private ArrayList<Tweet> tweets;
    private TweetArrayAdapter aTweets;
    private ListView lvTweets;

    private SwipeRefreshLayout swipeContainer;

    // Inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        /*
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateTimeline();
            }
        });
        */
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // find the listView
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);

        // hookup the adapter to the list view
        lvTweets.setAdapter(aTweets);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered when new data needs to be loaded
                ((TimelineActivity)getActivity()).populateTimeline(totalItemsCount);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
        return v;
    }

    // Creation lifecycle event
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the array list
        tweets = new ArrayList<>();
        //create the adapter from the data source
        aTweets = new TweetArrayAdapter(getActivity(), tweets);
    }

    public void addAll(int pos, ArrayList<Tweet> twtList) {
        if (pos == 0) {
            tweets.addAll(pos, twtList);
            lvTweets.invalidate(); // redraw the list view
        } else {
            tweets.addAll(twtList);
        }

        aTweets.notifyDataSetChanged();
    }

    public void swipeRefresh(boolean refresh) {
        // setRefreshing(false) => signal refresh has finished
        swipeContainer.setRefreshing(refresh);
    }

    public Tweet getTweet(int pos) {
       return (tweets.get(pos));
    }
}
