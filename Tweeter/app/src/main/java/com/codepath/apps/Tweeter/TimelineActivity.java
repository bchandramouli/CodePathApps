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
import android.widget.ListView;

import com.codepath.apps.Tweeter.Abstract.EndlessScrollListener;
import com.codepath.apps.Tweeter.Adapters.TweetArrayAdapter;
import com.codepath.apps.Tweeter.Fragments.TweetDialog;
import com.codepath.apps.Tweeter.models.QueryCtrs;
import com.codepath.apps.Tweeter.models.Tweet;
import com.codepath.apps.Tweeter.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class TimelineActivity extends ActionBarActivity implements TweetDialog.OnSaveListener {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetArrayAdapter aTweets;
    private ListView lvTweets;

    private QueryCtrs queryCtrs = QueryCtrs.getInstance();

    private Boolean networkAvailable() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    @Override
    public void onPostTweet(String post, long reply_id) {

        // Send the post to twitter first.
        client.postTweet(post, reply_id, new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Parse the JSON Array for the details.
                aTweets.add(Tweet.tweetFromJsonObj(response));
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

        // find the listView
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered when new data needs to be loaded
                populateTimeline();
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

        // Create the array list
        tweets = new ArrayList<>();
        //create the adapter from the data source
        aTweets = new TweetArrayAdapter(this, tweets);

        // hookup the adapter to the list view
        lvTweets.setAdapter(aTweets);

        if (networkAvailable()) {
            /* Perform a network request */
            client = TwitterApp.getRestClient();
            populateTimeline();
        } else {
            /* Get from local DB - TODO */
        }
    }

    /* Parse the JSON Array and fill the data model */
    private void populateTimeline() {

        client.getHomeTimeline(queryCtrs.getCount(), queryCtrs.getSinceId(), queryCtrs.getMaxId(),
                new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Parse the JSON Array for the details.
                aTweets.addAll(Tweet.fromJsonArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //failure case
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    private void setupNewTweet(long reply_id) {
        FragmentManager fm = getSupportFragmentManager();
        TweetDialog tweetDialog = TweetDialog.newInstance(reply_id);
        // TODO - This needs to pass the User object!!!
        tweetDialog.show(fm, "new_tweet");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Handle presses on the action bar items
        switch (mi.getItemId()) {
            case R.id.miShare:
                setupNewTweet(0);
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(mi);
        }
    }
}
