package com.codepath.apps.HandShake;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.HandShake.Abstract.EndlessScrollListener;
import com.codepath.apps.HandShake.Adapters.LinkedInArrayAdapter;
import com.codepath.apps.HandShake.models.Connection;
import com.codepath.apps.HandShake.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TimelineActivity extends ActionBarActivity {

    private LinkedInClient client;

    private ArrayList<Connection> connections;
    private LinkedInArrayAdapter aConnections;
    private ListView lvConnections;

    private User self;

    private Boolean networkAvailable() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    /* Parse the JSON Array and fill the data model */
    private void populateTimeline() {

        client.getHomeTimeline(new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Parse the JSON Object for the details.

                JSONArray responseArr;
                try {
                    responseArr = response.getJSONArray("values");
                } catch (JSONException e) {
                    responseArr = null;
                }

                if (responseArr != null) {
                    connections.addAll(Connection.fromJsonArray(responseArr));
                    aConnections.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //failure case
            }
        });
    }

    private void getSelfProfile() {

        client.getUserSettings(new JsonHttpResponseHandler(){
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Parse the JSON Object for the details.
                self = new User(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //failure case
            }
        });

    }

    private void getUserProfile() {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("user", self);

        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //Set a toolbar to replace the ActionBar;
        Toolbar tb = (Toolbar) findViewById(R.id.tbShare);
        setSupportActionBar(tb);


        // find the listView
        lvConnections = (ListView) findViewById(R.id.lvTweets);
        lvConnections.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered when new data needs to be loaded
                populateTimeline();
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

        // Create the array list
        connections = new ArrayList<>();
        //create the adapter from the data source
        aConnections = new LinkedInArrayAdapter(this, connections);

        // hookup the adapter to the list view
        lvConnections.setAdapter(aConnections);


        if (networkAvailable()) {
            /* Perform a network request */
            client = LinkedInApp.getRestClient();
            getSelfProfile();
            populateTimeline();
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
            case R.id.miProfile:
                getUserProfile();
                return true;
            default:
                return super.onOptionsItemSelected(mi);
        }
    }
}
