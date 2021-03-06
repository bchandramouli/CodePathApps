package com.codepath.apps.Tweeter.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.Tweeter.Abstract.EndlessScrollListener;
import com.codepath.apps.Tweeter.R;
import com.codepath.apps.Tweeter.TimelineActivity;
import com.codepath.apps.Tweeter.TwitterApp;
import com.codepath.apps.Tweeter.TwitterClient;
import com.codepath.apps.Tweeter.models.QueryCtrs;
import com.codepath.apps.Tweeter.models.Tweet;
import com.codepath.apps.Tweeter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by moulib on 3/11/15.
 */
public class HomeTimelineFragment extends TweetListFragment {

    public void onPostTweet(String post, long reply_id) {
        // Send the post to twitter first.
        getClient().postTweet(post, reply_id, new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Parse the JSON Array for the details.
                updateTimeline();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //failure case
            }
        });
    }

    /* Parse the JSON Array and fill the data model */
    public void updateTimeline() {

        getClient().updateHomeTimeline(getQueryCount(), getQuerySinceId(),
            new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Parse the JSON Array for the details.
                // Insert in the front!
                addAll(0, Tweet.fromJsonArray(response));
                resetSwipeRefresh();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //failure case
                resetSwipeRefresh();
            }
        });
    }

    /* Parse the JSON Array and fill the data model */
    public void populateTimeline(int count) {

        if (count == 0) {
            count = getQueryCount();
        }

        getClient().getHomeTimeline(count, getQueryMaxId(), new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Parse the JSON Array for the details.
                addAll(-1, Tweet.fromJsonArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //failure case
            }
        });
    }

    public Boolean networkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        getListView().setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered when new data needs to be loaded
                populateTimeline(totalItemsCount);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

        // Setup refresh listener which triggers new data loading
        getSwipeContainer().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateTimeline();
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (networkAvailable()) {
            /* Perform a network request */
            populateTimeline(0);
        } else {
            /* Get from local DB - TODO */
        }
    }
}
