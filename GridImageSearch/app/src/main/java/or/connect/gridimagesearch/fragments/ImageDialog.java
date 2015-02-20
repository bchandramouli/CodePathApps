package or.connect.gridimagesearch.fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import or.connect.gridimagesearch.R;
import or.connect.gridimagesearch.activities.FullImageActivity;
import or.connect.gridimagesearch.activities.SearchActivity;

/**
 * Created by moulib on 2/14/15.
 */
public class ImageDialog extends DialogFragment {
    public ImageDialog() {
        // Empty constructor needed for dialog fragments
    }

    public static ImageDialog newInstance(String url, String caption) {
        ImageDialog img = new ImageDialog();
        Bundle args = new Bundle();

        args.putString("full_image", url);
        args.putString("caption", caption);
        img.setArguments(args);
        return img;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.full_image_layout, container);
        final ImageView ivFullImage = (ImageView) view.findViewById(R.id.ivFullImage);
        TextView tvFullTitle = (TextView) view.findViewById(R.id.tvFullTitle);

        //ivFullImage.setImageResource(0);

        String url = getArguments().getString("full_image");
        Picasso.with(getDialog().getContext()).load(url).into(ivFullImage, new Callback() {
            @Override
            public void onSuccess() {
                //Setup the share intent after the image is loaded
                ((FullImageActivity)getActivity()).setupShareIntent();
            }

            @Override
            public void onError() {
                // No work. :)
            }
        });

        tvFullTitle.setText(getArguments().getString("caption"));

        // Get rid of the title on the full screen images
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getDialog().setCancelable(true);
        //getDialog().setCanceledOnTouchOutside(true);

        return view;
    }

}
