package or.connect.gridimagesearch.fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import or.connect.gridimagesearch.R;

/**
 * Created by moulib on 2/14/15.
 */
public class ImageDialog extends DialogFragment {
    public ImageDialog() {
        // Empty constructor needed for dialog fragments
    }

    public static ImageDialog newInstance(String url) {
        ImageDialog img = new ImageDialog();
        Bundle args = new Bundle();

        args.putString("full_image", url);
        img.setArguments(args);
        return img;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.full_image_layout, container);
        ImageView ivFullImage = (ImageView) view.findViewById(R.id.ivFullImage);

        //ivFullImage.setImageResource(0);

        String url = getArguments().getString("full_image");
        Picasso.with(getDialog().getContext()).load(url).into(ivFullImage);

        // Get rid of the title on the full screen images
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

        return view;
    }


}
