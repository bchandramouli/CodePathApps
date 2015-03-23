package com.codepath.apps.HandShake;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.HandShake.models.User;
import com.squareup.picasso.Picasso;


public class ProfileActivity extends ActionBarActivity {

    private ImageView ivUserProfile;
    private TextView tvUserName;
    private TextView tvUserDescription;
    private TextView tvUserIndustry;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = getIntent().getParcelableExtra("user");

        if (savedInstanceState == null) {

            ivUserProfile = (ImageView) findViewById(R.id.ivUserProfile);
            tvUserName = (TextView) findViewById(R.id.tvUserName);
            tvUserDescription = (TextView) findViewById(R.id.tvUserDescription);
            tvUserIndustry = (TextView) findViewById(R.id.tvUserIndustry);

            //getSupportActionBar().setTitle(user.getFirstName() + "'s Profile");

            tvUserName.setText(user.getFirstName() + " " + user.getLastName());
            tvUserIndustry.setText(user.getIndustry());
            tvUserDescription.setText(user.getDescription());

            if (!user.getImageUrl().matches("")) {
                Picasso.with(getApplicationContext()).load(user.getImageUrl()).
                        into(ivUserProfile);
            }
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
