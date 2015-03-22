package com.codepath.apps.HandShake.Adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.HandShake.R;
import com.codepath.apps.HandShake.models.Connection;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by moulib on 2/21/15.
 */
public class LinkedInArrayAdapter extends ArrayAdapter<Connection> {

    /* Apply the viewHolder Pattern and cache views for faster loading */
    private static class ViewHolder {
        ImageView ivUserProfile;
        TextView tvFirstName;
        TextView tvLastName;
        TextView tvIndustry;
        TextView tvDescription;
    };

    public LinkedInArrayAdapter(Context context, List<Connection> connections) {
        super(context, android.R.layout.simple_list_item_1, connections);
    }

    // What does our item look like?
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder; //view lookup cache -> stored in tag!

        // Get the data item for this position
        Connection connection = getItem(position);

        // Check if we are using a recycled view, if not inflate
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            // create a new view from template and don't attach to parent just yet.
            convertView = inflater.inflate(R.layout.display_connections, parent, false);

            // look up the views for populating the data.
            viewHolder.ivUserProfile = (ImageView)convertView.findViewById(R.id.ivUserProfile);
            viewHolder.tvFirstName = (TextView)convertView.findViewById(R.id.tvFirstName);
            viewHolder.tvLastName = (TextView)convertView.findViewById(R.id.tvLastName);
            viewHolder.tvIndustry = (TextView)convertView.findViewById(R.id.tvIndustry);
            viewHolder.tvDescription = (TextView)convertView.findViewById(R.id.tvDescription);

            convertView.setTag(viewHolder);

        } else {
            // get the viewHolder from the tag
            viewHolder = (ViewHolder)convertView.getTag();
        }

        /*
         * Insert the image data using picasso
         *     - First clear out the image
         */
        viewHolder.ivUserProfile.setImageResource(0);

        Picasso.with(getContext()).load(connection.getImageUrl()).
                into(viewHolder.ivUserProfile);

        // insert the model data into the view
        viewHolder.tvFirstName.setText(connection.getFirstName());
        viewHolder.tvLastName.setText(" " + connection.getLastName());

        viewHolder.tvIndustry.setText(connection.getIndustry());
        viewHolder.tvDescription.setText(connection.getDescription());

        // return the convertView
        return convertView;
    }
}

