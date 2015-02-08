package or.connect.instaview;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;



/**
 * Created by moulib on 2/4/15.
 */
public class InstagramPhtotosAdapter extends ArrayAdapter<InstagramPhoto> {

    // Take context and the data source. We already have a layout - so don't need the resource
    public InstagramPhtotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    // What does our item look like?
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        InstagramPhoto photo = getItem(position);

        // Check if we are using a recycled view, if not inflate
        if (convertView == null) {
            // create a newview from template and don't attach to parent just yet.
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        // look up the views for populating the data.
        TextView tvCaption = (TextView)convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
        TextView tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
        TextView tvLikes = (TextView)convertView.findViewById(R.id.tvLikes);
        TextView tvDate = (TextView)convertView.findViewById(R.id.tvDate);
        ImageView ivUserProfile = (ImageView)convertView.findViewById(R.id.ivUserProfile);

        // insert the model data into the view
        tvCaption.setText(photo.caption);
        tvDate.setText(DateUtils.getRelativeTimeSpanString((photo.timeCreated * 1000), System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS, (DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_ABBREV_TIME | DateUtils.FORMAT_ABBREV_RELATIVE)));

        tvUserName.setText(photo.username);
        tvLikes.setText(Integer.toString(photo.likesCount) + " likes");

        /*
         * Insert the image data using picasso
         *     - First clear out the image
         */
        ivPhoto.setImageResource(0);
        ivUserProfile.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).fit().centerInside().into(ivPhoto);
        //Picasso.with(getContext()).load(photo.userProfileUrl).fit().transform(new CircleTransform()).into(ivUserProfile);

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(40)
                .oval(false)
                .build();
        Picasso.with(getContext()).load(photo.userProfileUrl).transform(transformation).fit().into(ivUserProfile);

        // return the convertView
        return convertView;
    }
}
