package or.connect.gridimagesearch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import or.connect.gridimagesearch.R;
import or.connect.gridimagesearch.models.ImageResult;

/**
 * Created by moulib on 2/14/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

    /* Apply the viewHolder Pattern and cache views for faster loading */
    private static class ViewHolder {
        //TextView tvTitle;
        ImageView ivImage;
    };

    private ViewHolder viewHolder; //view lookup cache -> stored in tag!

    // Take context and the data source. We already have a layout - so don't need the resource
    public ImageResultsAdapter(Context context, ArrayList<ImageResult> images) {
        super(context, android.R.layout.simple_list_item_1, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ImageResult image = getItem(position);

        // Check if we are using a recycled view, if not inflate
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            // create a new view from template and don't attach to parent just yet.
            convertView = inflater.inflate(R.layout.image_layout, parent, false);

            // look up the views for populating the data.
            //viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
            viewHolder.ivImage = (ImageView)convertView.findViewById(R.id.ivImage);

            convertView.setTag(viewHolder);

        } else {
            // get the viewHolder from the tag
            viewHolder = (ViewHolder)convertView.getTag();
        }

        /*
         * Insert the image data using picasso
         *     - First clear out the image
         */
        viewHolder.ivImage.setImageResource(0);
        if ((image.tbWidth > 0) && (image.tbHeight > 0)) {
            Picasso.with(getContext()).load(image.thumbUrl).
                    resize(image.tbWidth, image.tbHeight).centerInside().into(viewHolder.ivImage);
        } else {
            Picasso.with(getContext()).load(image.thumbUrl).into(viewHolder.ivImage);
        }

        // insert the model data into the view
        //viewHolder.tvTitle.setText(image.title);

        // return the convertView
        return convertView;
    }
}
