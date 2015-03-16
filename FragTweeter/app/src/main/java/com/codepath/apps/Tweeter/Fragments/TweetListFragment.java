package com.codepath.apps.Tweeter.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.codepath.apps.Tweeter.TwitterClient;
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

    private TwitterClient client;

    public Boolean networkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    // Inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // find the listView
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);

        // hookup the adapter to the list view
        lvTweets.setAdapter(aTweets);

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

        client = TwitterApp.getRestClient();
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

    public void resetSwipeRefresh() {
        // setRefreshing(false) => signal refresh has finished
        swipeContainer.setRefreshing(false);
    }

    public Tweet getTweet(int pos) {
       return (tweets.get(pos));
    }

    public ListView getListView() {
        return lvTweets;
    }

    public SwipeRefreshLayout getSwipeContainer() {
        return swipeContainer;
    }

    public TwitterClient getClient() {
        return client;
    }
}
