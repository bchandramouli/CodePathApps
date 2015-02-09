package or.connect.instaview;

import android.content.Context;
import android.media.Image;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moulib on 2/8/15.
 */
public class CommentsAdapter extends ArrayAdapter<PhotoComment> {


    // Take context and the data source. We already have a layout - so don't need the resource
    public CommentsAdapter(Context context, ArrayList<PhotoComment> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    // return the convertView

    // What does our item look like?
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PhotoComment comment = getItem(position);

        // Check if we are using a recycled view, if not inflate
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            // create a newview from template and don't attach to parent just yet.
            convertView = inflater.inflate(R.layout.item_comment, parent, false);
        }

        // look up the views for populating the data.
        TextView tvFrom = (TextView)convertView.findViewById(R.id.tvFrom);
        TextView tvCreateTime = (TextView)convertView.findViewById(R.id.tvCreateTime);
        TextView tvComment = (TextView)convertView.findViewById(R.id.tvComment);
        ImageView ivFromProfile = (ImageView)convertView.findViewById(R.id.ivFromProfile);


        // insert the model data into the view
        tvFrom.setText(comment.from);
        tvCreateTime.setText(DateUtils.getRelativeTimeSpanString((comment.createTime * 1000),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS,
                (DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_ABBREV_TIME | DateUtils.FORMAT_ABBREV_RELATIVE)));
        tvComment.setText(comment.text);
        /*
         * Insert the image data using picasso
         *     - First clear out the image
         */
        ivFromProfile.setImageResource(0);
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(40)
                .oval(false)
                .build();
        Picasso.with(getContext()).load(comment.fromProfile).
                transform(transformation).fit().into(ivFromProfile);

        return convertView;
    }
}
