package com.codepath.apps.Tweeter;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.Tweeter.Fragments.HomeTimelineFragment;
import com.codepath.apps.Tweeter.Fragments.MentionsTimelineFragement;
import com.codepath.apps.Tweeter.Fragments.TweetDialog;
import com.codepath.apps.Tweeter.models.Tweet;
import com.codepath.apps.Tweeter.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class TimelineActivity extends ActionBarActivity implements TweetDialog.OnSaveListener {

    private HomeTimelineFragment homeTimelineFragment;

    Intent profileIntent;

    public void setupNewTweet(Tweet tweet) {
        FragmentManager fm = getSupportFragmentManager();
        TweetDialog tweetDialog = TweetDialog.newInstance(tweet, homeTimelineFragment.getSelf());
        tweetDialog.show(fm, "new_tweet");
    }

    public void onReTweet(View v) {
        ListView lvTweets = homeTimelineFragment.getListView();
        int position = lvTweets.getPositionForView((View)v.getParent());
        Tweet tweet = homeTimelineFragment.getTweet(position);
        setupNewTweet(tweet);
    }

    @Override
    public void onPostTweet(String post, long reply_id) {
        homeTimelineFragment.onPostTweet(post, reply_id);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //homeTimelineFragment = (HomeTimelineFragment) getSupportFragmentManager().findFragmentById(R.id.fragTimeline);

        //Set a toolbar to replace the ActionBar;
        Toolbar tb = (Toolbar) findViewById(R.id.tbShare);
        setSupportActionBar(tb);

        // Get the viewpager
        ViewPager vPager = (ViewPager) findViewById(R.id.viewpager);
        // Set the viewpager adapter for the pager
        vPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        // Find the pager sliding tabs
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the pager tabs to the view pager
        tabStrip.setViewPager(vPager);
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
                //setupNewTweet(null);
                return true;
            case R.id.miProfile:
                userProfile();
                return true;
            default:
                return super.onOptionsItemSelected(mi);
        }
    }

    public void userProfile() {
        //Launch the user profile activity
        TwitterClient client = new TwitterApp().getRestClient();
        profileIntent = new Intent(this, ProfileActivity.class);

        client.getUserSettings(new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Parse the JSON User Object for the details.
                User user = new User(response);
                profileIntent.putExtra("user", user);
                startActivity(profileIntent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //failure case
            }
        });
    }

    // Return the order of the Fragments in the View Pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {

        final int PAGE_COUNT = 2;

        private String tabTitles[] = { "Home", "Mentions"};

        // Adapter gets the manager to insert or remove from the activity
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // The order and creation of fragments in the pager
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragement();
            } else {
                return null;
            }
        }

        // Returns the tab title
        @Override
        public int getCount(){
            return tabTitles.length;
        }

        // How many tabs to swipe between?
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
