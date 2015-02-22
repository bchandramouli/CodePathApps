package com.codepath.apps.Tweeter;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.Tweeter.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class TimelineActivity extends ActionBarActivity {

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApp.getRestClient();
        populateTimeline();
    }

    private void timelineParseJsonArray() {
        /* JsonResponse
        [
          {
                "created_at": "Tue Aug 28 21:16:23 +0000 2012",
                "favorited": false,
                "text": "just another test",
                "source": "<a href="//realitytechnicians.com\"" rel="\"nofollow\"">OAuth Dancer Reborn</a>",
                "user": {
                      "name": "OAuth Dancer",
                      "profile_image_url":"http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
                      "created_at": "Wed Mar 03 19:37:35 +0000 2010",
                      "location": "San Francisco, CA",

                      "default_profile": false,

                      "url": "http://bit.ly/oauth-dancer",
                      "profile_image_url_https":"https://si0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",

                      "profile_use_background_image": true,
                      "profile_text_color": "333333",

                      "followers_count": 28,

                      "profile_background_color": "C0DEED",

                      "profile_background_image_url_https":"https://si0.twimg.com/profile_background_images/80151733/oauth-dance.png",
                      "statuses_count": 166,
                      "profile_background_image_url":"http://a0.twimg.com/profile_background_images/80151733/oauth-dance.png",

                      "friends_count": 14,
                      "screen_name": "oauth_dancer"
                    },
             },
        ]
        */




    }

    /* Parse the JSON Array and fill the data model */
    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Parse the JSON Array for the details.
                timelineParseJsonArray(response);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
