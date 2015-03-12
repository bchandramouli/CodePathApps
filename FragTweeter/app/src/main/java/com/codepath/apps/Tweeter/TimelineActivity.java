package com.codepath.apps.Tweeter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.codepath.apps.Tweeter.Abstract.EndlessScrollListener;
import com.codepath.apps.Tweeter.Adapters.TweetArrayAdapter;
import com.codepath.apps.Tweeter.Fragments.TweetDialog;
import com.codepath.apps.Tweeter.Fragments.TweetListFragment;
import com.codepath.apps.Tweeter.models.QueryCtrs;
import com.codepath.apps.Tweeter.models.Tweet;
import com.codepath.apps.Tweeter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity implements TweetDialog.OnSaveListener {

    private TweetListFragment tweetListFragment;
    private TwitterClient client;
    private User self;
    private QueryCtrs queryCtrs = QueryCtrs.getInstance();


    private Boolean networkAvailable() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    private void setupNewTweet(Tweet tweet) {
        FragmentManager fm = getSupportFragmentManager();
        TweetDialog tweetDialog = TweetDialog.newInstance(tweet, self);
        tweetDialog.show(fm, "new_tweet");
    }

    public void onReTweet(View v) {
        ListView lvTweets = (ListView) findViewById(R.id.lvTweets);
        int position = lvTweets.getPositionForView((View)v.getParent());
        Tweet tweet = tweetListFragment.getTweet(position);
        setupNewTweet(tweet);
    }

    // Get the current user
    private void getCurrentUser() {
        client.getUserSettings(new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Parse the JSON User Object for the details.
                self = new User(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //failure case
            }
        });
    }

    /* Parse the JSON Array and fill the data model */
    private void updateTimeline() {

        client.updateHomeTimeline(queryCtrs.getCount(), queryCtrs.getSinceId(),
            new JsonHttpResponseHandler() {
                // Success
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    // Parse the JSON Array for the details.
                    // Insert in the front!
                    tweetListFragment.addAll(0, Tweet.fromJsonArray(response));
                    tweetListFragment.swipeRefresh(false);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    //failure case
                    tweetListFragment.swipeRefresh(false);
                }
            });
    }

    @Override
    public void onPostTweet(String post, long reply_id) {
        // Send the post to twitter first.
        client.postTweet(post, reply_id, new JsonHttpResponseHandler() {
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
    public void populateTimeline(int count) {

        if (count == 0) {
            count = queryCtrs.getCount();
        }

        client.getHomeTimeline(count, queryCtrs.getMaxId(), new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Parse the JSON Array for the details.
                tweetListFragment.addAll(-1, Tweet.fromJsonArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //failure case
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //Set a toolbar to replace the ActionBar;
        Toolbar tb = (Toolbar) findViewById(R.id.tbShare);
        setSupportActionBar(tb);

        if (savedInstanceState == null) {
            tweetListFragment = (TweetListFragment) getSupportFragmentManager().findFragmentById(R.id.fragTimeline);
        }

        if (networkAvailable()) {
            /* Perform a network request */
            client = TwitterApp.getRestClient();
            getCurrentUser();
            populateTimeline(0);
        } else {
            /* Get from local DB - TODO */
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Handle presses on the action bar items
        switch (mi.getItemId()) {
            case R.id.miShare:
                setupNewTweet(null);
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(mi);
        }
    }
}
