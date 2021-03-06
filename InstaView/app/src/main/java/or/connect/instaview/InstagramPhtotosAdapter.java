package or.connect.instaview;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    /* Apply the viewHolder Pattern and cache views for faster loading */
    private static class ViewHolder {
        TextView tvCaption;
        ImageView ivPhoto;
        TextView tvUserName;
        TextView tvLikes;
        TextView tvComments;
        TextView tvDate;
        ImageView ivUserProfile;
        TextView tvComment1;
        TextView tvCommentUser1;
        TextView tvComment2;
        TextView tvCommentUser2;
        TextView tvBg;
    };

    private ViewHolder viewHolder; //view lookup cache -> stored in tag!

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
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            // create a newview from template and don't attach to parent just yet.
            convertView = inflater.inflate(R.layout.item_photo, parent, false);
            // look up the views for populating the data.
            viewHolder.tvCaption = (TextView)convertView.findViewById(R.id.tvCaption);
            viewHolder.ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
            viewHolder.tvLikes = (TextView)convertView.findViewById(R.id.tvLikes);
            viewHolder.tvComments = (TextView)convertView.findViewById(R.id.tvComments);
            viewHolder.tvDate = (TextView)convertView.findViewById(R.id.tvDate);
            viewHolder.ivUserProfile = (ImageView)convertView.findViewById(R.id.ivUserProfile);
            viewHolder.tvComment1 = (TextView)convertView.findViewById(R.id.tvComment1);
            viewHolder.tvCommentUser1 = (TextView)convertView.findViewById(R.id.tvCommentUser1);
            viewHolder.tvComment2 = (TextView)convertView.findViewById(R.id.tvComment2);
            viewHolder.tvCommentUser2 = (TextView)convertView.findViewById(R.id.tvCommentUser2);
            viewHolder.tvBg = (TextView)convertView.findViewById(R.id.tvBg);

            convertView.setTag(viewHolder);

        } else {
            // get the viewHolder from the tag
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // insert the model data into the view
        viewHolder.tvCaption.setText(photo.caption);
        viewHolder.tvDate.setText(DateUtils.getRelativeTimeSpanString((photo.timeCreated * 1000),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS,
                (DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_ABBREV_TIME | DateUtils.FORMAT_ABBREV_RELATIVE)));

        viewHolder.tvUserName.setText(photo.username);
        viewHolder.tvLikes.setText(Integer.toString(photo.likesCount) + " likes");
        if (photo.commentCount > 0) {
            viewHolder.tvComments.setText("View all " + Integer.toString(photo.commentTotal) + " comments");
        } else {
            viewHolder.tvComments.setVisibility(View.INVISIBLE);
        }

        /*
         * Insert the image data using picasso
         *     - First clear out the image
         */
        viewHolder.ivPhoto.setImageResource(0);
        viewHolder.ivUserProfile.setImageResource(0);

        //Picasso.with(getContext()).load(photo.userProfileUrl).fit().transform(new CircleTransform()).into(ivUserProfile);
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(40)
                .oval(false)
                .build();
        Picasso.with(getContext()).load(photo.userProfileUrl).
                transform(transformation).fit().into(viewHolder.ivUserProfile);

        Picasso.with(getContext()).load(photo.imageUrl).resize(photo.imageWidth, photo.imageHeight).centerInside().into(viewHolder.ivPhoto);

        viewHolder.tvBg.setBackgroundColor(0x12000000);

        if (photo.commentCount > 0) {
            viewHolder.tvCommentUser1.setText(photo.comments.get(0).from);
            viewHolder.tvComment1.setText(photo.comments.get(0).text);
        } else {
            viewHolder.tvCommentUser1.setVisibility(View.INVISIBLE);
            viewHolder.tvComment1.setVisibility(View.INVISIBLE);
        }
        if (photo.commentCount > 1) {
            viewHolder.tvCommentUser2.setText(photo.comments.get(1).from);
            viewHolder.tvComment2.setText(photo.comments.get(1).text);
        } else {
            viewHolder.tvCommentUser2.setVisibility(View.INVISIBLE);
            viewHolder.tvComment2.setVisibility(View.INVISIBLE);
        }

        // return the convertView
        return convertView;
    }
}
