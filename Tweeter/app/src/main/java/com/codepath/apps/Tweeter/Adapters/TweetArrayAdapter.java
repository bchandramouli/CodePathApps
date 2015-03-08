package com.codepath.apps.Tweeter.Adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.Tweeter.R;
import com.codepath.apps.Tweeter.models.Tweet;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by moulib on 2/21/15.
 */
public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

    /* Apply the viewHolder Pattern and cache views for faster loading */
    private static class ViewHolder {
        ImageView ivUserProfile;
        TextView tvUserName;
        TextView tvUserHandle;
        TextView tvTimeStamp;
        TextView tvBody;
        TextView tvRetweets;
        TextView tvFavorites;
        ImageView ivMedia;
    };

    public TweetArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();

            // TODO - can we abbreviate further? - mins to just M?
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS,
                    (DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_ABBREV_TIME | DateUtils.FORMAT_ABBREV_RELATIVE)).toString();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // What does our item look like?
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder; //view lookup cache -> stored in tag!

        // Get the data item for this position
        Tweet tweet = getItem(position);

        // Check if we are using a recycled view, if not inflate
        //if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            // create a new view from template and don't attach to parent just yet.
            convertView = inflater.inflate(R.layout.display_tweets, parent, false);

            // look up the views for populating the data.
            viewHolder.ivUserProfile = (ImageView)convertView.findViewById(R.id.ivUserProfile);
            viewHolder.ivMedia = (ImageView)convertView.findViewById(R.id.ivMedia);
            viewHolder.tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
            viewHolder.tvUserHandle = (TextView)convertView.findViewById(R.id.tvUserHandle);
            viewHolder.tvTimeStamp = (TextView)convertView.findViewById(R.id.tvTimeStamp);
            viewHolder.tvBody = (TextView)convertView.findViewById(R.id.tvBody);
            viewHolder.tvRetweets = (TextView)convertView.findViewById(R.id.tvRetweets);
            viewHolder.tvFavorites = (TextView)convertView.findViewById(R.id.tvFavorites);

            convertView.setTag(viewHolder);

        /* XXX - Todo - temporarily disabled to not reuse - so the image view looks clean!
        } else {
            // get the viewHolder from the tag
            viewHolder = (ViewHolder)convertView.getTag();
        }
        */

        /*
         * Insert the image data using picasso
         *     - First clear out the image
         */
        viewHolder.ivUserProfile.setImageResource(0);
        viewHolder.ivMedia.setImageResource(0);

        if (!tweet.getMediaUrl().equals("")) {
            viewHolder.ivMedia.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(tweet.getMediaUrl()).into(viewHolder.ivMedia);
        }

        Picasso.with(getContext()).load(tweet.getUser().getProfile_url()).
                into(viewHolder.ivUserProfile);

        // insert the model data into the view
        viewHolder.tvUserName.setText(tweet.getUser().getName());
        viewHolder.tvUserHandle.setText("@ " + tweet.getUser().getHandle());

        viewHolder.tvTimeStamp.setText(getRelativeTimeAgo(tweet.getTimestamp()));
        viewHolder.tvBody.setText(tweet.getBody());

        if (tweet.getRetweets() > 0) {
            viewHolder.tvRetweets.setText(Integer.toString(tweet.getRetweets()));
        } else {
            viewHolder.tvRetweets.setVisibility(View.INVISIBLE);
        }

        if (tweet.getFavorites() > 0) {
            viewHolder.tvFavorites.setText(Integer.toString(tweet.getFavorites()));
        } else {
            viewHolder.tvFavorites.setVisibility(View.INVISIBLE);
        }

        // return the convertView
        return convertView;
    }
}

