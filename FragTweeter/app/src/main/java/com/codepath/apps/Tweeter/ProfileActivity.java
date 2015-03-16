package com.codepath.apps.Tweeter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.Tweeter.Fragments.UserHeaderFragment;
import com.codepath.apps.Tweeter.Fragments.UserTimelineFragment;
import com.codepath.apps.Tweeter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (savedInstanceState == null) {
            // Create a new user time line fragment;
            UserHeaderFragment userHeaderFragment = new UserHeaderFragment();
            UserTimelineFragment userTimelineFragment = new UserTimelineFragment();

            // Display the user fragment within this activity - dynamic way
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.rlUserHeader, userHeaderFragment);
            ft.replace(R.id.flContainer, userTimelineFragment);

            // Commit the transaction and change the fragment
            ft.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
